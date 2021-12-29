package com.kata.bootfetch.kata_fetch.controller;

import com.kata.bootfetch.kata_fetch.ExeptionHandler.DataInfoHandler;
import com.kata.bootfetch.kata_fetch.ExeptionHandler.UserWithSuchLoginExist;
import com.kata.bootfetch.kata_fetch.entity.Role;
import com.kata.bootfetch.kata_fetch.entity.User;
import com.kata.bootfetch.kata_fetch.service.RoleService;
import com.kata.bootfetch.kata_fetch.service.RoleServiceImpl;
import com.kata.bootfetch.kata_fetch.service.UserDetailsServiceImpl;
import com.kata.bootfetch.kata_fetch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private UserService userDetailsService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminRestController(UserDetailsServiceImpl userDetailsService, RoleServiceImpl roleService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<Set<User>> userList() {
        Set<User> users = userDetailsService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //Получение аутенфицированного пользователя
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        User user = userDetailsService.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //Получение юзера по ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> findUserByID(@PathVariable Long id) {
        User user = userDetailsService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<Role> roleByID(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> findAllRoles() {
        List<Role> allRoles = roleService.allRoles();
        return new ResponseEntity<>(allRoles, HttpStatus.OK);
    }

    //Создание нового юзера
    @PostMapping("/users")
    public ResponseEntity<DataInfoHandler> apiAddNewUser(@Valid @RequestBody User user,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new DataInfoHandler(error), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userDetailsService.addOrUpdateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            throw new UserWithSuchLoginExist("User with such login Exist");
        }
    }

    //Обновление юзера
    @PutMapping("/users/{id}")
    public ResponseEntity<DataInfoHandler> apiUpdateUser(@PathVariable("id") long id,
                                                         @RequestBody @Valid User user,
                                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new DataInfoHandler(error), HttpStatus.BAD_REQUEST);
        }

        String bCryptPassword = user.getPassword().isEmpty() ?
                userDetailsService.getUserById(user.getId()).getPassword() :
                passwordEncoder.encode(user.getPassword());

        user.setPassword(bCryptPassword);

        try {
            userDetailsService.addOrUpdateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            throw new UserWithSuchLoginExist("User with such login Exist");
        }
    }

    //удаление пользователя
    @DeleteMapping("users/{id}")
    public ResponseEntity<DataInfoHandler> deleteUser(@PathVariable("id") long id) {
        userDetailsService.removeUserById(id);
        return new ResponseEntity<>(new DataInfoHandler("User was deleted"), HttpStatus.OK);
    }

    //Собираем все ошибки валидации в коллекцию и выводим в окне
    private String getErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.joining("; "));
    }
}
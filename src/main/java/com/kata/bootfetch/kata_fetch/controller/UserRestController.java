package com.kata.bootfetch.kata_fetch.controller;

import com.kata.bootfetch.kata_fetch.entity.Role;
import com.kata.bootfetch.kata_fetch.entity.User;
import com.kata.bootfetch.kata_fetch.service.RoleService;
import com.kata.bootfetch.kata_fetch.service.RoleServiceImpl;
import com.kata.bootfetch.kata_fetch.service.UserDetailsServiceImpl;
import com.kata.bootfetch.kata_fetch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private UserService userDetailsService;
    private RoleService roleService;

    @Autowired
    public UserRestController(UserDetailsServiceImpl userDetailsService, RoleServiceImpl roleService) {
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
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

}

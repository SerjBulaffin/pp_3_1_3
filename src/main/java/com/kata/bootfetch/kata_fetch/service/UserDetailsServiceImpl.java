package com.kata.bootfetch.kata_fetch.service;

import com.kata.bootfetch.kata_fetch.ExeptionHandler.NoUserWithSuchIdException;
import com.kata.bootfetch.kata_fetch.ExeptionHandler.NoUserWithSuchLogin;
import com.kata.bootfetch.kata_fetch.entity.User;
import com.kata.bootfetch.kata_fetch.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    private UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    // «Пользователь» – это просто Object. В большинстве случаев он может быть
    //  приведен к классу UserDetails.
    // Для создания UserDetails используется интерфейс UserDetailsService, с единственным методом:
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        return userDao.getUserByName(s);
//    }

    //Получение юзера по email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserByEmail(email);

        if (user != null) {
            return user;
        } else {
            throw new NoUserWithSuchLogin("There is not user with such login");
        }
    }

    //получение юзера по ID
    @Override
    public User getUserById(Long id) {
        return userDao.findById(id).orElseThrow(() -> new NoUserWithSuchIdException("User with such id does not exist"));
    }

    //получение юзера по email
    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    //получение юзера по имени
    @Override
    public User getUserByName(String name) {
        return userDao.getUserByFirstName(name);
    }

    //получение всех пользователей
    @Override
    public Set<User> getAllUsers() {
        return userDao.findAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    //создание или обновление пользователя
    @Override
    public void addOrUpdateUser(User user) {
        userDao.save(user);
    }

    //удаление пользователя по ID
    @Override
    public void removeUserById(Long id) {
        userDao.delete(getUserById(id));
    }


}

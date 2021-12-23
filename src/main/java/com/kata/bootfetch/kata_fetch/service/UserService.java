package com.kata.bootfetch.kata_fetch.service;

import com.kata.bootfetch.kata_fetch.entity.User;

import java.util.Set;

public interface UserService {
    User getUserById(Long id);

    User getUserByEmail(String email);

    User getUserByName(String name);

    Set<User> getAllUsers();

    void addOrUpdateUser(User user);

    void removeUserById(Long id);
}

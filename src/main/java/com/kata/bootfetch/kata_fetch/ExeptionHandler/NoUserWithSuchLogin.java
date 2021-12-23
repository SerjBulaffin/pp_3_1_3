package com.kata.bootfetch.kata_fetch.ExeptionHandler;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

//Ошибка авторизации пользователя
public class NoUserWithSuchLogin extends UsernameNotFoundException {

    public NoUserWithSuchLogin(String msg) {
        super(msg);
    }
}

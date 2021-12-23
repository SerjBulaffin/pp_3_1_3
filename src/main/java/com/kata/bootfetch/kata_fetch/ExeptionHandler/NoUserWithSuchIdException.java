package com.kata.bootfetch.kata_fetch.ExeptionHandler;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

//Исключение, если пользователь с таким ID не найден
public class NoUserWithSuchIdException extends UsernameNotFoundException {

    public NoUserWithSuchIdException(String msg) {
        super(msg);
    }
}

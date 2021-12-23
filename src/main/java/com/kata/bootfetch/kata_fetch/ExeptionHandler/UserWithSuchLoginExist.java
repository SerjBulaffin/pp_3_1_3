package com.kata.bootfetch.kata_fetch.ExeptionHandler;

import org.springframework.dao.DataIntegrityViolationException;

//Исключение если пользователь с таким логином существует
public class UserWithSuchLoginExist extends DataIntegrityViolationException {
    public UserWithSuchLoginExist(String msg) {
        super(msg);
    }
}

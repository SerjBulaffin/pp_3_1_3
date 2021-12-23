package com.kata.bootfetch.kata_fetch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user")
    public String userPage() {
        return "user";
    }

}

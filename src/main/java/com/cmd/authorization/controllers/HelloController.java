package com.cmd.authorization.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class HelloController {

    @PostMapping("/hello")
    public String hello(@RequestBody String name) {
        return "Hello, "+ name + "!";
    }
}

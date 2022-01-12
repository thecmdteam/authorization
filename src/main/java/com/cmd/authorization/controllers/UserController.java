package com.cmd.authorization.controllers;

import com.cmd.authorization.dto.CreateUserDTO;
import com.cmd.authorization.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final String WEB_CLIENT = "web-client";
    private final String ANDROID_CLIENT = "android-client";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    public ResponseEntity name(@RequestBody CreateUserDTO user, Authentication auth) {
        if (auth.getName().equals(ANDROID_CLIENT)) {
            return userService.createUserMobile(user);
        }
        return ResponseEntity.ok()
                .body("Wrong client");
    }
}

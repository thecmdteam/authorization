package com.cmd.authorization.controllers;

import com.cmd.authorization.dto.CreateUserDTO;
import com.cmd.authorization.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TemplateController {

    private final UserService userService;

    public TemplateController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        CreateUserDTO userDTO = new CreateUserDTO();
        model.addAttribute("user", userDTO);
        return "signup";
    }

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute CreateUserDTO userDTO) {
        userService.createUserMobile(userDTO);
        return "registration_consent";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token_id") String tokenId) {
        return null
    }
}

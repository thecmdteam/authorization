package com.cmd.authorization.controllers;

import com.cmd.authorization.dto.UserDTO;
import com.cmd.authorization.services.TokenService;
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
    private final TokenService tokenService;

    public TemplateController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }


    @GetMapping("/signup")
    public String signUp(Model model) {
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        model.addAttribute("errors", null);

        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") UserDTO user, Model model) {
        if(!user.getPassword().equals(user.getMatchingPassword())){
            model.addAttribute("errors", "Passwords don't match");
            return "signup";
        }
        if (userService.createUser(user))
            return "registration_consent";
        model.addAttribute("errors", "Account with email already exists");
        return "signup";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token_id") String tokenId, Model model) {
        var response = tokenService.verifyToken(tokenId);

        return switch (response) {
            case INVALID_TOKEN -> {
                model.addAttribute("error", "to be invalid");
                yield "verification_error";
            }
            case EXPIRED_TOKEN -> {
                model.addAttribute("error", "to have expired");
                yield "verification_error";
            }
            case VALID_TOKEN -> "redirect:login";

        };
    }


    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }
}

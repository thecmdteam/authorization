package com.cmd.authorization.controllers;

import com.cmd.authorization.dto.UserDTO;
import com.cmd.authorization.events.RecoverAccountEvent;
import com.cmd.authorization.model.User;
import com.cmd.authorization.services.TokenService;
import com.cmd.authorization.services.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TemplateController {


    private final UserService userService;
    private final UserDetailsManager manager;
    private final TokenService tokenService;
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder encoder;

    public TemplateController(UserService userService, UserDetailsManager manager,
                              TokenService tokenService, ApplicationEventPublisher publisher,
                              PasswordEncoder encoder) {
        this.userService = userService;
        this.manager = manager;
        this.tokenService = tokenService;
        this.publisher = publisher;
        this.encoder = encoder;
    }


    @GetMapping("/signup")
    public String signUp(Model model) {
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        model.addAttribute("errors", false);
        model.addAttribute("email_exists", false);

        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") UserDTO user, Model model) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        if (!user.getPassword().matches(regex)) {
            model.addAttribute("errors", true);
            return "signup";
        }
        if (userService.createUser(user)) {
            model.addAttribute("message",
                    "Account verification email sent to " + user.getUsername());
            return "email_sent";
        }
        model.addAttribute("email_exists", true);
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
            case VALID_TOKEN -> "redirect:https://cmd-app.netlify.app/validate";

        };
    }

    @GetMapping("/recover")
    public String verifyRecoveryEmail(@RequestParam("token_id") String tokenId, Model model) {
        var user = tokenService.verifyRecoveryToken(tokenId);

        if (user == null) {
            model.addAttribute("error", "to be invalid");
            return "verification_error";
        }

        return "forward:/changePassword?email=" + user.getUsername();
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("matcherror", false);
        model.addAttribute("error", false);
        return "change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("email") String email,
                                 @ModelAttribute("password") String password,
                                 @ModelAttribute("matchPassword") String matchPassword,
                                 Model model) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        if (!password.matches(regex)) {
            model.addAttribute("matcherror", true);
            model.addAttribute("error", false);
            return "change-password";
        }
        if (!password.equals(matchPassword)) {
            model.addAttribute("matcherror", false);
            model.addAttribute("error", true);
            return "change-password";
        }
        var user = (User) manager.loadUserByUsername(email);
        user.setPassword(encoder.encode(password));
        manager.updateUser(user);
        return "redirect:https://cmd-app.netlify.app/validate";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        model.addAttribute("error", false);
        return "recovery_email";
    }

    @PostMapping("forgotPassword")
    public String emailLink(@ModelAttribute("email") String email, Model model) {
        if (manager.userExists(email)) {
            publisher.publishEvent(new RecoverAccountEvent(email));
            model.addAttribute("message",
                    "Account recovery email sent to " + email);
            return "email_sent";
        }
        model.addAttribute("error", true);
        return "recovery_email";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

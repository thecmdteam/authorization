package com.cmd.authorization.services;

import com.cmd.authorization.dto.UserDTO;
import com.cmd.authorization.events.CreateNewUserEvent;
import com.cmd.authorization.model.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {
        private final JdbcUserDetailsManager userDetailsManager;
        private final PasswordEncoder passwordEncoder;
        private final ApplicationEventPublisher applicationEventPublisher;

    public UserService(UserDetailsService userDetailsManager, PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher) {
        this.userDetailsManager = (JdbcUserDetailsManager) userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    public boolean createUser(UserDTO user) {
        if (userDetailsManager.userExists(user.getEmail())) {
            return false;
        } else {
            User newUser = mapUser(user);
            userDetailsManager.createUser(newUser);
            applicationEventPublisher.publishEvent(new CreateNewUserEvent(user));
            return true;
        }
    }

    private User mapUser(UserDTO user) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUsername(user.getEmail());
        return newUser;
    }
}

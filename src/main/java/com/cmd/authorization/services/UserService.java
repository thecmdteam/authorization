package com.cmd.authorization.services;

import com.cmd.authorization.dto.UserDTO;
import com.cmd.authorization.events.CreateNewUserEvent;
import com.cmd.authorization.model.User;
import com.cmd.authorization.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public record UserService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          ApplicationEventPublisher applicationEventPublisher) {

    public boolean createUserMobile(UserDTO user) {
        var optionalUser = userRepository.findByUsername(user.getEmail());

        if (optionalUser.isPresent()) {
            return false;
        } else {
            User newUser = mapUser(user);
            userRepository.save(newUser);
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

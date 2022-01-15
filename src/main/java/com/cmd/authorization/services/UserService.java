package com.cmd.authorization.services;

import com.cmd.authorization.events.CreateNewUserEvent;
import com.cmd.authorization.model.User;
import com.cmd.authorization.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public boolean createUserMobile(User user) {
        var optionalUser = userRepository.findByEmail(user.getEmail());

        if (optionalUser.isPresent()) {
             return false;
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setDateCreated(new Date(System.currentTimeMillis()));
            userRepository.save(user);
            applicationEventPublisher.publishEvent(new CreateNewUserEvent(user));
            return true;
        }
    }
}

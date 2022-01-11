package com.cmd.authorization.services;

import com.cmd.authorization.dto.CreateUserDTOMobile;
import com.cmd.authorization.dto.UserDTO;
import com.cmd.authorization.model.User;
import com.cmd.authorization.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = new ModelMapper();
    }

    public ResponseEntity<Object> createUserMobile(CreateUserDTOMobile userDTO) {
        var optionalUser = userRepository.findByEmail(userDTO.getEmail());

        if (optionalUser.isPresent()) {
             return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User Already exists");
        }
        else {
            var user = modelMapper.map(userDTO, User.class);
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setDateCreated(new Date(System.currentTimeMillis()));
            user = userRepository.save(user);
            var createdUser = modelMapper.map(user, UserDTO.class);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(createdUser);
        }
    }
}

package com.cmd.authorization.events;

import com.cmd.authorization.dto.CreateUserDTO;
import org.springframework.stereotype.Component;

@Component
public record CreateNewUserEvent(CreateUserDTO userDTO) {
}

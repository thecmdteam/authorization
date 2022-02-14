package com.cmd.authorization.events;

import com.cmd.authorization.dto.UserDTO;
import com.cmd.authorization.model.User;

public record CreateNewUserEvent(UserDTO user) {
}

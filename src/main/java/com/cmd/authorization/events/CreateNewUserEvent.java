package com.cmd.authorization.events;

import com.cmd.authorization.model.User;

public record CreateNewUserEvent(User user) {
}

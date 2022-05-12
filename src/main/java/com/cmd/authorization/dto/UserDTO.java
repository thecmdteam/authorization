package com.cmd.authorization.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class UserDTO {
    private String firstName;
    private String lastName;
    private String password;
    private String username;
}

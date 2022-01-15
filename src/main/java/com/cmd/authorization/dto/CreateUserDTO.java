package com.cmd.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    private String fName;

    private String lName;

    private String email;

    private String password;

    private String matchingPassword;
}

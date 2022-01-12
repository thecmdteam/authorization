package com.cmd.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String phone;

    @NotBlank(message = "First name is mandatory")
    String fName;
    @NotBlank(message = "Last name is mandatory")
    String lName;

    String password;
    String matchingPassword;
}

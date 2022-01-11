package com.cmd.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTOMobile {
    @NonNull
    private String email;

    private String phone;
    @NonNull
    String fName;
    @NonNull
    String lName;
    @NonNull
    String password;
}

package com.cmd.authorization.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class User {
    @Id
    private String username;

    private String password;
    private boolean enabled = false;
    private boolean credentialsNonExpired = false;
    private boolean accountNonLocked = false;
    private boolean accountNonExpired = false;
}

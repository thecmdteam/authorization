package com.cmd.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    String id;

    @Indexed(unique=true)
    private String email;

    private String phone;
    private String fName;
    private String lName;
    private String password;
    private Date dateCreated;
    private Date lastUpdated;
    private String imageUrl;
    private String gitHubLink;
    private String linkedInLink;
    private Date dateOfBirth;
    private boolean isEnabled;
    private boolean isCredentialsNonExpired;
    private boolean isAccountNonLocked;
    private boolean isAccountNonExpired;
    private List<String> authorities;

}

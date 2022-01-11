package com.cmd.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    String id;

    private String email;

    private String phone;
    String fName;
    String lName;
    private Date dateCreated;
    private Date lastUpdated;
    private String imageUrl;
    private String gitHubLink;
    private String linkedInLink;
    private Date dateOfBirth;
}

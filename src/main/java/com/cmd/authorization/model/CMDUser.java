package com.cmd.authorization.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "user")
@Data
public class CMDUser {
    @Id
    private String id;
    private String fName;
    private String lName;
    @Indexed(unique = true)
    private String username;

    public CMDUser(String fName, String lName, String username) {
        this.fName = fName;
        this.lName = lName;
        this.username = username;
    }
}
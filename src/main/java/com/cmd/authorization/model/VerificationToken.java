package com.cmd.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {
    @Id
    private String tokenId;

    private String userEmail;
    private Date expiryDate;
}

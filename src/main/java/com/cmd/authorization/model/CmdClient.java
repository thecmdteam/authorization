package com.cmd.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdClient {
    @Id
    private String id;

    private String clientId;
    private String _rId;
    private RegisteredClient registeredClient;
}

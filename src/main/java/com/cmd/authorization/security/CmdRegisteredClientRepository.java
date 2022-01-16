package com.cmd.authorization.security;

import com.cmd.authorization.model.CmdClient;
import com.cmd.authorization.repositories.CmdClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

public class CmdRegisteredClientRepository implements RegisteredClientRepository {
    @Autowired
    private CmdClientRepository clientRepository;


    @Override
    public void save(RegisteredClient registeredClient) {
        if (registeredClient == null)
            return;
        var client = new CmdClient();
        client.set_rId(registeredClient.getId());
        client.setClientId(registeredClient.getClientId());
        client.setRegisteredClient(registeredClient);
        clientRepository.save(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        var optionalClient = clientRepository.findCmdClientBy_rId(id);
        if (optionalClient.isEmpty())
            return null;
        return optionalClient.get().getRegisteredClient();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        var optionalClient = clientRepository.findCmdClientByClientId(clientId);
        if (optionalClient.isEmpty())
            return null;
        return optionalClient.get().getRegisteredClient();
    }
}

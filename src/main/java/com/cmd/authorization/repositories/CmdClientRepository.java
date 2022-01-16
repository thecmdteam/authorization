package com.cmd.authorization.repositories;

import com.cmd.authorization.model.CmdClient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CmdClientRepository extends MongoRepository<CmdClient, String> {

    Optional<CmdClient> findCmdClientBy_rId(String Id);
    Optional<CmdClient> findCmdClientByClientId(String Id);
}

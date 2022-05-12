package com.cmd.authorization.repositories;

import com.cmd.authorization.model.CMDUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CMDUserRepository extends MongoRepository<CMDUser, String> {
}

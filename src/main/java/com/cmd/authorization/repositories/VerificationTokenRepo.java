package com.cmd.authorization.repositories;

import com.cmd.authorization.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationTokenRepo extends MongoRepository<VerificationToken, String> {
}

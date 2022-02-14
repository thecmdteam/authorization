package com.cmd.authorization.repositories;

import com.cmd.authorization.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken, String> {
}

package com.cmd.authorization.eventlisteners;

import com.cmd.authorization.events.CreateNewUserEvent;
import com.cmd.authorization.repositories.VerificationTokenRepo;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CreateUserEventListener {

    private final VerificationTokenRepo tokenRepo;

    public CreateUserEventListener(VerificationTokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @EventListener
    public void emailUser(CreateNewUserEvent newUserEvent) {

    }
}

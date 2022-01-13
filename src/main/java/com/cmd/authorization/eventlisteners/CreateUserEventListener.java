package com.cmd.authorization.eventlisteners;

import com.cmd.authorization.events.CreateNewUserEvent;
import com.cmd.authorization.model.VerificationToken;
import com.cmd.authorization.repositories.VerificationTokenRepo;
import com.cmd.authorization.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Date;

@Component
public class CreateUserEventListener {

    private final VerificationTokenRepo tokenRepo;
    private final EmailService emailService;

    @Value("${baseUrl}")
    private String baseUrl;

    public CreateUserEventListener(VerificationTokenRepo tokenRepo, EmailService emailService) {
        this.tokenRepo = tokenRepo;
        this.emailService = emailService;
    }

    @EventListener
    public void emailUser(CreateNewUserEvent newUserEvent) {
        var user = newUserEvent.userDTO();
        var verificationToken = new VerificationToken();
        verificationToken.setUserEmail(user.getEmail());
        verificationToken.setExpiryDate(addHoursToDate(new Date(System.currentTimeMillis()), 24));
        verificationToken = tokenRepo.save(verificationToken);
        var tokenId = verificationToken.getTokenId();
        var link = baseUrl+"/verify?token_id="+tokenId;
        try {
            emailService.sendVerifyEmailMessage(user.getEmail(), link);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
}

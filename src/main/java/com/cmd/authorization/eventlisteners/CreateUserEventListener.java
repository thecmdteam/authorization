package com.cmd.authorization.eventlisteners;

import com.cmd.authorization.events.CreateNewUserEvent;
import com.cmd.authorization.events.RecoverAccountEvent;
import com.cmd.authorization.model.CMDUser;
import com.cmd.authorization.model.VerificationToken;
import com.cmd.authorization.repositories.CMDUserRepository;
import com.cmd.authorization.repositories.VerificationTokenRepo;
import com.cmd.authorization.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class CreateUserEventListener {

    private final VerificationTokenRepo tokenRepo;
    private final EmailService emailService;
    ;
    private final CMDUserRepository repository;

    @Value("${baseUrl}")
    private String baseUrl;

    public CreateUserEventListener(VerificationTokenRepo tokenRepo, EmailService emailService,
                                   CMDUserRepository repository) {
        this.tokenRepo = tokenRepo;
        this.emailService = emailService;
        this.repository = repository;
    }

    @EventListener
    public void emailUser(CreateNewUserEvent newUserEvent) {
        var user = newUserEvent.user();

        // save user to mongodb
        var cmdUser = new CMDUser(user.getFirstName(), user.getLastName(),
                user.getUsername());
        repository.save(cmdUser);

        var sendMail = newUserEvent.sendMail();

        if (sendMail) {
            VerificationToken verificationToken = getVerificationToken(user.getUsername());
            var tokenId = verificationToken.getTokenId();
            var subject = "CMD: Verify Your Account";
            var message =  """
                Please click on the link below to reset your password.
                Note that this email will expire within 24 hours.
                """;
            var text = message + baseUrl+"/verify?token_id="+tokenId;
            try {
                emailService.sendVerifyEmailMessage(user.getUsername(), subject, text);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }


    }


    @EventListener
    public void recoverAccount(RecoverAccountEvent event) {
        var email = event.email();
        VerificationToken verificationToken = getVerificationToken(email);
        var tokenId = verificationToken.getTokenId();
        var subject = "CMD: Recover Your Account";
        var message =  """
                Please click on the link below to reset your password.
                Note that this email will expire within 24 hours.
                """;
        var text = message + baseUrl + "/recover?token_id="+tokenId;
        try {
            emailService.sendVerifyEmailMessage(email, subject, text);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private VerificationToken getVerificationToken(String user) {
        var verificationToken = new VerificationToken();
        verificationToken.setUsername(user);
        verificationToken.setExpiryDate(addHoursToDate(new Date(System.currentTimeMillis())));
        verificationToken.setTokenId(UUID.randomUUID().toString());
        verificationToken = tokenRepo.save(verificationToken);
        return verificationToken;
    }
    private Date addHoursToDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        return calendar.getTime();
    }
}

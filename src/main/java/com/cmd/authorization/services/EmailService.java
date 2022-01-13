package com.cmd.authorization.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {


    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendVerifyEmailMessage(
            String to, String link) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Verify your email now");
        helper.setText(
                "<html>"
                        + "<body>"
                        + "<img src='cid:image'/>"
                        + "<div style='margin:10px'><strong>Thank you for joining CMD. "
                        + "Click <a href='"+link+"'>here</a> to verify your email address. This link will "
                        +"expire within 24 hours</strong>"
                        + "<div>"
                , true);
        helper.addInline("image",
                new File("src/main/resources/static/images/cmdverifyEmail.png"));


        emailSender.send(message);
    }

}


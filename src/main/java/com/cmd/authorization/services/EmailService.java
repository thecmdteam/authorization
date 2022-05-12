package com.cmd.authorization.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
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
            String to, String subject, String text) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();


        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);



        emailSender.send(message);
    }

}


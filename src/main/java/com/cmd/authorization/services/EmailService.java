package com.cmd.authorization.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    public void sendMessageWithImage(
            String to, String subject, String key) {

        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(
                    "<html>"
                            + "<body>"
                            + "<img src='cid:image'/>"
                            + "<div style='margin:10px'><strong>Thanks for verifying your CMD account</strong>"
                            + "<div style='margin:30px 50px'><strong style='font-size: 26px'>Your code is: " + key + "</strong></div>"
                            + "<div>"
                    , true);
            helper.addInline("image",
                    new File("src/main/resources/images/cmdverifyEmail.png"));


            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

package ru.simankovdju.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;

    public void sendMessageWithAttachment(
            String toEmail, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);

        log.info("Mail sent to {} successfully", toEmail);
    }


}

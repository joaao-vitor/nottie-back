package com.nottie.service;

import com.nottie.config.EmailConfig;
import com.nottie.mail.EmailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final EmailSender emailSender;
    private final EmailConfig emailConfig;

    public EmailService(EmailSender emailSender, EmailConfig emailConfig) {
        this.emailSender = emailSender;
        this.emailConfig = emailConfig;
    }

    public void sendSimpleMail(String to, String subject, String message) {
        emailSender
                .to(to)
                .withSubject(subject)
                .withMessage(message)
                .send(emailConfig);

    }
}

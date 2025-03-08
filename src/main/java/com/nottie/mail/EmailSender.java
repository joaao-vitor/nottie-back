package com.nottie.mail;

import com.nottie.config.EmailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;

@Component
public class EmailSender {
    private final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private final JavaMailSender mailSender;
    private String to;
    private String subject;
    private String body;
    private ArrayList<InternetAddress> recipients;
    private File attachment;

    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public EmailSender to(String to) {
        this.to = to;
        this.recipients = getRecipients(to);
        return this;
    }

    private ArrayList<InternetAddress> getRecipients(String to) {
        String toWithoutSpaces = to.replace(" ", "");
        String[] toAddresses = toWithoutSpaces.split(";");
        ArrayList<InternetAddress> recipients = new ArrayList<>();

        for (String address : toAddresses) {
            try {
                recipients.add(new InternetAddress(address));
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        }
        return recipients;
    }

    public EmailSender withSubject(String subject) {
        this.subject = subject;
        return this;

    }

    public EmailSender withMessage(String body) {
        this.body = body;
        return this;

    }

    public EmailSender attach(String fileDir) {
        this.attachment = new File(fileDir);
        return this;
    }

    public void send(EmailConfig emailConfig) {
        System.out.println(emailConfig.getUsername());
        System.out.println(emailConfig.getPassword());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(emailConfig.getUsername());
            mimeMessageHelper.setTo(recipients.toArray(new InternetAddress[0]));
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);
            if (attachment != null) {
                mimeMessageHelper.addAttachment(attachment.getName(), attachment);
            }
            mailSender.send(mimeMessage);
            
            logger.info("Email sent to " + to);
        
            reset();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private void reset() {
        this.to = null;
        this.subject = null;
        this.body = null;
        this.recipients = null;
        this.attachment = null;
    }
}

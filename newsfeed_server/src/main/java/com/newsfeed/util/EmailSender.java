package com.newsfeed.util;

import com.newsfeed.model.NotificationHistory;
import com.newsfeed.util.constants.Messages;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EmailSender {

    public static void sendEmails(List<NotificationHistory> notifications,
                                                   Map<String, String> userEmails) {
        for (NotificationHistory notification : notifications) {
            String userId = notification.getUserId();
            String email = userEmails.get(userId);
            if (email != null && !email.isBlank()) {
            	sendEmailToUser(email, notification);
            }
        }
    }

    private static void sendEmailToUser(String recipientEmail, NotificationHistory notification) {
        String senderEmail = ApplicationProperties.get("mail.username");
        String senderPassword = ApplicationProperties.get("mail.password");

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", ApplicationProperties.get("mail.smtp.auth"));
        mailProps.put("mail.smtp.starttls.enable", ApplicationProperties.get("mail.smtp.starttls.enable"));
        mailProps.put("mail.smtp.host", ApplicationProperties.get("mail.smtp.host"));
        mailProps.put("mail.smtp.port", ApplicationProperties.get("mail.smtp.port"));

        Session session = Session.getInstance(mailProps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(notification.getTitle());

            String body = String.format(Messages.EMAIL_FORMAT, notification.getMessage());
            message.setText(body);

            Transport.send(message);

        } catch (MessagingException exception) {
            System.err.println("Failed to send email to " + recipientEmail + ": " + exception.getMessage());
        }
    }
}

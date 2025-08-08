package com.telerik.utils;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailUtil {
    public static void sendEmailWithAttachment(String zipFilePath) throws Exception {
        Properties props = new Properties();
        props.load(EmailUtil.class.getResourceAsStream("/configurations/email.properties"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        String username = props.getProperty("mail.username");
        String password = props.getProperty("mail.password");
        String to = props.getProperty("mail.to");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Test Automation Report - Execution Summary");

        // Body part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Hi,\n\nPlease find the attached test execution report.\n\nRegards,\nAutomation Bot");

        // Attachment part
        MimeBodyPart attachmentPart = new MimeBodyPart();
        File file = new File(zipFilePath);
        if (!file.exists() || file.length() == 0) {
            throw new IOException("Attachment file not found or empty: " + zipFilePath);
        }
        attachmentPart.attachFile(file);

        // Combine
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);
        Transport.send(message);
    }

    public static void main(String[] args) {
        try {
            sendEmailWithAttachment("extent-reports/01_08_2025_17_03_28.zip");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


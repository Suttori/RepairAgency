package com.suttori.service;

import com.suttori.ProjectProperties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.UUID;

public class EmailSenderService {

    private final String username = ProjectProperties.getProperty("mail.username");
    private final String password = ProjectProperties.getProperty("mail.password");

    public void send(String subject, String text, String toEmail){
        Session session = Session.getDefaultInstance(ProjectProperties.getEmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //тема сообщения
            message.setSubject(subject);
            //текст
            message.setText(text);

            //отправляем сообщение
            Transport.send(message);
            System.out.println("Сообщение отправлено");
        } catch (MessagingException e) {
            System.out.println("----------------------------------------");
            e.printStackTrace();
        }
    }
}

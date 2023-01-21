package com.suttori.service;

import com.suttori.ProjectProperties;
import com.suttori.entity.User;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

import java.util.UUID;

public class EmailSenderService {

    private final Logger log = Logger.getLogger(EmailSenderService.class);
    private final String username = ProjectProperties.getProperty("mail.username");
    private final String password = ProjectProperties.getProperty("mail.password");

    public void sendActivationCode(User user) {
        String message = String.format("Hello, %s, Welcome to Repair Agency, Please visit next link to activation your email:  " +
        ProjectProperties.getProperty("host") + "/activate/?code=%s", user.getFirstName(), user.getActivationCode());

        send("Registration on Repair Agency", message, "kyepta888@gmail.com");
    }





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
            System.out.println("---------------------------------------- сообщение не отправлено");
            e.printStackTrace();
        }
    }
}

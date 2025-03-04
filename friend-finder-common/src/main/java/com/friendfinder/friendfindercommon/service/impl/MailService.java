package com.friendfinder.friendfindercommon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * MailService is a service class responsible for sending email notifications asynchronously using the Spring MailSender.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>mailSender: The MailSender interface, which provides the functionality to send email messages.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>sendMail(to, subject, text): Sends an email message to the specified recipient asynchronously. The method
 *     accepts the recipient's email address, subject, and text content as parameters, and it sends the email using the
 *     MailSender's SimpleMailMessage.</li>
 *     <li>sendFromMail(fromEmail, subject, name, text): Sends an email message from a specified sender's email address
 *     to a predefined recipient email address asynchronously. The method accepts the sender's email address, subject,
 *     sender's name, and text content as parameters. The email is sent to the predefined email address, and it includes
 *     information about the sender's name, email address, and the text content provided.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * MailService is used to send email notifications in an asynchronous manner, making it suitable for scenarios where email
 * sending should not block the main application thread. It is typically used in various parts of the application to send
 * email notifications to users or administrators. For example, it can be utilized to send email verification links during
 * user registration, to notify users about new friend requests, or to notify administrators about important events or
 * issues in the application. By using the MailService's asynchronous methods, the application can continue executing other
 * tasks without waiting for the email to be sent, enhancing the overall performance and responsiveness of the application.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;

    @Async
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        mailSender.send(simpleMailMessage);
    }

    @Async
    public void sendFromMail(String fromEmail, String subject, String name, String text) {
        String toEmail = "finderfriend2023@gmail.com";
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText("Sender: " + name + ", " + fromEmail
                + "\nmessage: " + text);

        mailSender.send(message);
    }
}

package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.service.impl.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private MailSender mailSender;

    @InjectMocks
    private MailService mailService;

    @Test
    void testSendMail() throws InterruptedException {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        mailService.sendMail(to, subject, text);

        latch.await(5, TimeUnit.SECONDS);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendFromMail() throws InterruptedException {
        String fromEmail = "sender@example.com";
        String subject = "Test Subject";
        String name = "John Doe";
        String text = "Test Message";

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        mailService.sendFromMail(fromEmail, subject, name, text);

        latch.await(5, TimeUnit.SECONDS);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
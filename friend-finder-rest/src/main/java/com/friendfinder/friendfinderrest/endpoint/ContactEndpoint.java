package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.contactDto.ContactFormRequestDto;
import com.friendfinder.friendfindercommon.exception.custom.FilterMailContactException;
import com.friendfinder.friendfindercommon.service.impl.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API endpoint for handling contact form submissions.
 *
 * <p>This class provides an endpoint for users to send a contact form with their name, email,
 * subject, and message.
 */
@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
@Slf4j
public class ContactEndpoint {

    private final MailService mailService;

    /**
     * Sends a contact form submission to the specified email address.
     *
     * @param contactFormRequestDto The DTO containing the contact form information.
     * @return ResponseEntity with a message indicating the status of the email sending.
     */
    @PostMapping("/send-contact-form")
    public ResponseEntity<String> sendContact(
            @RequestBody ContactFormRequestDto contactFormRequestDto
    ) {
        if (!filterMailContact(contactFormRequestDto)) {
            log.error("wrong contact form data, class: ContactEndpoint, method: sendContact", new FilterMailContactException("wrong contact form data"));
            return ResponseEntity.badRequest().build();
        }

        mailService.sendFromMail(
                contactFormRequestDto.getEmail(),
                contactFormRequestDto.getSubject(),
                contactFormRequestDto.getName(),
                contactFormRequestDto.getText()
        );

        return ResponseEntity.ok("Mail Sent!");
    }

    /**
     * Filters the contact form data to ensure that required fields are not null.
     *
     * @param contactFormRequestDto The DTO containing the contact form information.
     * @return True if all required fields are not null, false otherwise.
     */
    boolean filterMailContact(ContactFormRequestDto contactFormRequestDto) {
        return contactFormRequestDto.getSubject() != null &&
                contactFormRequestDto.getEmail() != null &&
                contactFormRequestDto.getText() != null &&
                contactFormRequestDto.getName() != null;
    }
}

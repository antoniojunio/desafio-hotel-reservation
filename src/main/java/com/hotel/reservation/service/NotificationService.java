
package com.hotel.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    public void sendConfirmation(String customerEmail, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(customerEmail);
        email.setSubject("Confirmação de Reserva");
        email.setText(message);

        mailSender.send(email);
        System.out.println("Email enviado para: " + customerEmail);
    }
}

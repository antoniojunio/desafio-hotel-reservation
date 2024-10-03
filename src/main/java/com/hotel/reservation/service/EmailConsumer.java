package com.hotel.reservation.service;

import com.hotel.reservation.config.RabbitMQConfig;
import com.hotel.reservation.model.EmailMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void receiveEmailMessage(EmailMessage emailMessage) {
        try {
            System.out.println("Enviando e-mail para: " + emailMessage.getTo());
        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail, tentando novamente: " + e.getMessage());
            throw e;
        }
    }
}
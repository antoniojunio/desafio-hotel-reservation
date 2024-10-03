package com.hotel.reservation.controller;

import com.hotel.reservation.dto.ReservationDto;
import com.hotel.reservation.model.EmailMessage;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.service.EmailProducer;
import com.hotel.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmailProducer emailProducer;

    @PostMapping
    public Reservation createReservation(@RequestBody ReservationDto reservationDto) {
        Reservation reservation = reservationService.createReservation(
                reservationDto.getHotelId(),
                reservationDto.getRoomId(),
                reservationDto.getCustomerName(),
                reservationDto.getCustomerEmail(),
                reservationDto.getCheckIn(),
                reservationDto.getCheckOut()
        );


        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(reservationDto.getCustomerEmail());
        emailMessage.setSubject("Confirmação de Reserva");
        emailMessage.setBody("Sua reserva foi confirmada para o período de " + reservationDto.getCheckIn() + " até " + reservationDto.getCheckOut() + ".");

        emailProducer.sendEmailMessage(emailMessage);

        return reservation;
    }
}
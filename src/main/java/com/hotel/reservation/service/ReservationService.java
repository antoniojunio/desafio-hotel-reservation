package com.hotel.reservation.service;

import com.hotel.reservation.model.Hotel;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.Room;
import com.hotel.reservation.repository.ReservationRepository;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private NotificationService notificationService;  // Serviço de Notificações

    @Transactional
    public Reservation createReservation(Long hotelId, Long roomId, String customerName, String customerEmail, LocalDate checkIn, LocalDate checkOut) {

        log.info("Verificando quarto com ID: {}, hotelId: {}, isAvailable: {}", roomId, hotelId, true);

        log.info("Iniciando criação de reserva para o Hotel ID: {}, Quarto ID: {}, Cliente: {}", hotelId, roomId, customerName);

        Hotel hotel = hotelService.getHotelById(hotelId);
        log.info("Hotel encontrado: {}", hotel.getName());

        Room room = roomRepository.findByIdAndHotelIdAndIsAvailable(roomId, hotelId, true).orElseThrow(() -> {
            log.error("Room with ID {} not found or not available for Hotel with ID {}", roomId, hotelId);
            return new EntityNotFoundException("Quarto não encontrado ou não disponível para o hotel especificado");
        });

        if (!room.getHotel().getId().equals(hotelId)) {
            log.error("Room with ID {} does not belong to Hotel with ID {}", roomId, hotelId);
            throw new EntityNotFoundException("Quarto não pertence ao hotel especificado");
        }

        log.info("Quarto encontrado: {} (Tipo: {}, Capacidade: {}, Disponível: {})", room.getId(), room.getRoomType(), room.getCapacity(), room.isAvailable());

        if (!room.isAvailable()) {
            log.error("Quarto com ID {} não está disponível para reserva", roomId);
            throw new IllegalStateException("Quarto não disponível");
        }

        Reservation reservation = new Reservation();
        reservation.setCustomerName(customerName);
        reservation.setCustomerEmail(customerEmail);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setHotel(hotel);
        reservation.setRoom(room);
        reservation.setStatus("PENDING");

        log.info("Reserva criada com sucesso para o cliente {}, período: {} até {}", customerName, checkIn, checkOut);

        // Atualizar disponibilidade do quarto
        room.setAvailable(false);
        roomRepository.save(room);
        log.info("Quarto ID: {} atualizado com sucesso, Disponível: {}", room.getId(), room.isAvailable());

        // Salvar a reserva no banco de dados
        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reserva salva no banco de dados com ID: {}", savedReservation.getId());

        // Preparar mensagem de confirmação
        String message = String.format("Reserva confirmada para %s no hotel %s de %s a %s", reservation.getCustomerName(), hotel.getName(), checkIn, checkOut);

        // Enviar notificação de confirmação após a transação estar concluída
        try {
            notificationService.sendConfirmation(customerEmail, message);
            log.info("Notificação de confirmação enviada com sucesso para o email: {}", customerEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar notificação: {}", e.getMessage(), e);
            // Aqui você pode decidir se quer reverter a reserva ou apenas logar o erro
        }

        return savedReservation;
    }

    public List<Reservation> getReservationsByCustomer(String email) {
        return reservationRepository.findByCustomerEmail(email);
    }
}
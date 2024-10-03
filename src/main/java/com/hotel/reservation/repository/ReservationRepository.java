package com.hotel.reservation.repository;

import com.hotel.reservation.model.Hotel;
import com.hotel.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomerEmail(String email);

    List<Reservation> findByHotel(Hotel hotel);

    void deleteByHotelId(Long hotelId);

    void deleteByRoomId(Long id);
}
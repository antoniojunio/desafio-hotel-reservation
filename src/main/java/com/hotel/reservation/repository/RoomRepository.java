package com.hotel.reservation.repository;

import com.hotel.reservation.model.Hotel;
import com.hotel.reservation.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelAndIsAvailable(Hotel hotel, boolean isAvailable);
    List<Room> findByHotelId(Long hotelId);
    Optional<Room> findByIdAndHotelIdAndIsAvailable(Long roomId, Long hotelId, boolean isAvailable);

    void deleteByHotelId(Long hotelId);
}

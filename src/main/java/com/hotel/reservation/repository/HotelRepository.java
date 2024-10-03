package com.hotel.reservation.repository;

import com.hotel.reservation.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT h FROM Hotel h WHERE h.location = :location AND h.availableRooms >= :rooms " +
            "AND h.id NOT IN (SELECT r.hotel.id FROM Room r JOIN r.reservations b WHERE (b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)) " +
            "AND EXISTS (SELECT r FROM Room r WHERE r.hotel = h AND r.capacity >= :guests)")
    List<Hotel> findHotelsByCriteria(@Param("location") String location,
                                     @Param("checkIn") LocalDate checkIn,
                                     @Param("checkOut") LocalDate checkOut,
                                     @Param("rooms") int rooms,
                                     @Param("guests") int guests);


    List<Hotel> findByLocation(String location);
}
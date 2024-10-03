package com.hotel.reservation.service;

import com.hotel.reservation.model.Hotel;
import com.hotel.reservation.model.Room;
import com.hotel.reservation.repository.HotelRepository;
import com.hotel.reservation.repository.ReservationRepository;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    public List<Hotel> searchHotels(String location, LocalDate checkIn, LocalDate checkOut, int rooms, int guests) {
        return hotelRepository.findHotelsByCriteria(location, checkIn, checkOut, rooms, guests);
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Hotel não encontrado"));
    }

    public Hotel createHotel(Hotel hotel) {
        List<Room> rooms = hotel.getRooms();

        if (rooms != null && !rooms.isEmpty()) {
            for (int i = 0; i < rooms.size(); i++) {
                Room room = rooms.get(i);
                if (room.getId() != null) {
                    Room managedRoom = roomRepository.findById(room.getId())
                            .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Room not found with id: " + room.getId()));
                    rooms.set(i, managedRoom);
                } else {
                    room.setHotel(hotel);
                }
            }
        }

        hotel.setRooms(rooms);
        return hotelRepository.save(hotel);
    }

    public List<Hotel> compareHotels(String location, List<String> amenities, double maxPrice, int rating) {
        List<Hotel> hotels = hotelRepository.findByLocation(location);

        hotels = hotels.stream()
                .filter(hotel -> hotel.getAmenities().containsAll(amenities))
                .collect(Collectors.toList());

        hotels = hotels.stream()
                .filter(hotel -> hotel.getRooms().stream().anyMatch(room -> room.getPrice() <= maxPrice))
                .filter(hotel -> hotel.getRating() >= rating)
                .collect(Collectors.toList());

        return hotels;
    }

    @Transactional
    public void deleteHotel(Long hotelId) {
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        for (Room room : rooms) {
            reservationRepository.deleteByRoomId(room.getId());
        }
        roomRepository.deleteByHotelId(hotelId);

        hotelRepository.deleteById(hotelId);
    }
}
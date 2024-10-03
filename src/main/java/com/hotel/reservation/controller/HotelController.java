package com.hotel.reservation.controller;

import com.hotel.reservation.model.Hotel;
import com.hotel.reservation.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public List<Hotel> searchHotels(
            @RequestParam String location,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam int rooms,
            @RequestParam int guests) {

        return hotelService.searchHotels(location, checkIn, checkOut, rooms, guests);
    }

    @GetMapping("/compare")
    public List<Hotel> compareHotels(
            @RequestParam String location,
            @RequestParam List<String> amenities,
            @RequestParam double maxPrice,
            @RequestParam int rating) {
        return hotelService.compareHotels(location, amenities, maxPrice, rating);
    }

    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelService.createHotel(hotel);
    }

    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
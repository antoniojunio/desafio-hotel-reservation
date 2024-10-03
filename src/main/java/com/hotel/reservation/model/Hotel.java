package com.hotel.reservation.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private int rating;
    private String name;
    private String location;
    private int availableRooms;
    private double pricePerNight;

    @ElementCollection
    private List<String> amenities;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Reservation> reservations;
}
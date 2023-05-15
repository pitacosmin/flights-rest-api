package com.flightsapp.flights.app.server.rest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;
    private String title;
    private String firstName;
    private String lastName;
    private String seatNumber;
    @Column(unique = true)
    private Long passportId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "reservation_id",
            referencedColumnName = "reservationId"
    )
    private Reservation reservation;


}

package com.flightsapp.flights.app.server.rest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long seatId;

    @ManyToOne()
    @JoinColumn(
            name = "flight_id",
            referencedColumnName = "flightId"
    )
    private Flight flight;

    //add length constraint
    private String seatNumber;
}

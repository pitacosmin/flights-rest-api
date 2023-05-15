package com.flightsapp.flights.app.server.rest.entity;

import com.flightsapp.flights.app.server.rest.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private LocalDate registeredAt;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private AppUser user;

    private Float totalPrice;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name="flight_id",
            referencedColumnName = "flightId"
    )
    private Flight flight;
}

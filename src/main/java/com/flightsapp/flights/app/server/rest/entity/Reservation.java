package com.flightsapp.flights.app.server.rest.entity;

import com.flightsapp.flights.app.server.rest.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private LocalDateTime registeredAt;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private AppUser user;

    private Float totalPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name="departure_flight_id",
            referencedColumnName = "flightId"
    )
    private Flight departureFlight;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name="return_flight_id",
            referencedColumnName = "flightId"
    )
    private Flight returnFlight;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "reservation_id",
            referencedColumnName = "reservationId"
    )
    private List<Passenger> passengers;

}

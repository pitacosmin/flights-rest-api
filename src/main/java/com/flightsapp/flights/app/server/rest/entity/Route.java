package com.flightsapp.flights.app.server.rest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {
    @Id
    private String flightNumber;
    @Temporal(TemporalType.TIME)
    private Date departureTime;
    @Temporal(TemporalType.TIME)
    private Date arrivalTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "origin",
            referencedColumnName = "airportIataCode"
    )
    private Airport originAirport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "destination",
            referencedColumnName = "airportIataCode"
    )
    private Airport destinationAirport;
}

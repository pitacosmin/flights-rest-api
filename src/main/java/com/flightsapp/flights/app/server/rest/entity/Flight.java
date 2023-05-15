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
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "flight_number",
            referencedColumnName = "flightNumber"
    )
    private Route route;

    @Temporal(TemporalType.DATE)
    private Date departureDate;
    @Temporal(TemporalType.DATE)
    private Date arrivalDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "airplane_id",
            referencedColumnName = "airplaneId"
    )
    private Airplane airplane;
}

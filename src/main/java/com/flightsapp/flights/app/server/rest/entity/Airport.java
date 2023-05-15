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
public class Airport {
    @Id
    @Column(length = 3)
    private String airportIataCode;
    private String airportName;
    private float latitude;
    private float longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "city_id",
            referencedColumnName = "cityId"
    )
    private City city;
}

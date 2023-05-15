package com.flightsapp.flights.app.server.rest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Airplane {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long airplaneId;
    private String model;
    private int seatingCapacity;
    private int columns;
    private float avgSpeed;
    private int[] layout;
}

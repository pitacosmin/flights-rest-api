package com.flightsapp.flights.app.server.rest.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ReservationDTO {
    private String email;
    private Float price;
    private FlightDTO flights;
    private List<PassengerDTO> passengers;
}

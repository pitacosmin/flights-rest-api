package com.flightsapp.flights.app.server.rest.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CheckoutMetadataDTO {
    private String email;
    private Long price;
    private FlightDTO flights;
    private List<PassengerDTO> passengers;
}

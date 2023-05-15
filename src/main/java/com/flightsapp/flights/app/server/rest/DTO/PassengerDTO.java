package com.flightsapp.flights.app.server.rest.DTO;

import lombok.Data;

@Data
public class PassengerDTO {
    private String title;
    private String firstName;
    private String lastName;
    private String passportId;
    private String departureSeatNumber;
    private String returnSeatNumber;
}

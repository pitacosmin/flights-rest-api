package com.flightsapp.flights.app.server.rest.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class FlightDTO {
    private String departureTicket;
    private String returnTicket;
    private Long departurePrice;
    private Long returnPrice;
    private String origin;
    private String destination;
    private Date departureDate;
    private Date returnDate;
}

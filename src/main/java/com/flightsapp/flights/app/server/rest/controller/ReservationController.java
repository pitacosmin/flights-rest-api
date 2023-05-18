package com.flightsapp.flights.app.server.rest.controller;

import com.flightsapp.flights.app.server.rest.DTO.FlightDTO;
import com.flightsapp.flights.app.server.rest.DTO.PassengerDTO;
import com.flightsapp.flights.app.server.rest.DTO.ReservationDTO;
import com.flightsapp.flights.app.server.rest.entity.Reservation;
import com.flightsapp.flights.app.server.rest.service.ReservationService;
import com.flightsapp.flights.app.server.rest.user.AppUser;
import com.flightsapp.flights.app.server.rest.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/reservation")
@CrossOrigin
public class ReservationController {

    @Autowired
    ReservationService reservationService;


    @PostMapping(path = "/create")
    public ResponseEntity<Reservation> createReservation(
            @RequestBody ReservationDTO reservationDTO
    ) {

        Reservation reservation = reservationService.createReservation(
                reservationDTO.getEmail(),
                reservationDTO.getPrice(),
                reservationDTO.getFlights(),
                reservationDTO.getPassengers()
        );

        return ResponseEntity.ok(reservation);
    }

}

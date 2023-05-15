package com.flightsapp.flights.app.server.rest.controller;

import com.flightsapp.flights.app.server.rest.entity.Airplane;
import com.flightsapp.flights.app.server.rest.repository.AirplaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/airplanes")
@CrossOrigin
public class AirplaneController {
    @Autowired
    AirplaneRepository airplaneRepository;

    @GetMapping(path = "/layout/{airplaneId}")
    public ResponseEntity<int[]> findLayoutByAirplaneId(@PathVariable Long airplaneId) {
        Airplane airplane = airplaneRepository.findById(airplaneId).orElse(null);
        if(airplane == null)
            return null;
        int[] layout = airplane.getLayout();

        return ResponseEntity.ok(layout);
    }

    @GetMapping(path = "/layout/flight/{flightId}")
    public ResponseEntity<int[]> findLayoutByFlightId(@PathVariable Long flightId) {
        Airplane airplane = airplaneRepository.findByFlightFlightId(flightId);
        int[] layout = airplane.getLayout();

        return ResponseEntity.ok(layout);
    }

    @GetMapping(path = "/capacity/flight/{flightId}")
    public ResponseEntity<Integer> findCapacityByFlightId(@PathVariable Long flightId) {
        Airplane airplane = airplaneRepository.findByFlightFlightId(flightId);
        int capacity = airplane.getSeatingCapacity();

        return ResponseEntity.ok(capacity);
    }

}


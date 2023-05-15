package com.flightsapp.flights.app.server.rest.controller;

import com.flightsapp.flights.app.server.rest.entity.Airplane;
import com.flightsapp.flights.app.server.rest.entity.Flight;
import com.flightsapp.flights.app.server.rest.entity.Route;
import com.flightsapp.flights.app.server.rest.repository.AirplaneRepository;
import com.flightsapp.flights.app.server.rest.repository.FlightRepository;
import com.flightsapp.flights.app.server.rest.repository.RouteRepository;
import com.flightsapp.flights.app.server.rest.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequestMapping(path = "/flights")
@CrossOrigin
public class FlightController {

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    FlightService flightService;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    AirplaneRepository airplaneRepository;

    @GetMapping(path = "/{flightId}/price")
    public ResponseEntity<Double> getBasePriceForFlight(@PathVariable Long flightId) {
        double basePrice = flightService.calculateBasePrice(flightId);
        return ResponseEntity.ok(basePrice);
    }

    @GetMapping(path = "/{originIata}/{destinationIata}/departure-dates")
    public ResponseEntity<List<Date>> findDepartureDates(
            @PathVariable String originIata,
            @PathVariable String destinationIata
    ) {
        List<Date> departureDates = flightRepository.findDepartureDates(originIata, destinationIata);

        return ResponseEntity.ok(departureDates);
    }

    @GetMapping(path = "/{originIata}/{destinationIata}/{departureDate}")
    public ResponseEntity<List<Flight>> findByRouteOriginAirportAndRouteDestinationAirportAndDepartureDate(
            @PathVariable String originIata,
            @PathVariable String destinationIata,
            @PathVariable("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date departureDate
    ){
        List<Flight> flights = flightRepository.findByRouteOriginAirportAndRouteDestinationAirportAndDepartureDate(originIata, destinationIata, departureDate);

        return ResponseEntity.ok(flights);
    }

    @PostMapping(path = "/admin/populateTable")
    public void populateTable() {
        Flight.FlightBuilder flightBuilder = Flight.builder();

        //get all routes
        List<Route> routes = routeRepository.findAll();
        //for each route save a flight with random dates
        for(Route route : routes){
            //generate random dates for each route
            List<Date> dates = flightService.generateDates();
            for (Date date : dates) {
                flightBuilder.route(route);
                flightBuilder.departureDate(date);

                Random random = new Random(System.currentTimeMillis());
                long aiplaneID = random.nextLong(5) + 1;
                Airplane airplane = airplaneRepository.findById(aiplaneID).orElse(null);
                flightBuilder.airplane(airplane);

                //check if arrival is on same day as departure
                //also handle the case where departure 31st -> arrival 1st
                if(!flightService.hasDayPassed(route)) {
                    flightBuilder.arrivalDate(date);
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE,1);
                    flightBuilder.arrivalDate(calendar.getTime());
                }
                Flight flight = flightBuilder.build();
                flightRepository.save(flight);
            }
        }
    }

    @PostMapping(path = "/admin/populate")
    public ArrayList<Flight> testPopulateTable() {
        ArrayList<Flight> flights = new ArrayList<>();
        Flight.FlightBuilder flightBuilder = Flight.builder();

        //get all routes
        List<Route> routes = routeRepository.findAll();
        //for each route save a flight with random dates
        for(Route route : routes){
            //generate random dates for each route
            List<Date> dates = flightService.generateDates();
            flightBuilder.route(route);
            for (Date date : dates) {
//                flightBuilder.route(route);
                flightBuilder.departureDate(date);

                Random random = new Random(System.currentTimeMillis());
                long aiplaneID = random.nextLong(5 - 1 + 1) + 1;
                Airplane airplane = airplaneRepository.findById(aiplaneID).orElse(null);
                flightBuilder.airplane(airplane);

                //check if arrival is on same day as departure
                //also handle the case where departure 31st -> arrival 1st
                if(flightService.hasDayPassed(route)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE,1);
                    flightBuilder.arrivalDate(calendar.getTime());
                } else {
                    flightBuilder.arrivalDate(date);
                }
                Flight flight = flightBuilder.build();
                flights.add(flight);
            }
        }

        return flights;
    }
}

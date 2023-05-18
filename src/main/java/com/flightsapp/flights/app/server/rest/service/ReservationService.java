package com.flightsapp.flights.app.server.rest.service;

import com.flightsapp.flights.app.server.rest.DTO.FlightDTO;
import com.flightsapp.flights.app.server.rest.DTO.PassengerDTO;
import com.flightsapp.flights.app.server.rest.entity.Flight;
import com.flightsapp.flights.app.server.rest.entity.Passenger;
import com.flightsapp.flights.app.server.rest.entity.Reservation;
import com.flightsapp.flights.app.server.rest.entity.Seat;
import com.flightsapp.flights.app.server.rest.repository.FlightRepository;
import com.flightsapp.flights.app.server.rest.repository.PassenerRepository;
import com.flightsapp.flights.app.server.rest.repository.ReservationRepository;
import com.flightsapp.flights.app.server.rest.repository.SeatRepository;
import com.flightsapp.flights.app.server.rest.user.AppUser;
import com.flightsapp.flights.app.server.rest.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PassenerRepository passenerRepository;

    public Reservation createReservation(String email, Float price, FlightDTO flightDTO, List<PassengerDTO> passengerDTO) {

        Reservation.ReservationBuilder reservationBuilder = Reservation.builder();

        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        reservationBuilder.user(user);
        reservationBuilder.totalPrice(price);
        reservationBuilder.registeredAt(LocalDateTime.now());

        //get departure flight
        Flight departureFlight = flightRepository.findById(flightDTO.getDepartureTicket()).orElse(null);
        reservationBuilder.departureFlight(departureFlight);
        System.out.println("departureFlight = " + departureFlight);


        Flight returnFlight = null;
        //get return flight if exists
        if(flightDTO.getReturnTicket() != null) {
            returnFlight = flightRepository.findById(flightDTO.getReturnTicket()).orElse(null);
            reservationBuilder.returnFlight(returnFlight);
            System.out.println("returnFlight = " + returnFlight);
        }

        //create passengers and add to database
        Passenger.PassengerBuilder passengerBuilder = Passenger.builder();
        List<Passenger> passengerList = new ArrayList<>();
        for(PassengerDTO passengerData : passengerDTO) {
            passengerBuilder.firstName(passengerData.getFirstName());
            passengerBuilder.lastName(passengerData.getLastName());
            passengerBuilder.passportId(passengerData.getPassportId());
            passengerBuilder.title(passengerData.getTitle());
            passengerBuilder.departureSeatNumber(passengerData.getDepartureSeatNumber());
            passengerBuilder.returnSeatNumber(passengerData.getReturnSeatNumber());
            Passenger passenger = passengerBuilder.build();
            passengerList.add(passenger);
        }

        for(Passenger passenger : passengerList) {
            passenerRepository.save(passenger);
        }
        reservationBuilder.passengers(passengerList);


        //create seats and add database
        Seat.SeatBuilder seatBuilder = Seat.builder();
        for(PassengerDTO passenger : passengerDTO) {
            seatBuilder.seatNumber(passenger.getDepartureSeatNumber());
            seatBuilder.flight(departureFlight);
            Seat departureSeat = seatBuilder.build();
            System.out.println("departure seat = " + departureSeat.getSeatNumber());
            seatRepository.save(departureSeat);

            seatBuilder.seatNumber(passenger.getReturnSeatNumber());
            if(returnFlight != null) {
                seatBuilder.flight(returnFlight);
            }
            Seat returnSeat = seatBuilder.build();
            System.out.println("return seat = " + returnSeat.getSeatNumber());
            seatRepository.save(returnSeat);
        }

        Reservation reservation = reservationBuilder.build();
        reservationRepository.save(reservation);
        return reservation;
    }
}

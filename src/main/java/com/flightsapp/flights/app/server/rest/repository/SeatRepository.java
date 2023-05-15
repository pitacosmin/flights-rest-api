package com.flightsapp.flights.app.server.rest.repository;

import com.flightsapp.flights.app.server.rest.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {

    List<Seat> findByFlightFlightId(Long flightId);

    @Query("SELECT s.seatNumber FROM Seat s WHERE s.flight.flightId = :flightId")
    List<String> findSeatNumberByFlightFlightId(Long flightId);

    @Query("SELECT count(s.seatNumber) FROM Seat s WHERE s.flight.flightId = :flightId")
    int countSeatsByFlightId(Long flightId);
}

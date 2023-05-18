package com.flightsapp.flights.app.server.rest.repository;


import com.flightsapp.flights.app.server.rest.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}

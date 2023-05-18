package com.flightsapp.flights.app.server.rest.repository;

import com.flightsapp.flights.app.server.rest.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassenerRepository extends JpaRepository<Passenger, Long> {
}

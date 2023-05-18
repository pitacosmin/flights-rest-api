package com.flightsapp.flights.app.server.rest.repository;

import com.flightsapp.flights.app.server.rest.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, String> {

}

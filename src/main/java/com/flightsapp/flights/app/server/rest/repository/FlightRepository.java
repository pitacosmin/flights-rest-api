package com.flightsapp.flights.app.server.rest.repository;

import com.flightsapp.flights.app.server.rest.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query(nativeQuery = true,
            value = "SELECT f.departure_date FROM Flight f "
            + "JOIN Route r on r.flight_number = f.flight_number "
            + "JOIN Airport o ON o.airport_iata_code = r.origin "
            + "JOIN Airport d ON d.airport_iata_code = r.destination "
            + "WHERE r.origin = :originIata AND r.destination = :destinationIata"
    )
    List<Date> findDepartureDates(@Param("originIata") String originIata, @Param("destinationIata") String destinationIata);

    @Query("SELECT f FROM Flight f " +
            "JOIN f.route r " +
            "JOIN r.originAirport o " +
            "JOIN r.destinationAirport d " +
            "WHERE o.airportIataCode = :originIata AND d.airportIataCode = :destinationIata AND f.departureDate = :departureDate"
    )
    List<Flight> findByRouteOriginAirportAndRouteDestinationAirportAndDepartureDate(
            @Param("originIata") String originIata,
            @Param("destinationIata") String destinationIata,
            @Param("departureDate") Date departureDate
    );
}

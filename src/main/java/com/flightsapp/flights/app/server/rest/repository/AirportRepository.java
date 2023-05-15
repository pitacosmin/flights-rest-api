package com.flightsapp.flights.app.server.rest.repository;

import com.flightsapp.flights.app.server.rest.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface AirportRepository extends JpaRepository<Airport, String> {
    @Query("SELECT DISTINCT a.airportIataCode as airportIataCode, a.airportName as airportName, "
            + "c.cityName as cityName, co.countryName as countryName "
            + "FROM Route r "
            + "JOIN Airport a ON a.airportIataCode = r.originAirport "
            + "JOIN City c ON a.city.cityId = c.cityId "
            + "JOIN Country co ON co.countryIso2 = c.country")
    List<Map<String, String>> findDistinctAirportIataCodesWithCityAndCountryNames();

    @Query(nativeQuery = true,
            value = "SELECT DISTINCT a.airport_iata_code AS \"airportIataCode\", a.airport_name AS \"airportName\", "
            + "c.city_Name AS \"cityName\", co.country_name AS \"countryName\" "
            + "FROM Route r "
            + "JOIN Airport a ON a.airport_iata_code = r.destination "
            + "JOIN City c ON a.city_id = c.city_id "
            + "JOIN Country co ON co.country_iso2 = c.country_iso2 "
            + "WHERE r.origin = :originIata")
    List<Map<String, String>> findDistinctAirportIataCodesWithCityAndCountryNamesByOrigin(@Param("originIata") String originIata);
}
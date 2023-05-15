package com.flightsapp.flights.app.server.rest.repository;

import com.flightsapp.flights.app.server.rest.entity.City;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query(
            value = "SELECT DISTINCT ON (city_name, country_iso2, timezone) * FROM city " +
                    "WHERE city_name = :city_name " +
                    "AND country_iso2 = :country_iso2 " +
                    "AND timezone = :timezone",
            nativeQuery = true
    )
    City findDistinctByCityName(
            @Param("city_name") String cityName,
            @Param("country_iso2") String countryIso2,
            @Param("timezone") String timezone
    );

    List<City> findByCityName(String cityName);
}

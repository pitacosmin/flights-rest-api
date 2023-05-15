package com.flightsapp.flights.app.server.rest.repository;

import com.flightsapp.flights.app.server.rest.entity.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {

//    @Query(
//            value = "SELECT EXISTS (SELECT 1 FROM airplane WHERE model = :model) AS model_exists;",
//            nativeQuery = true
//    )
//    boolean existsAirplane(@Param("model") String model);


    @Query("SELECT a.layout FROM Airplane a WHERE a.airplaneId = :airplaneId")
    int[] findLayoutByAirplaneId(Long airplaneId);

    @Query("SELECT a FROM Airplane a JOIN Flight f on a.airplaneId = f.airplane.airplaneId WHERE f.flightId=:flightId")
    Airplane findByFlightFlightId(Long flightId);

}

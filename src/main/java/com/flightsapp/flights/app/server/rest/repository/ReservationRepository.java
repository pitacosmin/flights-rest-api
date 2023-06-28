package com.flightsapp.flights.app.server.rest.repository;


import com.flightsapp.flights.app.server.rest.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.user.id = :userId order by r.registeredAt DESC")
    List<Reservation> findReservationsByUserId(Long userId);
}

package com.flightsapp.flights.app.server.rest;

import com.flightsapp.flights.app.server.rest.entity.Airplane;
import com.flightsapp.flights.app.server.rest.repository.AirplaneRepository;

public class SQLGenerator {
    public static void initialScript(AirplaneRepository airplaneRepository) {
        generateAirplanes(airplaneRepository);
    }

    private static void generateAirplanes(AirplaneRepository airplaneRepository) {
        if(airplaneRepository.findAll().size() == 0) {
            Airplane boeing787 = Airplane.builder()
                    .model("Boeing787")
                    .seatingCapacity(252)
                    .columns(3)
                    .avgSpeed(907)
                    .layout(new int[]{3, 3, 3})
                    .build();
            airplaneRepository.save(boeing787);

            Airplane boeing777 = Airplane.builder()
                    .model("Boeing777")
                    .seatingCapacity(270)
                    .columns(3)
                    .avgSpeed(905)
                    .layout(new int[]{3, 3, 3})
                    .build();
            airplaneRepository.save(boeing777);

            Airplane airbus320 = Airplane.builder()
                    .model("Airbus320")
                    .seatingCapacity(144)
                    .columns(2)
                    .avgSpeed(840)
                    .layout(new int[]{3, 3})
                    .build();
            airplaneRepository.save(airbus320);

            Airplane airbus330 = Airplane.builder()
                    .model("Airbus330")
                    .seatingCapacity(294)
                    .columns(3)
                    .avgSpeed(875)
                    .layout(new int[]{2, 3, 2})
                    .build();
            airplaneRepository.save(airbus330);

            Airplane airbus350 = Airplane.builder()
                    .model("Airbus350")
                    .seatingCapacity(513)
                    .columns(3)
                    .avgSpeed(903)
                    .layout(new int[]{3, 3, 3})
                    .build();
            airplaneRepository.save(airbus350);
        }
    }
}

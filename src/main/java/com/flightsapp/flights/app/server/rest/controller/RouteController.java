package com.flightsapp.flights.app.server.rest.controller;

import com.flightsapp.flights.app.server.rest.entity.Airport;
import com.flightsapp.flights.app.server.rest.entity.Route;
import com.flightsapp.flights.app.server.rest.repository.AirportRepository;
import com.flightsapp.flights.app.server.rest.repository.RouteRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(path = "/routes")
@CrossOrigin
public class RouteController {
    @Autowired
    AirportRepository airportRepository;

    @Autowired
    RouteRepository routeRepository;

    String airlineIata = "ZZ";

    @PostMapping(path = "/admin/populateTable")
    public void populateTables() throws FileNotFoundException, ParseException {
        ArrayList<Route> routes = new ArrayList<>();
        int counter = 1;

        String path = "D:\\Licenta\\flights-app\\get-data\\data\\flights.json";
        path = path.replace("\\", "/");
        File file = new File(path);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(new FileReader(file), JsonArray.class);

        for(int i = 0; i < jsonArray.size(); i++) {
            System.out.println(jsonArray.get(i).getAsJsonObject().getAsJsonObject("departure"));
            JsonObject departureJsonObject = jsonArray.get(i).getAsJsonObject().getAsJsonObject("departure");
            JsonObject arrivalJsonObject = jsonArray.get(i).getAsJsonObject().getAsJsonObject("arrival");

            JsonElement departureIataCode = departureJsonObject.get("iata");
            JsonElement departureTime = departureJsonObject.get("scheduled");
            JsonElement arrivalIataCode = arrivalJsonObject.get("iata");
            JsonElement arrivalTime = arrivalJsonObject.get("scheduled");

            Route.RouteBuilder routeBuilder = Route.builder();

            if(departureIataCode == null || arrivalIataCode == null) {
                continue;
            }
            if(departureTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                Date date = sdf.parse(departureTime.getAsString());
                routeBuilder.departureTime(date);
            }
            if(arrivalTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                Date date = sdf.parse(arrivalTime.getAsString());
                routeBuilder.arrivalTime(date);
            }
            Airport origin = airportRepository.findById(departureIataCode.getAsString()).orElse(null);
            Airport destination = airportRepository.findById(arrivalIataCode.getAsString()).orElse(null);
            if(destination == null || origin == null) {
                continue;
            }
            routeBuilder.originAirport(origin);
            routeBuilder.destinationAirport(destination);
            String flightNumber = airlineIata + String.format("%04d", counter);
            routeBuilder.flightNumber(flightNumber);
            counter++;

            Route route = routeBuilder.build();
            routeRepository.save(route);
        }
    }

    @PostMapping(path = "/admin/populate")
    public ArrayList<Route> testPopulateTables() throws FileNotFoundException, ParseException {
        ArrayList<Route> routes = new ArrayList<>();
        int counter = 1;

        String path = "D:\\Licenta\\flights-app\\get-data\\data\\flights.json";
        path = path.replace("\\", "/");
        File file = new File(path);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(new FileReader(file), JsonArray.class);

        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject departureJsonObject = jsonArray.get(i).getAsJsonObject().getAsJsonObject("departure");
            JsonObject arrivalJsonObject = jsonArray.get(i).getAsJsonObject().getAsJsonObject("arrival");

            JsonElement departureIataCode = departureJsonObject.get("iata");
            JsonElement departureTime = departureJsonObject.get("scheduled");
            JsonElement arrivalIataCode = arrivalJsonObject.get("iata");
            JsonElement arrivalTime = arrivalJsonObject.get("scheduled");

            Route.RouteBuilder routeBuilder = Route.builder();

            if(departureIataCode == null || arrivalIataCode == null) {
                continue;
            }
            if(departureTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                Date date = sdf.parse(departureTime.getAsString());
                routeBuilder.departureTime(date);
            }
            if(arrivalTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                Date date = sdf.parse(arrivalTime.getAsString());
                routeBuilder.arrivalTime(date);
            }
            Airport origin = airportRepository.findById(departureIataCode.getAsString()).orElse(null);
            routeBuilder.originAirport(origin);
            Airport destination = airportRepository.findById(arrivalIataCode.getAsString()).orElse(null);
            routeBuilder.destinationAirport(destination);
            String flightNumber = airlineIata + String.format("%04d", counter);
            routeBuilder.flightNumber(flightNumber);
            counter++;

            Route route = routeBuilder.build();
            routes.add(route);
        }

        return routes;
    }
}

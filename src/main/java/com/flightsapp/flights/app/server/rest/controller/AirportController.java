package com.flightsapp.flights.app.server.rest.controller;

import com.flightsapp.flights.app.server.rest.entity.Airport;
import com.flightsapp.flights.app.server.rest.entity.City;
import com.flightsapp.flights.app.server.rest.repository.AirportRepository;
import com.flightsapp.flights.app.server.rest.repository.CityRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(path = "/airports")
@CrossOrigin
public class AirportController {

    @Autowired
    AirportRepository airportRepository;
    @Autowired
    CityRepository cityRepository;

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<Airport>> findAll() {
        List<Airport> airports = airportRepository.findAll();

        return ResponseEntity.ok(airports);
    }

    @GetMapping(path = "/getOrigins")
    public ResponseEntity<List<Map<String, String>>> findDistinctAirportIataCodesWithCityAndCountryNames() {
        List<Map<String, String>> airports = airportRepository.findDistinctAirportIataCodesWithCityAndCountryNames();

        return ResponseEntity.ok(airports);
    }

    @GetMapping(path = "/get-destinations/{originIata}")
    public ResponseEntity<List<Map<String, String>>> findDistinctAirportIataCodesWithCityAndCountryNamesByOrigin(@PathVariable String originIata) {
        List<Map<String, String>> airports = airportRepository.findDistinctAirportIataCodesWithCityAndCountryNamesByOrigin(originIata);

        return ResponseEntity.ok(airports);
    }


    @PostMapping(path = "/admin/populateTable")
    public void populateTables() throws FileNotFoundException {
        ArrayList<Airport> airports = new ArrayList<>();
        String path = "D:\\Licenta\\flights-app\\get-data\\data\\airports.json";
        path = path.replace("\\", "/");
        File file = new File(path);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(new FileReader(file), JsonArray.class);

        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            JsonElement airportIataCode = jsonObject.get("iata_code");
            JsonElement airportName = jsonObject.get("airport_name");
            JsonElement latitude = jsonObject.get("latitude");
            JsonElement longitude = jsonObject.get("longitude");
            JsonElement cityName = jsonObject.get("city_name");
            JsonElement countryIso2 = jsonObject.get("country_iso2");
            JsonElement timezone = jsonObject.get("timezone");

            Airport.AirportBuilder airportBuilder = Airport.builder();
            if(airportName == null || airportIataCode == null) {
                continue;
            }
            if(cityName == null || countryIso2 == null || timezone == null) {
                continue;
            }
            if(latitude != null) {
                airportBuilder.latitude(latitude.getAsFloat());
            }
            if(longitude != null) {
                airportBuilder.longitude(longitude.getAsFloat());
            }
            airportBuilder.airportName(airportName.getAsString());
            airportBuilder.airportIataCode(airportIataCode.getAsString());

            //Get the list of cities with the same name
            List<City> cityList = cityRepository.findByCityName(cityName.getAsString());
            City city = null;
            for(City c : cityList) {
                //check if timezone and countryIso2 is the same then save that city
                if(Objects.equals(c.getTimezone(), timezone.getAsString()) &&
                        Objects.equals(c.getCountry().getCountryIso2(), countryIso2.getAsString())) {
                    city = c;
                    break;
                }
            }

            if(city != null) {
                airportBuilder.city(city);
            } else continue;

            Airport airport = airportBuilder.build();
            airportRepository.save(airport);
        }
    }

    @PostMapping(path = "/admin/populate")
    public ArrayList<Airport> testPopulateTables() throws FileNotFoundException {
        ArrayList<Airport> airports = new ArrayList<>();
        String path = "D:\\Licenta\\flights-app\\get-data\\data\\airports.json";
        path = path.replace("\\", "/");
        File file = new File(path);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(new FileReader(file), JsonArray.class);

        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            JsonElement airportIataCode = jsonObject.get("city_iata_code");
            JsonElement airportName = jsonObject.get("airport_name");
            JsonElement latitude = jsonObject.get("latitude");
            JsonElement longitude = jsonObject.get("longitude");
            JsonElement cityName = jsonObject.get("city_name");
            JsonElement countryIso2 = jsonObject.get("country_iso2");
            JsonElement timezone = jsonObject.get("timezone");

            Airport.AirportBuilder airportBuilder = Airport.builder();
            if(airportName == null || airportIataCode == null) {
                continue;
            }
            if(cityName == null || countryIso2 == null || timezone == null) {
                continue;
            }
            if(latitude != null) {
                airportBuilder.latitude(latitude.getAsFloat());
            }
            if(longitude != null) {
                airportBuilder.longitude(longitude.getAsFloat());
            }
            airportBuilder.airportName(airportName.getAsString());
            airportBuilder.airportIataCode(airportIataCode.getAsString());

            List<City> cityList = cityRepository.findByCityName(cityName.getAsString());
            City city = null;
            for(City c : cityList) {
                if(Objects.equals(c.getTimezone(), timezone.getAsString()) &&
                        Objects.equals(c.getCountry().getCountryIso2(), countryIso2.getAsString())) {
                    city = c;
                    break;
                }
            }
            if(city != null) {
                airportBuilder.city(city);
            } else continue;

            Airport airport = airportBuilder.build();
            airports.add(airport);
        }
        return airports;
    }
}
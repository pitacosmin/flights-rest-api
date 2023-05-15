package com.flightsapp.flights.app.server.rest.controller;

import com.flightsapp.flights.app.server.rest.entity.City;
import com.flightsapp.flights.app.server.rest.entity.Country;
import com.flightsapp.flights.app.server.rest.repository.CityRepository;
import com.flightsapp.flights.app.server.rest.repository.CountryRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/cities")
@CrossOrigin
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @PostMapping(path = "/admin/populateTable")
    public void populateCities() throws IOException {

        Gson gson = new Gson();
        String directoryPath = "D:\\Licenta\\flights-app\\get-data\\data\\cities";
        directoryPath = directoryPath.replace("\\", "/");
        List<Path> jsonFiles = Files.list(Paths.get(directoryPath))
                .filter(path -> path.toString().endsWith(".json")).toList();
        for(Path jsonFile : jsonFiles) {
            JsonArray jsonArray = gson.fromJson(new FileReader(String.valueOf(jsonFile)), JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                JsonElement cityName = jsonObject.get("city_name");
                JsonElement timezone = jsonObject.get("timezone");
                JsonElement countryIso2 = jsonObject.get("country_iso2");
                City.CityBuilder cityBuilder = City.builder();
                if (cityName == null || countryIso2 == null) {
                    continue;
                }
                if(timezone != null) {
                    cityBuilder.timezone(timezone.getAsString());
                }

                cityBuilder.cityName(cityName.getAsString());
                Country country = countryRepository.findById(countryIso2.getAsString()).orElse(null);
                cityBuilder.country(country);

                City city = cityBuilder.build();
                cityRepository.save(city);
            }
        }
    }


    @PostMapping(path = "/admin/populate")
    public ArrayList<City> testPopulateCities() throws IOException {

        Gson gson = new Gson();
        String directoryPath = "D:\\Licenta\\flights-app\\get-data\\data\\cities";
        directoryPath = directoryPath.replace("\\", "/");
        List<Path> jsonFiles = Files.list(Paths.get(directoryPath))
                .filter(path -> path.toString().endsWith(".json")).toList();
        ArrayList<City> cities = new ArrayList<>();
        for(Path jsonFile : jsonFiles) {
            JsonArray jsonArray = gson.fromJson(new FileReader(String.valueOf(jsonFile)), JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                JsonElement cityName = jsonObject.get("city_name");
                JsonElement timezone = jsonObject.get("timezone");
                JsonElement countryIso2 = jsonObject.get("country_iso2");
                City.CityBuilder cityBuilder = City.builder();
                if(cityName != null) {
                    cityBuilder.cityName(cityName.getAsString());
                }
                if(timezone != null) {
                    cityBuilder.timezone(timezone.getAsString());
                }
                if(countryIso2 != null) {
                    Country country = countryRepository.findById(countryIso2.getAsString()).orElse(null);
                    cityBuilder.country(country);
                }
                City city = cityBuilder.build();
                cities.add(city);
            }
        }
        return cities;
    }
}

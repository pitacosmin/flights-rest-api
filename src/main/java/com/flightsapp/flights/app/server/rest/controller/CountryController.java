package com.flightsapp.flights.app.server.rest.controller;

import com.flightsapp.flights.app.server.rest.entity.Country;
import com.flightsapp.flights.app.server.rest.repository.CountryRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

@RestController
@RequestMapping(path = "/countries")
@CrossOrigin
public class CountryController {

    @Autowired
    private CountryRepository countryRepository;

    @PostMapping(path = "/admin/populateTable")
    public void populateCountryTable() throws FileNotFoundException {
        String path = "D:\\Licenta\\flights-app\\get-data\\data\\countries.json";
        path = path.replace("\\", "/");
        File file = new File(path);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(new FileReader(file), JsonArray.class);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            JsonElement countryIso2 = jsonObject.get("country_iso2");
            JsonElement countryName = jsonObject.get("country_name");
            Country.CountryBuilder countryBuilder = Country.builder();
            if (countryIso2 != null) {
                countryBuilder.countryIso2(countryIso2.getAsString());
            }
            if (countryName != null) {
                countryBuilder.countryName(countryName.getAsString());
            }

            Country country = countryBuilder.build();
            countryRepository.save(country);
        }
    }


    @PostMapping(value = "/admin/populate")
    public ArrayList<Country> Populate() {
        String path = "D:\\Licenta\\flights-app\\get-data\\data\\countries.json";
        path = path.replace("\\", "/");
        File file = new File(path);
        Gson gson = new Gson();
        ArrayList<Country> countries = new ArrayList<>();
        try{
            JsonArray jsonArray = gson.fromJson(new FileReader(file), JsonArray.class);

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                JsonElement countryIso2 = jsonObject.get("country_iso2");
                JsonElement countryName = jsonObject.get("country_name");
                Country.CountryBuilder countryBuilder = Country.builder();
                if(countryIso2 != null) {
                    countryBuilder.countryIso2(countryIso2.getAsString());
                }
                if(countryName != null) {
                    countryBuilder.countryName(countryName.getAsString());
                }

                Country country = countryBuilder.build();
                countries.add(country);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return countries;
    }
}

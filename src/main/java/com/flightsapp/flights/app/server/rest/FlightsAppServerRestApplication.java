package com.flightsapp.flights.app.server.rest;

import com.flightsapp.flights.app.server.rest.repository.AirplaneRepository;
import com.flightsapp.flights.app.server.rest.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightsAppServerRestApplication implements CommandLineRunner {

	@Autowired
	private AirplaneRepository airplaneRepository;

	public static void main(String[] args) {
		SpringApplication.run(FlightsAppServerRestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		SQLGenerator.initialScript(airplaneRepository);
	}
}


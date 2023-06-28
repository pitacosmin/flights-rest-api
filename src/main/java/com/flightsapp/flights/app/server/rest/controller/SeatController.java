package com.flightsapp.flights.app.server.rest.controller;

import com.flightsapp.flights.app.server.rest.entity.Flight;
import com.flightsapp.flights.app.server.rest.entity.Seat;
import com.flightsapp.flights.app.server.rest.repository.FlightRepository;
import com.flightsapp.flights.app.server.rest.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/seats")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SeatController {

    @Autowired
    SeatRepository seatRepository;
    @Autowired
    FlightRepository flightRepository;

    @GetMapping(path = "/{flightId}")
    public ResponseEntity<List<Seat>> findSeatsByFlightId(@PathVariable Long flightId) {
        List<Seat> seats = seatRepository.findByFlightFlightId(flightId);

        return ResponseEntity.ok(seats);
    }

    @GetMapping(path = "/flight/{flightId}")
    public ResponseEntity<List<String>> findSeatNumberByFlightId(@PathVariable Long flightId) {
        List<String> seats = seatRepository.findSeatNumberByFlightFlightId(flightId);

        return ResponseEntity.ok(seats);
    }

    @GetMapping(path = "/{flightId}/generate/{numOfPassengers}/seats")
    public ResponseEntity<Set<String>> generateRandomSeatsByNumOfPassengers(
            @PathVariable Long flightId,
            @PathVariable Integer numOfPassengers
    ) {
        Flight flight = flightRepository.findById(flightId).orElse(null);
        int capacity = flight.getAirplane().getSeatingCapacity();
        int[] layout = flight.getAirplane().getLayout();
        List<String> occupiedSeats = seatRepository.findSeatNumberByFlightFlightId(flightId);

        int seatsPerRow = Arrays.stream(layout).sum();
        int rows = (int) Math.ceil((double) capacity/seatsPerRow);
        Set<String> seatsToOccupy = generateRandomSeatNumbers(occupiedSeats, numOfPassengers, rows, seatsPerRow);

        return ResponseEntity.ok(seatsToOccupy);
    }

    public Set<String> generateRandomSeatNumbers(List<String> occupiedSeats, int numOfPassengers, int rows, int cols) {
        Set<String> seatNumbers = new HashSet<>();
        Random random = new Random();

        while (seatNumbers.size() < numOfPassengers) {
            int row = random.nextInt(rows) + 1;
            char col = (char)('A' + random.nextInt(cols));
            String seatNumber = row + "" + col;
            if(!occupiedSeats.contains(seatNumber)) {
                seatNumbers.add(seatNumber);
            }
        }

        return seatNumbers;
    }


    //TODO fix randomisation
    @PostMapping(path = "/populateTable")
    public void populateTable() {
        Seat.SeatBuilder seatBuilder = Seat.builder();

        //get flights
        List<Flight> flightList = flightRepository.findAll();

        // for each flight we need to add some seats
        // somewhere around 10%
        for(Flight flight : flightList) {
            //get the layout, eg. {2,3,2}
            int[] layout = flight.getAirplane().getLayout();
            int capacity = flight.getAirplane().getSeatingCapacity();

            //get seatsPerRow, with which we can determine the number of rows (capacity / seatsPerRow)
            int seatsPerRow = Arrays.stream(layout).sum();
            int rows = (int) Math.ceil((double) capacity/seatsPerRow);

            List<Seat> seatList = new ArrayList<>();
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < seatsPerRow; j++) {
                    String seatNumber = (i+1) + Character.toString((char)('A' + j));
                    seatBuilder.seatNumber(seatNumber);
                    seatBuilder.flight(flight);
                    Seat seat = seatBuilder.build();
                    seatList.add(seat);
                }
            }

            // select 10% of the seats in order to save them in the database
            // use reservoir sampling algorithm
            int percentageOfSeatsToSave = (int) Math.ceil(seatList.size() * 0.1); // = 10%
            List<Seat> seatsToSave = new ArrayList<>(percentageOfSeatsToSave);

            Random random = new Random();
            for (int i = 0; i < seatList.size(); i++) {
                if (i < percentageOfSeatsToSave) {
                    seatsToSave.add(seatList.get(i));
                } else {
                    int j = random.nextInt(i + 1);
                    if (j < percentageOfSeatsToSave) {
                        seatsToSave.set(j, seatList.get(i));
                    }
                }
            }

            //Use bulking for insertion
            //Decreases the insertion time for the seats
            int batchSize = 50; // Define the batch size
            for (int i = 0; i < seatsToSave.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, seatsToSave.size());
                List<Seat> batch = seatList.subList(i, endIndex);
                seatRepository.saveAll(batch);
            }

            seatList = null; // set seatList to null to free up memory
            seatsToSave = null;
        }
    }

    @PostMapping(path = "/populate")
    public ArrayList<Seat> testPopulateTable() {
        ArrayList<Seat> seats = new ArrayList<>();
        Seat.SeatBuilder seatBuilder = Seat.builder();

        //get flights
        List<Flight> flightList = flightRepository.findAll();

        //for each flight we need to add all the seats
        for(Flight flight : flightList) {
            //get the layout, eg. {2,3,2}
            int[] layout = flight.getAirplane().getLayout();
            int capacity = flight.getAirplane().getSeatingCapacity();

            //get seatsPerRow, so we can determine the number of rows (capacity / seatsPerRow)
            int seatsPerRow = Arrays.stream(layout).sum();
            int rows = (int) Math.ceil((double) capacity/seatsPerRow);

            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < seatsPerRow; j++) {
                    String seatNumber = (i+1) + Character.toString((char)('A' + j));
                    seatBuilder.seatNumber(seatNumber);
                    seatBuilder.flight(flight);
                    Seat seat = seatBuilder.build();
                    seats.add(seat);
                }
            }
        }

        return seats;
    }

    public static boolean getRandomAvailability(double probability) {
        Random random = new Random();
        return random.nextDouble() < probability;
    }

}

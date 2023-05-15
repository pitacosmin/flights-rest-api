package com.flightsapp.flights.app.server.rest.service;

import com.flightsapp.flights.app.server.rest.entity.Airport;
import com.flightsapp.flights.app.server.rest.entity.Flight;
import com.flightsapp.flights.app.server.rest.entity.Route;
import com.flightsapp.flights.app.server.rest.repository.FlightRepository;
import com.flightsapp.flights.app.server.rest.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FlightService {

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    SeatRepository seatRepository;

    public static final double RADIUS_OF_EARTH_KM = 6371.01;

    public static double calculateDistance(Flight flight) {
        float originLatitude = flight.getRoute().getOriginAirport().getLatitude();
        float originLongitude = flight.getRoute().getOriginAirport().getLongitude();

        float destinationLatitude = flight.getRoute().getDestinationAirport().getLatitude();
        float destinationLongitude = flight.getRoute().getDestinationAirport().getLongitude();

        double lat1Radians = Math.toRadians(originLatitude);
        double lon1Radians = Math.toRadians(originLongitude);
        double lat2Radians = Math.toRadians(destinationLatitude);
        double lon2Radians = Math.toRadians(destinationLongitude);

        double dlon = lon2Radians - lon1Radians;
        double dlat = lat2Radians - lat1Radians;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1Radians) * Math.cos(lat2Radians)
                * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIUS_OF_EARTH_KM * c;
    }

    public double calculateBasePrice(Long flightId) {
        Flight flight = flightRepository.findById(flightId).orElseThrow();
        double distanceInKm = calculateDistance(flight);
        double basePrice = distanceInKm * 0.1; //calculated in euros

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(flight.getArrivalDate());
        int month = calendar.get(Calendar.MONTH) + 1;

        // adjust base price based on month of the year
        if (month == 6 || month == 7 || month == 8) {
            basePrice *= 1.1;  // summer season, increase price by 10%
        } else if (month == 12 || month == 1 || month == 2) {
            basePrice *= 1.3;  //holidays, increase by 30%
        }

        int numOfOcuppiedSeats = seatRepository.countSeatsByFlightId(flight.getFlightId());
        if (numOfOcuppiedSeats >= 150) {
            basePrice *= 1.15;  // high demand, increase price by 15%
        } else if (numOfOcuppiedSeats >= 50) {
            basePrice *= 1.05;  // moderate demand, increase price by 5%
        }

        calendar.setTime(flight.getRoute().getDepartureTime());
        long hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour <= 9) {
            basePrice *= 1.1;  // peak morning hour, increase price by 10%
        } else if (hour >= 16 && hour <= 18) {
            basePrice *= 1.2;  // peak late hour, increase price by 20%
        } else if (hour >= 20 && hour <= 22) {
            basePrice *= 1.15;  // peak late hour, increase price by 20%
        } else {
            basePrice *= 0.9;  // off-peak hour, decrease price by 10%
        }

        basePrice = Double.parseDouble(String.format("%.2f", basePrice));
        return basePrice;
    }

    public List<Date> generateDates() {
        // Set the start and end dates for the date range
        Calendar start = Calendar.getInstance();
        int randomStartDay = new Random().nextInt(3) + 3;
        start.set(2023, Calendar.JULY, randomStartDay);
        Calendar end = Calendar.getInstance();
        end.set(2023, Calendar.JULY, 31);
        List<Date> dates = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        int days = random.nextInt(3) + 2;
        while(start.before(end)) {
            dates.add(start.getTime());
            start.add(Calendar.DATE, days);
        }
        return dates;
    }

    public boolean hasDayPassed(Route route) {
        TimeZone departureTz = TimeZone.getTimeZone(route.getOriginAirport().getCity().getTimezone());
        TimeZone arrivalTz = TimeZone.getTimeZone(route.getDestinationAirport().getCity().getTimezone());
        Calendar departureCalendar = Calendar.getInstance(departureTz);
        departureCalendar.setTime(route.getDepartureTime());

        Calendar arrivalCalendar = Calendar.getInstance(arrivalTz);
        arrivalCalendar.setTime(route.getArrivalTime());

        // Set the date of the arrival calendar to match the date of the departure calendar
        arrivalCalendar.set(Calendar.YEAR, departureCalendar.get(Calendar.YEAR));
        arrivalCalendar.set(Calendar.MONTH, departureCalendar.get(Calendar.MONTH));
        arrivalCalendar.set(Calendar.DATE, departureCalendar.get(Calendar.DATE));

        // Check if the arrival time is before the departure time
        if (arrivalCalendar.before(departureCalendar)) {
            // Add one day to the arrival date
            arrivalCalendar.add(Calendar.DATE, 1);
        }

        // Check if the arrival time is on a different day than the departure time
        return arrivalCalendar.get(Calendar.DATE) > departureCalendar.get(Calendar.DATE);
    }
}

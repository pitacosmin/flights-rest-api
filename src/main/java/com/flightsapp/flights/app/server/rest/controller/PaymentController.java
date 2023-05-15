package com.flightsapp.flights.app.server.rest.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightsapp.flights.app.server.rest.DTO.CheckoutMetadataDTO;
import com.flightsapp.flights.app.server.rest.DTO.FlightDTO;
import com.flightsapp.flights.app.server.rest.DTO.PassengerDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/stripe")
@CrossOrigin
public class PaymentController {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @PostMapping("/create-checkout-session")
    public String createCheckoutSession(@RequestBody CheckoutMetadataDTO checkoutMetadata) throws StripeException {
        Stripe.apiKey = "sk_test_51N6BIwLb37730FcYCqL1AbnCz4I3iV6qqgcFoKIkcNmTR0nh9EgWuoFwplwYn1blfDU8ZGOLnq6nqnRuOUxTsU1400TXZflz6a";

        System.out.println("requestBody = " + checkoutMetadata);

        Long price = checkoutMetadata.getPrice();
        String email = checkoutMetadata.getEmail();
        FlightDTO flights = checkoutMetadata.getFlights();
        List<PassengerDTO> passengers = checkoutMetadata.getPassengers();

        //Send flights and passengers as json objects
        ObjectMapper objectMapper = new ObjectMapper();
        String passengersJson = "";
        String flightJson = "";
        try {
            passengersJson = objectMapper.writeValueAsString(passengers);
            flightJson = objectMapper.writeValueAsString(flights);
        } catch (JsonProcessingException e) {
            System.out.println("Error on creating passenger JSON = " + e);
        }

        //Create a price product
        PriceCreateParams paramsPrice =
                PriceCreateParams.builder()
                        .setCurrency("usd")
                        .setUnitAmount(price)
                        .setProductData(
                                PriceCreateParams.ProductData.builder()
                                        .setName("Ticket")
                                        .build())
                        .build();
        Price priceProduct = Price.create(paramsPrice);

        //Create metadata in order to store it in the session
        Map<String, String> metadata = new HashMap<>();
        metadata.put("email", email);
        metadata.put("price", String.valueOf(price));
        metadata.put("flights", flightJson);
        metadata.put("passengers", passengersJson);

        //Create the stripe session
        List<Object> lineItems = new ArrayList<>();
        Map<String, Object> lineItem1 = new HashMap<>();
        lineItem1.put("price", priceProduct.getId());
        lineItem1.put("quantity", 1);
        lineItems.add(lineItem1);
        Map<String, Object> params = new HashMap<>();
        params.put("line_items", lineItems);
        params.put("mode", "payment");
        params.put("customer_email", email);
        params.put(
                "success_url",
                "http://localhost:3000/payment-success?sessionId={CHECKOUT_SESSION_ID}"
        );
        params.put(
                "metadata",
                metadata
        );
        System.out.println("params = " + params);

        Session session = Session.create(params);
//        System.out.println("sessionId = " + session.getId());

        String successUrl = session.getSuccessUrl().replace("{CHECKOUT_SESSION_ID}", session.getId());
        session.setSuccessUrl(successUrl);


//        System.out.println("session with success url = " + session);
        return session.getId();
    }

    @GetMapping(path = "/metadata/{sessionId}")
    public Map<String, String> retrieveMetadataId(@PathVariable String sessionId) throws StripeException {
        Stripe.apiKey = "sk_test_51N6BIwLb37730FcYCqL1AbnCz4I3iV6qqgcFoKIkcNmTR0nh9EgWuoFwplwYn1blfDU8ZGOLnq6nqnRuOUxTsU1400TXZflz6a";

        Session session = Session.retrieve(sessionId);
        session.getMetadata().put("paymentStatus", session.getPaymentStatus());

        System.out.println("session.getMetadata() = " + session.getMetadata());


        return session.getMetadata();
    }
}

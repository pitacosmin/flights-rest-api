package com.flightsapp.flights.app.server.rest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long cityId;
    private String cityName;
    private String timezone;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "country_iso2",
            referencedColumnName = "countryIso2"
    )
    private Country country;

}

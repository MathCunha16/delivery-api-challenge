package com.cocobambu.delivery.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateAddressRequest(

        @NotBlank(message = "street name is requeried")
        @JsonProperty("street_name")
        String streetName,

        @NotBlank(message = "street number is requeried")
        @JsonProperty("street_number")
        String streetNumber,

        @NotBlank(message = "neighborhood is requeried")
        String neighborhood,

        @NotBlank(message = "City is requeried")
        String city,

        @NotBlank(message = "State is requeried")
        String state,

        @NotBlank(message = "Postal code is requeried")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "Invalid CEP") // Aceita 70000-000 ou 70000000
        @JsonProperty("zip_code")
        String postalCode,

        String country,

        String reference,

        CreateCoordinatesRequest coordinates
) {}
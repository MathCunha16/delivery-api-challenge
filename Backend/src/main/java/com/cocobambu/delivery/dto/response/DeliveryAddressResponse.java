package com.cocobambu.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeliveryAddressResponse(
        String reference,

        @JsonProperty("street_name")
        String streetName,

        @JsonProperty("postal_code")
        String postalCode,

        String country,

        String city,

        String neighborhood,

        @JsonProperty("street_number")
        String streetNumber,

        String state,

        CordinatesResponse coordinates
) {
}

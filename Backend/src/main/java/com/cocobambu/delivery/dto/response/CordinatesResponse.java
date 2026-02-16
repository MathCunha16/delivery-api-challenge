package com.cocobambu.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CordinatesResponse(
        BigDecimal longitude,

        BigDecimal latitude,

        @JsonProperty("id")
        Long cordinateId
) {
}

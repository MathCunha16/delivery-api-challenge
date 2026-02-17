package com.cocobambu.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CoordinatesResponse(
        BigDecimal longitude,

        BigDecimal latitude,

        @JsonProperty("id")
        Long coordinateId
) {
}

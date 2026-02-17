package com.cocobambu.delivery.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record CreateCoordinatesRequest(
        BigDecimal latitude,
        BigDecimal longitude,
        @JsonProperty("coordinate_id")
        Long coordinateId
) {}

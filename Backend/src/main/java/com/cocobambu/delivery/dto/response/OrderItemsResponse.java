package com.cocobambu.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemsResponse(

        @JsonProperty("code")
        Integer externalCode,

        @JsonProperty("price")
        BigDecimal unitPrice,

        String observations,

        @JsonProperty("total_price")
        BigDecimal totalPrice,

        String name,

        Integer quantity,

        List<CondimentResponse> condiments

) {
}

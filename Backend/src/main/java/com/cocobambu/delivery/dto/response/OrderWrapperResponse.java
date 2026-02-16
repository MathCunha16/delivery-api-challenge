package com.cocobambu.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record OrderWrapperResponse(
        @JsonProperty("store_id")
        UUID storeId,

        @JsonProperty("order_id")
        UUID orderId,

        OrderDetailsResponse order

) {
}

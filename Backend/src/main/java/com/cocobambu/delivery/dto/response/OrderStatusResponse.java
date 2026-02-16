package com.cocobambu.delivery.dto.response;

import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.enums.StatusOrigin;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderStatusResponse(

        @JsonProperty("created_at")
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        OffsetDateTime createdAt,

        OrderStatus name,

        @JsonProperty("order_id")
        UUID orderId,

        StatusOrigin origin
) {
}

package com.cocobambu.delivery.dto.response;

import com.cocobambu.delivery.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderDetailsResponse(

        List<PaymentResponse> payments,

        @JsonProperty("last_status_name")
        OrderStatus lastStatusName,

        StoreResponse store,

        @JsonProperty("total_price")
        BigDecimal totalPrice,

        @JsonProperty("order_id")
        UUID orderId, // No json de exemplo, o order id é repetido aqui e no wrapper

        List<OrderItemsResponse> items,

        @JsonProperty("created_at")
        Long createdAt,

        List<OrderStatusResponse> statuses,

        CustomerResponse customer,

        @JsonProperty("delivery_address")
        DeliveryAddressResponse deliveryAddress

) {
}

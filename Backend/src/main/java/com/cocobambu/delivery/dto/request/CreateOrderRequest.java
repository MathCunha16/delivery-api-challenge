package com.cocobambu.delivery.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        @NotNull(message = "Store id is required")
        @JsonProperty("store_id")
        UUID storeId,

        @Valid
        @NotNull(message = "Client data is requiered")
        CustomerRequest customer,

        @Valid
        @NotEmpty
        List<CreateOrderItemRequest> items,

        @Valid
        @NotEmpty(message = "At least one payment method is required")
        List<CreatePaymentRequest> payments,

        @Valid
        @NotNull(message = "Delivery address is required")
        @JsonProperty("delivery_address")
        CreateAddressRequest deliveryAddress
) {
}

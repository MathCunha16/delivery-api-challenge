package com.cocobambu.delivery.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderItemRequest(

        Integer externalCode,

        @NotBlank(message = "item name is requeried")
        String name,

        @NotNull
        @Min(value = 1, message = "Quantity must be greater than 0")
        Integer quantity,

        @NotNull
        @Positive(message = "Unit price must be greater than 0")
        BigDecimal unitPrice,

        String observations,

        @Valid
        List<CreateCondimentRequest> condiment
) {
}

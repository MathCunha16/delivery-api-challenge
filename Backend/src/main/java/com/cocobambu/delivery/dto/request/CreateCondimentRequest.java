package com.cocobambu.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateCondimentRequest(

        @NotBlank(message = "condiment name is required")
        String name,

        @NotNull
        @PositiveOrZero(message = "Price must be greater than or equal to 0")
        BigDecimal price
) {
}

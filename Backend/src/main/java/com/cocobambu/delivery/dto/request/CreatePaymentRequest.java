package com.cocobambu.delivery.dto.request;

import com.cocobambu.delivery.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePaymentRequest(

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Value is required")
        @Positive(message = "Value must be positive")
        BigDecimal value,

        Boolean isPrepaid
) {
}

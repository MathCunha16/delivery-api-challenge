package com.cocobambu.delivery.dto.request;

import com.cocobambu.delivery.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePaymentRequest(

        @NotNull(message = "Payment method is required")
        @JsonProperty("payment_method")
        PaymentMethod paymentMethod,

        @NotNull(message = "Value is required")
        @Positive(message = "Value must be positive")
        BigDecimal value,

        @JsonProperty("is_prepaid")
        Boolean isPrepaid
) {
}

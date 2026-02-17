package com.cocobambu.delivery.dto.response;

import com.cocobambu.delivery.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record PaymentResponse(
        boolean prepaid,

        BigDecimal value,

        @JsonProperty("origin")
        PaymentMethod paymentMethod
) {
}

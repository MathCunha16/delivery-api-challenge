package com.cocobambu.delivery.dto.response;

import java.math.BigDecimal;

public record CondimentResponse(
        String name,
        BigDecimal price
) {
}

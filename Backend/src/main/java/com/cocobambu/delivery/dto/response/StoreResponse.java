package com.cocobambu.delivery.dto.response;

import java.util.UUID;

public record StoreResponse(
        UUID id,
        String name
) {
}

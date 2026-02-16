package com.cocobambu.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(

        @NotBlank(message = "Name is required")
        String name,

        String temporaryPhone
) {
}

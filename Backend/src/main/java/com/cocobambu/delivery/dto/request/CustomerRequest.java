package com.cocobambu.delivery.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(

        @NotBlank(message = "Name is required")
        String name,

        @JsonProperty("temporary_phone")
        String temporaryPhone
) {
}

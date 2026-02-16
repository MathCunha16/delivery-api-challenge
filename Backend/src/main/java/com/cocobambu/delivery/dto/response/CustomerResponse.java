package com.cocobambu.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CustomerResponse(

        @JsonProperty("temporary_phone")
        String temporaryPhone,

        String name
) {
}

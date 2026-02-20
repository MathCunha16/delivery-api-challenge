package com.cocobambu.delivery.config;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class HttpConfig {

    private HttpConfig() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final HttpProtocolBuilder HTTP_PROTOCOL = http
            .baseUrl("http://localhost:8080/api/v1")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");
}

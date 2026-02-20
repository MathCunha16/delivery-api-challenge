package com.cocobambu.delivery.request;

import com.cocobambu.delivery.dto.request.*;
import com.cocobambu.delivery.enums.PaymentMethod;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class OrderRequests {

    private static final JsonMapper jsonMapper = JsonMapper.builder().build();

    private OrderRequests() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final HttpRequestActionBuilder createOrder = http("POST Create Order")
            .post("/orders")
            .body(StringBody(OrderRequests::buildCreateOrderPayload)).asJson()
            .check(status().is(201));

    public static final HttpRequestActionBuilder getAllOrders = http("GET All Orders")
            .get("/orders?page=0&size=10")
            .check(status().is(200));

    private static String buildCreateOrderPayload(Session session) {
        try {
            String storeIdStr = Objects.requireNonNull(session.getString("storeId"), "Store ID missing in session");
            UUID storeId = UUID.fromString(storeIdStr);

            String customerName = Objects.requireNonNull(session.getString("customerName"), "Customer Name missing");
            String customerPhone = Objects.requireNonNull(session.getString("customerPhone"), "Customer Phone missing");
            String city = Objects.requireNonNull(session.getString("city"), "City missing");

            CustomerRequest customer = new CustomerRequest(customerName, customerPhone);

            CreateAddressRequest address = new CreateAddressRequest(
                    "Avenida T-63", "1234", "Bueno", city, "GO", "74000-000", "BR", "Perto do parque",
                    new CreateCoordinatesRequest(new BigDecimal("-16.686891"), new BigDecimal("-49.264690"), null)
            );

            CreateOrderItemRequest item = new CreateOrderItemRequest(
                    123, "Camarão Internacional", 1, new BigDecimal("150.00"), "Sem cebola", List.of()
            );

            CreatePaymentRequest payment = new CreatePaymentRequest(
                    PaymentMethod.CREDIT_CARD, new BigDecimal("150.00"), true
            );

            CreateOrderRequest requestBody = new CreateOrderRequest(
                    storeId, customer, List.of(item), List.of(payment), address
            );

            return jsonMapper.writeValueAsString(requestBody);

        } catch (JacksonException e) {
            throw new IllegalStateException("Failed to serialize CreateOrder payload in Gatling", e);
        }
    }
}
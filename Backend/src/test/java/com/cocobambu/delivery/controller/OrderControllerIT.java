package com.cocobambu.delivery.controller;

import com.cocobambu.delivery.config.BaseIntegrationTest;
import com.cocobambu.delivery.dto.request.*;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Store;
import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.enums.PaymentMethod;
import com.cocobambu.delivery.repository.OrderRepository;
import com.cocobambu.delivery.repository.StoreRepository;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

class OrderControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderRepository orderRepository;

    private static final String BASE_URL = "/api/v1/orders";
    private UUID testStoreId;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        storeRepository.deleteAll();

        testStoreId = UUID.randomUUID();
        Store store = new Store(testStoreId, "Coco Bambu - Goiânia", OffsetDateTime.now());
        storeRepository.save(store);
    }

    @Test
    @DisplayName("Should create successfully a order and return 201 Created")
    void createOrder_Success_ShouldReturnCreated() throws Exception {
        CreateOrderRequest request = buildValidCreateOrderRequest(testStoreId);
        String jsonRequest = jsonMapper.writeValueAsString(request);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.order_id").exists())
                .andExpect(jsonPath("$.store_id").value(testStoreId.toString()))
                .andExpect(jsonPath("$.order.customer.name").value("Cliente Teste"))
                .andExpect(jsonPath("$.order.delivery_address.city").value("Goiânia"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when creating an order with an invalid DTO")
    void createOrder_InvalidData_ShouldReturnBadRequest() throws Exception {
        CreateOrderRequest invalidRequest = new CreateOrderRequest(
                null, null, List.of(), List.of(), null
        );
        String jsonRequest = jsonMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Validation error")))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should fetch an order by ID successfully")
    void getOrderById_Success_ShouldReturnOk() throws Exception {
        UUID orderId = createOrderAndGetId(testStoreId);

        mockMvc.perform(get(BASE_URL + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order_id").value(orderId.toString()))
                .andExpect(jsonPath("$.store_id").value(testStoreId.toString()))
                .andExpect(jsonPath("$.order.total_price").exists());
    }

    @Test
    @DisplayName("Should return 404 Not Found when searching for an ID that doesn’t exist")
    void getOrderById_NotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should list all orders, paginated")
    void getAllOrders_Success_ShouldReturnPage() throws Exception {
        createOrderAndGetId(testStoreId);
        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.page.totalElements").exists());
    }

    @Test
    @DisplayName("Should list orders for a specific store, paginated")
    void getOrdersByStore_Success_ShouldReturnPage() throws Exception {
        createOrderAndGetId(testStoreId);

        mockMvc.perform(get(BASE_URL + "/store/{storeId}", testStoreId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].store_id").value(testStoreId.toString()))
                .andExpect(jsonPath("$.page.totalElements").exists());
    }

    @Test
    @DisplayName("Should update an order’s status via PATCH")
    void updateOrderStatus_Success_ShouldReturnOk() throws Exception {
        UUID orderId = createOrderAndGetId(testStoreId);

        UpdateOrderStatusRequest statusRequest = new UpdateOrderStatusRequest(OrderStatus.valueOf("CANCELED"));
        String jsonRequest = jsonMapper.writeValueAsString(statusRequest);

        mockMvc.perform(patch(BASE_URL + "/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order.last_status_name").value("CANCELED"));
    }

    @Test
    @DisplayName("Should update customer and address data via PUT")
    void updateOrder_Success_ShouldReturnOk() throws Exception {
        UUID orderId = createOrderAndGetId(testStoreId);

        CreateCoordinatesRequest coordinates = new CreateCoordinatesRequest(
                new BigDecimal("-16.70"), new BigDecimal("-49.30"), null
        );
        CreateAddressRequest newAddress = new CreateAddressRequest(
                "Nova Avenida", "999", "Setor Marista", "Goiânia", "GO", "74000-000", "BR", "Apto 101", coordinates
        );
        UpdateOrderRequest updateRequest = new UpdateOrderRequest(
                "Cliente Atualizado", "62988888888", newAddress
        );

        String jsonRequest = jsonMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put(BASE_URL + "/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order.customer.name").value("Cliente Atualizado"))
                .andExpect(jsonPath("$.order.delivery_address.street_name").value("Nova Avenida"));
    }

    @Test
    @DisplayName("Should delete an order successfully and return 204 No Content")
    void deleteOrder_Success_ShouldReturnNoContent() throws Exception {
        UUID orderId = createOrderAndGetId(testStoreId);

        UpdateOrderStatusRequest statusRequest = new UpdateOrderStatusRequest(OrderStatus.valueOf("CANCELED"));
        mockMvc.perform(patch(BASE_URL + "/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(delete(BASE_URL + "/{id}", orderId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/{id}", orderId))
                .andExpect(status().isNotFound());
    }


    // --- Helper methods ---
    private UUID createOrderAndGetId(UUID storeId) throws Exception {
        CreateOrderRequest request = buildValidCreateOrderRequest(storeId);
        String jsonRequest = jsonMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        OrderWrapperResponse response = jsonMapper.readValue(responseBody, OrderWrapperResponse.class);
        assertNotNull(response.orderId());
        return response.orderId();
    }

    private CreateOrderRequest buildValidCreateOrderRequest(UUID storeId) {
        CustomerRequest customer = new CustomerRequest("Cliente Teste", "62999999999");

        CreateCondimentRequest condiment = new CreateCondimentRequest("Bacon Extra", new BigDecimal("5.00"));
        CreateOrderItemRequest item = new CreateOrderItemRequest(
                123, "Camarão Internacional", 1, new BigDecimal("150.00"), "Sem cebola", List.of(condiment)
        );

        CreatePaymentRequest payment = new CreatePaymentRequest(
                PaymentMethod.valueOf("CREDIT_CARD"), new BigDecimal("155.00"), false
        );

        CreateCoordinatesRequest coordinates = new CreateCoordinatesRequest(
                new BigDecimal("-16.686891"), new BigDecimal("-49.264690"), null
        );

        CreateAddressRequest address = new CreateAddressRequest(
                "Avenida T-63", "1234", "Bueno", "Goiânia", "GO", "74000-000", "BR", "Perto do parque", coordinates
        );

        return new CreateOrderRequest(
                storeId, customer, List.of(item), List.of(payment), address
        );
    }
}
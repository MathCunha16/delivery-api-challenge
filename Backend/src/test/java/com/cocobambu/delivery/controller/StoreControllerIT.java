package com.cocobambu.delivery.controller;

import com.cocobambu.delivery.config.BaseIntegrationTest;
import com.cocobambu.delivery.entity.Store;
import com.cocobambu.delivery.repository.OrderRepository;
import com.cocobambu.delivery.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StoreControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderRepository orderRepository;

    private static final String BASE_URL = "/api/v1/stores";
    private UUID existingStoreId;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        storeRepository.deleteAll();

        existingStoreId = UUID.randomUUID();
        Store store = new Store(existingStoreId, "Coco Bambu - Test Store", OffsetDateTime.now());
        storeRepository.save(store);
    }

    @Test
    @DisplayName("Should return a paginated list of stores")
    void getAllStores_Success_ShouldReturnPaginatedStores() throws Exception {
        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[0].name").value("Coco Bambu - Test Store"))
                .andExpect(jsonPath("$.page.totalElements").exists());
    }

    @Test
    @DisplayName("Should return store details when searching by a valid ID")
    void getStoreById_Success_ShouldReturnStoreDetails() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", existingStoreId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingStoreId.toString()))
                .andExpect(jsonPath("$.name").value("Coco Bambu - Test Store"));
    }

    @Test
    @DisplayName("Should return 404 Not Found when searching for a non-existent store ID")
    void getStoreById_NotFound_ShouldReturn404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get(BASE_URL + "/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
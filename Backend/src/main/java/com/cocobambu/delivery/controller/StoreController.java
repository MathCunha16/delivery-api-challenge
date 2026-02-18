package com.cocobambu.delivery.controller;

import com.cocobambu.delivery.controller.api.StoreApi;
import com.cocobambu.delivery.dto.response.StoreResponse;
import com.cocobambu.delivery.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreController implements StoreApi {

    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    @GetMapping
    @Override
    public ResponseEntity<Page<StoreResponse>> getAllStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.getAllStores(page, size));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getStoreById(id));
    }
}

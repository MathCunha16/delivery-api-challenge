package com.cocobambu.delivery.controller;

import com.cocobambu.delivery.controller.api.OrderApi;
import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.service.OrderService;
import com.cocobambu.delivery.util.UriLocationBuilderHelper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController implements OrderApi {

    private final OrderService service;
    private final UriLocationBuilderHelper uriLocationBuilderHelper;

    public OrderController(OrderService service, UriLocationBuilderHelper uriLocationBuilderHelper) {
        this.service = service;
        this.uriLocationBuilderHelper = uriLocationBuilderHelper;
    }

    @PostMapping
    @Override
    public ResponseEntity<OrderWrapperResponse> createOrder(@RequestBody @Valid CreateOrderRequest request){
        OrderWrapperResponse response = service.createOrder(request);
        URI location = uriLocationBuilderHelper.buildLocationUri(response.orderId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Override
    public ResponseEntity<Page<OrderWrapperResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.getAllOrders(page, size));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<OrderWrapperResponse> getOrderById(@PathVariable UUID id){
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("/store/{storeId}")
    @Override
    public ResponseEntity<Page<OrderWrapperResponse>> getOrdersByStore(
            @PathVariable UUID storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(service.getOrdersByStore(storeId, page, size));
    }
}

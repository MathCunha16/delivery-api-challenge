package com.cocobambu.delivery.controller;

import com.cocobambu.delivery.controller.api.OrderApi;
import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.service.OrderService;
import com.cocobambu.delivery.util.UriLocationBuilderHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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
}

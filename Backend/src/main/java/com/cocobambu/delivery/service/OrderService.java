package com.cocobambu.delivery.service;

import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.service.usecase.CreateOrderUseCase;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderService(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    public OrderWrapperResponse createOrder(CreateOrderRequest request){
        return createOrderUseCase.execute(request);
    }

}

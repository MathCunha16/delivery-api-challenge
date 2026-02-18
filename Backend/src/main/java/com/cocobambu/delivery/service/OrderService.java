package com.cocobambu.delivery.service;

import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.service.usecase.CreateOrderUseCase;
import com.cocobambu.delivery.service.usecase.GetAllOrdersUseCase;
import com.cocobambu.delivery.service.usecase.GetOrderByIdUseCase;
import com.cocobambu.delivery.service.usecase.GetOrdersByStoreUseCase;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final GetOrdersByStoreUseCase getOrdersByStoreUseCase;

    public OrderService(CreateOrderUseCase createOrderUseCase, GetAllOrdersUseCase getAllOrdersUseCase, GetOrderByIdUseCase getOrderByIdUseCase, GetOrdersByStoreUseCase getOrdersByStoreUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getAllOrdersUseCase = getAllOrdersUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.getOrdersByStoreUseCase = getOrdersByStoreUseCase;
    }

    public OrderWrapperResponse createOrder(CreateOrderRequest request){
        return createOrderUseCase.execute(request);
    }

    public Page<OrderWrapperResponse> getAllOrders(int page, int size){
        return getAllOrdersUseCase.execute(page, size);
    }

    public OrderWrapperResponse getOrderById(UUID id){
        return getOrderByIdUseCase.execute(id);
    }

    public Page<OrderWrapperResponse> getOrdersByStore(UUID storeId, int page, int size){
        return getOrdersByStoreUseCase.execute(storeId, page, size);
    }
}

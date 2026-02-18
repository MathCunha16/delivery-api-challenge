package com.cocobambu.delivery.service;

import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.request.UpdateOrderStatusRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.service.usecase.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetAllOrdersUseCase getAllOrdersUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final GetOrdersByStoreUseCase getOrdersByStoreUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    public OrderService(CreateOrderUseCase createOrderUseCase, GetAllOrdersUseCase getAllOrdersUseCase, GetOrderByIdUseCase getOrderByIdUseCase, GetOrdersByStoreUseCase getOrdersByStoreUseCase, UpdateOrderStatusUseCase updateOrderStatusUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getAllOrdersUseCase = getAllOrdersUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.getOrdersByStoreUseCase = getOrdersByStoreUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
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

    public OrderWrapperResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request){
        return updateOrderStatusUseCase.execute(orderId, request);
    }
}

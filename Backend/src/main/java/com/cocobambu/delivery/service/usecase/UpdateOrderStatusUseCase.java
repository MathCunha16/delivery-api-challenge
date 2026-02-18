package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.request.UpdateOrderStatusRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.enums.StatusOrigin;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.mapper.OrderMapper;
import com.cocobambu.delivery.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class UpdateOrderStatusUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    public UpdateOrderStatusUseCase(OrderRepository orderRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderWrapperResponse execute(UUID orderId, UpdateOrderStatusRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id:" + orderId));

        order.changeStatusTo(request.status(), StatusOrigin.STORE, OffsetDateTime.now());
        return mapper.toResponse(orderRepository.save(order));
    }
}

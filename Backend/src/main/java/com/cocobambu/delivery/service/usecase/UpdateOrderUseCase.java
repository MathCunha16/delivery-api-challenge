package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.request.UpdateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.exception.BusinessException;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.mapper.OrderMapper;
import com.cocobambu.delivery.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class UpdateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    public UpdateOrderUseCase(OrderRepository orderRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderWrapperResponse execute (UUID id, UpdateOrderRequest request){

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (order.getLastStatus() != OrderStatus.RECEIVED && order.getLastStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException("Cannot update order data for orders with status " + order.getLastStatus());
        }

        mapper.updateEntityFromDto(request, order);

        if (order.getDeliveryAddress() != null) {
            mapper.updateAddressFromDto(request.deliveryAddress(), order.getDeliveryAddress());
        }

        order.setUpdatedAt(OffsetDateTime.now());

        return mapper.toResponse(orderRepository.save(order));
    }
}

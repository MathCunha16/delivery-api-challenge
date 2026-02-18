package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.exception.BusinessException;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class DeleteOrderUseCase {

    private final OrderRepository orderRepository;

    public DeleteOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void execute(UUID id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

        if (order.getLastStatus() != OrderStatus.CANCELED && order.getLastStatus() != OrderStatus.DELIVERED){
            throw new BusinessException("Cannot delete order with status " + order.getLastStatus()
                    + ". Only Canceled and Delivered orders can be deleted.");
        }

        orderRepository.delete(order);
    }
}

package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.exception.BusinessException;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DeleteOrderUseCase deleteOrderUseCase;

    @Test
    @DisplayName("Should delete order successfully when status is CANCELED")
    void shouldDeleteOrderSuccessfullyWhenCanceled() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setLastStatus(OrderStatus.CANCELED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // ACT
        deleteOrderUseCase.execute(orderId);

        // ASSERT
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    @DisplayName("Should delete order successfully when status is DELIVERED")
    void shouldDeleteOrderSuccessfullyWhenDelivered() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setLastStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // ACT
        deleteOrderUseCase.execute(orderId);

        // ASSERT
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    @DisplayName("Should throw BusinessException when tries to delete a order that is not Canceled or Delivered")
    void shouldThrowExceptionWhenTriesToDeleteAnActiveOrder() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setId(orderId);
        order.setLastStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // ACT & ASSERT
        BusinessException exception = assertThrows(BusinessException.class, () -> deleteOrderUseCase.execute(orderId));

        assertTrue(exception.getMessage().contains("Cannot delete order with status"));

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order does not exist")
    void shouldThrowExceptionWhenOrderNotFound() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> deleteOrderUseCase.execute(orderId));

        assertEquals("Order not found with id " + orderId, exception.getMessage());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).delete(any());
    }
}
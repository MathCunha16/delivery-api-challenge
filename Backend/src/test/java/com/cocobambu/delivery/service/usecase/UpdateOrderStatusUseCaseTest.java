package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.request.UpdateOrderStatusRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.mapper.OrderMapper;
import com.cocobambu.delivery.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderStatusUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Test
    @DisplayName("Should update order status and return mapped response successfully")
    void shouldUpdateOrderStatusSuccessfully() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        OrderStatus newStatus = OrderStatus.CONFIRMED;

        UpdateOrderStatusRequest request = mock(UpdateOrderStatusRequest.class);
        when(request.status()).thenReturn(newStatus);

        Order order = mock(Order.class);
        Order savedOrder = new Order();
        OrderWrapperResponse expectedResponse = mock(OrderWrapperResponse.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(mapper.toResponse(savedOrder)).thenReturn(expectedResponse);

        // ACT
        OrderWrapperResponse actualResponse = updateOrderStatusUseCase.execute(orderId, request);

        // ASSERT
        assertThat(actualResponse)
                .isNotNull()
                .isEqualTo(expectedResponse);

        verify(orderRepository).findById(orderId);
        verify(order).changeStatusTo(eq(newStatus), any(), any());
        verify(orderRepository).save(order);
        verify(mapper).toResponse(savedOrder);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order does not exist")
    void shouldThrowExceptionWhenOrderNotFound() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        UpdateOrderStatusRequest request = mock(UpdateOrderStatusRequest.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                updateOrderStatusUseCase.execute(orderId, request)
        );

        assertThat(exception.getMessage()).isEqualTo("Order not found with id:" + orderId);
        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any());
        verifyNoInteractions(mapper);
    }
}
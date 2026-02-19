package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.request.UpdateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.DeliveryAddress;
import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.exception.BusinessException;
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
class UpdateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private UpdateOrderUseCase updateOrderUseCase;

    @Test
    @DisplayName("Should update order successfully when status is RECEIVED and has delivery address")
    void shouldUpdateOrderWhenStatusIsReceivedAndHasAddress() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        UpdateOrderRequest request = mock(UpdateOrderRequest.class);

        Order order = new Order();
        order.setId(orderId);
        order.setLastStatus(OrderStatus.RECEIVED);

        DeliveryAddress address = new DeliveryAddress();
        order.setDeliveryAddress(address);

        OrderWrapperResponse expectedResponse = mock(OrderWrapperResponse.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(mapper.toResponse(order)).thenReturn(expectedResponse);

        // ACT
        OrderWrapperResponse actualResponse = updateOrderUseCase.execute(orderId, request);

        // ASSERT
        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);
        assertThat(order.getUpdatedAt()).isNotNull();

        verify(orderRepository).findById(orderId);
        verify(mapper).updateEntityFromDto(request, order);
        verify(mapper).updateAddressFromDto(request.deliveryAddress(), address);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should update order successfully when status is CONFIRMED and has NO delivery address")
    void shouldUpdateOrderWhenStatusIsConfirmedAndNoAddress() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        UpdateOrderRequest request = mock(UpdateOrderRequest.class);

        Order order = new Order();
        order.setId(orderId);
        order.setLastStatus(OrderStatus.CONFIRMED);
        order.setDeliveryAddress(null);

        OrderWrapperResponse expectedResponse = mock(OrderWrapperResponse.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(mapper.toResponse(order)).thenReturn(expectedResponse);

        // ACT
        OrderWrapperResponse actualResponse = updateOrderUseCase.execute(orderId, request);

        // ASSERT
        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);

        verify(mapper).updateEntityFromDto(request, order);
        verify(mapper, never()).updateAddressFromDto(any(), any());
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should throw BusinessException when order status is not RECEIVED or CONFIRMED")
    void shouldThrowExceptionWhenOrderStatusIsInvalidForUpdate() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        UpdateOrderRequest request = mock(UpdateOrderRequest.class);

        Order order = new Order();
        order.setId(orderId);
        order.setLastStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // ACT & ASSERT
        BusinessException exception = assertThrows(BusinessException.class, () ->
                updateOrderUseCase.execute(orderId, request)
        );

        assertThat(exception.getMessage()).contains("Cannot update order data for orders with status DELIVERED");

        verify(orderRepository).findById(orderId);
        verify(mapper, never()).updateEntityFromDto(any(), any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order does not exist")
    void shouldThrowExceptionWhenOrderNotFound() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        UpdateOrderRequest request = mock(UpdateOrderRequest.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                updateOrderUseCase.execute(orderId, request)
        );

        assertThat(exception.getMessage()).isEqualTo("Order not found with id: " + orderId);

        verify(orderRepository).findById(orderId);
        verifyNoInteractions(mapper);
        verify(orderRepository, never()).save(any());
    }
}
package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Order;
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
class GetOrderByIdUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private GetOrderByIdUseCase getOrderByIdUseCase;

    @Test
    @DisplayName("Should return mapped order successfully when order exists")
    void shouldReturnMappedOrderSuccessfully() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        OrderWrapperResponse expectedResponse = mock(OrderWrapperResponse.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(mapper.toResponse(order)).thenReturn(expectedResponse);

        // ACT
        OrderWrapperResponse actualResponse = getOrderByIdUseCase.execute(orderId);

        // ASSERT
        assertThat(actualResponse)
                .isNotNull()
                .isEqualTo(expectedResponse);

        verify(orderRepository, times(1)).findById(orderId);
        verify(mapper, times(1)).toResponse(order);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order does not exist")
    void shouldThrowExceptionWhenOrderNotFound() {
        // ARRANGE
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> getOrderByIdUseCase.execute(orderId));

        assertThat(exception.getMessage()).isEqualTo("Order not found with id:" + orderId);

        verify(orderRepository, times(1)).findById(orderId);
        verify(mapper, never()).toResponse(any());
    }
}
package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.mapper.OrderMapper;
import com.cocobambu.delivery.repository.OrderRepository;
import com.cocobambu.delivery.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOrdersByStoreUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private GetOrdersByStoreUseCase getOrdersByStoreUseCase;

    @Test
    @DisplayName("Should return paginated orders by store successfully when store exists")
    void shouldReturnOrdersByStoreSuccessfully() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        Pageable expectedPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Order order = new Order();
        OrderWrapperResponse responseDto = mock(OrderWrapperResponse.class);
        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(orderRepository.findAllByStoreId(storeId, expectedPageable)).thenReturn(orderPage);
        when(mapper.toResponse(order)).thenReturn(responseDto);

        // ACT
        Page<OrderWrapperResponse> result = getOrdersByStoreUseCase.execute(storeId, page, size);

        // ASSERT
        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        assertThat(result.getContent().getFirst()).isEqualTo(responseDto);

        verify(storeRepository).existsById(storeId);
        verify(orderRepository).findAllByStoreId(storeId, expectedPageable);
        verify(mapper).toResponse(order);
    }

    @Test
    @DisplayName("Should return an empty page when store exists but has no orders")
    void shouldReturnEmptyPageWhenStoreHasNoOrders() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        Pageable expectedPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> emptyPage = new PageImpl<>(Collections.emptyList());

        when(storeRepository.existsById(storeId)).thenReturn(true);
        when(orderRepository.findAllByStoreId(storeId, expectedPageable)).thenReturn(emptyPage);

        // ACT
        Page<OrderWrapperResponse> result = getOrdersByStoreUseCase.execute(storeId, page, size);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

        verify(storeRepository).existsById(storeId);
        verify(orderRepository).findAllByStoreId(storeId, expectedPageable);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when store does not exist")
    void shouldThrowExceptionWhenStoreNotFound() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        when(storeRepository.existsById(storeId)).thenReturn(false);

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                getOrdersByStoreUseCase.execute(storeId, 0, 10)
        );

        assertThat(exception.getMessage()).contains("Store not found with id: " + storeId);

        verify(storeRepository).existsById(storeId);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(mapper);
    }
}
package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.mapper.OrderMapper;
import com.cocobambu.delivery.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllOrdersUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private GetAllOrdersUseCase getAllOrdersUseCase;

    @Test
    @DisplayName("Should return a paginated list of mapped orders successfully")
    void shouldReturnPaginatedOrdersSuccessfully() {
        // ARRANGE
        int page = 0;
        int size = 10;

        Pageable expectedPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Order order = new Order();
        OrderWrapperResponse responseDto = mock(OrderWrapperResponse.class);

        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(orderRepository.findAll(expectedPageable)).thenReturn(orderPage);
        when(mapper.toResponse(order)).thenReturn(responseDto);

        // ACT
        Page<OrderWrapperResponse> result = getAllOrdersUseCase.execute(page, size);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(responseDto);

        verify(orderRepository, times(1)).findAll(expectedPageable);
        verify(mapper, times(1)).toResponse(order);
    }

    @Test
    @DisplayName("Should return an empty page when no orders exist in the database")
    void shouldReturnEmptyPageWhenNoOrdersExist() {
        // ARRANGE
        int page = 0;
        int size = 10;
        Pageable expectedPageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> emptyPage = new PageImpl<>(Collections.emptyList());

        when(orderRepository.findAll(expectedPageable)).thenReturn(emptyPage);

        // ACT
        Page<OrderWrapperResponse> result = getAllOrdersUseCase.execute(page, size);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

        verify(orderRepository, times(1)).findAll(expectedPageable);
        verify(mapper, never()).toResponse(any());
    }
}
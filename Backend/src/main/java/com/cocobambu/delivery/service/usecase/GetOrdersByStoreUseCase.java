package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.Order;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.mapper.OrderMapper;
import com.cocobambu.delivery.repository.OrderRepository;
import com.cocobambu.delivery.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class GetOrdersByStoreUseCase {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final OrderMapper mapper;

    public GetOrdersByStoreUseCase(OrderRepository orderRepository, StoreRepository storeRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<OrderWrapperResponse> execute(UUID storeId, int page, int size){

        if (!storeRepository.existsById(storeId)){
            throw new ResourceNotFoundException("Store not found with id: " + storeId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findAllByStoreId(storeId, pageable);
        return orders.map(mapper::toResponse);
    }
}

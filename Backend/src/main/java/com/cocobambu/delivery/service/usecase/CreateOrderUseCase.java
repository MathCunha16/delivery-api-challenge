package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.*;
import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.enums.PaymentMethod;
import com.cocobambu.delivery.enums.StatusOrigin;
import com.cocobambu.delivery.exception.BusinessException;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.mapper.OrderMapper;
import com.cocobambu.delivery.repository.OrderRepository;
import com.cocobambu.delivery.repository.StoreRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final OrderMapper mapper;

    public CreateOrderUseCase(OrderRepository orderRepository, StoreRepository storeRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderWrapperResponse execute(CreateOrderRequest request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id:" + request.storeId()));

        OffsetDateTime now = OffsetDateTime.now();

        Order order = mapper.toEntity(request);
        order.setId(UUID.randomUUID());
        order.setStore(store);
        order.setCreatedAt(now);
        order.changeStatusTo(OrderStatus.RECEIVED, StatusOrigin.STORE, now); // ja adiciona no histórico automaticamente
        calculateOrderTotal(order);
        validateOrderTotals(order);
        validatePaymentMethods(order);

        Order savedOrder = orderRepository.save(order);

        return mapper.toResponse(savedOrder);

    }

    private void calculateOrderTotal(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            order.setTotalPrice(BigDecimal.ZERO);
            return;
        }

        BigDecimal total = order.getItems().stream()
                .map(item -> {
                    BigDecimal itemTotal = item.getUnitPrice();

                    if (item.getCondiments() != null && !item.getCondiments().isEmpty())  {
                        BigDecimal condimentsTotal = item.getCondiments().stream()
                                .map(OrderItemCondiment::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        itemTotal = itemTotal.add(condimentsTotal);
                    }

                    BigDecimal finalItemPrice = itemTotal.multiply(BigDecimal.valueOf(item.getQuantity()));
                    item.setTotalPrice(finalItemPrice);
                    return finalItemPrice;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(total);
    }

    private void validateOrderTotals(Order order) {
        if (order.getPayments() == null || order.getPayments().isEmpty()) {
            throw new BusinessException("Order must have at least one payment");
        }

        BigDecimal totalPayments = order.getPayments().stream()
                .map(Payment::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (order.getTotalPrice().compareTo(totalPayments) != 0) {
            throw new BusinessException(
                    String.format("Value mismatch: Order Total (%s) differs from Total Paid (%s)",
                            order.getTotalPrice(), totalPayments));
        }
    }

    private void validatePaymentMethods(Order order) {
        for (Payment payment : order.getPayments()) {
            PaymentMethod method = payment.getPaymentMethod();
            boolean isPrepaid = payment.getPrepaid();

            if (isPrepaid && !method.acceptsOnline()) {
                throw new BusinessException(
                        String.format("Payment method %s is not accepted online", method));
            }

            if (!isPrepaid && !method.acceptsPresential()) {
                throw new BusinessException(
                        String.format("The payment method %s is not accepted upon delivery.", method));
            }
        }
    }
}

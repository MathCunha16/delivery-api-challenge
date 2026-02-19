package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.*;
import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.enums.PaymentMethod;
import com.cocobambu.delivery.exception.BusinessException;
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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OrderMapper mapper;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Test
    @DisplayName("Should create order successfully when all data is valid")
    void shouldCreateOrderSuccessfully() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        CreateOrderRequest request = mock(CreateOrderRequest.class);
        when(request.storeId()).thenReturn(storeId);

        Store store = new Store();
        store.setId(storeId);

        Order order = createValidOrderMock();
        Order savedOrder = new Order();
        OrderWrapperResponse expectedResponse = mock(OrderWrapperResponse.class);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(mapper.toEntity(request)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(mapper.toResponse(savedOrder)).thenReturn(expectedResponse);

        // ACT
        OrderWrapperResponse actualResponse = createOrderUseCase.execute(request);

        // ASSERT
        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);
        assertThat(order.getTotalPrice()).isEqualByComparingTo(new BigDecimal("110.00"));
        assertThat(order.getLastStatus()).isEqualTo(OrderStatus.RECEIVED);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when store does not exist")
    void shouldThrowExceptionWhenStoreNotFound() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        CreateOrderRequest request = mock(CreateOrderRequest.class);
        when(request.storeId()).thenReturn(storeId);

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Store not found with id:" + storeId);

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException when order total differs from payments total")
    void shouldThrowExceptionWhenTotalDiffersFromPayment() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        CreateOrderRequest request = mock(CreateOrderRequest.class);
        when(request.storeId()).thenReturn(storeId);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(new Store()));

        Order order = createValidOrderMock();
        order.getPayments().getFirst().setValue(new BigDecimal("50.00"));

        when(mapper.toEntity(request)).thenReturn(order);

        // ACT & ASSERT
        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Value mismatch: Order Total");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException when trying to use CASH for online payment")
    void shouldThrowExceptionWhenPaymentMethodNotAcceptedOnline() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        CreateOrderRequest request = mock(CreateOrderRequest.class);
        when(request.storeId()).thenReturn(storeId);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(new Store()));

        Order order = createValidOrderMock();
        Payment payment = order.getPayments().getFirst();
        payment.setPaymentMethod(PaymentMethod.CASH);
        payment.setPrepaid(true);

        when(mapper.toEntity(request)).thenReturn(order);

        // ACT & ASSERT
        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("is not accepted online");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessException when trying to use PIX for delivery payment")
    void shouldThrowExceptionWhenPaymentMethodNotAcceptedPresential() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        CreateOrderRequest request = mock(CreateOrderRequest.class);
        when(request.storeId()).thenReturn(storeId);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(new Store()));

        Order order = createValidOrderMock();
        Payment payment = order.getPayments().getFirst();
        payment.setPaymentMethod(PaymentMethod.PIX);
        payment.setPrepaid(false);

        when(mapper.toEntity(request)).thenReturn(order);

        // ACT & ASSERT
        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("is not accepted upon delivery");

        verify(orderRepository, never()).save(any());
    }

    // --- Helper Methods ---

    private Order createValidOrderMock() {
        Order order = new Order();

        OrderItem item = new OrderItem();
        item.setUnitPrice(new BigDecimal("100.00"));
        item.setQuantity(1);

        OrderItemCondiment condiment = new OrderItemCondiment();
        condiment.setPrice(new BigDecimal("10.00"));

        item.addCondiment(condiment);

        order.addItem(item);

        Payment payment = new Payment();
        payment.setValue(new BigDecimal("110.00"));
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setPrepaid(true);

        order.addPayment(payment);

        return order;
    }
}
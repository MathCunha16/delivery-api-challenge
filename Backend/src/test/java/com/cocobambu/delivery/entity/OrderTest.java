package com.cocobambu.delivery.entity;

import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.enums.StatusOrigin;
import com.cocobambu.delivery.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    @DisplayName("Should establish bidirectional relationship when setting Delivery Address")
    void shouldEstablishBidirectionalRelationshipWhenSettingDeliveryAddress() {
        // ARRANGE
        Order order = new Order();
        DeliveryAddress address = new DeliveryAddress();

        // ACT
        order.setDeliveryAddress(address);

        // ASSERT
        assertThat(order.getDeliveryAddress()).isEqualTo(address);
        assertThat(address.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("Should establish bidirectional relationship when adding an Item")
    void shouldEstablishBidirectionalRelationshipWhenAddingItem() {
        // ARRANGE
        Order order = new Order();
        OrderItem item = new OrderItem();

        // ACT
        order.addItem(item);

        // ASSERT
        assertThat(order.getItems()).containsExactly(item);
        assertThat(item.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("Should establish bidirectional relationship when adding a Payment")
    void shouldEstablishBidirectionalRelationshipWhenAddingPayment() {
        // ARRANGE
        Order order = new Order();
        Payment payment = new Payment();

        // ACT
        order.addPayment(payment);

        // ASSERT
        assertThat(order.getPayments()).containsExactly(payment);
        assertThat(payment.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("Should successfully change status and add to history when transition is valid")
    void shouldChangeStatusAndCreateHistoryRecord() {
        // ARRANGE
        Order order = new Order();
        OffsetDateTime now = OffsetDateTime.now();

        // ACT
        order.changeStatusTo(OrderStatus.RECEIVED, StatusOrigin.STORE, now);

        // ASSERT
        assertThat(order.getLastStatus()).isEqualTo(OrderStatus.RECEIVED);
        assertThat(order.getHistory()).hasSize(1);

        OrderStatusHistory historyRecord = order.getHistory().getFirst();
        assertThat(historyRecord.getStatus()).isEqualTo(OrderStatus.RECEIVED);
        assertThat(historyRecord.getOrigin()).isEqualTo(StatusOrigin.STORE);
        assertThat(historyRecord.getCreatedAt()).isEqualTo(now);
        assertThat(historyRecord.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("Should do nothing when trying to change to the exact same status (Idempotency)")
    void shouldDoNothingWhenChangingToSameStatus() {
        // ARRANGE
        Order order = new Order();
        order.setLastStatus(OrderStatus.RECEIVED);
        OffsetDateTime now = OffsetDateTime.now();

        // ACT
        order.changeStatusTo(OrderStatus.RECEIVED, StatusOrigin.STORE, now);

        // ASSERT
        assertThat(order.getLastStatus()).isEqualTo(OrderStatus.RECEIVED);
        assertThat(order.getHistory()).isEmpty();
    }

    @Test
    @DisplayName("Should throw BusinessException when state transition is invalid")
    void shouldThrowExceptionWhenTransitionIsInvalid() {
        // ARRANGE
        Order order = new Order();
        order.setLastStatus(OrderStatus.DELIVERED);
        OffsetDateTime now = OffsetDateTime.now();

        // ACT & ASSERT
        BusinessException exception = assertThrows(BusinessException.class, () ->
                order.changeStatusTo(OrderStatus.RECEIVED, StatusOrigin.STORE, now)
        );

        assertThat(exception.getMessage()).contains("Invalid status transition");
        assertThat(order.getLastStatus()).isEqualTo(OrderStatus.DELIVERED);
        assertThat(order.getHistory()).isEmpty();
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when any parameter in changeStatusTo is null")
    void shouldThrowExceptionWhenChangeStatusArgumentsAreNull() {
        // ARRANGE
        Order order = new Order();
        OffsetDateTime now = OffsetDateTime.now();

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                order.changeStatusTo(null, StatusOrigin.STORE, now)
        );

        assertThrows(IllegalArgumentException.class, () ->
                order.changeStatusTo(OrderStatus.RECEIVED, null, now)
        );

        assertThrows(IllegalArgumentException.class, () ->
                order.changeStatusTo(OrderStatus.RECEIVED, StatusOrigin.STORE, null)
        );
    }

}
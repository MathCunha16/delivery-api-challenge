package com.cocobambu.delivery.entity;

import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.enums.StatusOrigin;
import com.cocobambu.delivery.exception.BusinessException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_status", nullable = false)
    private OrderStatus lastStatus;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    // Relationships

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private DeliveryAddress deliveryAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @OrderBy("createdAt ASC")
    private List<OrderStatusHistory> history = new ArrayList<>(); // evita NPE

    public Order() {
        // Empty Constructor
    }

    public Order(UUID id, Store store, BigDecimal totalPrice, OrderStatus lastStatus, String customerName, String customerPhone, OffsetDateTime createdAt, OffsetDateTime updatedAt, List<OrderItem> items, DeliveryAddress deliveryAddress, List<Payment> payments, List<OrderStatusHistory> history) {
        this.id = id;
        this.store = store;
        this.totalPrice = totalPrice;
        this.lastStatus = lastStatus;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.payments = payments;
        this.history = history;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(OrderStatus lastStatus) { // não dá pra deixar protected por causa do mapper do seeder
        this.lastStatus = lastStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        if (deliveryAddress != null) {
            deliveryAddress.setOrder(this);
        }
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public List<OrderStatusHistory> getHistory() {
        return history;
    }

    // ------ Controle de estado -----
    public void changeStatusTo(OrderStatus newStatus, StatusOrigin origin, OffsetDateTime when) {
        if (newStatus == null) throw new IllegalArgumentException("newStatus is null");
        if (origin == null) throw new IllegalArgumentException("origin is null");
        if (when == null) throw new IllegalArgumentException("when is null");

        if (this.lastStatus == newStatus) return;

        if (this.lastStatus != null && !this.lastStatus.canTransitionTo(newStatus)) {
            throw new BusinessException("Invalid status transition: " + this.lastStatus + " -> " + newStatus);
        }

        this.lastStatus = newStatus;

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrder(this);
        orderStatusHistory.setStatus(newStatus);
        orderStatusHistory.setOrigin(origin);
        orderStatusHistory.setCreatedAt(when);

        this.history.add(orderStatusHistory);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // ------ Helper methods -----

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setOrder(this);
    }

    public void addHistory(OrderStatusHistory historyItem) {
        history.add(historyItem);
        historyItem.setOrder(this);
    }
}

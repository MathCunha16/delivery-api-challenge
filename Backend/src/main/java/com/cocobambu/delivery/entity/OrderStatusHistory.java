package com.cocobambu.delivery.entity;

import com.cocobambu.delivery.enums.OrderStatus;
import com.cocobambu.delivery.enums.StatusOrigin;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrigin origin;

    public OrderStatusHistory() {
        // Empty Constructor
    }

    public OrderStatusHistory(UUID id, Order order, OrderStatus status, OffsetDateTime createdAt, StatusOrigin origin) {
        this.id = id;
        this.order = order;
        this.status = status;
        this.createdAt = createdAt;
        this.origin = origin;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public StatusOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(StatusOrigin origin) {
        this.origin = origin;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatusHistory that = (OrderStatusHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

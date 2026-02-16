package com.cocobambu.delivery.entity;

import com.cocobambu.delivery.enums.PaymentMethod;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(name = "is_prepaid")
    private Boolean isPrepaid;

    public Payment() {
        // Empty Constructor
    }

    public Payment(UUID id, Order order, PaymentMethod paymentMethod, BigDecimal value, Boolean isPrepaid) {
        this.id = id;
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.value = value;
        this.isPrepaid = isPrepaid;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getPrepaid() {
        return isPrepaid;
    }

    public void setPrepaid(Boolean prepaid) {
        isPrepaid = prepaid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

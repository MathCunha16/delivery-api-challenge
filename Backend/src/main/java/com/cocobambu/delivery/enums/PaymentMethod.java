package com.cocobambu.delivery.enums;

public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PIX,
    CASH,
    VR,
    VA;

    public boolean acceptsOnline() {
        return this != CASH;
    }

    public boolean acceptsPresential() {
        return this != PIX;
    }
}

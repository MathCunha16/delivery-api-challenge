package com.cocobambu.delivery.enums;

public enum OrderStatus {
    RECEIVED {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == CONFIRMED || newStatus == CANCELED;
        }
    },

    CONFIRMED {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == DISPATCHED || newStatus == CANCELED;
        }
    },

    DISPATCHED {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == DELIVERED || newStatus == CANCELED;
        }
    },

    DELIVERED {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return false;
        }
    },

    CANCELED {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(OrderStatus newStatus);
}
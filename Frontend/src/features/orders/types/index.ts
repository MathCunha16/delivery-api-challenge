import type { Store } from '../../stores/types';

export type OrderStatus = 'RECEIVED' | 'CONFIRMED' | 'DISPATCHED' | 'DELIVERED' | 'CANCELED';

export interface Customer {
    id?: number; // Not always present in response, but useful
    name: string;
    temporary_phone?: string;
}

export interface Coordinates {
    latitude: number;
    longitude: number;
}

export interface Address {
    street_name: string;
    street_number: string;
    neighborhood: string;
    city: string;
    state: string;
    zip_code?: string; // Used in POST/PUT
    postal_code?: string; // Used in GET responses
    complement?: string;
    reference?: string;
    coordinates?: Coordinates;
}

export interface Payment {
    origin: 'CREDIT_CARD' | 'DEBIT_CARD' | 'PIX' | 'CASH' | 'VR' | 'VA';
    value: number;
    prepaid: boolean;
}

export interface Condiment {
    name: string;
    price: number;
}

export interface OrderItem {
    code: number;
    name: string;
    quantity: number;
    price: number; // unit price
    total_price: number;
    discount?: number;
    observations?: string;
    condiments?: Condiment[];
    // Swagger mentions 'code' as int32
}

export interface OrderStatusHistory {
    created_at: number; // epoch milliseconds
    name: OrderStatus;
    order_id: string; // uuid
    origin: 'STORE' | 'CUSTOMER' | 'SYSTEM' | 'DRIVER';
}

export interface Order {
    order_id: string; // UUID
    store: Store;
    customer: Customer;
    delivery_address: Address;
    items: OrderItem[];
    payments: Payment[];
    total_price: number;
    created_at: number; // epoch milliseconds
    last_status_name: OrderStatus;
    statuses: OrderStatusHistory[];
}

export interface OrderWrapper {
    store_id: string;
    order_id: string;
    order: Order;
}

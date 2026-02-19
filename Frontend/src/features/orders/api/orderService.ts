import api from '../../../lib/axios';
import type { PagedModel } from '../../stores/types';
import type { OrderWrapper } from '../types';
import type { Order } from '../types';
import type { OrderStatus } from '../types';

export const getOrdersByStore = async (storeId: string, page = 0, size = 10): Promise<PagedModel<OrderWrapper>> => {
  const response = await api.get<PagedModel<OrderWrapper>>(`/orders/store/${storeId}`, {
    params: { page, size },
  });
  return response.data;
};

export const getOrderById = async (orderId: string): Promise<Order> => {
  const response = await api.get<OrderWrapper>(`/orders/${orderId}`);
  return response.data.order;
};

export const updateOrderStatus = async (orderId: string, status: OrderStatus): Promise<Order> => {
  const response = await api.patch<OrderWrapper>(`/orders/${orderId}/status`, { status });
  return response.data.order;
};

export const updateOrder = async (orderId: string, payload: {
  customer_name: string;
  temporary_phone: string;
  delivery_address: {
    street_name: string;
    street_number: string;
    neighborhood: string;
    city: string;
    state: string;
    zip_code: string;
    complement?: string;
    reference?: string;
    country: string;
    coordinates?: {
      latitude: number;
      longitude: number;
      coordinate_id?: number;
    }
  }
}): Promise<Order> => {
  const response = await api.put<OrderWrapper>(`/orders/${orderId}`, payload);
  // The API might return different structures for PUT, but usually consistent with GET/POST
  return response.data.order;
};

export const deleteOrder = async (orderId: string): Promise<void> => {
  await api.delete(`/orders/${orderId}`);
};

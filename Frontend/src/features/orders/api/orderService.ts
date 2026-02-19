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

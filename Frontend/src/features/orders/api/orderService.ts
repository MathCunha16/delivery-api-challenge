import api from '../../../lib/axios';
import type { PagedModel } from '../../stores/types';
import type { OrderWrapper } from '../types';

export const getOrdersByStore = async (storeId: string, page = 0, size = 10): Promise<PagedModel<OrderWrapper>> => {
    const response = await api.get<PagedModel<OrderWrapper>>(`/orders/store/${storeId}`, {
        params: { page, size },
    });
    return response.data;
};

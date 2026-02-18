import api from '../../../lib/axios';
import type { PagedModel, Store } from '../types';

export const getStores = async (page = 0, size = 50): Promise<PagedModel<Store>> => {
    const response = await api.get<PagedModel<Store>>('/stores', {
        params: { page, size },
    });
    return response.data;
};

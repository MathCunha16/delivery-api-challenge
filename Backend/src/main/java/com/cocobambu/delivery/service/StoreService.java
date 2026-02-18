package com.cocobambu.delivery.service;

import com.cocobambu.delivery.dto.response.StoreResponse;
import com.cocobambu.delivery.service.usecase.GetAllStoresUseCase;
import com.cocobambu.delivery.service.usecase.GetStoreByIdUseCase;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StoreService {

    private final GetAllStoresUseCase getAllStoresUseCase;
    private final GetStoreByIdUseCase getStoreByIdUseCase;

    public StoreService(GetAllStoresUseCase getAllStoresUseCase, GetStoreByIdUseCase getStoreByIdUseCase) {
        this.getAllStoresUseCase = getAllStoresUseCase;
        this.getStoreByIdUseCase = getStoreByIdUseCase;
    }

    public Page<StoreResponse> getAllStores(int page, int size){
        return getAllStoresUseCase.execute(page, size);
    }

    public StoreResponse getStoreById(UUID id){
        return getStoreByIdUseCase.execute(id);
    }
}

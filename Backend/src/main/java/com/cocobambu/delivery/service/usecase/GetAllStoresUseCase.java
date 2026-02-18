package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.response.StoreResponse;
import com.cocobambu.delivery.entity.Store;
import com.cocobambu.delivery.mapper.StoreMapper;
import com.cocobambu.delivery.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetAllStoresUseCase {

    private final StoreRepository storeRepository;
    private final StoreMapper mapper;

    public GetAllStoresUseCase(StoreRepository storeRepository, StoreMapper mapper) {
        this.storeRepository = storeRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<StoreResponse> execute(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Store> stores = storeRepository.findAll(pageable);
        return stores.map(mapper::toResponse);
    }
}

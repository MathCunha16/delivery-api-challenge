package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.response.StoreResponse;
import com.cocobambu.delivery.entity.Store;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.mapper.StoreMapper;
import com.cocobambu.delivery.repository.StoreRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class GetStoreByIdUseCase {

    private final StoreRepository storeRepository;
    private final StoreMapper mapper;

    public GetStoreByIdUseCase(StoreRepository storeRepository, StoreMapper mapper) {
        this.storeRepository = storeRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public StoreResponse execute(UUID id){
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No store was found with id:" + id));

        return mapper.toResponse(store);
    }
}

package com.cocobambu.delivery.mapper;

import com.cocobambu.delivery.dto.response.StoreResponse;
import com.cocobambu.delivery.entity.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    StoreResponse toResponse(Store store);

    // feito para pegar o json da seeder
    Store toStoreEntityFromResponse(StoreResponse storeResponse);
}

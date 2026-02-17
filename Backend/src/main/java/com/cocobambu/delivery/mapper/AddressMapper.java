package com.cocobambu.delivery.mapper;

import com.cocobambu.delivery.dto.request.CreateAddressRequest;
import com.cocobambu.delivery.dto.response.DeliveryAddressResponse;
import com.cocobambu.delivery.entity.DeliveryAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "coordinateId", target = "coordinates.coordinateId")
    @Mapping(source = "latitude", target = "coordinates.latitude")
    @Mapping(source = "longitude", target = "coordinates.longitude")
    DeliveryAddressResponse toAddressResponse(DeliveryAddress address);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(source = "coordinates.coordinateId", target = "coordinateId")
    @Mapping(source = "coordinates.latitude", target = "latitude")
    @Mapping(source = "coordinates.longitude", target = "longitude")
    DeliveryAddress toAddressEntity(CreateAddressRequest request);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "coordinateId", source = "coordinates.coordinateId")
    @Mapping(target = "latitude", source = "coordinates.latitude")
    @Mapping(target = "longitude", source = "coordinates.longitude")
    DeliveryAddress toAddressEntityFromResponse(DeliveryAddressResponse dto); // <-- para o seeder
}
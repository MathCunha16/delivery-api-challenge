package com.cocobambu.delivery.mapper;

import com.cocobambu.delivery.dto.response.OrderStatusResponse;
import com.cocobambu.delivery.entity.OrderStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatusHistoryMapper {

    @Mapping(source = "status", target = "name")
    @Mapping(source = "order.id", target = "orderId")
    OrderStatusResponse toStatusResponse(OrderStatusHistory history);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "status", source = "name")
    OrderStatusHistory toStatusEntityFromResponse(OrderStatusResponse dto); // <-- para o seeder
}

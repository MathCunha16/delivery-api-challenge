package com.cocobambu.delivery.mapper;

import com.cocobambu.delivery.dto.request.CreateCondimentRequest;
import com.cocobambu.delivery.dto.request.CreateOrderItemRequest;
import com.cocobambu.delivery.dto.response.CondimentResponse;
import com.cocobambu.delivery.dto.response.OrderItemsResponse;
import com.cocobambu.delivery.entity.OrderItem;
import com.cocobambu.delivery.entity.OrderItemCondiment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "externalCode", target = "externalCode")
    @Mapping(source = "unitPrice", target = "unitPrice")
    OrderItemsResponse toOrderItemsResponse(OrderItem item);

    CondimentResponse toCondimentResponse(OrderItemCondiment condiment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "discount", constant = "0.0") // por padrão $0.0 de desconto
    OrderItem toOrderItemEntity(CreateOrderItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItem", ignore = true)
    OrderItemCondiment toCondimentEntity(CreateCondimentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "externalCode", source = "externalCode")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "discount", source = "discount", defaultValue = "0.0")
    OrderItem toItemEntityFromResponse(OrderItemsResponse dto); // <--- para o seeder

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderItem", ignore = true)
    OrderItemCondiment toCondimentEntityFromResponse(CondimentResponse dto); // <-- para o seeder
}

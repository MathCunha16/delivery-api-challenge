package com.cocobambu.delivery.mapper;

import com.cocobambu.delivery.dto.response.OrderStatusResponse;
import com.cocobambu.delivery.entity.OrderStatusHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface StatusHistoryMapper {

    @Mapping(source = "status", target = "name")
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "offsetDateTimeToMillis")
    OrderStatusResponse toStatusResponse(OrderStatusHistory history);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "status", source = "name")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "millisToOffsetDateTime")
    OrderStatusHistory toStatusEntityFromResponse(OrderStatusResponse dto); // <-- para o seeder

    @Named("millisToOffsetDateTime")
    default OffsetDateTime mapMillis(Long timestamp) {
        if (timestamp == null) return null;
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }

    @Named("offsetDateTimeToMillis")
    default Long mapOffsetDateTime(OffsetDateTime date) {
        if (date == null) return null;
        return date.toInstant().toEpochMilli();
    }
}
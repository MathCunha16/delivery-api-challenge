package com.cocobambu.delivery.mapper;

import com.cocobambu.delivery.dto.request.CreateAddressRequest;
import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.request.UpdateOrderRequest;
import com.cocobambu.delivery.dto.response.CustomerResponse;
import com.cocobambu.delivery.dto.response.OrderDetailsResponse;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import com.cocobambu.delivery.entity.DeliveryAddress;
import com.cocobambu.delivery.entity.Order;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(
        componentModel = "spring",
        uses = {
                StoreMapper.class,
                PaymentMapper.class,
                AddressMapper.class,
                ItemMapper.class,
                StatusHistoryMapper.class
        }
)
public abstract class OrderMapper {

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = ".", target = "order")
    public abstract OrderWrapperResponse toResponse(Order order);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "lastStatus", target = "lastStatusName")
    @Mapping(source = "history", target = "statuses")
    @Mapping(source = ".", target = "customer")
    public abstract OrderDetailsResponse toDetailsResponse(Order order);

    // Lógica manual pra mapear os dados do cliente (plano no banco, objeto no DTO pra seguir regras do json)
    protected CustomerResponse mapCustomer(Order order) {
        if (order == null) return null;
        return new CustomerResponse(order.getCustomerPhone(), order.getCustomerName());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "customerPhone", source = "customer.temporaryPhone")
    @Mapping(target = "lastStatus", ignore = true)
    @Mapping(target = "history", ignore = true)
    public abstract Order toEntity(CreateOrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "lastStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "payments", ignore = true)
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "deliveryAddress", ignore = true)
    public abstract void updateEntityFromDto(UpdateOrderRequest dto, @MappingTarget Order entity);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "coordinateId", source = "coordinates.coordinateId")
    @Mapping(target = "latitude", source = "coordinates.latitude")
    @Mapping(target = "longitude", source = "coordinates.longitude")
    public abstract void updateAddressFromDto(CreateAddressRequest dto, @MappingTarget DeliveryAddress entity);

    @Mapping(target = "id", source = "orderId")
    @Mapping(target = "lastStatus", source = "lastStatusName")
    @Mapping(target = "history", source = "statuses")
    @Mapping(target = "store", source = "store")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "customerPhone", source = "customer.temporaryPhone")
    @Mapping(target = "deliveryAddress", source = "deliveryAddress")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "payments", source = "payments")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "orderMillisToDate")
    public abstract Order toEntityFromResponse(OrderDetailsResponse dto); // <-- para o seder

    // conversor de Data (Millis -> OffsetDateTime)
    @Named("orderMillisToDate")
    protected OffsetDateTime mapMillis(Long timestamp) {
        if (timestamp == null) return null;
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }

    protected Long mapOffsetDateTimeToMillis(OffsetDateTime date) {
        if (date == null) return null;
        return date.toInstant().toEpochMilli();
    }

    @AfterMapping
    protected void linkBidirectionalReferences(@MappingTarget Order order) {
        if (order.getItems() != null) {
            order.getItems().forEach(item -> {
                item.setOrder(order);
                if (item.getCondiments() != null) {
                    item.getCondiments().forEach(c -> c.setOrderItem(item));
                }
            });
        }

        if (order.getPayments() != null) {
            order.getPayments().forEach(payment -> payment.setOrder(order));
        }

        if (order.getDeliveryAddress() != null) {
            order.getDeliveryAddress().setOrder(order);
        }

        if (order.getHistory() != null) {
            order.getHistory().forEach(h -> h.setOrder(order));
        }
    }
}
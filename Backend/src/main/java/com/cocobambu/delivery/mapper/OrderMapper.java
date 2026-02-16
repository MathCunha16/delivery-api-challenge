package com.cocobambu.delivery.mapper;

import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.response.*;
import com.cocobambu.delivery.entity.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = ".", target = "order")
    OrderWrapperResponse toResponse(Order order);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "lastStatus", target = "lastStatusName")
    @Mapping(source = ".", target = "customer")
    OrderDetailsResponse toDetailsResponse(Order order);

    StoreResponse toStoreResponse(Store store);

    PaymentResponse toPaymentResponse(Payment payment);

    OrderItemsResponse toOrderItemsResponse(OrderItem item);

    @Mapping(source = "coordinateId", target = "coordinates.cordinateId")
    @Mapping(source = "latitude", target = "coordinates.latitude")
    @Mapping(source = "longitude", target = "coordinates.longitude")
    DeliveryAddressResponse toAddressResponse(DeliveryAddress address);

    default CustomerResponse mapCustomer(Order order){
        if (order == null) return null;
        return new CustomerResponse(order.getCustomerPhone(), order.getCustomerName());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "lastStatus", constant = "RECEIVED")
    @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "store", ignore = true)
    Order toEntity(CreateOrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "discount", constant = "0.0")
    OrderItem toOrderItemEntity(com.cocobambu.delivery.dto.request.CreateOrderItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "prepaid", source = "isPrepaid")
    Payment toPaymentEntity(com.cocobambu.delivery.dto.request.CreatePaymentRequest request);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "coordinateId", ignore = true)
    DeliveryAddress toAddressEntity(com.cocobambu.delivery.dto.request.CreateAddressRequest request);

    @AfterMapping
    default void linkBidirectionalReferences(@MappingTarget Order order) {
        // vincula itens ao pedido
        if (order.getItems() != null) {
            order.getItems().forEach(item -> {
                item.setOrder(order);
                // vincula condimentos ao item (se houver)
                if (item.getCondiments() != null) {
                    item.getCondiments().forEach(c -> c.setOrderItem(item));
                }
            });
        }

        // vincula pagamentos ao pedido
        if (order.getPayments() != null) {
            order.getPayments().forEach(payment -> payment.setOrder(order));
        }

        // vincula endereço ao pedido
        if (order.getDeliveryAddress() != null) {
            order.getDeliveryAddress().setOrder(order);
        }
    }

}

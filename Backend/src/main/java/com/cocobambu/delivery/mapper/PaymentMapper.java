package com.cocobambu.delivery.mapper;

import com.cocobambu.delivery.dto.request.CreatePaymentRequest;
import com.cocobambu.delivery.dto.response.PaymentResponse;
import com.cocobambu.delivery.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "paymentMethod", target = "paymentMethod")
    PaymentResponse toPaymentResponse(Payment payment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "prepaid", source = "isPrepaid")
    Payment toPaymentEntity(CreatePaymentRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(target = "prepaid", source = "prepaid")
    Payment toPaymentEntityFromResponse(PaymentResponse dto); // <-- para o seeder
}

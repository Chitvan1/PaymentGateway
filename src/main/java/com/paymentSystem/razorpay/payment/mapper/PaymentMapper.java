package com.paymentSystem.razorpay.payment.mapper;

import com.paymentSystem.razorpay.payment.dto.response.PaymentResponse;
import com.paymentSystem.razorpay.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
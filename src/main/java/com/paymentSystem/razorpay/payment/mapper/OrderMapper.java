package com.paymentSystem.razorpay.payment.mapper;

import com.paymentSystem.razorpay.payment.dto.response.OrderResponse;
import com.paymentSystem.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderResponse toResponse(OrderRecord orderRecord);
}

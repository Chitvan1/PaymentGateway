package com.paymentSystem.razorpay.payment.gateway.dto;

import com.paymentSystem.razorpay.common.entity.Money;
import com.paymentSystem.razorpay.common.enums.PaymentMethods;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(
        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethods method,
        Map<String, Object> methodDetails
) {
}

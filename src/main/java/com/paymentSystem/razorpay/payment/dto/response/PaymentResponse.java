package com.paymentSystem.razorpay.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paymentSystem.razorpay.common.entity.Money;
import com.paymentSystem.razorpay.common.enums.PaymentStatus;
import com.paymentSystem.razorpay.common.enums.PaymentMethods;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        UUID id,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentStatus status,
        PaymentMethods method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        LocalDateTime capturedAt,
        LocalDateTime createdAt
) {
}

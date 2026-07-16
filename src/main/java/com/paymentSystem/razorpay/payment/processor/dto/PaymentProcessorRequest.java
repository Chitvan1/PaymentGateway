package com.paymentSystem.razorpay.payment.processor.dto;

import com.paymentSystem.razorpay.common.entity.Money;
import com.paymentSystem.razorpay.common.enums.PaymentMethods;

import java.util.Map;

public record PaymentProcessorRequest(
        PaymentMethods methods,
        Money amount,
        Map<String, Object> methodDetails
) {
}

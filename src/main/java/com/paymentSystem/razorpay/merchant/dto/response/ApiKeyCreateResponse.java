package com.paymentSystem.razorpay.merchant.dto.response;

import com.paymentSystem.razorpay.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}

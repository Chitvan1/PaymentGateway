package com.paymentSystem.razorpay.merchant.dto.request;

import com.paymentSystem.razorpay.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}

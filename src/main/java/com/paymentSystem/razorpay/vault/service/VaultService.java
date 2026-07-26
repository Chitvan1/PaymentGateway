package com.paymentSystem.razorpay.vault.service;

import com.paymentSystem.razorpay.common.entity.Money;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.paymentSystem.razorpay.vault.dto.request.TokenizeRequest;
import com.paymentSystem.razorpay.vault.dto.response.TokenizeResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId,String token, Money amount, Map<String, Object> methodDetails);
}

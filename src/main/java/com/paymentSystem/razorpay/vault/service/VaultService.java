package com.paymentSystem.razorpay.vault.service;

import com.paymentSystem.razorpay.vault.dto.request.TokenizeRequest;
import com.paymentSystem.razorpay.vault.dto.response.TokenizeResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);
}

package com.paymentSystem.razorpay.merchant.service;

import com.paymentSystem.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.paymentSystem.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.paymentSystem.razorpay.merchant.dto.response.ApiKeyResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {


    ApiKeyCreateResponse create(UUID merchantId,CreateApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    @Nullable  ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}

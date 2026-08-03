package com.paymentSystem.razorpay.merchant.service.impl;

import com.paymentSystem.razorpay.common.exceptions.ResourceNotFoundException;
import com.paymentSystem.razorpay.common.util.RandomizerUtil;
import com.paymentSystem.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.paymentSystem.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.paymentSystem.razorpay.merchant.dto.response.ApiKeyResponse;
import com.paymentSystem.razorpay.merchant.entity.ApiKey;
import com.paymentSystem.razorpay.merchant.entity.Merchant;
import com.paymentSystem.razorpay.merchant.mapper.ApiKeyMapper;
import com.paymentSystem.razorpay.merchant.repository.ApiKeyRepository;
import com.paymentSystem.razorpay.merchant.repository.MerchantRepository;
import com.paymentSystem.razorpay.merchant.service.ApiKeyService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
          Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(
                  ()-> new ResourceNotFoundException("merchant", merchantId)
          );

          String keyId = "rzp_"+request.environment().name().toLowerCase()+"_"+ RandomizerUtil.randomBase64(24);
          String rawSecret = RandomizerUtil.randomBase64(45);

        ApiKey apiKey = ApiKey.builder()
                        .merchant(merchant)
                        .keyId(keyId)
                        .keySecretHash(bCryptPasswordEncoder.encode(rawSecret))
                        .environment(request.environment())
                        .build();

        apiKey = apiKeyRepository.save(apiKey);
        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyMapper.toResponseList(apiKeyRepository.findByMerchant_Id(merchantId));
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey key = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("Apikey", keyId));

        key.setEnabled(false);
    }

    @Override
    @Transactional
    public @Nullable  ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("Apikey", keyId));

        if(!apiKey.isEnabled()) {
            throw new RuntimeException("Cannot rotate a disabled key");
        }

        String newRawSecret = RandomizerUtil.randomBase64(45);
        apiKey.setPreviousKeySecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(bCryptPasswordEncoder.encode(newRawSecret));
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(),
                newRawSecret, apiKey.getEnvironment());
    }
}

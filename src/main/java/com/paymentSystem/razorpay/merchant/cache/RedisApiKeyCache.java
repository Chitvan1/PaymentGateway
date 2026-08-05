package com.paymentSystem.razorpay.merchant.cache;

import java.util.Optional;

public class RedisApiKeyCache implements ApiKeyCache {

    @Override
    public Optional<ApiKeyCacheEntry> get(String keyId) {
        return Optional.empty();
    }

    @Override
    public void put(String keyId, ApiKeyCacheEntry entry) {

    }

    @Override
    public void evict(String keyId) {

    }
}

package com.paymentSystem.razorpay.merchant.repository;

import com.paymentSystem.razorpay.merchant.dto.response.ApiKeyResponse;
import com.paymentSystem.razorpay.merchant.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {
    List<ApiKey> findByMerchant_Id(UUID merchantId);
}

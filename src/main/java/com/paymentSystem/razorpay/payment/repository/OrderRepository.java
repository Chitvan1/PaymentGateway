package com.paymentSystem.razorpay.payment.repository;

import com.paymentSystem.razorpay.payment.dto.response.OrderResponse;
import com.paymentSystem.razorpay.payment.entity.OrderRecord;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {

    boolean existsByMerchantIdAndReceipt(UUID merchantId, @Size(max = 100) String receipt);

    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);
}

package com.paymentSystem.razorpay.vault.repository;

import com.paymentSystem.razorpay.vault.entity.CardToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardTokenRepository extends JpaRepository<CardToken, UUID> {
    Optional<CardToken> findByTokenAndRevokeAtIsNull(String token);
}

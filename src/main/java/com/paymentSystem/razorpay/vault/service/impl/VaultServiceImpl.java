package com.paymentSystem.razorpay.vault.service.impl;

import com.paymentSystem.razorpay.vault.dto.request.TokenizeRequest;
import com.paymentSystem.razorpay.vault.dto.response.TokenizeResponse;
import com.paymentSystem.razorpay.vault.repository.CardTokenRepository;
import com.paymentSystem.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultService vaultService;

    @Override
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {
        return null;

    }
}

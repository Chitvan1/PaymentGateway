package com.paymentSystem.razorpay.vault.service.impl;

import com.paymentSystem.razorpay.common.enums.CardBrand;
import com.paymentSystem.razorpay.vault.dto.request.TokenizeRequest;
import com.paymentSystem.razorpay.vault.dto.response.TokenizeResponse;
import com.paymentSystem.razorpay.vault.repository.CardTokenRepository;
import com.paymentSystem.razorpay.vault.repository.VaultCardRepository;
import com.paymentSystem.razorpay.vault.service.VaultService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.paymentSystem.razorpay.vault.config.VaultEncryptionConfig.panEncryptor;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;

    @Override
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {
        String lastFour = request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0, 6);
        CardBrand cardBrand = detectBrand(request.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = panEncryptor(dek).encrypt(request.pan().getBytes(StandardCharsets.UTF_8));
        return null;

    }

    private CardBrand detectBrand(String pan) {
        if (pan.startsWith("4")) return CardBrand.VISA;
        if (pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
        if (pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
        if (pan.startsWith("65") || pan.startsWith("6011")) return CardBrand.DISCO;
        return CardBrand.RUPAY;
    }
}

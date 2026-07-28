package com.paymentSystem.razorpay.vault.service.impl;

import com.paymentSystem.razorpay.common.entity.Money;
import com.paymentSystem.razorpay.common.enums.CardBrand;
import com.paymentSystem.razorpay.common.exceptions.ResourceNotFoundException;
import com.paymentSystem.razorpay.common.util.RandomizerUtil;
import com.paymentSystem.razorpay.payment.processor.PaymentProcessorRouter;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.paymentSystem.razorpay.vault.config.VaultEncryptionConfig;
import com.paymentSystem.razorpay.vault.dto.request.TokenizeRequest;
import com.paymentSystem.razorpay.vault.dto.response.TokenizeResponse;
import com.paymentSystem.razorpay.vault.entity.CardToken;
import com.paymentSystem.razorpay.vault.entity.VaultCard;
import com.paymentSystem.razorpay.vault.repository.CardTokenRepository;
import com.paymentSystem.razorpay.vault.repository.VaultCardRepository;
import com.paymentSystem.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static com.paymentSystem.razorpay.vault.config.VaultEncryptionConfig.panEncryptor;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;
    private final BytesEncryptor dekEncryptor;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {
        String lastFour = request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0, 6);
        CardBrand cardBrand = detectBrand(request.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = panEncryptor(dek).encrypt(request.pan().getBytes(StandardCharsets.UTF_8));
        byte[] encryptedDek = dekEncryptor.encrypt(dek);
        VaultCard vaultCard = VaultCard.builder()
                .brand(cardBrand)
                .expiryYear(request.expiryYear().toString())
                .expiryMonth(request.expiryMonth().toString())
                .bin(bin)
                .lastFour(lastFour)
                .encryptedDek(encryptedDek)
                .encryptedPan(encryptedPan)
                .cardHolderName(request.cardHolderName())
                .build();

        vaultCard = vaultCardRepository.save(vaultCard);

        String token = "tok_" + RandomizerUtil.randomBase64(32);
        cardTokenRepository.save(CardToken.builder()
                        .vaultCard(vaultCard)
                        .token(token)
                        .customer(request.customerId())
                        .merchant(merchantId)
                        .build());

        return new TokenizeResponse(token, lastFour, cardBrand, request.expiryMonth(), request.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails) {
        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token)
                        .orElseThrow(()-> new ResourceNotFoundException("CardToken", token));

        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] panBytes = null;

        try {

            byte[] dek = dekEncryptor.decrypt(vaultCard.getEncryptedDek());
            panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());

            String pan = new String(panBytes, StandardCharsets.UTF_8);
            String expiry = vaultCard.getExpiryMonth() + "/" + vaultCard.getExpiryYear();

            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.card(paymentId, pan, expiry, amount, methodDetails);
            PaymentProcessorResponse response = paymentProcessorRouter.charge(paymentProcessorRequest);

            log.info("Vault charge registered, token={}********", token.substring(0, 4));

            return response;
        } catch (Exception e) {
            log.warn("Vault charge failed, token={}****", token.substring(0, 4));
            return new PaymentProcessorResponse.Failure("VAULT_CHARGE_FAILED", e.getMessage());
        }finally {
            if(panBytes != null) Arrays.fill(panBytes, (byte) 0);
        }
    }

    private CardBrand detectBrand(String pan) {
        if (pan.startsWith("4")) return CardBrand.VISA;
        if (pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
        if (pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
        if (pan.startsWith("65") || pan.startsWith("6011")) return CardBrand.DISCO;
        return CardBrand.RUPAY;
    }
}

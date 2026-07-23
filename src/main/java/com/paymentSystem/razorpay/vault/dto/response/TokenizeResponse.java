package com.paymentSystem.razorpay.vault.dto.response;

import com.paymentSystem.razorpay.common.enums.CardBrand;

public record TokenizeResponse(
        String token,
        String lastFour,
        CardBrand cardBrand,
        Integer expiryMonth,
        Integer expiryYear

) {
}

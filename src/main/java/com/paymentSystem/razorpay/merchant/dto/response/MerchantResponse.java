package com.paymentSystem.razorpay.merchant.dto.response;

import com.paymentSystem.razorpay.common.enums.BusinessType;
import com.paymentSystem.razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus

) {

}

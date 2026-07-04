package com.paymentSystem.razorpay.merchant.service;

import com.paymentSystem.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.paymentSystem.razorpay.merchant.dto.response.MerchantResponse;


public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);
}

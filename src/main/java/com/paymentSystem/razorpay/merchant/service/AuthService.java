package com.paymentSystem.razorpay.merchant.service;

import com.paymentSystem.razorpay.merchant.dto.request.LoginRequest;
import com.paymentSystem.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.paymentSystem.razorpay.merchant.dto.response.LoginResponse;
import com.paymentSystem.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;


public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);

    LoginResponse login(LoginRequest request);
}

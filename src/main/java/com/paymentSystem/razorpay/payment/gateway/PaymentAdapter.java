package com.paymentSystem.razorpay.payment.gateway;

import com.paymentSystem.razorpay.payment.gateway.dto.PaymentRequest;

public interface PaymentAdapter {
    void initiate(PaymentRequest request);
}

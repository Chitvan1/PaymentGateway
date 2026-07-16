package com.paymentSystem.razorpay.payment.gateway.adapter;

import com.paymentSystem.razorpay.payment.gateway.PaymentAdapter;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentRequest;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentResult;

public class NetBankingAdapter implements PaymentAdapter {
    @Override
    public PaymentResult initiate(PaymentRequest request){
        return null;
    }
}

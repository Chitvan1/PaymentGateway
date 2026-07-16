package com.paymentSystem.razorpay.payment.processor;

import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {
    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}

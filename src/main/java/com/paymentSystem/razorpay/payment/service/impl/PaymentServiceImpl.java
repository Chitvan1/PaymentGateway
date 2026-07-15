package com.paymentSystem.razorpay.payment.service.impl;

import com.paymentSystem.razorpay.payment.dto.request.PaymentInitRequest;
import com.paymentSystem.razorpay.payment.dto.response.PaymentResponse;
import com.paymentSystem.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest requestDto) {
        return null;
    }
}

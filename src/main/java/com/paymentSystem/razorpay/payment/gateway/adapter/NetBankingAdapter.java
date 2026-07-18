package com.paymentSystem.razorpay.payment.gateway.adapter;

import com.paymentSystem.razorpay.payment.gateway.PaymentAdapter;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentRequest;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentResult;
import com.paymentSystem.razorpay.payment.processor.PaymentProcessorRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NetBankingAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter  paymentProcessorRouter;

    @Override
    public PaymentResult initiate(PaymentRequest request){
        log.info("Initiate Payment with NetBankingAdapter, paymentId: {} " +request.paymentId());

        return null;
    }
}

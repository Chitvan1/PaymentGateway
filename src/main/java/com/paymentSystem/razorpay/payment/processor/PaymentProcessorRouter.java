package com.paymentSystem.razorpay.payment.processor;

import com.paymentSystem.razorpay.common.enums.PaymentMethods;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private Map<PaymentMethods, PaymentProcessor> paymentProcessorMap;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request){
            PaymentProcessor paymentProcessor = paymentProcessorMap.get(request.methods());
            if(paymentProcessor == null){
                throw new IllegalArgumentException("No Payment processor registered for method: "+request.methods());
            }
            return paymentProcessor.charge(request);
    }
}
package com.paymentSystem.razorpay.payment.gateway;

import com.paymentSystem.razorpay.common.config.PaymentAdapterConfig;
import com.paymentSystem.razorpay.common.enums.PaymentMethods;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

   private final Map<PaymentMethods, PaymentAdapter> paymentAdapterMap;

    public void initiate(PaymentRequest request) {

        PaymentAdapter adapter = paymentAdapterMap.get(request.method());
        if(adapter == null) {
            throw new IllegalArgumentException("No PaymentAdapter registered for method " + request.method());
        }
        adapter.initiate(request);
    }
}
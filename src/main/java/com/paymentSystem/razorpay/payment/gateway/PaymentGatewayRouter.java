package com.paymentSystem.razorpay.payment.gateway;

import com.paymentSystem.razorpay.common.enums.PaymentMethods;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentRequest;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

   private final Map<PaymentMethods, PaymentAdapter> paymentAdapterMap;

    public PaymentResult initiate(PaymentRequest request) {

        PaymentAdapter adapter = paymentAdapterMap.get(request.method());
        if(adapter == null) {
            throw new IllegalArgumentException("No PaymentAdapter registered for method " + request.method());
        }
        return adapter.initiate(request);
    }

    public PaymentResult capture(PaymentMethods paymentMethod, UUID paymentId) {
        PaymentAdapter adapter = paymentAdapterMap.get(paymentMethod);
        if(adapter == null) {
            throw new IllegalArgumentException("No PaymentAdapter registered for method ");
        }
        return adapter.capture(paymentId);
    }
}
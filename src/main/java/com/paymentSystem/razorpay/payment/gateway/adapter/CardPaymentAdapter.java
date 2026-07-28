package com.paymentSystem.razorpay.payment.gateway.adapter;

import com.paymentSystem.razorpay.payment.gateway.PaymentAdapter;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentRequest;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentResult;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.paymentSystem.razorpay.vault.service.VaultService;
import com.paymentSystem.razorpay.vault.service.impl.VaultServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {

    public final VaultService vaultService;

    @Override
    public PaymentResult initiate(PaymentRequest request){
        String token = (String) request.methodDetails().get("token");
        PaymentProcessorResponse response = vaultService.charge(
            request.paymentId(), token, request.amount(), request.methodDetails()
        );

        return switch (response){
            case PaymentProcessorResponse.Success  success ->  new PaymentResult.Success(success.bankReference());
            case PaymentProcessorResponse.Failure  failure ->  new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorReference());
        };
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("CARD_REF");
    }

}

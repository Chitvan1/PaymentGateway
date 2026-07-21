package com.paymentSystem.razorpay.payment.processor.strategy;

import com.paymentSystem.razorpay.common.util.RandomizerUtil;
import com.paymentSystem.razorpay.payment.processor.PaymentProcessor;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class UpiPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null ? request.methodDetails().get("BANK").toString(): null;

        //Simulation
        if(BANK_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("BANK_REJECTED",
                    "Bank Rejected the transaction registration");
        }

        String processorRef = "NBK_PROCESSOR_" + RandomizerUtil.randomBase64(16);
        String redirectRef = "http://REDIRECT_BANK.com/" + processorRef;
        return new PaymentProcessorResponse.Success(processorRef, redirectRef);
    }
}

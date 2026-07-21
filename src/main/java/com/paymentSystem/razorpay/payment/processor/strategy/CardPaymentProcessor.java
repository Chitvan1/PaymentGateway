package com.paymentSystem.razorpay.payment.processor.strategy;

import com.paymentSystem.razorpay.common.util.RandomizerUtil;
import com.paymentSystem.razorpay.payment.processor.PaymentProcessor;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.paymentSystem.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        final String VPA_CODE_FAIL = "fail@okaxis";

        String bankCode = request.methodDetails() != null ? request.methodDetails().get("vpa").toString(): null;

        //Simulation
        if(VPA_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("UPI_REJECTED",
                    "Bank Rejected the transaction registration");
        }

        String processorRef = "UPI_PROCESSOR_" + RandomizerUtil.randomBase64(16);
        String bankRef = "BANK_REF" + RandomizerUtil.randomBase64(16);
        return new PaymentProcessorResponse.Success(processorRef, bankRef);
    }
}

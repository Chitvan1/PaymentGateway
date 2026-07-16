package com.paymentSystem.razorpay.payment.processor.dto;

public sealed interface PaymentProcessorResponse permits
        PaymentProcessorResponse.Pending,
        PaymentProcessorResponse.Failure,
        PaymentProcessorResponse.Success{
    record Pending(String processorReference) implements PaymentProcessorResponse {}
    record Success(String processorReference, String bankReference)implements PaymentProcessorResponse {}
    record Failure(String errorCode, String errorDescription)implements PaymentProcessorResponse {}
}

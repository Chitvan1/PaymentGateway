package com.paymentSystem.razorpay.payment.config;

import com.paymentSystem.razorpay.common.enums.PaymentMethods;
import com.paymentSystem.razorpay.payment.processor.PaymentProcessor;
import com.paymentSystem.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.paymentSystem.razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.paymentSystem.razorpay.payment.processor.strategy.UpiPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;
    private final UpiPaymentProcessor upiPaymentProcessor;

    @Bean
    public Map<PaymentMethods, PaymentProcessor> paymentProcessorMap(){
        return Map.of(
                PaymentMethods.CARD, cardPaymentProcessor,
                PaymentMethods.NETBANKING, netBankingPaymentProcessor,
                PaymentMethods.UPI, upiPaymentProcessor
        );
    }
}

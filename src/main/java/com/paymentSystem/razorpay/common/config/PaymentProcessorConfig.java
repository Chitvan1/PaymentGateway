package com.paymentSystem.razorpay.common.config;

import com.paymentSystem.razorpay.common.enums.PaymentMethods;
import com.paymentSystem.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.paymentSystem.razorpay.payment.gateway.adapter.NetBankingAdapter;
import com.paymentSystem.razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import com.paymentSystem.razorpay.payment.processor.PaymentProcessor;
import com.paymentSystem.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.paymentSystem.razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.paymentSystem.razorpay.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {
    @Bean
    public Map<PaymentMethods, PaymentProcessor> paymentProcessorMap(){
        return Map.of(
                PaymentMethods.CARD, new CardPaymentProcessor(),
                PaymentMethods.NETBANKING, new NetBankingPaymentProcessor(),
                PaymentMethods.UPI, new UpiPaymentProcessor()
        );
    }
}

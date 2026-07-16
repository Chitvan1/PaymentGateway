package com.paymentSystem.razorpay.common.config;

import com.paymentSystem.razorpay.common.enums.PaymentMethods;
import com.paymentSystem.razorpay.payment.gateway.PaymentAdapter;
import com.paymentSystem.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.paymentSystem.razorpay.payment.gateway.adapter.NetBankingAdapter;
import com.paymentSystem.razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentAdapterConfig{

    @Bean
    public Map<PaymentMethods, PaymentAdapter> paymentAdapterMap(){
        return Map.of(
                PaymentMethods.CARD, new CardPaymentAdapter(),
                PaymentMethods.NETBANKING, new NetBankingAdapter(),
                PaymentMethods.UPI, new UpiPaymentAdapter()
        );
    }
}

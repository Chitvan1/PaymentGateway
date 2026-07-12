package com.paymentSystem.razorpay.payment.dto.request;

import com.paymentSystem.razorpay.common.entity.Money;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
public record CreateOrderRequest(

        @NotNull(message = "Amount is required")
        Money amount,

        @Size(max = 100)
        String receipt, //order-id (known to merchant)

        Map<String, Object> notes,
        LocalDateTime expiresAt
) {
}

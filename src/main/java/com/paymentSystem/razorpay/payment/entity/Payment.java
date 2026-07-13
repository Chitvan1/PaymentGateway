package com.paymentSystem.razorpay.payment.entity;

import com.paymentSystem.razorpay.common.entity.Money;
import com.paymentSystem.razorpay.common.enums.OrderStatus;
import com.paymentSystem.razorpay.common.enums.PaymentMethods;
import com.paymentSystem.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name="payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="order_id", nullable=false)
    private OrderRecord order;

    @Column(name="merchant_id", nullable=false)
    private UUID merchantId;

    @Embedded
    private Money money;

    @Column(nullable = false, length = 100)
    private String idempotencyKey;

   @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false, length = 20)
    private PaymentMethods paymentMethod;

    @JdbcTypeCode((SqlTypes.JSON))
    @Column(columnDefinition = "jsonb", name = "method_details")
    private Map<String, Object> methodDetails;

    @Column(length= 100)
    private String bankReferenceNumber;

    @Column(length= 100)
    private String errorCode;

    @Column(length= 250)
    private String errorDescription;

    private LocalDateTime authorizedAt;

    private LocalDateTime capturedAt;

    private LocalDateTime failedAt;

    private LocalDateTime refundedAt;

    private LocalDateTime settledAt;
}

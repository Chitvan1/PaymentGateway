package com.paymentSystem.razorpay.payment.entity;

import com.paymentSystem.razorpay.common.enums.OrderStatus;
import com.paymentSystem.razorpay.common.enums.PaymentActor;
import com.paymentSystem.razorpay.common.enums.PaymentEvent;
import com.paymentSystem.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="payment_transition_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentTransitionLog {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="payment_id", nullable=false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentEvent event;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus toStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentActor actor;

    @Column(name="occurred_at", nullable = false)
    private LocalDateTime occurredAt;
}

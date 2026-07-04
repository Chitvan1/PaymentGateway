package com.paymentSystem.razorpay.operations.entity;

import com.paymentSystem.razorpay.payment.entity.Payment;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name="settlement_payment")
public class SettlementPayment{

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId("settlementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="settlement_id", nullable = false)
    private Settlement settlementId;
}

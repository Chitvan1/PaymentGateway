package com.paymentSystem.razorpay.payment.service.impl;

import com.paymentSystem.razorpay.common.enums.OrderStatus;
import com.paymentSystem.razorpay.common.enums.PaymentStatus;
import com.paymentSystem.razorpay.common.exceptions.BusinessRuleViolationException;
import com.paymentSystem.razorpay.common.exceptions.ResourceNotFoundException;
import com.paymentSystem.razorpay.payment.dto.request.PaymentInitRequest;
import com.paymentSystem.razorpay.payment.dto.response.PaymentResponse;
import com.paymentSystem.razorpay.payment.entity.OrderRecord;
import com.paymentSystem.razorpay.payment.entity.Payment;
import com.paymentSystem.razorpay.payment.gateway.PaymentGatewayRouter;
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentRequest;
import com.paymentSystem.razorpay.payment.repository.OrderRepository;
import com.paymentSystem.razorpay.payment.repository.PaymentRepository;
import com.paymentSystem.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(request.orderId(), merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if (order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.ATTEMPTED) {
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE", "Order cannot accept payment in status: " + order.getOrderStatus());
        }
        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts() + 1);

        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .paymentMethod(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                request.orderId(),
                merchantId,
                order.getAmount(),
                request.method(),
                request.methodDetails());

        paymentGatewayRouter.initiate(paymentRequest);


        return null;
    }
}

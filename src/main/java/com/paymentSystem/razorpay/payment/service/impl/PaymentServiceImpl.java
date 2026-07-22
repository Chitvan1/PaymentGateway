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
import com.paymentSystem.razorpay.payment.gateway.dto.PaymentResult;
import com.paymentSystem.razorpay.payment.mapper.PaymentMapper;
import com.paymentSystem.razorpay.payment.repository.OrderRepository;
import com.paymentSystem.razorpay.payment.repository.PaymentRepository;
import com.paymentSystem.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;

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

        PaymentResult result = paymentGatewayRouter.initiate(paymentRequest);

        switch (result) {
            case PaymentResult.Pending(
                    String registrationRef
            ) -> payment.setProcessorReference(registrationRef);
            case PaymentResult.Failure(String errorCode, String errorDescription) -> {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setErrorCode(errorCode);
                payment.setErrorDescription(errorDescription);
            }
            case PaymentResult.Success success -> {}
            case null, default -> {
            }
        }
        payment = paymentRepository.save(payment);
        orderRepository.save(order);
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId.toString()));

        payment.setStatus(PaymentStatus.CAPTURING); //TODO: Proper state machine
        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getPaymentMethod(),paymentId);

        if(paymentResult instanceof PaymentResult.Success success){
                payment.setStatus(PaymentStatus.CAPTURED);
                payment.setCapturedAt(LocalDateTime.now());
                log.info("Payment captured, paymentID: {} "+paymentId.toString());
        }else if(paymentResult instanceof PaymentResult.Failure(String errorCode, String errorDescription)){
                payment.setStatus(PaymentStatus.AUTHORIZED);
                payment.setErrorCode(errorCode);
                payment.setErrorDescription(errorDescription);
                log.warn("Payment captured failed, paymentID: {} "+paymentId.toString());
        }
        payment = paymentRepository.save(payment);
        return paymentMapper.toResponse(payment);
    }
}

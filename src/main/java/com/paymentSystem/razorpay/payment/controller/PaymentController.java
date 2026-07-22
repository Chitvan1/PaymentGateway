package com.paymentSystem.razorpay.payment.controller;

import com.paymentSystem.razorpay.payment.dto.request.PaymentInitRequest;
import com.paymentSystem.razorpay.payment.dto.response.PaymentResponse;
import com.paymentSystem.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    UUID merchantId = UUID.fromString("2fb39aed-ef70-466f-8c89-e519073a0e03");  //TODO:  Replace it with merchant context

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInitRequest request) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    paymentService.initiate(merchantId, request)
            );
    }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId) {
            return ResponseEntity.ok(paymentService.capture(merchantId, paymentId));
    }

}

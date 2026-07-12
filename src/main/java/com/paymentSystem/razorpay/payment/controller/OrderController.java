package com.paymentSystem.razorpay.payment.controller;

import com.paymentSystem.razorpay.payment.dto.request.CreateOrderRequest;
import com.paymentSystem.razorpay.payment.dto.response.OrderResponse;
import com.paymentSystem.razorpay.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    UUID merchantId = UUID.fromString("2fb39aed-ef70-466f-8c89-e519073a0e03");//TODO: Replace it with merchant context

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid  CreateOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(merchantId,request));
    }

}

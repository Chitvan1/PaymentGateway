package com.paymentSystem.razorpay.vault.controller;

import com.paymentSystem.razorpay.vault.dto.request.TokenizeRequest;
import com.paymentSystem.razorpay.vault.dto.response.TokenizeResponse;
import com.paymentSystem.razorpay.vault.service.VaultService;
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
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {

    private final VaultService vaultService;
    UUID merchantId = UUID.fromString("2fb39aed-ef70-466f-8c89-e519073a0e03");  //TODO:  Replace it with merchant context

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vaultService.tokenize(request, merchantId));
    }
}

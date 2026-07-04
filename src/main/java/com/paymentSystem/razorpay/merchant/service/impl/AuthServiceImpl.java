package com.paymentSystem.razorpay.merchant.service.impl;

import com.paymentSystem.razorpay.common.enums.MerchantStatus;
import com.paymentSystem.razorpay.common.enums.UserRole;
import com.paymentSystem.razorpay.common.exceptions.DuplicateResourceException;
import com.paymentSystem.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.paymentSystem.razorpay.merchant.dto.response.MerchantResponse;
import com.paymentSystem.razorpay.merchant.entity.AppUser;
import com.paymentSystem.razorpay.merchant.entity.Merchant;
import com.paymentSystem.razorpay.merchant.repository.AppUserRepository;
import com.paymentSystem.razorpay.merchant.repository.MerchantRepository;
import com.paymentSystem.razorpay.merchant.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL", "Merchant with email already exists: "+request.email());
        }
        Merchant merchant = Merchant.builder()
                .name(request.name())
                .email(request.email())
                .businessName(request.businessName())
                .businessType(request.businessType())
                .status(MerchantStatus.PENDING_KYC)
                .build();

        merchant =  merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password()) //TODO: encrypt using Bcrypt
                .role(UserRole.OWNER)
                .build();

        appUserRepository.save(appUser);
        return new MerchantResponse(merchant.getId(), merchant.getEmail(), merchant.getName(), merchant.getBusinessName(), merchant.getBusinessType(), merchant.getStatus());
    }
}

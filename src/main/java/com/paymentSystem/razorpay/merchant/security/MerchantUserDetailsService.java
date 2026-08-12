package com.paymentSystem.razorpay.merchant.security;

import com.paymentSystem.razorpay.common.exceptions.ResourceNotFoundException;
import com.paymentSystem.razorpay.merchant.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return appUserRepository.findByEmail(email)
	              .orElseThrow(()-> new ResourceNotFoundException("User",email));
    }
}

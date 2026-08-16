package com.paymentSystem.razorpay.common.idempotency;

import com.paymentSystem.razorpay.merchant.security.MerchantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {

    private static final Set<String> GUARDED_METHODS = Set.of("POST", "PUT", "PATCH");
    private final MerchantContext merchantContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (!GUARDED_METHODS.contains(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String rawKey = request.getHeader("X-Idempotency-Key");
        if (rawKey == null || rawKey.isBlank()) { // No idem-key found, continue
            chain.doFilter(request, response);
            return;
        }

        UUID merchantId = merchantContext.getMerchantId();
        String key = merchantId != null ? merchantId+":"+rawKey : rawKey;

    }
}

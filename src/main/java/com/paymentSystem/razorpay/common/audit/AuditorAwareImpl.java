package com.paymentSystem.razorpay.common.audit;

import com.paymentSystem.razorpay.merchant.security.MerchantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MerchantContext merchantContext;

    @Override
    public Optional<String> getCurrentAuditor() {

        try{
            String keyId = merchantContext.getKeyId();
            if(keyId != null && !keyId.isBlank()){
                return Optional.of(keyId);
            }

            if(merchantContext.getMerchantId() != null){
                return Optional.of("merchant_id: "+merchantContext.getKeyId());
            }
        }catch(Exception e){

        }
        return Optional.of("SYSTEM");
    }
}

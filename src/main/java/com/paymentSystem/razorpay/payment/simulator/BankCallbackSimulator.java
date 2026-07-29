package com.paymentSystem.razorpay.payment.simulator;

import com.paymentSystem.razorpay.common.enums.ChaosMode;
import com.paymentSystem.razorpay.common.enums.PaymentMethods;
import com.paymentSystem.razorpay.common.enums.PaymentStatus;
import com.paymentSystem.razorpay.common.util.RandomizerUtil;
import com.paymentSystem.razorpay.payment.entity.Payment;
import com.paymentSystem.razorpay.payment.repository.PaymentRepository;
import com.paymentSystem.razorpay.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankCallbackSimulator {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final SimulatorConfiguration simulatorConfiguration;

    @Scheduled(fixedDelayString = "${payment.simulator.poll-interval-ms: 5000}")
    public void processCallback(){
        LocalDateTime globalWindow = LocalDateTime.now().minusSeconds(1);
        List<Payment> candidates = paymentRepository
                .findByStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING, globalWindow);

        log.info("Simulating payments for {} payments", candidates.size());

        if(candidates.isEmpty()) return;

        for(Payment payment : candidates){
            simulateCallback(payment);
        }
    }

    private void simulateCallback(Payment payment) {
            SimulatorConfiguration.MethodSimulatorConfig methodConfig = simulatorConfiguration.configFor(payment.getPaymentMethod());

            LocalDateTime dueAt = dueAt(payment, methodConfig);

            if(LocalDateTime.now().isBefore(dueAt)){
                return;
            }

            ChaosMode chaosMode = simulatorConfiguration.getChaosMode();

            switch (chaosMode){
                case SUCCESS ->  resolve(payment, true);
                case FAILURE ->  resolve(payment, false);
                case NORMAL, SLOW -> resolve(payment, shouldApprove(payment, methodConfig));
                case TIMEOUT -> {
                    log.debug("BankCallback simulator: Payment Timed out");
                }
            }
    }

    private void resolve(Payment payment, boolean approve) {
        if (approve) {
            String bankRef = "SIM_BANK_REF"+ RandomizerUtil.randomBase64(8);
            paymentService.resolveAuthorization(payment.getId(), true, bankRef, null, null);
        } else {
            paymentService.resolveAuthorization(payment.getId(), false, null, "SIM_BANK_ERROR_CODE", "Simulated Bank Decline");
        }
    }

    private boolean shouldApprove(Payment payment, SimulatorConfiguration.MethodSimulatorConfig methodConfig){
        int bucket = Math.abs(payment.getId().hashCode()) % 100;

        return bucket < methodConfig.getSuccessRate();
    }
    private  LocalDateTime dueAt(Payment payment, SimulatorConfiguration.MethodSimulatorConfig methodConfig){
        int range = methodConfig.getMaxDelaySeconds() -  methodConfig.getMinDelaySeconds();
        int delaySeconds =  methodConfig.getMinDelaySeconds() + Math.abs(payment.getId().hashCode()) % (range + 1);

        if(simulatorConfiguration.getChaosMode() == ChaosMode.SLOW){
            delaySeconds *= 2;
        }
        return  payment.getCreatedAt().plusSeconds(delaySeconds);
    }

}

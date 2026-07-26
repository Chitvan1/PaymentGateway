package com.paymentSystem.razorpay.payment.simulator;

import com.paymentSystem.razorpay.common.enums.ChaosMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "payment.simulator")
public class SimulatorConfiguration {

    private Integer pollIntervalMs = 2000;
    private ChaosMode chaosMode = ChaosMode.NORMAL;
    private Map<String, MethodSimulatorConfig> methods = new HashMap<>();

//    public SimulatorConfig.MethodSimulatorConfig configFor(PaymentMethod method) {
//        return methods.getOrDefault(method.name(), new MethodSimulatorConfig());
//    }

    @Getter
    @Setter
    public static  class MethodSimulatorConfig{
        private Integer minDelaySeconds = 1;
        private Integer maxDelaySeconds = 5;
        private Integer successRate = 80;
    }

}

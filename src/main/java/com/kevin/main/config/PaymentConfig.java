package com.kevin.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class PaymentConfig {
    @Value("${stripe.secret}")
    private String stripeSecretKey;

    @PostConstruct
    public void setKey() {
        Stripe.apiKey = stripeSecretKey;
        log.info("Key initialising...");
    }
}

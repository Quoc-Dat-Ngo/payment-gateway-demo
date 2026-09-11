package com.kevin.main;

public record PaymentCreationRequest(
        Integer amount,
        String currency) {

}

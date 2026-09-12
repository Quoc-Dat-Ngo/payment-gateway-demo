package com.kevin.main;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/hosted-checkout")
public class HostedCheckoutController {

    @PostMapping
    public ResponseEntity<Void> checkout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/success.html")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(2000L)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("T-shirt")
                                                                .build())
                                                .build())
                                .build())
                // Provide a name (for example, hosted_web_0001) to label this Checkout
                // integration and measure its conversion independently
                .setIntegrationIdentifier("hosted_web_0001")
                .build();

        Session session = Session.create(params);
        System.out.println("Session URL " + session.getUrl());

        URI location = URI.create(session.getUrl());

        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(location).build();

    }
}

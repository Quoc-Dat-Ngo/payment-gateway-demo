package com.kevin.main;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    @PostMapping
    public PaymentCreationResponse createNewPayment(@RequestBody PaymentCreationRequest request) throws Exception {
        Long amount = (long) request.amount();
        String currency = request.currency();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return new PaymentCreationResponse(paymentIntent.getClientSecret());
    }

}

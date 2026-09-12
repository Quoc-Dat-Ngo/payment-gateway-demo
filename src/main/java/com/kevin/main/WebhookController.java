package com.kevin.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/webhooks/stripe")
public class WebhookController {

    @Value("${stripe.webhook.signing}")
    private String signingKey;

    @PostMapping
    public String handleWebhookEvent(
            HttpServletRequest request,
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String header) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, header, signingKey);
        } catch (SignatureVerificationException e) {
            return "Signature verification failed";
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                var intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (intent != null) {
                    System.out
                            .println("PaymentIntent succeeded: " + intent.getId() + ", amount = " + intent.getAmount());
                    // Update DB here...
                }

                break;

            case "payment_intent.payment_failed":
                var failedIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (failedIntent != null) {
                    System.out.println("PaymentIntent failed: " + failedIntent.getId());
                    // Update DB here...
                }
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return "ok";
    }
}

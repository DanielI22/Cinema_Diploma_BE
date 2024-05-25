package sit.tu_varna.bg.core.common;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;

@ApplicationScoped
public class StripePaymentService {
    @ConfigProperty(name = "stripe.api.key")
    String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createPaymentIntent(BigDecimal amount, String paymentMethodId) {
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(amount.longValue())
                .setCurrency("bgn")
                .setPaymentMethod(paymentMethodId)
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(createParams);
            return paymentIntent.getClientSecret();
        } catch (StripeException e) {
            e.printStackTrace();
            throw new RuntimeException("PaymentIntent creation failed", e);
        }
    }
}

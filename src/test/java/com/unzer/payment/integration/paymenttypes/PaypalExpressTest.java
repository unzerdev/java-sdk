package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.BaseTransaction.Status;
import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.BasketV2TestData;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.PaypalData;
import com.unzer.payment.paymenttypes.Paypal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaypalExpressTest extends AbstractPaymentTest {
    @Test
    public void testAuthorize() {
        Unzer unzer = getUnzer();

        Paypal type = unzer.createPaymentType(new Paypal());
        Basket basket = unzer.createBasket(BasketV2TestData.getMaxTestBasketV2());

        // Create authorization
        Authorization authorization = (Authorization) new Authorization()
                .setAmount(BigDecimal.valueOf(500.5))
                .setTypeId(type.getId())
                .setBasketId(basket.getId())
                .setCurrency(Currency.getInstance("EUR"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setPaypal(new PaypalData().setCheckoutType(PaypalData.CheckoutType.EXPRESS))
                );

        Authorization createdAuthorization = unzer.authorize(authorization);
        assertEquals(Status.PENDING, createdAuthorization.getStatus());
    }


    @Test
    public void testCharge() {
        Unzer unzer = getUnzer();

        Paypal type = unzer.createPaymentType(new Paypal());
        Basket basket = unzer.createBasket(BasketV2TestData.getMaxTestBasketV2());

        // Create charge
        Charge charge = (Charge) new Charge()
                .setAmount(BigDecimal.valueOf(500.5))
                .setTypeId(type.getId())
                .setBasketId(basket.getId())
                .setCurrency(Currency.getInstance("EUR"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setPaypal(new PaypalData().setCheckoutType(PaypalData.CheckoutType.EXPRESS))
                );

        Charge createdCharge = unzer.charge(charge);
        assertEquals(Status.PENDING, createdCharge.getStatus());
    }
}

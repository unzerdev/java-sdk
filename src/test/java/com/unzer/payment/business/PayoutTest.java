package com.unzer.payment.business;


import static com.unzer.payment.business.BasketV1TestData.getMaxTestBasketV1;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.PaymentException;
import com.unzer.payment.Payout;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Card;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PayoutTest extends AbstractPaymentTest {

    @Disabled("Further Configuration needed")
    @Test
    public void testPayoutCardMinimal() throws MalformedURLException, HttpCommunicationException {
        Card card = createPaymentTypeCard(getUnzer(), "4711100000000000");
        Payout payout = getUnzer().payout(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), new URL("https://www.unzer.com"));
        assertNotNull(payout);

        Payout payoutFetched = getUnzer().fetchPayout(payout.getPaymentId(), payout.getId());
        assertPayoutEqual(payout, payoutFetched);
    }

    @Disabled("Further Configuration needed")
    @Test
    public void testPayoutCardWithAllData() throws MalformedURLException, HttpCommunicationException, ParseException {
        Card card = createPaymentTypeCard(getUnzer(), "4711100000000000");
        Payout payout = getUnzer().payout(getTestPayout(card.getId()));
        assertNotNull(payout);
        assertNotNull(payout.getId());

        Payout payoutFetched = getUnzer().fetchPayout(payout.getPaymentId(), payout.getId());
        assertPayoutEqual(payout, payoutFetched);

    }

    private void assertPayoutEqual(Payout payout, Payout payoutFetched) {
        assertEquals(payout.getAmount(), payoutFetched.getAmount());
        assertEquals(payout.getBasketId(), payoutFetched.getBasketId());
        assertEquals(payout.getCurrency(), payoutFetched.getCurrency());
        assertEquals(payout.getCustomerId(), payoutFetched.getCustomerId());
        assertEquals(payout.getDate(), payoutFetched.getDate());
        assertEquals(payout.getId(), payoutFetched.getId());
        assertEquals(payout.getMetadataId(), payoutFetched.getMetadataId());
        assertEquals(payout.getOrderId(), payoutFetched.getOrderId());
        assertEquals(payout.getPaymentId(), payoutFetched.getPaymentId());
        assertEquals(payout.getPaymentReference(), payoutFetched.getPaymentReference());
        assertEquals(payout.getRedirectUrl(), payoutFetched.getRedirectUrl());
        assertEquals(payout.getReturnUrl(), payoutFetched.getReturnUrl());
        assertEquals(payout.getStatus(), payoutFetched.getStatus());
        assertEquals(payout.getTypeId(), payoutFetched.getTypeId());
        assertEquals(payout.getPaymentReference(), payoutFetched.getPaymentReference());
    }

    private Payout getTestPayout(String typeId) throws PaymentException, HttpCommunicationException, ParseException, MalformedURLException {
        Unzer unzer = getUnzer();
        Payout payout = new Payout();
        payout.setAmount(new BigDecimal(856.4900));
        payout.setCurrency(Currency.getInstance("EUR"));
        payout.setOrderId(generateUuid());
        payout.setPaymentReference("My Payment Reference");
        payout.setReturnUrl(new URL("https://www.unzer.com"));
        payout.setTypeId(typeId);
        payout.setBasketId(unzer.createBasket(getMaxTestBasketV1()).getId());
        payout.setCustomerId(unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid())).getId());
        payout.setMetadataId(unzer.createMetadata(getTestMetadata()).getId());
        return payout;
    }
}

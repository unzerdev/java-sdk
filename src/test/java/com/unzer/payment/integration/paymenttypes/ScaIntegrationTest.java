package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Payment;
import com.unzer.payment.PaymentException;
import com.unzer.payment.Sca;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.paymenttypes.Card;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScaIntegrationTest extends AbstractPaymentTest {

    private static final String TEST_CARD_EXPIRY = "03/30";
    private static final String TEST_CARD_CVC = "123";
    private static final BigDecimal SCA_AMOUNT = BigDecimal.valueOf(100.00);
    private static final String RETURN_URL = "https://www.unzer.com";

    @Override
    public Unzer getUnzer() {
        return new Unzer(Keys.UNZER_ONE_PRIVATE_KEY);
    }

    @Test
    void testCreateSca() {
        Sca result = createCardAndSca();

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getPaymentId());
        assertTrue(result.getId().startsWith("s-sca-"));
    }

    @Test
    void testFetchSca() {
        Sca created = createCardAndSca();
        Sca fetched = getUnzer().fetchSca(created.getPaymentId(), created.getId());

        assertNotNull(fetched);
        assertEquals(created.getId(), fetched.getId());
        assertEquals(created.getPaymentId(), fetched.getPaymentId());
    }

    @Test
    void testFetchPaymentWithSca() {
        // Create SCA transaction
        Sca sca = createCardAndSca();
        assertScaCreated(sca);

        // Fetch the payment - SCA should be included
        Payment payment = getUnzer().fetchPayment(sca.getPaymentId());

        assertNotNull(payment, "Payment should not be null");
        assertNotNull(payment.getSca(), "Payment should contain SCA transaction");
        assertEquals(sca.getId(), payment.getSca().getId(), "SCA ID should match");
        assertEquals(sca.getPaymentId(), payment.getSca().getPaymentId(), "Payment ID should match");
        assertEquals(sca.getProcessing().getUniqueId(), payment.getSca().getProcessing().getUniqueId(), "Unique ID should match");
    }

    @ParameterizedTest(name = "Incomplete SCA should fail for {0}")
    @ValueSource(strings = {"authorize", "charge"})
    void testPaymentWithIncompleteSCA(String operationType) {
        Sca sca = createCardAndSca();
        assertScaCreated(sca);

        // Use the cardId from the SCA transaction
        String cardId = sca.getTypeId();

        // Attempting to authorize/charge with incomplete SCA should fail
        PaymentException exception = assertThrows(PaymentException.class, () -> {
            if ("authorize".equals(operationType)) {
                Authorization authorization = new Authorization();
                authorization.setAmount(BigDecimal.valueOf(50.00))
                        .setTypeId(cardId)
                        .setCurrency(Currency.getInstance("EUR"));
                getUnzer().authorizeSca(sca.getPaymentId(), authorization);
            } else {
                Charge charge = new Charge();
                charge.setAmount(BigDecimal.valueOf(50.00))
                        .setTypeId(cardId)
                        .setCurrency(Currency.getInstance("EUR"));
                getUnzer().chargeSca(sca.getPaymentId(), charge);
            }
        });

        assertIncompleteScaException(exception);
    }

    // Helper methods

    private Sca createCardAndSca() {
        Card card = new Card(VISA_3DS_ENABLED_CARD_NUMBER, TEST_CARD_EXPIRY, TEST_CARD_CVC);
        card = getUnzer().createPaymentType(card);

        Sca sca = new Sca();
        sca.setAmount(SCA_AMOUNT)
                .setCurrency(Currency.getInstance("EUR"))
                .setTypeId(card.getId())
                .setReturnUrl(unsafeUrl(RETURN_URL));

        return getUnzer().sca(sca);
    }

    private void assertScaCreated(Sca sca) {
        assertNotNull(sca);
        assertNotNull(sca.getId());
        assertNotNull(sca.getPaymentId());
        assertNotNull(sca.getRedirectUrl());
    }

    private void assertIncompleteScaException(PaymentException exception) {
        assertEquals(400, exception.getStatusCode());
        assertEquals(1, exception.getPaymentErrorList().size());
        assertEquals("API.500.100.060", exception.getPaymentErrorList().get(0).getCode());
        assertEquals("Cannot perform payment on an unsuccessful strong customer authentication.",
                exception.getPaymentErrorList().get(0).getMerchantMessage());
    }
}

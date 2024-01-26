package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;
import com.unzer.payment.service.PaymentService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;

import static com.unzer.payment.business.BasketV1TestData.getMinTestBasketV1;
import static com.unzer.payment.business.BasketV2TestData.getMinTestBasketV2;
import static com.unzer.payment.business.Keys.LEGACY_PRIVATE_KEY;
import static com.unzer.payment.util.Types.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;


public class SepaDirectDebitSecuredTest extends AbstractPaymentTest {

    @Test
    public void testCreateSepaDirectDebitSecuredManatoryType() {
        SepaDirectDebitSecured sdd = new SepaDirectDebitSecured("DE89370400440532013000");
        sdd = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(sdd);
        assertNotNull(sdd.getId());
    }

    @Test
    public void testCreateSepaDirectDebitSecuredFullType() {
        SepaDirectDebitSecured sddOriginal = getSepaDirectDebitSecured();
        SepaDirectDebitSecured sddCreated = getUnzer(LEGACY_PRIVATE_KEY).createPaymentType(sddOriginal);
        assertSddEquals(sddOriginal, sddCreated);
    }

    @Test
    public void testFetchSepaDirectDebitSecuredType() {
        Unzer unzer = getUnzer(LEGACY_PRIVATE_KEY);
        SepaDirectDebitSecured sdd = unzer.createPaymentType(getSepaDirectDebitSecured());
        assertNotNull(sdd.getId());
        SepaDirectDebitSecured fetchedSdd = (SepaDirectDebitSecured) unzer.fetchPaymentType(sdd.getId());
        assertNotNull(fetchedSdd.getId());
        assertSddEquals(sdd, fetchedSdd);
    }

    @Test
    @Deprecated
    public void testChargeSepaDirectDebitSecuredTypeBasketV1() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        SepaDirectDebitSecured sdd = unzer.createPaymentType(getSepaDirectDebitSecured());
        assertNotNull(sdd.getId());
        Basket basket = getMinTestBasketV1();
        sdd.charge(
                basket.getAmountTotalGross(),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                getMaximumCustomerSameAddress(generateUuid()),
                basket
        );
    }

    @Test
    public void testChargeSepaDirectDebitSecuredTypeBasketV2() throws HttpCommunicationException, ParseException {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        SepaDirectDebitSecured sdd = unzer.createPaymentType(getSepaDirectDebitSecured());
        assertNotNull(sdd.getId());
        Basket basket = getMinTestBasketV2();
        sdd.charge(
                basket.getTotalValueGross(),
                Currency.getInstance("EUR"),
                unsafeUrl("https://www.unzer.com"),
                getMaximumCustomerSameAddress(generateUuid()),
                basket
        );
    }

    private void assertSddEquals(SepaDirectDebitSecured sddOriginal, SepaDirectDebitSecured sddCreated) {
        assertNotNull(sddCreated.getId());
        assertEquals(sddOriginal.getBic(), sddCreated.getBic());
        assertEquals(sddOriginal.getHolder(), sddCreated.getHolder());
        assertEquals(sddOriginal.getIban(), sddCreated.getIban());
    }

    @Test
    public void testChargeSepaDirectDebitGuaranteed() {
        Unzer unzer = getUnzer(Keys.LEGACY_PRIVATE_KEY);
        HttpClientBasedRestCommunication restCommunication = new HttpClientBasedRestCommunication();
        JsonParser jsonParser = new JsonParser();
        PaymentService paymentService = new PaymentService(unzer, restCommunication);

        String response = restCommunication.httpPost("https://api.unzer.com/v1/types/sepa-direct-debit-guaranteed", unzer.getPrivateKey(), new SepaDirectDebitSecured("DE89370400440532013000"));
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        SepaDirectDebitSecured sepaDirectDebitSecured = paymentService.fetchPaymentType(jsonResponse.getId());

        boolean matches = sepaDirectDebitSecured.getId().matches("s-ddg-\\w*");
        assertTrue(matches);

        Charge charge = sepaDirectDebitSecured.charge(BigDecimal.TEN, Currency.getInstance("EUR"), unsafeUrl("https://www.meinShop.de"), getMaximumCustomerSameAddress(generateUuid()));
        assertNotNull(charge.getPaymentId());
    }

    private SepaDirectDebitSecured getSepaDirectDebitSecured() {
        SepaDirectDebitSecured sdd = new SepaDirectDebitSecured("DE89370400440532013000");
        sdd.setBic("COBADEFFXXX");
        sdd.setHolder("Max Mustermann");
        return sdd;
    }

}

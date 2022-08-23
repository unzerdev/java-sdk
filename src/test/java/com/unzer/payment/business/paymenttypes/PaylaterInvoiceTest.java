package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.PaylaterInvoiceConfig;
import com.unzer.payment.paymenttypes.PaylaterInvoice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


public class PaylaterInvoiceTest extends AbstractPaymentTest {
    @Test
    public void testCreatePaylaterType() throws HttpCommunicationException {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());
        assertNotNull(paylaterInvoice.getId());
    }

    @Test
    public void testChargePaylaterType() throws HttpCommunicationException {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());

        Charge charge = getUnzer().charge(
                BigDecimal.ONE,
                Currency.getInstance("EUR"),
                paylaterInvoice
        );

        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testPaylaterCanbeAuthorized() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        PaylaterInvoice paylaterInvoice = unzer.createPaymentType(new PaylaterInvoice());
        Basket basket = unzer.createBasket(
                new Basket()
                        .setTotalValueGross(new BigDecimal("500.5"))
                        .setCurrencyCode(Currency.getInstance("EUR"))
                        .setOrderId(generateUuid())
                        .addBasketItem(
                                new BasketItem()
                                        .setBasketItemReferenceId("Artikelnummer4711")
                                        .setQuantity(5)
                                        .setVat(BigDecimal.ZERO)
                                        .setAmountDiscountPerUnitGross(BigDecimal.ZERO)
                                        .setAmountPerUnitGross(new BigDecimal("100.1"))
                                        .setTitle("Apple iPhone")
                        )
        );
        Customer customer = unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid()));

        Authorization authorization = (Authorization) new Authorization()
                .setAmount(BigDecimal.valueOf(99.99))
                .setCurrency(Currency.getInstance("EUR"))
                .setTypeId(paylaterInvoice.getId())
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setBasketId(basket.getId())
                .setCustomerId(customer.getId());

        Authorization response = unzer.authorize(authorization);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertFalse(response.getId().isEmpty());
        assertEquals(AbstractTransaction.Status.SUCCESS, authorization.getStatus());
    }

    // clientIpCanBeManuallySetForPaylaterInvoiceType

    @Test
    public void testFetchPaylaterType() throws HttpCommunicationException {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());
        assertNotNull(paylaterInvoice.getId());

        PaylaterInvoice fetchedPaylaterInvoice = (PaylaterInvoice) getUnzer().fetchPaymentType(paylaterInvoice.getId());
        assertNotNull(fetchedPaylaterInvoice.getId());
    }

    @TestFactory
    public Collection<DynamicTest> testFetchConfig() {
        class TestCase {
            final String name;
            final CustomerType customerType;
            final Locale locale;
            final PaylaterInvoiceConfig expectedConfig;

            public TestCase(String name, CustomerType customerType, Locale locale, PaylaterInvoiceConfig expectedConfig) {
                this.name = name;
                this.customerType = customerType;
                this.locale = locale;
                this.expectedConfig = expectedConfig;
            }
        }

        return Stream.of(
                new TestCase(
                        "B2B Germany",
                        CustomerType.B2B,
                        Locale.GERMANY,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                ),

                new TestCase(
                        "B2C Germany",
                        CustomerType.B2C,
                        Locale.GERMANY,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                ),

                new TestCase(
                        "B2C Italy",
                        CustomerType.B2C,
                        Locale.ITALY,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                ),

                new TestCase(
                        "Null locale",
                        CustomerType.B2C,
                        null,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                ),

                new TestCase(
                        "Not a country",
                        CustomerType.B2C,
                        Locale.ENGLISH,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.payolution.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                )
        ).map(tc -> dynamicTest(tc.name, () -> {
                    Unzer unzer = getUnzer();

                    PaylaterInvoiceConfig actualConfig = unzer.fetchPaylaterConfig(CustomerType.B2C, Locale.GERMANY);

                    assertNotNull(actualConfig);
                    assertEquals(tc.expectedConfig.getDataPrivacyConsent(), actualConfig.getDataPrivacyConsent());
                    assertEquals(tc.expectedConfig.getDataPrivacyDeclaration(), actualConfig.getDataPrivacyDeclaration());
                    assertEquals(tc.expectedConfig.getTermsAndConditions(), actualConfig.getTermsAndConditions());
                })
        ).collect(Collectors.toList());
    }
}

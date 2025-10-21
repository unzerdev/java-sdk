package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.BaseTransaction;
import com.unzer.payment.Basket;
import com.unzer.payment.BasketItem;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.PaylaterInvoiceConfig;
import com.unzer.payment.models.RiskData;
import com.unzer.payment.models.ShippingTransactionData;
import com.unzer.payment.paymenttypes.PaylaterInvoice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unzer.payment.util.Types.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


class PaylaterInvoiceTest extends AbstractPaymentTest {

    public static final String UPL_CONFIG_DOMAIN = "https://test-payment.paylater.unzer.com/";

    @Test
    void testCreatePaylaterType() {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());
        assertNotNull(paylaterInvoice.getId());
    }

    @Test
    void testFetchPaylaterType() {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());
        assertNotNull(paylaterInvoice.getId());

        PaylaterInvoice fetchedPaylaterInvoice =
                (PaylaterInvoice) getUnzer().fetchPaymentType(paylaterInvoice.getId());
        assertNotNull(fetchedPaylaterInvoice.getId());
    }

    @TestFactory
    @Disabled("unstable")
    public Collection<DynamicTest> testAuthorizeAndChargePaylater() {
        class TestCase {
            final String name;
            final PaylaterInvoice paylaterInvoice;
            final Basket basket;
            final Customer customer;
            final Authorization authorization;

            public TestCase(String name, PaylaterInvoice paylaterInvoice, Basket basket,
                            Customer customer, Authorization authorization) {
                this.name = name;
                this.paylaterInvoice = paylaterInvoice;
                this.basket = basket;
                this.customer = customer;
                this.authorization = authorization;
            }
        }

        return Stream.of(
                new TestCase(
                        "with risk data",
                        new PaylaterInvoice(),
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
                                ),
                        getMaximumCustomerSameAddress(generateUuid()),
                        (Authorization) new Authorization()
                                .setAmount(BigDecimal.valueOf(500.5))
                                .setCurrency(Currency.getInstance("EUR"))
                                .setReturnUrl(unsafeUrl("https://unzer.com"))
                                .setInvoiceId(generateUuid())
                                .setOrderId(generateUuid())
                                .setAdditionalTransactionData(
                                        new AdditionalTransactionData()
                                                .setRiskData(
                                                        new RiskData()
                                                                .setConfirmedAmount(2569.0)
                                                                .setConfirmedOrders(14)
                                                                .setRegistrationLevel(RiskData.RegistrationLevel.REGISTERED)
                                                                .setCustomerGroup(RiskData.CustomerGroup.GOOD)
                                                                .setRegistrationDate(new Date())
                                                )
                                )
                ),
                new TestCase(
                        "no risk data",
                        new PaylaterInvoice(),
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
                                ),
                        getMaximumCustomerSameAddress(generateUuid()),
                        (Authorization) new Authorization()
                                .setAmount(BigDecimal.valueOf(500.5))
                                .setCurrency(Currency.getInstance("EUR"))
                                .setReturnUrl(unsafeUrl("https://unzer.com"))
                )
        ).map(tc -> dynamicTest(tc.name, () -> {
            Unzer unzer = getUnzer();

            PaylaterInvoice paylaterInvoice = unzer.createPaymentType(tc.paylaterInvoice);
            if (tc.basket != null) {
                Basket basket = unzer.createBasket(tc.basket);
                tc.authorization.setBasketId(basket.getId());
            }

            if (tc.customer != null) {
                Customer customer = unzer.createCustomer(tc.customer);
                tc.authorization.setCustomerId(customer.getId());

                if (tc.authorization.getAdditionalTransactionData() != null &&
                        tc.authorization.getAdditionalTransactionData().getRiskData() != null) {
                    tc.authorization.getAdditionalTransactionData().getRiskData()
                            .setCustomerId(customer.getCustomerId());
                }
            }
            tc.authorization.setTypeId(paylaterInvoice.getId());

            // Authorize
            Authorization responseAuthorization = unzer.authorize(tc.authorization);

            assertNotNull(responseAuthorization);
            assertNotNull(responseAuthorization.getId());
            assertFalse(responseAuthorization.getId().isEmpty());
            assertEquals(BaseTransaction.Status.SUCCESS, responseAuthorization.getStatus());
            assertNotNull(responseAuthorization.getPaymentId());
            assertFalse(responseAuthorization.getPaymentId().isEmpty());
            assertEquals(tc.authorization.getInvoiceId(), responseAuthorization.getInvoiceId());
            assertEquals(tc.authorization.getOrderId(), responseAuthorization.getOrderId());

            // Charge
            Charge responseCharge = unzer.chargeAuthorization(responseAuthorization.getPaymentId());
            assertNotNull(responseCharge);
            assertEquals(BaseTransaction.Status.SUCCESS, responseCharge.getStatus());
            assertNotNull(responseCharge.getId());
            assertEquals(tc.authorization.getInvoiceId(), responseCharge.getInvoiceId());
            assertEquals(tc.authorization.getOrderId(), responseCharge.getOrderId());

        })).collect(Collectors.toList());
    }

    @TestFactory
    @Disabled("unstable")
    public Collection<DynamicTest> testCancelAuthorization() {
        class TestCase {
            final String name;
            final PaylaterInvoice paylaterInvoice;
            final Basket basket;
            final Customer customer;
            final BigDecimal amount;
            final Authorization authorization;

            public TestCase(
                    String name,
                    PaylaterInvoice paylaterInvoice,
                    Basket basket,
                    Customer customer,
                    BigDecimal amount,
                    Authorization authorization
            ) {
                this.name = name;
                this.paylaterInvoice = paylaterInvoice;
                this.basket = basket;
                this.customer = customer;
                this.amount = amount;
                this.authorization = authorization;
            }
        }

        return Stream.of(
                new TestCase(
                        "full cancel",
                        new PaylaterInvoice(),
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
                                ),
                        getMaximumCustomerSameAddress(generateUuid()),
                        BigDecimal.valueOf(500.5),
                        (Authorization) new Authorization()
                                .setAmount(BigDecimal.valueOf(500.5))
                                .setCurrency(Currency.getInstance("EUR"))
                                .setReturnUrl(unsafeUrl("https://unzer.com"))
                                .setOrderId(generateUuid())
                                .setInvoiceId(generateUuid())
                ),
                new TestCase(
                        "partial cancel",
                        new PaylaterInvoice(),
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
                                ),
                        getMaximumCustomerSameAddress(generateUuid()),
                        BigDecimal.TEN,
                        (Authorization) new Authorization()
                                .setAmount(BigDecimal.valueOf(500.5))
                                .setCurrency(Currency.getInstance("EUR"))
                                .setReturnUrl(unsafeUrl("https://unzer.com"))
                )
        ).map(tc -> dynamicTest(tc.name, () -> {
            Unzer unzer = getUnzer();

            PaylaterInvoice paylaterInvoice = unzer.createPaymentType(tc.paylaterInvoice);
            if (tc.basket != null) {
                Basket basket = unzer.createBasket(tc.basket);
                tc.authorization.setBasketId(basket.getId());
            }

            if (tc.customer != null) {
                Customer customer = unzer.createCustomer(tc.customer);
                tc.authorization.setCustomerId(customer.getId());

                if (tc.authorization.getAdditionalTransactionData() != null &&
                        tc.authorization.getAdditionalTransactionData().getRiskData() != null) {
                    tc.authorization.getAdditionalTransactionData().getRiskData()
                            .setCustomerId(customer.getCustomerId());
                }
            }
            tc.authorization.setTypeId(paylaterInvoice.getId());

            // Authorize
            Authorization responseAuthorization = unzer.authorize(tc.authorization);

            assertNotNull(responseAuthorization);
            assertNotNull(responseAuthorization.getId());
            assertFalse(responseAuthorization.getId().isEmpty());
            assertEquals(BaseTransaction.Status.SUCCESS, responseAuthorization.getStatus());
            assertNotNull(responseAuthorization.getPaymentId());
            assertFalse(responseAuthorization.getPaymentId().isEmpty());

            // Cancel authorization (reversal)
            Cancel cancelResponse =
                    unzer.cancelAuthorization(responseAuthorization.getPaymentId(), tc.amount);
            assertNotNull(cancelResponse);
            assertEquals(BaseTransaction.Status.SUCCESS, cancelResponse.getStatus());
            assertNotNull(cancelResponse.getId());
            assertEquals(tc.authorization.getInvoiceId(), cancelResponse.getInvoiceId());
            assertEquals(tc.authorization.getOrderId(), cancelResponse.getOrderId());
        })).collect(Collectors.toList());
    }

    @TestFactory
    @Disabled("unstable")
    public Collection<DynamicTest> testCancelCharge() {
        class TestCase {
            final String name;
            final PaylaterInvoice paylaterInvoice;
            final Basket basket;
            final Customer customer;
            final BigDecimal amount;
            final Authorization authorization;

            public TestCase(String name, PaylaterInvoice paylaterInvoice, Basket basket,
                            Customer customer, BigDecimal amount, Authorization authorization) {
                this.name = name;
                this.paylaterInvoice = paylaterInvoice;
                this.basket = basket;
                this.customer = customer;
                this.amount = amount;
                this.authorization = authorization;
            }
        }

        return Stream.of(
                new TestCase(
                        "total refund",
                        new PaylaterInvoice(),
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
                                ),
                        getMaximumCustomerSameAddress(generateUuid()),
                        BigDecimal.valueOf(500.5),
                        (Authorization) new Authorization()
                                .setAmount(BigDecimal.valueOf(500.5))
                                .setCurrency(Currency.getInstance("EUR"))
                                .setReturnUrl(unsafeUrl("https://unzer.com"))
                                .setInvoiceId(generateUuid())
                                .setOrderId(generateUuid())
                ),
                new TestCase(
                        "partial refund",
                        new PaylaterInvoice(),
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
                                ),
                        getMaximumCustomerSameAddress(generateUuid()),
                        BigDecimal.TEN,
                        (Authorization) new Authorization()
                                .setAmount(BigDecimal.valueOf(500.5))
                                .setCurrency(Currency.getInstance("EUR"))
                                .setReturnUrl(unsafeUrl("https://unzer.com"))
                )
        ).map(tc -> dynamicTest(tc.name, () -> {
            Unzer unzer = getUnzer();

            PaylaterInvoice paylaterInvoice = unzer.createPaymentType(tc.paylaterInvoice);
            if (tc.basket != null) {
                Basket basket = unzer.createBasket(tc.basket);
                tc.authorization.setBasketId(basket.getId());
            }

            if (tc.customer != null) {
                Customer customer = unzer.createCustomer(tc.customer);
                tc.authorization.setCustomerId(customer.getId());

                if (tc.authorization.getAdditionalTransactionData() != null &&
                        tc.authorization.getAdditionalTransactionData().getRiskData() != null) {
                    tc.authorization.getAdditionalTransactionData().getRiskData()
                            .setCustomerId(customer.getCustomerId());
                }
            }
            tc.authorization.setTypeId(paylaterInvoice.getId());

            // Authorize
            Authorization responseAuthorization = unzer.authorize(tc.authorization);

            assertNotNull(responseAuthorization);
            assertNotNull(responseAuthorization.getId());
            assertFalse(responseAuthorization.getId().isEmpty());
            assertEquals(BaseTransaction.Status.SUCCESS, responseAuthorization.getStatus());
            assertNotNull(responseAuthorization.getPaymentId());
            assertFalse(responseAuthorization.getPaymentId().isEmpty());

            // Charge
            Charge responseCharge = unzer.chargeAuthorization(responseAuthorization.getPaymentId());
            assertNotNull(responseCharge);
            assertEquals(BaseTransaction.Status.SUCCESS, responseCharge.getStatus());
            assertNotNull(responseCharge.getId());

            // Cancel charge v1/payments/{PAYMENT_ID}/charges/cancels
            Cancel cancelResponse = unzer.cancelCharge(responseCharge.getPaymentId(), tc.amount);
            assertNotNull(cancelResponse);
            assertEquals(BaseTransaction.Status.SUCCESS, cancelResponse.getStatus());
            assertNotNull(cancelResponse.getId());
            assertEquals(tc.amount.setScale(3), cancelResponse.getAmount().setScale(3));
        })).collect(Collectors.toList());
    }

    @TestFactory
    public Collection<DynamicTest> testFetchConfig() {
        class TestCase {
            final String name;
            final CustomerType customerType;
            final Locale locale;

            public TestCase(String name, CustomerType customerType, Locale locale) {
                this.name = name;
                this.customerType = customerType;
                this.locale = locale;
            }
        }

        return Stream.of(
                new TestCase(
                        "B2B Germany",
                        CustomerType.B2B,
                        Locale.GERMANY
                ),

                new TestCase(
                        "B2C Germany",
                        CustomerType.B2C,
                        Locale.GERMANY
                ),

                new TestCase(
                        "B2C Italy",
                        CustomerType.B2C,
                        Locale.ITALY
                ),

                new TestCase(
                        "Null locale",
                        CustomerType.B2C,
                        null
                ),

                new TestCase(
                        "Not a country",
                        CustomerType.B2C,
                        Locale.ENGLISH
                )
        ).map(tc -> dynamicTest(tc.name, () -> {
                    Unzer unzer = getUnzer();

                    PaylaterInvoiceConfig actualConfig =
                            unzer.fetchPaylaterConfig(CustomerType.B2C, Locale.GERMANY);

                    assertNotNull(actualConfig);

            String consentUrl = actualConfig.getDataPrivacyConsent();
            String declarationURL = actualConfig.getDataPrivacyDeclaration();
            String termsUrl = actualConfig.getTermsAndConditions();

            assertNotNull(consentUrl);
            assertNotNull(declarationURL);
            assertNotNull(termsUrl);

            assertTrue(consentUrl.startsWith(UPL_CONFIG_DOMAIN));
            assertTrue(declarationURL.startsWith(UPL_CONFIG_DOMAIN));
            assertTrue(termsUrl.startsWith(UPL_CONFIG_DOMAIN));
                })
        ).collect(Collectors.toList());
    }

    @Test
    @Disabled("deprecated")
    public void testChargeAuthorizationWithChargeObject() {
        Unzer unzer = getUnzer();

        Basket basket = unzer.createBasket(new Basket()
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
                ));

        Customer customer = unzer.createCustomer(getMaximumCustomerSameAddress(generateUuid()));


        PaylaterInvoice paylaterInvoice = unzer.createPaymentType(new PaylaterInvoice());

        Authorization auth = unzer.authorize((Authorization) new Authorization()
                .setAmount(new BigDecimal("500.5"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setBasketId(basket.getId())
                .setCustomerId(customer.getId())
                .setTypeId(paylaterInvoice.getId())
                .setCurrency(Currency.getInstance("EUR"))
        );

        assertNull(auth.getAdditionalTransactionData());


        ShippingTransactionData shippingData = new ShippingTransactionData()
                .setDeliveryService(generateUuid())
                .setReturnTrackingId(generateUuid())
                .setDeliveryTrackingId(generateUuid());

        Charge charge = (Charge) new Charge()
                .setPaymentId(auth.getPaymentId())
                .setAmount(BigDecimal.TEN)
                .setAdditionalTransactionData(
                        new AdditionalTransactionData().setShipping(shippingData)
                );

        Charge resp = unzer.chargeAuthorization(charge.getPaymentId(), charge);
        assertNotNull(resp.getAdditionalTransactionData().getShipping());
        assertEquals(shippingData, resp.getAdditionalTransactionData().getShipping());
    }

}

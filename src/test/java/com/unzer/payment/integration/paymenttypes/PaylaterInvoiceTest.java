/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.*;
import com.unzer.payment.paymenttypes.PaylaterInvoice;
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
    public void testFetchPaylaterType() throws HttpCommunicationException {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());
        assertNotNull(paylaterInvoice.getId());

        PaylaterInvoice fetchedPaylaterInvoice = (PaylaterInvoice) getUnzer().fetchPaymentType(paylaterInvoice.getId());
        assertNotNull(fetchedPaylaterInvoice.getId());
    }

    @TestFactory
    public Collection<DynamicTest> testAuthorizeAndChargePaylater() {
        class TestCase {
            final String name;
            final PaylaterInvoice paylaterInvoice;
            final Basket basket;
            final Customer customer;
            final Authorization authorization;

            public TestCase(String name, PaylaterInvoice paylaterInvoice, Basket basket, Customer customer, Authorization authorization) {
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

                if (tc.authorization.getAdditionalTransactionData() != null && tc.authorization.getAdditionalTransactionData().getRiskData() != null) {
                    tc.authorization.getAdditionalTransactionData().getRiskData().setCustomerId(customer.getCustomerId());
                }
            }
            tc.authorization.setTypeId(paylaterInvoice.getId());

            // Authorize
            Authorization responseAuthorization = unzer.authorize(tc.authorization);

            assertNotNull(responseAuthorization);
            assertNotNull(responseAuthorization.getId());
            assertFalse(responseAuthorization.getId().isEmpty());
            assertEquals(AbstractTransaction.Status.SUCCESS, responseAuthorization.getStatus());
            assertNotNull(responseAuthorization.getPaymentId());
            assertFalse(responseAuthorization.getPaymentId().isEmpty());
            assertEquals(tc.authorization.getInvoiceId(), responseAuthorization.getInvoiceId());
            assertEquals(tc.authorization.getOrderId(), responseAuthorization.getOrderId());

            // Charge
            Charge responseCharge = unzer.chargeAuthorization(responseAuthorization.getPaymentId());
            assertNotNull(responseCharge);
            assertEquals(AbstractTransaction.Status.SUCCESS, responseCharge.getStatus());
            assertNotNull(responseCharge.getId());
            assertEquals(tc.authorization.getInvoiceId(), responseCharge.getInvoiceId());
            assertEquals(tc.authorization.getOrderId(), responseCharge.getOrderId());

        })).collect(Collectors.toList());
    }

    @TestFactory
    public Collection<DynamicTest> testCancelAuthorization() {
        class TestCase {
            final String name;
            final PaylaterInvoice paylaterInvoice;
            final Basket basket;
            final Customer customer;
            final BigDecimal amount;
            final Authorization authorization;
            final String errorCode;

            public TestCase(String name, PaylaterInvoice paylaterInvoice, Basket basket, Customer customer, BigDecimal amount, Authorization authorization, String errorCode) {
                this.name = name;
                this.paylaterInvoice = paylaterInvoice;
                this.basket = basket;
                this.customer = customer;
                this.amount = amount;
                this.authorization = authorization;
                this.errorCode = errorCode;
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
                                .setInvoiceId(generateUuid()),
                        null
                ),
                new TestCase(
                        "partial cancel - fails",
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
                                .setReturnUrl(unsafeUrl("https://unzer.com")),
                        "API.340.540.108" // Partial refund is not allowed
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

                if (tc.authorization.getAdditionalTransactionData() != null && tc.authorization.getAdditionalTransactionData().getRiskData() != null) {
                    tc.authorization.getAdditionalTransactionData().getRiskData().setCustomerId(customer.getCustomerId());
                }
            }
            tc.authorization.setTypeId(paylaterInvoice.getId());

            // Authorize
            Authorization responseAuthorization = unzer.authorize(tc.authorization);

            assertNotNull(responseAuthorization);
            assertNotNull(responseAuthorization.getId());
            assertFalse(responseAuthorization.getId().isEmpty());
            assertEquals(AbstractTransaction.Status.SUCCESS, responseAuthorization.getStatus());
            assertNotNull(responseAuthorization.getPaymentId());
            assertFalse(responseAuthorization.getPaymentId().isEmpty());

            // Cancel authorization (reversal)
            if (tc.errorCode == null) {
                Cancel cancelResponse = unzer.cancelAuthorization(responseAuthorization.getPaymentId(), tc.amount);
                assertNotNull(cancelResponse);
                assertEquals(AbstractTransaction.Status.SUCCESS, cancelResponse.getStatus());
                assertNotNull(cancelResponse.getId());
                assertEquals(tc.authorization.getInvoiceId(), cancelResponse.getInvoiceId());
                assertEquals(tc.authorization.getOrderId(), cancelResponse.getOrderId());
            } else {
                PaymentException ex = assertThrows(PaymentException.class, () -> {
                    unzer.cancelAuthorization(responseAuthorization.getPaymentId(), tc.amount);
                });
                assertEquals(tc.errorCode, ex.getPaymentErrorList().get(0).getCode());

            }
        })).collect(Collectors.toList());
    }

    @TestFactory
    public Collection<DynamicTest> testCancelCharge() {
        class TestCase {
            final String name;
            final PaylaterInvoice paylaterInvoice;
            final Basket basket;
            final Customer customer;
            final BigDecimal amount;
            final Authorization authorization;

            public TestCase(String name, PaylaterInvoice paylaterInvoice, Basket basket, Customer customer, BigDecimal amount, Authorization authorization) {
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

                if (tc.authorization.getAdditionalTransactionData() != null && tc.authorization.getAdditionalTransactionData().getRiskData() != null) {
                    tc.authorization.getAdditionalTransactionData().getRiskData().setCustomerId(customer.getCustomerId());
                }
            }
            tc.authorization.setTypeId(paylaterInvoice.getId());

            // Authorize
            Authorization responseAuthorization = unzer.authorize(tc.authorization);

            assertNotNull(responseAuthorization);
            assertNotNull(responseAuthorization.getId());
            assertFalse(responseAuthorization.getId().isEmpty());
            assertEquals(AbstractTransaction.Status.SUCCESS, responseAuthorization.getStatus());
            assertNotNull(responseAuthorization.getPaymentId());
            assertFalse(responseAuthorization.getPaymentId().isEmpty());

            // Charge
            Charge responseCharge = unzer.chargeAuthorization(responseAuthorization.getPaymentId());
            assertNotNull(responseCharge);
            assertEquals(AbstractTransaction.Status.SUCCESS, responseCharge.getStatus());
            assertNotNull(responseCharge.getId());

            // Cancel charge v1/payments/{PAYMENT_ID}/charges/cancels
            Cancel cancelResponse = unzer.cancelCharge(responseCharge.getPaymentId(), tc.amount);
            assertNotNull(cancelResponse);
            assertEquals(AbstractTransaction.Status.SUCCESS, cancelResponse.getStatus());
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
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                ),

                new TestCase(
                        "B2C Germany",
                        CustomerType.B2C,
                        Locale.GERMANY,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                ),

                new TestCase(
                        "B2C Italy",
                        CustomerType.B2C,
                        Locale.ITALY,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                ),

                new TestCase(
                        "Null locale",
                        CustomerType.B2C,
                        null,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
                        )
                ),

                new TestCase(
                        "Not a country",
                        CustomerType.B2C,
                        Locale.ENGLISH,
                        new PaylaterInvoiceConfig(
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacyconsent?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/dataprivacydeclaration?channelId=heidelpay-invoice-b2b-b2c",
                                "https://test-payment.paylater.unzer.com/payolution-payment/infoport/termsandconditions?channelId=heidelpay-invoice-b2b-b2c"
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

    @Test
    public void testChargeAuthorizationWithChargeObject() throws HttpCommunicationException {
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

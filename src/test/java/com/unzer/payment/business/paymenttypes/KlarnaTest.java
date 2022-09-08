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
package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.PaylaterInvoiceConfig;
import com.unzer.payment.paymenttypes.Klarna;
import com.unzer.payment.paymenttypes.PaylaterInvoice;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


public class KlarnaTest extends AbstractPaymentTest {
    @Test
    public void testCreatePaymentType() throws HttpCommunicationException {
        Klarna klarna = getUnzer().createPaymentType(new Klarna());
        assertNotNull(klarna);
        assertNotNull(klarna.getId());
    }

    @Test
    public void testFetchPaymentType() throws HttpCommunicationException {
        Klarna paymentType = getUnzer().createPaymentType(new Klarna());
        assertNotNull(paymentType.getId());

        Klarna fetched = (Klarna) getUnzer().fetchPaymentType(paymentType.getId());
        assertNotNull(fetched.getId());
    }

    @TestFactory
    public Collection<DynamicTest> testAuthorize() {
        class TestCase {
            final String name;
            final Customer customer;
            final Basket basket;
            final Authorization authorization;
            final Collection<PaymentError> expectedErrors;

            public TestCase(String name, Customer customer, Basket basket, Authorization authorization, Collection<PaymentError> expectedErrors) {
                this.name = name;
                this.authorization = authorization;
                this.customer = customer;
                this.basket = basket;
                this.expectedErrors = expectedErrors;
            }
        }

        return Stream.of(
                new TestCase(
                        "with terms urls",

                        getMaximumCustomer(generateUuid())
                                .setCompany("Unzer GmbH"),

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
                                ), (Authorization) new Authorization()
                        .setAmount(BigDecimal.valueOf(500.5))
                        .setCurrency(Currency.getInstance("EUR"))
                        .setReturnUrl(unsafeUrl("https://unzer.com"))
                        .setAdditionalTransactionData(
                                new AdditionalTransactionData()
                                        .setPrivacyPolicyUrl("https://en.wikipedia.org/wiki/Policy")
                                        .setTermsAndConditionsUrl("https://en.wikipedia.org/wiki/Terms_of_service")
                        ),

                        null
                ),
                new TestCase(
                        "without terms urls",

                        getMaximumCustomer(generateUuid())
                                .setCompany("Unzer GmbH"),

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
                                ), (Authorization) new Authorization()
                        .setAmount(BigDecimal.valueOf(500.5))
                        .setCurrency(Currency.getInstance("EUR"))
                        .setReturnUrl(unsafeUrl("https://unzer.com")),

                        Arrays.asList(
                                new PaymentError(
                                        "additionalTransactionData.termsAndConditionUrl is missing.",
                                        "An error occurred. Please contact us for more information.",
                                        "API.320.200.209"
                                ),
                                new PaymentError(
                                        "additionalTransactionData.privacyPolicyUrl is missing.",
                                        "An error occurred. Please contact us for more information.",
                                        "API.320.200.210"
                                )
                        )
                ),
                new TestCase(
                        "without language",

                        getMaximumCustomer(generateUuid())
                                .setLanguage(null)
                                .setCompany("Unzer GmbH"),

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
                                ), (Authorization) new Authorization()
                        .setAmount(BigDecimal.valueOf(500.5))
                        .setCurrency(Currency.getInstance("EUR"))
                        .setReturnUrl(unsafeUrl("https://unzer.com"))
                        .setAdditionalTransactionData(
                                new AdditionalTransactionData()
                                        .setPrivacyPolicyUrl("https://en.wikipedia.org/wiki/Policy")
                                        .setTermsAndConditionsUrl("https://en.wikipedia.org/wiki/Terms_of_service")
                        ),

                        Collections.singletonList(
                                new PaymentError(
                                        "system error( possible incorrect/missing input data)",
                                        "An unexpected error occurred. Please contact us for more information.",
                                        "COR.100.370.111"
                                )
                        )
                )
        ).map(tc -> dynamicTest(tc.name, () -> {
            Unzer unzer = getUnzer();

            Klarna type = unzer.createPaymentType(new Klarna());
            tc.authorization.setTypeId(type.getId());

            Customer customer = unzer.createCustomer(tc.customer);
            tc.authorization.setCustomerId(customer.getId());

            if (tc.basket != null) {
                Basket basket = unzer.createBasket(tc.basket);
                tc.authorization.setBasketId(basket.getId());
            }


            // Authorize
            if (tc.expectedErrors == null) {
                Authorization responseAuthorization = unzer.authorize(tc.authorization);

                assertNotNull(responseAuthorization);
                assertNotNull(responseAuthorization.getId());
                assertFalse(responseAuthorization.getId().isEmpty());
                assertNotEquals(AbstractTransaction.Status.ERROR, responseAuthorization.getStatus());
                assertNotNull(responseAuthorization.getPaymentId());
                assertFalse(responseAuthorization.getPaymentId().isEmpty());
                assertNotNull(responseAuthorization.getRedirectUrl());
            } else {
                PaymentException ex = assertThrows(PaymentException.class, () -> unzer.authorize(tc.authorization));
                assertEquals(tc.expectedErrors.size(), ex.getPaymentErrorList().size());
                assertEquals(
                        tc.expectedErrors.stream().sorted(Comparator.comparing(PaymentError::getCode)).collect(Collectors.toList()),
                        ex.getPaymentErrorList().stream().sorted(Comparator.comparing(PaymentError::getCode)).collect(Collectors.toList())
                );
            }

            // Charge is not possible without browser
            //
            // Actions to perform:
            // * Click button:id=buy-button
            // * Click button:id=onConfirm
            // * Find input:id=one-time-code -> Enter 999998
            // * Click button:id=invoice_kp-purchase-review-continue-button
            // * Get redirected
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
                        "full refund",
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
                                .setReturnUrl(unsafeUrl("https://unzer.com")),
                        null
                ),
                new TestCase(
                        "partial refund - fails",
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

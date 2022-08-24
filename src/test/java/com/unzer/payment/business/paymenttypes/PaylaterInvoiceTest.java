package com.unzer.payment.business.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.PaylaterInvoiceConfig;
import com.unzer.payment.models.RiskData;
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

    @TestFactory
    public Collection<DynamicTest> testAuthorizePaylater() {
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
                                .setAmount(BigDecimal.valueOf(99.99))
                                .setCurrency(Currency.getInstance("EUR"))
                                .setReturnUrl(unsafeUrl("https://unzer.com"))
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
                                .setAmount(BigDecimal.valueOf(99.99))
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

            Authorization response = unzer.authorize(tc.authorization);

            assertNotNull(response);
            assertNotNull(response.getId());
            assertFalse(response.getId().isEmpty());
            assertEquals(AbstractTransaction.Status.SUCCESS, response.getStatus());
        })).collect(Collectors.toList());
    }


    @Test
    public void testFetchPaylaterType() throws HttpCommunicationException {
        PaylaterInvoice paylaterInvoice = getUnzer().createPaymentType(new PaylaterInvoice());
        assertNotNull(paylaterInvoice.getId());

        PaylaterInvoice fetchedPaylaterInvoice = (PaylaterInvoice) getUnzer().fetchPaymentType(paylaterInvoice.getId());
        assertNotNull(fetchedPaylaterInvoice.getId());
    }

    // TODO: upl is cancelable

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

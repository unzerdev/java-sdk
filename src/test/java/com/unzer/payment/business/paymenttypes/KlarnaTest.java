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
import com.unzer.payment.paymenttypes.Klarna;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;


public class KlarnaTest extends AbstractPaymentTest {
    private ChromeDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    void driverSetup() {
        ChromeOptions chromeOptions = new ChromeOptions()
                .addArguments(
                        "--ignore-certificate-errors",
                        "--headless"
                );

        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, 2000);

    }

    @AfterEach
    void closeDriver() {
        if (driver != null) {
            driver.close();
        }
    }

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
                Authorization createdAuth = unzer.authorize(tc.authorization);

                assertNotNull(createdAuth);
                assertNotNull(createdAuth.getId());
                assertFalse(createdAuth.getId().isEmpty());
                assertNotEquals(AbstractTransaction.Status.ERROR, createdAuth.getStatus());
                assertNotNull(createdAuth.getPaymentId());
                assertFalse(createdAuth.getPaymentId().isEmpty());
                assertNotNull(createdAuth.getRedirectUrl());

                performKlarnaAuthorization(createdAuth.getRedirectUrl().toString());

                Authorization succeedAuth = unzer.fetchAuthorization(createdAuth.getPaymentId());
                assertNotNull(succeedAuth);
                assertEquals(AbstractTransaction.Status.SUCCESS, succeedAuth.getStatus());

                Charge charge = unzer.chargeAuthorization(succeedAuth.getPaymentId());
                assertNotNull(charge);
                assertEquals(AbstractTransaction.Status.SUCCESS, charge.getStatus());
            } else {
                PaymentException ex = assertThrows(PaymentException.class, () -> unzer.authorize(tc.authorization));
                assertEquals(tc.expectedErrors.size(), ex.getPaymentErrorList().size());
                assertEquals(
                        tc.expectedErrors.stream().sorted(Comparator.comparing(PaymentError::getCode)).collect(Collectors.toList()),
                        ex.getPaymentErrorList().stream().sorted(Comparator.comparing(PaymentError::getCode)).collect(Collectors.toList())
                );
            }
        })).collect(Collectors.toList());
    }

    private void performKlarnaAuthorization(String redirectUrl) {
        driverSetup();
        driver.get(redirectUrl);
        wait.until(ExpectedConditions.urlContains("https://pay.playground.klarna.com/"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("buy-button")));
        driver.findElement(By.id("buy-button")).click();
        driver.switchTo().frame("klarna-hpp-instance-fullscreen");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("onContinue")));
        driver.findElement(By.id("onContinue")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("otp_field")));
        driver.findElement(By.id("otp_field")).sendKeys("999998");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("invoice_kp-purchase-review-continue-button")));
        driver.findElement(By.id("invoice_kp-purchase-review-continue-button")).click();
        wait.until(ExpectedConditions.urlContains("unzer.com"));
    }
}

package com.unzer.payment.integration.paymenttypes;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Authorization;
import com.unzer.payment.Basket;
import com.unzer.payment.BasketItem;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.paymenttypes.Klarna;
import com.unzer.payment.util.Integration;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class KlarnaTest extends AbstractPaymentTest {
    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    private AbstractMap.SimpleEntry<WebDriver, WebDriverWait> driverSetup() {
        ChromeOptions chromeOptions = new ChromeOptions()
                .addArguments(
                        "--ignore-certificate-errors",
                        "--headless"
                );
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));

        return new AbstractMap.SimpleEntry<>(driver, wait);
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

    @Integration
    @TestFactory
    @Disabled("Klarna has changed UI workflow")
    public Collection<DynamicTest> testAuthorizeAndCharge() {
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
                                        .setPrivacyPolicyUrl("https://unzer.com")
                                        .setTermsAndConditionsUrl("https://unzer.com")
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
                        null
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
                                        .setPrivacyPolicyUrl("https://unzer.com")
                                        .setTermsAndConditionsUrl("https://unzer.com")
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
                assertEquals(sorted(tc.expectedErrors), sorted(ex.getPaymentErrorList()));
            }
        })).collect(Collectors.toList());
    }

    @Integration
    @TestFactory
    @Disabled("Klarna has changed UI workflow")
    public Collection<DynamicTest> testCancelAuthorize() {
        final BigDecimal totalAmount = BigDecimal.valueOf(500.5);
        class TestCase {
            final String name;
            final BigDecimal amount;
            final Collection<PaymentError> expectedErrors;

            public TestCase(String name, BigDecimal amount, Collection<PaymentError> expectedErrors) {
                this.name = name;
                this.amount = amount;
                this.expectedErrors = expectedErrors;
            }
        }

        return Stream.of(
                        new TestCase(
                                "full amount",
                                totalAmount,
                                null
                        ),
                        new TestCase(
                                "partial cancel",
                                totalAmount.subtract(BigDecimal.TEN),
                                Collections.singletonList(
                                        new PaymentError(
                                                "Partial reversal for payment type klarna is not supported.",
                                                "An error occurred. Please contact us for more information.",
                                                "API.340.540.108"
                                        )
                                )
                        ),
                        new TestCase(
                                "exceeded amount",
                                totalAmount.add(BigDecimal.TEN),
                                Collections.singletonList(
                                        new PaymentError(
                                                "The amount of 510.5 to be reversed exceeds the authorized amount of 500.5",
                                                "An error occurred. Please contact us for more information.",
                                                "API.340.100.017"
                                        )
                                )
                        )
                ).map(tc -> dynamicTest(tc.name, () -> {
                            Unzer unzer = getUnzer();

                            Customer customer = unzer.createCustomer(getMaximumCustomer(generateUuid()).setCompany("Unzer GmbH"));
                            Basket basket = unzer.createBasket(new Basket()
                                    .setTotalValueGross(totalAmount)
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


                            Klarna type = unzer.createPaymentType(new Klarna());

                            Authorization authorization = (Authorization) new Authorization()
                                    .setAmount(totalAmount)
                                    .setTypeId(type.getId())
                                    .setCustomerId(customer.getId())
                                    .setBasketId(basket.getId())
                                    .setCurrency(Currency.getInstance("EUR"))
                                    .setReturnUrl(unsafeUrl("https://unzer.com"))
                                    .setAdditionalTransactionData(
                                            new AdditionalTransactionData()
                                                    .setPrivacyPolicyUrl("https://unzer.com")
                                                    .setTermsAndConditionsUrl("https://unzer.com")
                                    );


                            // Authorize
                            Authorization createdAuth = unzer.authorize(authorization);

                            assertNotNull(createdAuth.getId());
                            performKlarnaAuthorization(createdAuth.getRedirectUrl().toString());

                            Authorization succeedAuth = unzer.fetchAuthorization(createdAuth.getPaymentId());

                            if (tc.expectedErrors == null) {
                                Cancel cancel = unzer.cancelAuthorization(succeedAuth.getPaymentId(), tc.amount);
                                assertEquals(AbstractTransaction.Status.SUCCESS, cancel.getStatus());
                            } else {
                                PaymentException ex = assertThrows(PaymentException.class, () -> unzer.cancelAuthorization(succeedAuth.getPaymentId(), tc.amount));
                                assertEquals(sorted(tc.expectedErrors), sorted(ex.getPaymentErrorList()));
                            }
                        })
                )
                .collect(Collectors.toList());
    }

    @Integration
    @TestFactory
    @Disabled("Klarna has changed UI workflow")
    public Collection<DynamicTest> testCancelCharge() {
        final BigDecimal totalAmount = BigDecimal.valueOf(500.5);
        class TestCase {
            final String name;
            final BigDecimal amount;
            final Collection<PaymentError> expectedErrors;

            public TestCase(String name, BigDecimal amount, Collection<PaymentError> expectedErrors) {
                this.name = name;
                this.amount = amount;
                this.expectedErrors = expectedErrors;
            }
        }

        return Stream.of(
                        new TestCase(
                                "full amount",
                                totalAmount,
                                null
                        ),
                        new TestCase(
                                "partial cancel",
                                totalAmount.subtract(BigDecimal.TEN),
                                null
                        ),
                        new TestCase(
                                "exceeded amount",
                                totalAmount.add(BigDecimal.TEN),
                                Collections.singletonList(
                                        new PaymentError(
                                                "Already charged.",
                                                "Already charged. Please contact us for more information.",
                                                "API.340.100.018"
                                        )
                                )
                        )
                ).map(tc -> dynamicTest(tc.name, () -> {
                            Unzer unzer = getUnzer();

                            Customer customer = unzer.createCustomer(getMaximumCustomer(generateUuid()).setCompany("Unzer GmbH"));
                            Basket basket = unzer.createBasket(new Basket()
                                    .setTotalValueGross(totalAmount)
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


                            Klarna type = unzer.createPaymentType(new Klarna());

                            Authorization authorization = (Authorization) new Authorization()
                                    .setAmount(totalAmount)
                                    .setTypeId(type.getId())
                                    .setCustomerId(customer.getId())
                                    .setBasketId(basket.getId())
                                    .setCurrency(Currency.getInstance("EUR"))
                                    .setReturnUrl(unsafeUrl("https://unzer.com"))
                                    .setAdditionalTransactionData(
                                            new AdditionalTransactionData()
                                                    .setPrivacyPolicyUrl("https://unzer.com")
                                                    .setTermsAndConditionsUrl("https://unzer.com")
                                    );


                            // Authorize
                            Authorization createdAuth = unzer.authorize(authorization);

                            assertNotNull(createdAuth.getId());
                            performKlarnaAuthorization(createdAuth.getRedirectUrl().toString());

                            Authorization succeedAuth = unzer.fetchAuthorization(createdAuth.getPaymentId());
                            assertEquals(Authorization.Status.SUCCESS, succeedAuth.getStatus());

                            // Charge
                            Charge charge = unzer.chargeAuthorization(succeedAuth.getPaymentId());
                            assertEquals(Authorization.Status.SUCCESS, charge.getStatus());

                            // Cancel charge
                            if (tc.expectedErrors == null) {
                                Cancel cancel = unzer.cancelCharge(succeedAuth.getPaymentId(), tc.amount);
                                assertEquals(AbstractTransaction.Status.SUCCESS, cancel.getStatus());
                            } else {
                                PaymentException ex = assertThrows(PaymentException.class, () -> unzer.cancelAuthorization(succeedAuth.getPaymentId(), tc.amount));
                                assertEquals(sorted(tc.expectedErrors), sorted(ex.getPaymentErrorList()));
                            }
                        })
                )
                .collect(Collectors.toList());
    }

    private void performKlarnaAuthorization(String redirectUrl) {
        AbstractMap.SimpleEntry<WebDriver, WebDriverWait> pair = driverSetup();
        WebDriver driver = pair.getKey();
        WebDriverWait wait = pair.getValue();

        try {
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
        } finally {
            driver.close();
        }
    }

    private List<PaymentError> sorted(Collection<PaymentError> errors) {
        return errors.stream().sorted(Comparator.comparing(PaymentError::getCode)).collect(Collectors.toList());
    }
}

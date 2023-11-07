package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.paymenttypes.Klarna;
import com.unzer.payment.util.Integration;
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
    public void testCreatePaymentType() {
        Klarna klarna = getUnzer().createPaymentType(new Klarna());
        assertNotNull(klarna);
        assertNotNull(klarna.getId());
    }

    @Test
    public void testFetchPaymentType() {
        Klarna paymentType = getUnzer().createPaymentType(new Klarna());
        assertNotNull(paymentType.getId());

        Klarna fetched = (Klarna) getUnzer().fetchPaymentType(paymentType.getId());
        assertNotNull(fetched.getId());
    }

    @Integration
    @TestFactory
    public Collection<DynamicTest> testAuthorize() {
        class TestCase {
            final String name;
            final Customer customer;
            final Basket basket;
            final Authorization authorization;
            final Collection<PaymentError> expectedErrors;

            public TestCase(String name, Customer customer, Basket basket, Authorization authorization,
                            Collection<PaymentError> expectedErrors) {
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
                assertNotEquals(BaseTransaction.Status.ERROR, createdAuth.getStatus());
                assertNotNull(createdAuth.getPaymentId());
                assertFalse(createdAuth.getPaymentId().isEmpty());
                assertNotNull(createdAuth.getRedirectUrl());

            } else {
                PaymentException ex =
                        assertThrows(PaymentException.class, () -> unzer.authorize(tc.authorization));
                assertEquals(tc.expectedErrors.size(), ex.getPaymentErrorList().size());
                assertEquals(sorted(tc.expectedErrors), sorted(ex.getPaymentErrorList()));
            }
        })).collect(Collectors.toList());
    }

    private List<PaymentError> sorted(Collection<PaymentError> errors) {
        return errors.stream().sorted(Comparator.comparing(PaymentError::getCode))
                .collect(Collectors.toList());
    }
}

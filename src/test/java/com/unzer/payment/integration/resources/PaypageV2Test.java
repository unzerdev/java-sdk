package com.unzer.payment.integration.resources;

import com.unzer.payment.Basket;
import com.unzer.payment.Customer;
import com.unzer.payment.Metadata;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.BasketV2TestData;
import com.unzer.payment.business.Keys;
import com.unzer.payment.models.CardTransactionData;
import com.unzer.payment.models.paypage.*;
import com.unzer.payment.resources.PaypageV2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaypageV2Test extends AbstractPaymentTest {

    private Unzer unzer;

    @BeforeAll
    public void setUpBeforeAll() {
        // Setup single unzer instance for all class tests. -> reusing jwt token stored in unzer instance.
        unzer = new Unzer(Keys.DEFAULT);
    }

    @Test
    void minimumPaypageCreation() {
        PaypageV2 paypage = new PaypageV2(new BigDecimal("9.99"), "EUR", "charge");
        assertNull(paypage.getRedirectUrl());
        assertNull(paypage.getId());

        // when
        testPaypageCreation(paypage);
    }

    @Test
    void fetchPaypageById() {
        PaypageV2 paypage = new PaypageV2(new BigDecimal("9.99"), "EUR", "charge");
        assertNull(paypage.getPayments());
        assertNull(paypage.getTotal());

        // when
        PaypageV2 paypageResponse = testPaypageCreation(paypage);
        PaypageV2 fetchedPaypage = unzer.fetchPaypagePayments(paypageResponse.getId());

        // Paypage initially has no payments
        assertEquals(0, fetchedPaypage.getTotal());
        assertNotNull(fetchedPaypage.getPayments());
        assertArrayEquals(new PaypagePayment[0], fetchedPaypage.getPayments());
    }

    @Test
    void paypageCreationWitOptionalFields() {
        PaypageV2 paypage = new PaypageV2(new BigDecimal("9.99"), "EUR", "charge");
        paypage.setCheckoutType("full");
        paypage.setInvoiceId("invoiceId");
        paypage.setOrderId("orderId");
        paypage.setPaymentReference("paymentReference");
        paypage.setRecurrenceType("unscheduled");
        paypage.setShopName("shopName");
        paypage.setType("hosted");

        // when
        testPaypageCreation(paypage);
    }

    @Test
    void createPaypageWithUrls() {
        Urls urls = new Urls();
        urls.setTermsAndCondition("https://termsandcondition.com");
        urls.setPrivacyPolicy("https://privacypolicy.com");
        urls.setImprint("https://imprint.com");
        urls.setHelp("https://help.com");
        urls.setContact("https://contact.com");
        urls.setFavicon("https://favicon.com");
        urls.setReturnSuccess("https://returnsuccess.com");
        urls.setReturnPending("https://returnpending.com");
        urls.setReturnFailure("https://returnfailure.com");
        urls.setReturnCancel("https://returncancel.com");

        PaypageV2 paypage = new PaypageV2(new BigDecimal("9.99"), "EUR", "charge");
        paypage.setUrls(urls);

        // when
        testPaypageCreation(paypage);
    }

    @Test
    void createPaypageWithStyle() {
        Style style = new Style()
                .setFontFamily("comic sans")
                .setButtonColor("red")
                .setPrimaryTextColor("green")
                .setLinkColor("blue")
                .setBackgroundColor("black")
                .setCornerRadius("5px")
                .setShadows(true)
                .setBackgroundImage("https://backgroundimage.com")
                .setLogoImage("https://logoimage.com")
                .setFooterColor("yellow")
                .setHeaderColor("purple")
                .setHideUnzerLogo(true);

        PaypageV2 paypage = new PaypageV2(new BigDecimal("9.99"), "EUR", "charge");
        paypage.setStyle(style);
        // when
        testPaypageCreation(paypage);
    }

    @Test
    void createPaypageWithMethodResourceIds() {
        Customer customer = unzer.createCustomer(this.getMinimumCustomer());
        Basket basket = unzer.createBasket(BasketV2TestData.getMinTestBasketV2());
        Metadata metadata = unzer.createMetadata((new Metadata()).addMetadata("Shop-Type", "'unitTests'"));


        Resources resources = new Resources(customer.getId(), basket.getId(), metadata.getId());

        PaypageV2 paypage = new PaypageV2(new BigDecimal("500.5"), "EUR", "charge");
        paypage.setResources(resources);
        // when
        testPaypageCreation(paypage);
    }

    @Test
    void createPaypageWithRiskData() {
        Risk risk = new Risk();

        risk
                .setCustomerGroup("top")
                .setConfirmedAmount(9.99)
                .setConfirmedOrders(42)
                .setRegistrationLevel(1)
                .setRegistrationDate(new Date());

        PaypageV2 paypage = new PaypageV2(new BigDecimal("500.5"), "EUR", "charge");
        paypage.setRisk(risk);
        testPaypageCreation(paypage);
    }

    private PaypageV2 testPaypageCreation(PaypageV2 paypage) {
        // when
        PaypageV2 response = unzer.createPaypage(paypage);

        // then
        assertCreatedPaypage(response);
        return response;
    }

    @ParameterizedTest(name = "Paypage can be created with \"{0}\"")
    @MethodSource("getPaymentMethodsConfigs")
    void createPaypageWithMethodConfigs(String name, HashMap<String, PaymentMethodConfig> configs) {
        PaypageV2 paypage = new PaypageV2(new BigDecimal("9.99"), "EUR");
        paypage.setPaymentMethodsConfigs(configs);
        // when
        testPaypageCreation(paypage);
    }

    private void assertCreatedPaypage(PaypageV2 paypage) {
        String redirectUrl = paypage.getRedirectUrl();
        String id = paypage.getId();
        assertNotNull(redirectUrl);
        assertNotNull(id);
        assertTrue(redirectUrl.contains(id));
    }

    public static Stream<Arguments> getPaymentMethodsConfigs() {
        PaymentMethodConfig enabledConfig = new PaymentMethodConfig(true);
        PaymentMethodConfig disabledConfig = new PaymentMethodConfig(false);

        PaymentMethodConfig cardConfig = (new PaymentMethodConfig(true, 1))
                .setCredentialOnFile(true)
                .setExemption(CardTransactionData.ExemptionType.LVP.toString().toLowerCase());

        PaymentMethodConfig paylaterConfig = (new PaymentMethodConfig(true))
                .setLabel("Paylater");

        // Prepare Config Maps
        HashMap<String, PaymentMethodConfig> emptyConfig = new HashMap<>();

        HashMap<String, PaymentMethodConfig> defaultEnabled = new HashMap<>();
        defaultEnabled.put("default", enabledConfig);
        defaultEnabled.put("cards", disabledConfig);

        HashMap<String, PaymentMethodConfig> defaultDisabled = new HashMap<>();
        defaultDisabled.put("default", disabledConfig);
        defaultDisabled.put("cards", enabledConfig);

        HashMap<String, PaymentMethodConfig> withCardConfig = new HashMap<>();
        withCardConfig.put("default", disabledConfig);
        withCardConfig.put("cards", cardConfig);

        HashMap<String, PaymentMethodConfig> withPaylaterConfig = new HashMap<>();
        withPaylaterConfig.put("cards", paylaterConfig);

        HashMap<String, PaymentMethodConfig> withEnumMethodNames = new HashMap<>();
        withEnumMethodNames.put(PaypageV2.MethodName.CARDS.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYPAL.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYLATER_INSTALLMENT.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.GOOGLEPAY.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.APPLEPAY.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.KLARNA.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.SEPA_DIRECT_DEBIT.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.EPS.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYLATER_INVOICE.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYLATER_DIRECT_DEBIT.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PREPAYMENT.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYU.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.IDEAL.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PRZELEWY24.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.ALIPAY.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.WECHATPAY.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.BANCONTACT.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PFCARD.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PFEFINANCE.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.TWINT.value(), enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.DEFAULT.value(), enabledConfig);

        return Stream.of(
                Arguments.of("Empty", emptyConfig),
                Arguments.of("Default Enabled", defaultEnabled),
                Arguments.of("Default Disabled", defaultEnabled),
                Arguments.of("Method Configs", withCardConfig),
                Arguments.of("CardSpecificConfig", withCardConfig),
                Arguments.of("PaylaterConfig", withPaylaterConfig),
                Arguments.of("Method name enums", withEnumMethodNames)
        );
    }
}

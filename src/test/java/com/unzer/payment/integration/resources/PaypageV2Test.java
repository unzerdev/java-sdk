package com.unzer.payment.integration.resources;

import com.unzer.payment.Basket;
import com.unzer.payment.Customer;
import com.unzer.payment.Metadata;
import com.unzer.payment.business.BasketV2TestData;
import com.unzer.payment.models.CardTransactionData;
import com.unzer.payment.models.RiskData;
import com.unzer.payment.models.WeroEventDependentPayment;
import com.unzer.payment.models.paypage.PaymentMethodConfig;
import com.unzer.payment.models.paypage.PaypagePayment;
import com.unzer.payment.models.paypage.Resources;
import com.unzer.payment.models.paypage.Risk;
import com.unzer.payment.models.paypage.Style;
import com.unzer.payment.models.paypage.Urls;
import com.unzer.payment.resources.PaypageV2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaypageV2Test extends BearerAuthBaseTest {

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
        urls.setReturnSuccess("https://returnsuccess.com");
        urls.setReturnPending("https://returnpending.com");
        urls.setReturnFailure("https://returnfailure.com");
        urls.setReturnCancel("https://returncancel.com");
        urls.setSubscriptionAgreement("https://subscriptionagreement.com");

        PaypageV2 paypage = new PaypageV2(new BigDecimal("9.99"), "EUR", "charge");
        paypage.setUrls(urls);

        // when
        testPaypageCreation(paypage);
    }

    @Test
    void createPaypageWithStyle() {
        Style style = new Style()
                .setBackgroundColor("#000000")
                .setBackgroundImage("https://backgroundimage.com")
                .setBrandColor("#000000")
                .setCornerRadius("5px")
                .setFavicon("https://favicon.com")
                .setFont("comic sans")
                .setFooterColor("#000000")
                .setHeaderColor("#000")
                .setHideBasket(true)
                .setHideUnzerLogo(true)
                .setLinkColor("#000000")
                .setLogoImage("https://logoimage.com")
                .setShadows(true)
                .setBasketBackgroundColor("#ffffff")
                .setPaymentFormBackgroundColor("#0000ff")
                .setTextColor("#000000");

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
                .setCustomerGroup(RiskData.CustomerGroup.TOP.toString())
                .setConfirmedAmount(9.99)
                .setConfirmedOrders(42)
                .setRegistrationLevel(1)
                .setRegistrationDate(new Date());

        PaypageV2 paypage = new PaypageV2(new BigDecimal("500.5"), "EUR", "charge");
        paypage.setRisk(risk);
        testPaypageCreation(paypage);
    }

    @ParameterizedTest(name = "Paypage can be created with \"{0}\"")
    @MethodSource("getPaymentMethodsConfigs")
    void createPaypageWithMethodConfigs(String name, HashMap<String, PaymentMethodConfig> configs) {
        PaypageV2 paypage = new PaypageV2(new BigDecimal("9.99"), "EUR");
        paypage.setPaymentMethodsConfigs(configs);
        // when
        testPaypageCreation(paypage);
    }

    @Test
    void createPaypageWithZeroAmount() {
        PaypageV2 paypage = new PaypageV2(BigDecimal.ZERO, "EUR", "preauthorize");
        // when
        testPaypageCreation(paypage);
    }

    public static Stream<Arguments> getPaymentMethodsConfigs() {
        PaymentMethodConfig enabledConfig = new PaymentMethodConfig(true);
        PaymentMethodConfig disabledConfig = new PaymentMethodConfig(false);

        PaymentMethodConfig cardConfig = (new PaymentMethodConfig(true, 1))
                .setCredentialOnFile(true)
                .setExemption(CardTransactionData.ExemptionType.LVP.toString().toLowerCase());

        PaymentMethodConfig paylaterConfig = (new PaymentMethodConfig(true))
                .setLabel("Paylater");

        // Wero configurations with eventDependentPayment
        WeroEventDependentPayment weroEventDependent1 = new WeroEventDependentPayment()
                .setCaptureTrigger(WeroEventDependentPayment.CaptureTrigger.SERVICEFULFILMENT)
                .setAmountPaymentType(WeroEventDependentPayment.AmountPaymentType.PAY)
                .setMaxAuthToCaptureTime(600)
                .setMultiCapturesAllowed(true);

        WeroEventDependentPayment weroEventDependent2 = new WeroEventDependentPayment()
                .setCaptureTrigger(WeroEventDependentPayment.CaptureTrigger.DELIVERY)
                .setAmountPaymentType(WeroEventDependentPayment.AmountPaymentType.PAYUPTO)
                .setMaxAuthToCaptureTime(300)
                .setMultiCapturesAllowed(false);

        PaymentMethodConfig weroConfig1 = new PaymentMethodConfig(true, 2)
                .setEventDependentPayment(weroEventDependent1);

        PaymentMethodConfig weroConfig2 = new PaymentMethodConfig(true)
                .setEventDependentPayment(weroEventDependent2);

        // Prepare Config Maps
        HashMap<String, PaymentMethodConfig> emptyConfig = new HashMap<>();

        HashMap<String, PaymentMethodConfig> defaultEnabled = new HashMap<>();
        defaultEnabled.put("default", enabledConfig);
        defaultEnabled.put("cards", disabledConfig);

        HashMap<String, PaymentMethodConfig> withCardConfig = new HashMap<>();
        withCardConfig.put("default", disabledConfig);
        withCardConfig.put("cards", cardConfig);

        HashMap<String, PaymentMethodConfig> withPaylaterConfig = new HashMap<>();
        withPaylaterConfig.put("cards", paylaterConfig);

        HashMap<String, PaymentMethodConfig> withWeroConfig1 = new HashMap<>();
        withWeroConfig1.put("wero", weroConfig1);

        HashMap<String, PaymentMethodConfig> withWeroConfig2 = new HashMap<>();
        withWeroConfig2.put("wero", weroConfig2);

        HashMap<String, PaymentMethodConfig> withMixedWeroConfig = new HashMap<>();
        withMixedWeroConfig.put("default", disabledConfig);
        withMixedWeroConfig.put("cards", enabledConfig);
        withMixedWeroConfig.put("wero", weroConfig1);

        HashMap<PaypageV2.MethodName, PaymentMethodConfig> withEnumMethodNames = new HashMap<>();
        withEnumMethodNames.put(PaypageV2.MethodName.CARDS, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYPAL, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYLATER_INSTALLMENT, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.GOOGLEPAY, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.APPLEPAY, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.KLARNA, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.SEPA_DIRECT_DEBIT, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.EPS, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYLATER_INVOICE, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYLATER_DIRECT_DEBIT, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PREPAYMENT, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PAYU, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.IDEAL, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PRZELEWY24, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.ALIPAY, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.WECHATPAY, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.BANCONTACT, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PFCARD, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.PFEFINANCE, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.TWINT, enabledConfig);
        withEnumMethodNames.put(PaypageV2.MethodName.WERO, weroConfig1);
        withEnumMethodNames.put(PaypageV2.MethodName.DEFAULT, enabledConfig);

        return Stream.of(
                Arguments.of("Empty", emptyConfig),
                Arguments.of("Default Enabled", defaultEnabled),
                Arguments.of("Default Disabled", defaultEnabled),
                Arguments.of("Method Configs", withCardConfig),
                Arguments.of("CardSpecificConfig", withCardConfig),
                Arguments.of("PaylaterConfig", withPaylaterConfig),
                Arguments.of("WeroConfig with ServiceFulfilment", withWeroConfig1),
                Arguments.of("WeroConfig with Delivery", withWeroConfig2),
                Arguments.of("Mixed Config with Wero", withMixedWeroConfig),
                Arguments.of("Method name enums", withEnumMethodNames)
        );
    }
}

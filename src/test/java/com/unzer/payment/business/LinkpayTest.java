package com.unzer.payment.business;


import com.unzer.payment.BasePaypage;
import com.unzer.payment.Linkpay;
import com.unzer.payment.Unzer;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.stream.Stream;

import static com.unzer.payment.util.Types.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class LinkpayTest extends AbstractPaymentTest {
    @Test
    public void fetch_linkpay() {
        Unzer unzer = getUnzer();
        Linkpay newLinkpay = getMaximumLinkpay(null);
        Linkpay createdLinkpay = unzer.linkpay(newLinkpay);
        assertNotNull(createdLinkpay.getId());

        Linkpay fetchLinkpay = unzer.fetchLinkpay(createdLinkpay.getId());
        assertEquals(createdLinkpay.getId(), fetchLinkpay.getId());
    }

    @Test
    public void charge_MaximumLinkpay() {
        Unzer unzer = getUnzer();
        Linkpay request = getMaximumLinkpay(null);
        request = unzer.linkpay(request);
        Linkpay response = unzer.fetchLinkpay(request.getId());

        assertNull(response.getCard3ds());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getRedirectUrl());
        assertNull(response.getPaymentId());

        assertEquals(request.getCurrency(), response.getCurrency());
        assertEquals(request.getReturnUrl(), response.getReturnUrl());
        assertEquals(request.getShopName(), response.getShopName());
        assertEquals(request.getShopDescription(), response.getShopDescription());
        assertEquals(request.getTagline(), response.getTagline());
        assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
        assertEquals(request.getLogoImage(), response.getLogoImage());
        assertEquals(request.getFullPageImage(), response.getFullPageImage());
        assertEquals(request.getContactUrl().toString(), response.getContactUrl().toString());
        assertEquals(request.getHelpUrl().toString(), response.getHelpUrl().toString());
        assertEquals(request.getImprintUrl().toString(), response.getImprintUrl().toString());
        assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
        assertEquals(request.getInvoiceId(), response.getInvoiceId());
        assertEquals(request.getOrderId(), response.getOrderId());
        assertEquals(request.getBillingAddressRequired(), response.getBillingAddressRequired());
        assertEquals(request.getShippingAddressRequired(), response.getShippingAddressRequired());
        assertEquals(Arrays.toString(request.getExcludeTypes()), Arrays.toString(response.getExcludeTypes()));
        assertEquals(request.getOneTimeUse(), response.getOneTimeUse());
        assertEquals(request.getIntention(), response.getIntention());
        assertEquals(BasePaypage.Action.CHARGE, response.getAction());

        for (String key : response.getCss().keySet()) {
            assertEquals(request.getCss().get(key), response.getCss().get(key));
        }
    }

    @Test
    public void charge_Linkpay_WithEmptyCssMap() {
        Unzer unzer = getUnzer();

        Linkpay request = getMaximumLinkpay(null);
        request.setCss(null);

        request = unzer.linkpay(request);
        Linkpay response = unzer.fetchLinkpay(request.getId());

        assertNull(response.getCard3ds());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getRedirectUrl());
        assertNull(response.getPaymentId());

        assertEquals(request.getCurrency(), response.getCurrency());
        assertEquals(request.getReturnUrl(), response.getReturnUrl());
        assertEquals(request.getShopName(), response.getShopName());
        assertEquals(request.getShopDescription(), response.getShopDescription());
        assertEquals(request.getTagline(), response.getTagline());
        assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
        assertEquals(request.getLogoImage(), response.getLogoImage());
        assertEquals(request.getFullPageImage(), response.getFullPageImage());
        assertEquals(request.getContactUrl().toString(), response.getContactUrl().toString());
        assertEquals(request.getHelpUrl().toString(), response.getHelpUrl().toString());
        assertEquals(request.getImprintUrl().toString(), response.getImprintUrl().toString());
        assertEquals(request.getTermsAndConditionUrl().toString(), response.getTermsAndConditionUrl().toString());
        assertEquals(request.getPrivacyPolicyUrl().toString(), response.getPrivacyPolicyUrl().toString());
        assertEquals(request.getInvoiceId(), response.getInvoiceId());
        assertEquals(request.getOrderId(), response.getOrderId());
        assertEquals(request.getBillingAddressRequired(), response.getBillingAddressRequired());
        assertEquals(request.getShippingAddressRequired(), response.getShippingAddressRequired());
        assertEquals(Arrays.toString(request.getExcludeTypes()), Arrays.toString(response.getExcludeTypes()));
        assertEquals(BasePaypage.Action.CHARGE, response.getAction());
    }

    @TestFactory
    public Stream<DynamicTest> test_action() {
        class TestCase {
            final String name;
            final Linkpay page;
            final String expectedAction;

            public TestCase(String name, Linkpay page, String expectedAction) {
                this.name = name;
                this.page = page;
                this.expectedAction = expectedAction;
            }
        }

        return Stream.of(
                new TestCase(
                        "authorize",
                        getMaximumLinkpay(BasePaypage.Action.AUTHORIZE),
                        "AUTHORIZE"
                ),
                new TestCase(
                        "charge",
                        getMaximumLinkpay(BasePaypage.Action.CHARGE),
                        "CHARGE"
                ),
                new TestCase(
                        "default charge",
                        getMaximumLinkpay(null),
                        "CHARGE"
                )
        ).map(tc -> dynamicTest(tc.name, () -> {
            Unzer unzer = getUnzer();
            Linkpay createdPage = unzer.linkpay(tc.page);
            Linkpay fetchedPage = unzer.fetchLinkpay(createdPage.getId());
            assertEquals(tc.expectedAction, fetchedPage.getAction());
        }));
    }

    private Linkpay getMaximumLinkpay(String action) {
        Linkpay page = new Linkpay();
        String[] excludeTypes = {"paypal"};
        page.setExcludeTypes(excludeTypes);
        page.setAmount(BigDecimal.ONE);
        page.setCurrency(Currency.getInstance("EUR"));
        page.setReturnUrl(unsafeUrl("https://unzer.com"));
        page.setShopName("Unzer Demo Shop");
        page.setShopDescription("Unzer Demo Shop Description");
        page.setTagline("Unzer Tagline");
        page.setTermsAndConditionUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));
        page.setPrivacyPolicyUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));
        page.setCss(getCssMap());
        page.setAction(action);

        page.setLogoImage("https://docs.unzer.com/payment-nutshell/payment-in-nutshell.png");
        page.setFullPageImage("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero");

        page.setContactUrl(unsafeUrl("mailto:support@unzer.com"));
        page.setHelpUrl(unsafeUrl("https://www.unzer.com/en/support/"));
        page.setImprintUrl(unsafeUrl("https://www.unzer.com/en/impressum/"));
        page.setPrivacyPolicyUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));
        page.setTermsAndConditionUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));

        page.setInvoiceId(generateUuid());
        page.setOrderId(generateUuid());
        return page;
    }
}

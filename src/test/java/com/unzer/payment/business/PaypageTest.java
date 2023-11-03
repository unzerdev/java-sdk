package com.unzer.payment.business;


import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.unzer.payment.BasePaypage;
import com.unzer.payment.Paypage;
import com.unzer.payment.Unzer;
import com.unzer.payment.service.UrlUtil;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Currency;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

public class PaypageTest extends AbstractPaymentTest {

  @Test
  public void testMaximumPaypage() {
    Unzer unzer = getUnzer();

    Paypage request = getMaximumPaypage(null);
    Paypage createdPaypage = unzer.paypage(request);

    Paypage fetchedPaypage = unzer.fetchPaypage(createdPaypage.getId());

    assertNull(fetchedPaypage.getCard3ds());
    assertNotNull(fetchedPaypage);
    assertNotNull(fetchedPaypage.getId());
    assertNotNull(fetchedPaypage.getRedirectUrl());
    assertNotNull(fetchedPaypage.getPaymentId());

    assertEquals(request.getCurrency(), fetchedPaypage.getCurrency());
    assertEquals(request.getReturnUrl(), fetchedPaypage.getReturnUrl());
    assertEquals(request.getShopName(), fetchedPaypage.getShopName());
    assertEquals(request.getShopDescription(), fetchedPaypage.getShopDescription());
    assertEquals(request.getTagline(), fetchedPaypage.getTagline());
    assertEquals(request.getTermsAndConditionUrl().toString(),
        fetchedPaypage.getTermsAndConditionUrl().toString());
    assertEquals(request.getPrivacyPolicyUrl().toString(),
        fetchedPaypage.getPrivacyPolicyUrl().toString());
    assertEquals(request.getLogoImage(), fetchedPaypage.getLogoImage());
    assertEquals(request.getFullPageImage(), fetchedPaypage.getFullPageImage());
    assertEquals(request.getContactUrl().toString(), fetchedPaypage.getContactUrl().toString());
    assertEquals(request.getHelpUrl().toString(), fetchedPaypage.getHelpUrl().toString());
    assertEquals(request.getImprintUrl().toString(), fetchedPaypage.getImprintUrl().toString());
    assertEquals(request.getTermsAndConditionUrl().toString(),
        fetchedPaypage.getTermsAndConditionUrl().toString());
    assertEquals(request.getPrivacyPolicyUrl().toString(),
        fetchedPaypage.getPrivacyPolicyUrl().toString());
    assertEquals(request.getInvoiceId(), fetchedPaypage.getInvoiceId());
    assertEquals(request.getOrderId(), fetchedPaypage.getOrderId());
    assertEquals(request.getBillingAddressRequired(), fetchedPaypage.getBillingAddressRequired());
    assertEquals(request.getShippingAddressRequired(), fetchedPaypage.getShippingAddressRequired());
    assertEquals(Arrays.toString(request.getExcludeTypes()),
        Arrays.toString(fetchedPaypage.getExcludeTypes()));
    assertEquals("charge", fetchedPaypage.getAction().toLowerCase());

    for (String key : fetchedPaypage.getCss().keySet()) {
      assertEquals(request.getCss().get(key), fetchedPaypage.getCss().get(key));
    }
  }

  @Test
  public void testPaypage_WithEmptyCssMap() {
    Unzer unzer = getUnzer();

    Paypage request = getMaximumPaypage(null);
    request.setCss(null);

    Paypage createdPaypage = unzer.paypage(request);

    Paypage fetchedPaypage = unzer.fetchPaypage(createdPaypage.getId());

    assertNull(fetchedPaypage.getCard3ds());

    assertNotNull(fetchedPaypage);
    assertNotNull(fetchedPaypage.getId());
    assertNotNull(fetchedPaypage.getRedirectUrl());
    assertNotNull(fetchedPaypage.getPaymentId());

    assertEquals(request.getCurrency(), fetchedPaypage.getCurrency());
    assertEquals(request.getReturnUrl(), fetchedPaypage.getReturnUrl());
    assertEquals(request.getShopName(), fetchedPaypage.getShopName());
    assertEquals(request.getShopDescription(), fetchedPaypage.getShopDescription());
    assertEquals(request.getTagline(), fetchedPaypage.getTagline());
    assertEquals(request.getTermsAndConditionUrl().toString(),
        fetchedPaypage.getTermsAndConditionUrl().toString());
    assertEquals(request.getPrivacyPolicyUrl().toString(),
        fetchedPaypage.getPrivacyPolicyUrl().toString());
    assertEquals(request.getLogoImage(), fetchedPaypage.getLogoImage());
    assertEquals(request.getFullPageImage(), fetchedPaypage.getFullPageImage());
    assertEquals(request.getContactUrl().toString(), fetchedPaypage.getContactUrl().toString());
    assertEquals(request.getHelpUrl().toString(), fetchedPaypage.getHelpUrl().toString());
    assertEquals(request.getImprintUrl().toString(), fetchedPaypage.getImprintUrl().toString());
    assertEquals(request.getTermsAndConditionUrl().toString(),
        fetchedPaypage.getTermsAndConditionUrl().toString());
    assertEquals(request.getPrivacyPolicyUrl().toString(),
        fetchedPaypage.getPrivacyPolicyUrl().toString());
    assertEquals(request.getInvoiceId(), fetchedPaypage.getInvoiceId());
    assertEquals(request.getOrderId(), fetchedPaypage.getOrderId());
    assertEquals(request.getBillingAddressRequired(), fetchedPaypage.getBillingAddressRequired());
    assertEquals(request.getShippingAddressRequired(), fetchedPaypage.getShippingAddressRequired());
    assertEquals(Arrays.toString(request.getExcludeTypes()),
        Arrays.toString(fetchedPaypage.getExcludeTypes()));
    assertEquals("charge", fetchedPaypage.getAction().toLowerCase());
  }

  @TestFactory
  public Collection<DynamicTest> testRestUrl() {
    UrlUtil urlUtil = new UrlUtil("any-key");
    class TestCase {
      final String name;
      final String action;
      final String expectedUrlPart;

      public TestCase(String name, String action, String expectedUrlPart) {
        this.name = name;
        this.action = action;
        this.expectedUrlPart = expectedUrlPart;
      }
    }

    return Stream.of(
        new TestCase(
            "charge",
            BasePaypage.Action.CHARGE,
            "paypage/charge/"
        ),
        new TestCase(
            "authorize",
            BasePaypage.Action.AUTHORIZE,
            "paypage/authorize/"
        )
    ).map(t -> dynamicTest(t.name, () -> {
      Paypage paypage = getMaximumPaypage(t.action);

      String actualUrl = urlUtil.getUrl(paypage);
      String expectedUrl = urlUtil.getRestUrl() + t.expectedUrlPart;

      assertEquals(expectedUrl, actualUrl);
    })).collect(Collectors.toList());
  }

  @TestFactory
  public Stream<DynamicTest> test_action() {
    class TestCase {
        final String name;
        final Paypage paypage;
        final String expectedAction;

        public TestCase(String name, Paypage paypage, String expectedAction) {
            this.name = name;
            this.paypage = paypage;
            this.expectedAction = expectedAction;
        }
    }

    return Stream.of(
        new TestCase(
            "authorize",
            getMaximumPaypage(BasePaypage.Action.AUTHORIZE),
            "AUTHORIZE"
        ),
        new TestCase(
            "charge",
            getMaximumPaypage(BasePaypage.Action.CHARGE),
            "CHARGE"
        ),
        new TestCase(
            "default charge",
            getMaximumPaypage(null),
            "CHARGE"
        )
    ).map(tc -> dynamicTest(tc.name, () -> {
      Unzer unzer = getUnzer();
      Paypage createdPaypage = unzer.paypage(tc.paypage);

      Paypage fetchedPaypage = unzer.fetchPaypage(createdPaypage.getId());

      assertEquals(tc.expectedAction, fetchedPaypage.getAction());
    }));
  }

  private Paypage getMaximumPaypage(String action) {
    Paypage paypage = new Paypage();
    String[] excludeTypes = {"paypal"};
    paypage.setExcludeTypes(excludeTypes);
    paypage.setAmount(BigDecimal.ONE);
    paypage.setCurrency(Currency.getInstance("EUR"));
    paypage.setReturnUrl(unsafeUrl("https://www.unzer.com/"));
    paypage.setShopName("Unzer Demo Shop");
    paypage.setShopDescription("Unzer Demo Shop Description");
    paypage.setTagline("Unzer Tagline");
    paypage.setTermsAndConditionUrl(unsafeUrl("https://www.unzer.com/en/privacy-statement/"));
    paypage.setPrivacyPolicyUrl(unsafeUrl("https://www.unzer.com/en/privacy-statement/"));
    paypage.setCss(getCssMap());
    paypage.setAction(action);

    paypage.setLogoImage("https://docs.unzer.com/payment-nutshell/payment-in-nutshell.png");
    paypage.setFullPageImage("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-12-pro-family-hero");

    paypage.setContactUrl(unsafeUrl("mailto:support@unzer.com"));
    paypage.setHelpUrl(unsafeUrl("https://www.unzer.com/en/support/"));
    paypage.setImprintUrl(unsafeUrl("https://www.unzer.com/en/impressum/"));
    paypage.setPrivacyPolicyUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));
    paypage.setTermsAndConditionUrl(unsafeUrl("https://www.unzer.com/en/datenschutz/"));

    paypage.setInvoiceId(generateUuid());
    paypage.setOrderId(generateUuid());
    return paypage;
  }
}

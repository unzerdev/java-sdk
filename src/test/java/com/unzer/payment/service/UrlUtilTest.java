package com.unzer.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.paymenttypes.BasePaymentType;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import com.unzer.payment.paymenttypes.Klarna;
import com.unzer.payment.paymenttypes.PaymentType;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;


public class UrlUtilTest extends AbstractPaymentTest {
  private static final String sbxTypesUrl = "https://sbx-api.unzer.com/v1/types/";

  @TestFactory
  public Collection<DynamicTest> testGetApiEndpoint() {
    class KeyTestCase {
      final String name;
      final String key;
      final String expectedEndpoint;

      public KeyTestCase(String name, String key, String expectedEndpoint) {
        this.name = name;
        this.key = key;
        this.expectedEndpoint = expectedEndpoint;
      }
    }

    return Stream.of(
            new KeyTestCase(
                "whenProductionKey_returnsProductionEndpoint",
                "p-random-key",
                "https://api.unzer.com/v1/types/klarna/"
            ),
            new KeyTestCase(
                "whenSbxKey_returnsSbxEndpoint",
                "s-random-key",
                "https://sbx-api.unzer.com/v1/types/klarna/"
            )
        ).map(tc -> dynamicTest(tc.name,
            () -> assertEquals(tc.expectedEndpoint, new UrlUtil(tc.key).getUrl(new Klarna()))))
        .collect(Collectors.toList());
  }

  @TestFactory
  public Collection<DynamicTest> testGetUrlForPaymentTypeInvoiceSecured() {
    class TestCase {
      final String name;
      final BasePaymentType type;
      final String expectedUrl;

      public TestCase(String name, BasePaymentType type, String expectedUrl) {
        this.name = name;
        this.type = type;
        this.expectedUrl = expectedUrl;
      }
    }

    return Stream.of(
        new TestCase("whenInvoiceSecured_returnsInvoiceSecuredUrl",
            new InvoiceSecured(),
            sbxTypesUrl + "invoice-secured/"),
        new TestCase("whenInstallmentSecured_returnsInstallmentSecuredUrl",
            new InstallmentSecuredRatePlan(),
            sbxTypesUrl + "installment-secured/"),
        new TestCase("whenSepaDirectDebitSecured_returnsSepaDirectDebitSecuredUrl",
            new SepaDirectDebitSecured(""),
            sbxTypesUrl + "sepa-direct-debit-secured/")
    ).map(tc -> dynamicTest(tc.name, () -> {
      assertEquals(tc.expectedUrl, new UrlUtil(Keys.KEY_WITHOUT_3DS).getUrl(tc.type));
    })).collect(Collectors.toList());
  }
}

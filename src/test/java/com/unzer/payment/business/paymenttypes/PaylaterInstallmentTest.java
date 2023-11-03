package com.unzer.payment.business.paymenttypes;


import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.PaylaterInstallmentPlans;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.json.paylater.ApiInstallmentPlan;
import com.unzer.payment.communication.json.paylater.ApiInstallmentPlanRate;
import com.unzer.payment.communication.json.paylater.ApiInstallmentPlans;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.InstallmentPlanRate;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.PaylaterInstallment;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

@WireMockTest(httpPort = 8080)
class PaylaterInstallmentTest {
  @Test
  void test_installment_plans() {
    Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
    String jsonBody = getPlansresponse("fetch-installment-plans.json");
    stubFor(
        get("/v1/types/paylater-installment/plans?amount=99.99&currency=EUR&country=DE&customerType=B2C").willReturn(
            jsonResponse(jsonBody, 200)));

    BigDecimal amount = new BigDecimal("99.99");
    InstallmentPlansRequest request =
        new InstallmentPlansRequest(amount, "EUR", "DE", CustomerType.B2C);

    ApiInstallmentPlans apiResponse =
        new JsonParser().fromJson(jsonBody, ApiInstallmentPlans.class);

    PaylaterInstallmentPlans installmentPlans = unzer.fetchPaylaterInstallmentPlans(request);

    assertEquals("Tx-xyz", installmentPlans.getId());
    assertEquals(Currency.getInstance("EUR"), installmentPlans.getCurrency());
    assertEquals(new BigDecimal("99.99").setScale(4), installmentPlans.getAmount());
    assertEquals(AbstractTransaction.Status.valueOf("SUCCESS"), installmentPlans.getStatus());

    List<InstallmentPlan> plans = installmentPlans.getPlans();
    assertEquals(5, plans.size());
    assertNotNull(plans);
    assertPlans(apiResponse.getPlans(), plans);
  }

  @Test
  void test_installment_type_creation() {
    Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
    String jsonBody = getPlansresponse("create-paylater-installment-type.json");
    stubFor(
        post("/v1/types/paylater-installment/").willReturn(
            jsonResponse(jsonBody, 200)));
    stubFor(
        get("/v1/types/s-pit-vs6pjunvuick").willReturn(
            jsonResponse(jsonBody, 200)));

    PaylaterInstallment paylaterInstallment = unzer.createPaymentType(new PaylaterInstallment()
        .setInquiryId("tx-xyz")
        .setHolder("max mustermann")
        .setIban("Iban")
        .setNumberOfRates(3));

    assertNull(paylaterInstallment.getHolder());
    assertNull(paylaterInstallment.getIban());
    assertNull(paylaterInstallment.getIban());

    assertEquals("s-pit-vs6pjunvuick", paylaterInstallment.getId());
    assertEquals(false, paylaterInstallment.getRecurring());
    assertEquals("123.123.123.123", paylaterInstallment.getGeoLocation().getClientIp());
    assertEquals("US", paylaterInstallment.getGeoLocation().getCountryIsoA2());

  }

  private void assertPlans(List<ApiInstallmentPlan> jsonPlans, List<InstallmentPlan> sdkPlans) {
    AtomicInteger indices = new AtomicInteger();

    jsonPlans.forEach((apiInstallmentPlan) -> {
      InstallmentPlan installmentPlan = sdkPlans.get(indices.getAndIncrement());
      assertEquals(apiInstallmentPlan.getTotalAmount(), installmentPlan.getTotalAmount());
      assertEquals(apiInstallmentPlan.getNumberOfRates(), installmentPlan.getNumberOfRates());
      assertEquals(apiInstallmentPlan.getNominalInterestRate(),
          installmentPlan.getNominalInterestRate());
      assertEquals(apiInstallmentPlan.getEffectiveInterestRate(),
          installmentPlan.getEffectiveInterestRate());
      assertEquals(apiInstallmentPlan.getSecciUrl(), installmentPlan.getSecciUrl());

      List<InstallmentPlanRate> installmentRates = installmentPlan.getInstallmentRates();
      assertNotNull(installmentRates);
      assertEquals(apiInstallmentPlan.getNumberOfRates(), installmentRates.size());
      assertRates(apiInstallmentPlan.getInstallmentRates(), installmentRates);
    });
  }

  private void assertRates(List<ApiInstallmentPlanRate> apiInstallmentRates,
                           List<InstallmentPlanRate> installmentRates) {
    AtomicInteger indices = new AtomicInteger();

    apiInstallmentRates.forEach((apiInstallmentRate) -> {
      InstallmentPlanRate sdkInstallmentRate = installmentRates.get(indices.getAndIncrement());

      assertEquals(apiInstallmentRate.getRate(), sdkInstallmentRate.getRate());
      assertEquals(apiInstallmentRate.getDate(), sdkInstallmentRate.getDate());

    });
  }

  private static String getPlansresponse(String response) {
    return new Scanner(Objects.requireNonNull(
        PaylaterInstallmentTest.class.getResourceAsStream(
            "/api-response/paylaterInstallment/" + response))).useDelimiter("\\A")
        .next();
  }
}
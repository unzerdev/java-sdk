package com.unzer.payment.integration.paymenttypes;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Authorization;
import com.unzer.payment.Basket;
import com.unzer.payment.BasketItem;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.PaylaterInstallmentPlans;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.PaylaterInstallment;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;


public class PaylaterInstallmentTest extends AbstractPaymentTest {

  @Test
  public void testRateRetrievalUrl() {
    BigDecimal amount = new BigDecimal("33.33");
    InstallmentPlansRequest request =
        new InstallmentPlansRequest(amount, "EUR", "DE", CustomerType.B2C);

    String uri = request.getRequestUrl();
    assertEquals(
        "/v1/types/paylater-installment/plans?amount=33.33&currency=EUR&country=DE&customerType=B2C",
        uri);
  }

  @Test
  public void testFetchInstallmentPlans() {
    BigDecimal amount = new BigDecimal("99.99");
    InstallmentPlansRequest request =
        new InstallmentPlansRequest(amount, "EUR", "DE", CustomerType.B2C);

    PaylaterInstallmentPlans installmentPlans = getUnzer().fetchPaylaterInstallmentPlans(request);
    assertNotEquals("", installmentPlans.getId());
    assertNumberEquals(new BigDecimal("99.99"), installmentPlans.getAmount());
    assertEquals(Currency.getInstance("EUR"), installmentPlans.getCurrency());
    assertEquals(AbstractTransaction.Status.SUCCESS, installmentPlans.getStatus());

    List<InstallmentPlan> plans = installmentPlans.getPlans();

    assertNotNull(plans);
    assertTrue(plans.size() > 0);
    assertPlans(plans);
  }

  @Test
  public void testCreatePaylaterInstallmentTypeWithAllParameter()
      throws HttpCommunicationException {
    PaylaterInstallmentPlans installmentPlans = getPaylaterInstallmentPlans();
    InstallmentPlan selectedPlan = installmentPlans.getPlans().get(0);

    // when
    PaylaterInstallment paylaterInstallment = getUnzer().createPaymentType(
        new PaylaterInstallment()
            .setInquiryId(installmentPlans.getId())
            .setNumberOfRates(selectedPlan.getNumberOfRates())
            .setHolder("Max Mustermann")
            .setIban("DE89370400440532013000")
    );

    // then
    assertNotNull(paylaterInstallment.getId());
    assertNotNull(paylaterInstallment.getGeoLocation());
    assertFalse(paylaterInstallment.getRecurring());

    // API Response doesn't contain request fields.
    assertNull(paylaterInstallment.getInquiryId());
    assertNull(paylaterInstallment.getNumberOfRates());
    assertNull(paylaterInstallment.getCountry());
    assertNull(paylaterInstallment.getHolder());
  }

  @Test
  public void testAuthorize() throws HttpCommunicationException, ParseException {
    PaylaterInstallmentPlans installmentPlans = getPaylaterInstallmentPlans();
    InstallmentPlan selectedPlan = installmentPlans.getPlans().get(0);

    // when
    PaylaterInstallment paylaterInstallment = getUnzer().createPaymentType(
        new PaylaterInstallment()
            .setInquiryId(installmentPlans.getId())
            .setNumberOfRates(selectedPlan.getNumberOfRates())
            .setHolder("Max Mustermann")
            .setIban("DE89370400440532013000")
    );

    Basket basket = getUnzer().createBasket(getBasket());
    Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));

    Authorization authorization = getUnzer().authorize(
        getAuthorization(paylaterInstallment.getId(),
            customer.getId(),
            basket.getId())
    );

    // then
    assertEquals(AbstractTransaction.Status.SUCCESS, authorization.getStatus());
    assertNumberEquals(getOrderAmount(), authorization.getAmount());
  }

  @Test
  public void testFullCharge() throws HttpCommunicationException, ParseException {
    PaylaterInstallmentPlans installmentPlans = getPaylaterInstallmentPlans();
    InstallmentPlan selectedPlan = installmentPlans.getPlans().get(0);

    PaylaterInstallment paylaterInstallment = getUnzer().createPaymentType(
        new PaylaterInstallment()
            .setInquiryId(installmentPlans.getId())
            .setNumberOfRates(selectedPlan.getNumberOfRates())
            .setHolder("Max Mustermann")
            .setIban("DE89370400440532013000")
    );

    Basket basket = getUnzer().createBasket(getBasket());
    Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));

    Authorization authorization = getUnzer().authorize(
        getAuthorization(paylaterInstallment.getId(),
            customer.getId(),
            basket.getId())
    );

    // when
    Charge charge = getUnzer().chargeAuthorization(authorization.getPaymentId());

    // then
    assertEquals(AbstractTransaction.Status.SUCCESS, charge.getStatus());
    assertNumberEquals(getOrderAmount(), charge.getAmount());
  }

  @Test
  public void testPartialCharge() throws HttpCommunicationException, ParseException {
    PaylaterInstallmentPlans installmentPlans = getPaylaterInstallmentPlans();
    InstallmentPlan selectedPlan = installmentPlans.getPlans().get(0);

    PaylaterInstallment paylaterInstallment = getUnzer().createPaymentType(
        new PaylaterInstallment()
            .setInquiryId(installmentPlans.getId())
            .setNumberOfRates(selectedPlan.getNumberOfRates())
            .setHolder("Max Mustermann")
            .setIban("DE89370400440532013000")
    );

    Basket basket = getUnzer().createBasket(getBasket());
    Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));

    Authorization authorization = getUnzer().authorize(
        getAuthorization(paylaterInstallment.getId(),
            customer.getId(),
            basket.getId())
    );

    // when
    BigDecimal partialChargeAmount = new BigDecimal("66.66");
    Charge charge = getUnzer().chargeAuthorization(
        authorization.getPaymentId(),
        partialChargeAmount
    );

    // then
    assertEquals(AbstractTransaction.Status.SUCCESS, charge.getStatus());
    assertNumberEquals(partialChargeAmount, charge.getAmount());
  }


  @Test
  public void testFullCancelAfterAuthorization() throws HttpCommunicationException, ParseException {
    PaylaterInstallmentPlans installmentPlans = getPaylaterInstallmentPlans();
    InstallmentPlan selectedPlan = installmentPlans.getPlans().get(0);

    // when
    PaylaterInstallment paylaterInstallment = getUnzer().createPaymentType(
        new PaylaterInstallment()
            .setInquiryId(installmentPlans.getId())
            .setNumberOfRates(selectedPlan.getNumberOfRates())
            .setHolder("Max Mustermann")
            .setIban("DE89370400440532013000")
    );

    Basket basket = getUnzer().createBasket(getBasket());
    Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));

    Authorization authorization = getUnzer().authorize(
        getAuthorization(paylaterInstallment.getId(),
            customer.getId(),
            basket.getId())
    );

    // when
    Cancel cancel = getUnzer().cancelAuthorization(authorization.getPaymentId(), getOrderAmount());

    // then
    assertEquals(AbstractTransaction.Status.SUCCESS, authorization.getStatus());
    assertNumberEquals(getOrderAmount(), authorization.getAmount());
  }

  @Test
  public void testFullCancelAfterCharge() throws HttpCommunicationException, ParseException {
    PaylaterInstallmentPlans installmentPlans = getPaylaterInstallmentPlans();
    InstallmentPlan selectedPlan = installmentPlans.getPlans().get(0);

    // when
    PaylaterInstallment paylaterInstallment = getUnzer().createPaymentType(
        new PaylaterInstallment()
            .setInquiryId(installmentPlans.getId())
            .setNumberOfRates(selectedPlan.getNumberOfRates())
            .setHolder("Max Mustermann")
            .setIban("DE89370400440532013000")
    );

    Basket basket = getUnzer().createBasket(getBasket());
    Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));

    Authorization authorization = getUnzer().authorize(
        getAuthorization(paylaterInstallment.getId(),
            customer.getId(),
            basket.getId())
    );

    Charge charge = getUnzer().chargeAuthorization(authorization.getPaymentId());

    // when
    Cancel cancel = getUnzer().cancelCharge(charge.getPaymentId(), getOrderAmount());

    // then
    assertEquals(AbstractTransaction.Status.SUCCESS, cancel.getStatus());
    assertNumberEquals(getOrderAmount(), cancel.getAmount());
  }

  @Test
  public void testPartialCancelAfterCharge() throws HttpCommunicationException, ParseException {
    PaylaterInstallmentPlans installmentPlans = getPaylaterInstallmentPlans();
    InstallmentPlan selectedPlan = installmentPlans.getPlans().get(0);

    // when
    PaylaterInstallment paylaterInstallment = getUnzer().createPaymentType(
        new PaylaterInstallment()
            .setInquiryId(installmentPlans.getId())
            .setNumberOfRates(selectedPlan.getNumberOfRates())
            .setHolder("Max Mustermann")
            .setIban("DE89370400440532013000")
    );

    Basket basket = getUnzer().createBasket(getBasket());
    Customer customer = getUnzer().createCustomer(getMaximumCustomer(generateUuid()));

    Authorization authorization = getUnzer().authorize(
        getAuthorization(paylaterInstallment.getId(),
            customer.getId(),
            basket.getId())
    );

    Charge charge = getUnzer().chargeAuthorization(authorization.getPaymentId());

    // when
    BigDecimal partialCancelAmount = new BigDecimal("66.66");
    Cancel cancel = getUnzer().cancelCharge(charge.getPaymentId(), partialCancelAmount);

    // then
    assertEquals(AbstractTransaction.Status.SUCCESS, cancel.getStatus());
    assertNumberEquals(partialCancelAmount, cancel.getAmount());
  }

  private void assertPlans(List<InstallmentPlan> plans) {
    plans.forEach((plan) -> {
      assertNotNull(plan.getTotalAmount());
      assertNotNull(plan.getNumberOfRates());
      assertNotNull(plan.getNominalInterestRate());
      assertNotNull(plan.getEffectiveInterestRate());
      assertNotNull(plan.getInstallmentRates());
      assertNotNull(plan.getSecciUrl());
      assertEquals(plan.getNumberOfRates(), plan.getInstallmentRates().size());
    });
  }

  private Basket getBasket() {
    return new Basket()
        .setTotalValueGross(getOrderAmount())
        .setCurrencyCode(Currency.getInstance("EUR"))
        .addBasketItem(
            new BasketItem()
                .setBasketItemReferenceId("Artikelnummer4711")
                .setQuantity(1)
                .setVat(BigDecimal.ZERO)
                .setAmountDiscountPerUnitGross(BigDecimal.ZERO)
                .setAmountPerUnitGross(getOrderAmount())
                .setTitle("Some Title")
        );
  }

  protected Authorization getAuthorization(String typeId, String customerId, String basketId) {
    Authorization authorization = new Authorization();
    authorization
        .setAmount(getOrderAmount())
        .setCurrency(Currency.getInstance("EUR"))
        .setTypeId(typeId)
        .setReturnUrl(unsafeUrl("https://www.unzer.com"))
        .setCustomerId(customerId)
        .setBasketId(basketId);
    return authorization;
  }

  private BigDecimal getOrderAmount() {
    BigDecimal amount = new BigDecimal("99.9900");
    return amount.setScale(2, RoundingMode.HALF_UP);
  }

  private PaylaterInstallmentPlans getPaylaterInstallmentPlans()
      throws HttpCommunicationException {
    return getUnzer().fetchPaylaterInstallmentPlans(
        new InstallmentPlansRequest(getOrderAmount(), "EUR", "DE", CustomerType.B2C)
    );
  }
}

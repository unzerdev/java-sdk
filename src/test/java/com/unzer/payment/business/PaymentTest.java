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

package com.unzer.payment.business;


import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Authorization;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.Payment;
import com.unzer.payment.PaymentException;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Card;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class PaymentTest extends AbstractPaymentTest {

  @Test
  public void testFetchPaymentWithAuthorization() throws HttpCommunicationException {
    Authorization authorize = getUnzer().authorize(
        getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId()));
    Payment payment = getUnzer().fetchPayment(authorize.getPayment().getId());
    assertNotNull(payment);
    assertNotNull(payment.getId());
    assertNotNull(payment.getAuthorization());
    assertNotNull(payment.getAuthorization().getId());
    assertNotNull(payment.getPaymentState());
  }

  @Test
  public void testFullChargeAfterAuthorize() throws HttpCommunicationException {
    Authorization authorize = getUnzer().authorize(
        getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
    Payment payment = getUnzer().fetchPayment(authorize.getPayment().getId());
    Charge charge = payment.charge();
    assertNotNull(charge);
    assertNotNull(charge.getId());
  }

  @Test
  public void testFetchPaymentWithCharges() throws HttpCommunicationException {
    Authorization authorize = getUnzer().authorize(
        getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
    Payment payment = getUnzer().fetchPayment(authorize.getPayment().getId());
    assertNotNull(payment);
    assertNotNull(payment.getId());
    assertNotNull(payment.getAuthorization());
    assertNotNull(payment.getAuthorization().getId());
    Charge charge = payment.charge();
    payment = charge.getPayment();
    assertNotNull(payment.getChargesList());
    assertEquals(1, payment.getChargesList().size());
    assertEquals(charge.getAmount(), payment.getCharge(0).getAmount());
    assertEquals(charge.getCurrency(), payment.getCharge(0).getCurrency());
    assertEquals(charge.getId(), payment.getCharge(0).getId());
    assertEquals(charge.getReturnUrl(), payment.getCharge(0).getReturnUrl());

  }

  @Test
  public void testPartialChargeAfterAuthorize() throws HttpCommunicationException {
    Authorization authorize = getUnzer().authorize(
        getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
    Payment payment = getUnzer().fetchPayment(authorize.getPayment().getId());
    Charge charge = payment.charge(BigDecimal.ONE);
    assertNotNull(charge);
    assertEquals("s-chg-1", charge.getId());
    assertEquals(getBigDecimal("1.0000"), charge.getAmount());
  }

  @Test
  public void testFullCancelAuthorize() throws HttpCommunicationException {
    Unzer unzer = getUnzer(Keys.KEY_WITHOUT_3DS);

    Card card = createPaymentTypeCard(unzer, "4711100000000000");
    Authorization authorize = unzer.authorize(getAuthorization(card.getId()));
    assertEquals(AbstractTransaction.Status.SUCCESS, authorize.getStatus());

    Payment payment = unzer.fetchPayment(authorize.getPayment().getId());

    Cancel cancel = payment.getAuthorization().cancel();
    assertNotNull(cancel);
    assertEquals("s-cnl-1", cancel.getId());
    assertEquals(authorize.getAmount(), cancel.getAmount());
    assertThrows(PaymentException.class, payment::cancel);
  }

  @Test
  public void testPartialCancelAuthorize() throws HttpCommunicationException {
    Authorization authorize = getUnzer().authorize(
        getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
    Payment payment = getUnzer().fetchPayment(authorize.getPayment().getId());
    Cancel cancel = payment.cancel(BigDecimal.ONE);
    assertNotNull(cancel);
    assertEquals("s-cnl-1", cancel.getId());
    assertEquals(getBigDecimal("1.0000"), cancel.getAmount());
  }

  @Test
  public void testFullCancelOnCharge() throws HttpCommunicationException {
    Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"),
        createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
        unsafeUrl("https://www.unzer.com"), false);
    Payment payment = getUnzer().fetchPayment(charge.getPaymentId());
    Cancel cancel = payment.getCharge("s-chg-1").cancel();
    assertNotNull(cancel);
  }

  @Test
  public void testPartialCancelOnCharge() throws HttpCommunicationException {
    Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"),
        createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
        unsafeUrl("https://www.unzer.com"), false);
    Payment payment = getUnzer().fetchPayment(charge.getPaymentId());
    Cancel cancel = payment.getCharge(0).cancel(BigDecimal.ONE);
    assertNotNull(cancel);
  }

  @Test
  public void testAuthorize() throws HttpCommunicationException {
    // Variant 1
    Unzer unzer = getUnzer();
    Payment payment = new Payment(unzer);
    Authorization authorizationUsingPayment =
        payment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"),
            createPaymentTypeCard(unzer, "4711100000000000").getId(),
            unsafeUrl("https://www.unzer.com"));

    // Variant 2
    Authorization authorizationUsingUnzer =
        unzer.authorize(BigDecimal.ONE, Currency.getInstance("EUR"),
            createPaymentTypeCard(unzer, "4711100000000000").getId(),
            unsafeUrl("https://www.unzer.com"));
    authorizationUsingUnzer.getPayment();

    assertNotNull(authorizationUsingPayment);
    assertNotNull(authorizationUsingUnzer);
  }

  @Test
  public void testAuthorizeWithExistedCustomer() throws HttpCommunicationException, ParseException {
    // Variant 1
    Payment payment = new Payment(getUnzer());
    Authorization authorizationUsingPayment =
        payment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"),
            createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
            unsafeUrl("https://www.unzer.com"), createFactoringOKCustomer().getId());

    Authorization authorizationUsingUnzer =
        getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"),
            createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
            unsafeUrl("https://www.unzer.com"), createFactoringOKCustomer().getId());
    authorizationUsingUnzer.getPayment();

    assertNotNull(authorizationUsingPayment);
    assertNotNull(authorizationUsingUnzer);
  }

  @Test
  public void testAuthorizeWithNotExistedPaymentTypeAndCustomer()
      throws HttpCommunicationException {
    Customer customerRequest = getMaximumCustomer("");
    Card cardRequest = getPaymentTypeCard();
    // Variant 1
    Payment payment = new Payment(getUnzer());
    Authorization authorizationUsingPayment =
        payment.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), cardRequest,
            unsafeUrl("https://www.unzer.com"), customerRequest);

    Authorization authorizationUsingUnzer =
        getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), cardRequest,
            unsafeUrl("https://www.unzer.com"), customerRequest);
    authorizationUsingUnzer.getPayment();

    assertNotNull(authorizationUsingPayment);
    assertNotNull(authorizationUsingUnzer);
  }

  @Test
  public void testChargeWithoutAuthorize()
      throws HttpCommunicationException {
    Charge chargeUsingPayment =
        new Payment(getUnzer()).charge(BigDecimal.ONE, Currency.getInstance("EUR"),
            createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
            unsafeUrl("https://www.unzer.com"));
    Charge chargeUsingUnzer = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"),
        createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
        unsafeUrl("https://www.unzer.com"));
    assertNotNull(chargeUsingPayment);
    assertNotNull(chargeUsingUnzer);
  }
}

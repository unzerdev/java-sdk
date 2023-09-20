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

package com.unzer.payment.integration.paymenttypes;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.beust.ah.A;
import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.PaylaterDirectDebit;
import com.unzer.payment.paymenttypes.Paypal;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.junit.jupiter.api.Test;


public class PaylaterDirectDebitTest extends AbstractPaymentTest {
  @Test
  public void create_and_fetch_payment_type_ok() {
    Unzer unzer = getUnzer();
    PaylaterDirectDebit type = paymentType();
    PaylaterDirectDebit response = unzer.createPaymentType(type);
    assertNotNull(response);
    assertNotNull(response.getId());

    PaylaterDirectDebit fetched = (PaylaterDirectDebit) unzer.fetchPaymentType(response.getId());
    assertNotNull(fetched);
    assertEquals(response.getId(), fetched.getId());
  }

  private static PaylaterDirectDebit paymentType() {
    PaylaterDirectDebit type = new PaylaterDirectDebit(
        "DE89370400440532013000",
        "Max Mustermann"
    );
    type.setCountry("DE");
    return type;
  }

  @Test
  public void authorize_and_charge_ok() {
    Unzer unzer = getUnzer();
    PaylaterDirectDebit type = unzer.createPaymentType(paymentType());
    assertNotNull(type.getId());

    Customer customer = unzer.createCustomer(getMaximumCustomer(generateUuid()));
    assertNotNull(customer.getId());

    Authorization authorize = unzer.authorize((Authorization) new Authorization()
        .setAmount(BigDecimal.TEN)
        .setCurrency(Currency.getInstance("EUR"))
        .setTypeId(type.getId())
        .setCustomerId(customer.getId())
        .setReturnUrl(unsafeUrl("https://unzer.com")));

    assertNotNull(authorize);
    assertNotNull(authorize.getId());
    assertNotNull(authorize.getPaymentId());
    assertEquals(AbstractTransaction.Status.SUCCESS, authorize.getStatus());

    Charge charge = unzer.chargeAuthorization(authorize.getPaymentId());
    assertNotNull(charge.getId());
    assertEquals(AbstractTransaction.Status.SUCCESS, authorize.getStatus());
  }
}

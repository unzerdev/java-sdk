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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.PayU;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

@Disabled("Payment type not configured for merchant. See: https://unz.atlassian.net/browse/CC-540")
public class PayUTest extends AbstractPaymentTest {

  @Test
  public void create_payment_type() {
    Unzer unzer = getUnzer();

    PayU payU = new PayU();
    payU = unzer.createPaymentType(payU);
    assertNotNull(payU.getId());

    PayU fetched = (PayU) unzer.fetchPaymentType(payU.getId());
    assertEquals(payU.getId(), fetched.getId());
  }

  @TestFactory
  public Stream<DynamicTest> charge() {
    return Stream.of("CZK", "PLN").map(currency -> dynamicTest(currency, () -> {
      Unzer unzer = getUnzer();
      PayU payU = new PayU();
      payU = unzer.createPaymentType(payU);

      Charge charge = unzer.charge(BigDecimal.TEN, Currency.getInstance(currency), payU.getId());

      assertNotNull(charge);
      assertNotNull(charge.getId());
      assertEquals(AbstractTransaction.Status.PENDING, charge.getStatus());
    }));
  }
}

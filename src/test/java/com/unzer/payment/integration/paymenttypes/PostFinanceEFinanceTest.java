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


import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.PostFinanceEFinance;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class PostFinanceEFinanceTest extends AbstractPaymentTest {

  @Test
  public void testCreatePostFinanceEFinanceMandatoryType() throws HttpCommunicationException {
    PostFinanceEFinance pfEFinance = new PostFinanceEFinance();
    pfEFinance = getUnzer().createPaymentType(pfEFinance);
    assertNotNull(pfEFinance.getId());
  }

  @Test
  public void testChargePostFinanceEFinanceType()
      throws HttpCommunicationException, MalformedURLException {
    Unzer unzer = getUnzer();
    PostFinanceEFinance pfEFinance = unzer.createPaymentType(getPostFinanceEFinance());
    Charge charge = pfEFinance.charge(BigDecimal.ONE, Currency.getInstance("CHF"),
        new URL("https://www.unzer.com"));
    assertNotNull(charge);
    assertNotNull(charge.getId());
    assertNotNull(charge.getRedirectUrl());
  }

  private PostFinanceEFinance getPostFinanceEFinance() {
    PostFinanceEFinance pfEFinance = new PostFinanceEFinance();
    return pfEFinance;
  }

  @Test
  public void testFetchPostFinanceEFinanceType() throws HttpCommunicationException {
    Unzer unzer = getUnzer();
    PostFinanceEFinance pfEFinance = unzer.createPaymentType(getPostFinanceEFinance());
    assertNotNull(pfEFinance.getId());
    PostFinanceEFinance fetchedPostFinanceEFinance =
        (PostFinanceEFinance) unzer.fetchPaymentType(pfEFinance.getId());
    assertNotNull(fetchedPostFinanceEFinance.getId());
  }


}

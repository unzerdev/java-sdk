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
package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.Authorization;
import com.unzer.payment.Basket;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.BasketV2TestData;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.PaypalData;
import com.unzer.payment.paymenttypes.Paypal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;

public class PaypalExpressTest extends AbstractPaymentTest {
    @Test
    public void testCharge() throws HttpCommunicationException {
        Unzer unzer = getUnzer();
        Paypal type = unzer.createPaymentType(new Paypal());

        Basket basket = unzer.createBasket(BasketV2TestData.getMaxTestBasketV2());

        Authorization authorization = (Authorization) new Authorization()
                .setAmount(BigDecimal.valueOf(500.5))
                .setTypeId(type.getId())
                .setBasketId(basket.getId())
                .setCurrency(Currency.getInstance("EUR"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setPrivacyPolicyUrl("https://en.wikipedia.org/wiki/Policy")
                                .setTermsAndConditionsUrl("https://en.wikipedia.org/wiki/Terms_of_service")
                                .setPaypal(new PaypalData().setCheckoutType(PaypalData.CheckoutType.EXPRESS))
                );

        Authorization createdAuthorization = unzer.authorize(authorization);
        createdAuthorization.getTypeUrl();
    }
}

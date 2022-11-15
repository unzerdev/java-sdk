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
package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Invoice business object
 *
 * @author Unzer E-Com GmbH
 * @deprecated use {@link PaylaterInvoice} instead
 */
@Deprecated
public class Invoice extends AbstractPaymentType implements PaymentType {

    @Override
    public String getTypeUrl() {
        return "types/invoice";
    }

    @Override
    public PaymentType map(PaymentType invoice, JsonObject jsonId) {
        ((Invoice) invoice).setId(jsonId.getId());
        ((Invoice) invoice).setRecurring(((JsonIdObject) jsonId).getRecurring());
        return invoice;
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return charge(amount, currency, returnUrl, null);
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

}

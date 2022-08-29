package com.unzer.payment.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonIdObject;
import com.unzer.payment.communication.json.JsonObject;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Invoice secured is an Invoice payment with guarantee for the Merchant
 */
@Deprecated
public class InvoiceSecured extends AbstractPaymentType implements PaymentType {

    @Override
    public String getTypeUrl() {
        return "types/invoice-secured";
    }

    @Override
    public PaymentType map(PaymentType invoiceSecured, JsonObject jsonId) {
        if (invoiceSecured instanceof InvoiceSecured && jsonId instanceof JsonIdObject) {
            ((InvoiceSecured) invoiceSecured).setId(jsonId.getId());
            ((InvoiceSecured) invoiceSecured).setRecurring(((JsonIdObject) jsonId).getRecurring());
        }
        return invoiceSecured;
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl) throws HttpCommunicationException {
        return charge(amount, currency, returnUrl, null);
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer) throws HttpCommunicationException {
        return getUnzer().charge(amount, currency, this, returnUrl, customer);
    }

    @Deprecated
    public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer, Basket basket, String invoiceId) throws HttpCommunicationException {
        return getUnzer().charge(getCharge(amount, currency, this, returnUrl, customer, basket, invoiceId));
    }

    @Deprecated
    private Charge getCharge(BigDecimal amount, Currency currency, InvoiceSecured invoiceSecured, URL returnUrl,
                             Customer customer, Basket basket, String invoiceId) throws HttpCommunicationException {
        return ((Charge) new Charge()
                .setAmount(amount)
                .setCurrency(currency)
                .setTypeId(getUnzer().createPaymentType(invoiceSecured).getId())
                .setReturnUrl(returnUrl)
                .setCustomerId(getUnzer().createCustomerIfPresent(customer).getId())
                .setBasketId(getUnzer().createBasket(basket).getId()))
                .setInvoiceId(invoiceId);
    }

}

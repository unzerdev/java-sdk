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

import com.unzer.payment.GeoLocation;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;

/**
 * PaylaterInvoice is a part of our Buy Now Pay Later (BNPL) offering and it’s a very convenient
 * payment option. This payment option offers customers a flexible and secure way to shop online
 * or in-store without having to pay upfront which limits the risks for customers. For merchants
 * the BNPL payment options drive conversion and customer satisfaction. With a strong BNPL partner
 * like Unzer, the risks for merchants are equally limited as they are for customers.
 * <p>
 * See more: <a href="https://docs.unzer.com/payment-methods/unzer-invoice-upl/">Unzer Docs</a>
 */
public class PaylaterInvoice extends AbstractPaymentType {
  @Override
  public String getTypeUrl() {
    return "types/paylater-invoice";
  }

  @Override
  public PaymentType map(PaymentType paymentType, ApiObject apiObject) {
    ((PaylaterInvoice) paymentType).setId(apiObject.getId());
    ((PaylaterInvoice) paymentType).setRecurring(((ApiIdObject) apiObject).getRecurring());
    GeoLocation tempGeoLocation =
        new GeoLocation(((ApiIdObject) apiObject).getGeoLocation().getClientIp(),
            ((ApiIdObject) apiObject).getGeoLocation().getCountryIsoA2());
    ((PaylaterInvoice) paymentType).setGeoLocation(tempGeoLocation);

    return paymentType;
  }
}

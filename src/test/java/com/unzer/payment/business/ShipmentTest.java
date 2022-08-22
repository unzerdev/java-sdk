package com.unzer.payment.business;

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

import com.unzer.payment.PaymentException;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShipmentTest extends AbstractPaymentTest {
    @Test
    public void testAuthorizeWithShipmentNotSameAddressWithInvoiceSecured() {
        assertThrows(PaymentException.class, () -> {
                    Unzer unzer = getUnzer();
                    InvoiceSecured paymentTypeInvoiceSecured = unzer.createPaymentType(new InvoiceSecured());
                    unzer.authorize(
                            getAuthorization(
                                    paymentTypeInvoiceSecured.getId(),
                                    createMaximumCustomer().getId()
                            )
                    );
                }
        );
    }

}

package com.unzer.payment;

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

import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PaymentTest {

    @Test
    public void testIsNotEmpty() {
        Payment payment = new Payment(getUnzer());
        assertFalse(payment.isNotEmpty(""));
        assertFalse(payment.isNotEmpty("   "));
        assertFalse(payment.isNotEmpty(null));
        assertTrue(payment.isNotEmpty("a"));
    }

    @Test
    public void testChargeOnNullAuthorization() throws HttpCommunicationException {
        Payment payment = new Payment(getUnzer());

        try {
            payment.cancel(BigDecimal.TEN);
        } catch (PaymentException paymentException) {
            assertEquals("Cancel is only possible for an Authorization", paymentException.getMessage());
        }

    }

    private Unzer getUnzer() {
        return null;
    }

}

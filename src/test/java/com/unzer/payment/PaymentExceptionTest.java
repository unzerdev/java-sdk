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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentExceptionTest {


    @Test
    public void testMessageIsNotNull() {
        PaymentException paymentException = new PaymentException("An Error occurred!");
        assertTrue(paymentException.getMessage() != null);
        assertEquals("An Error occurred!", paymentException.getMessage());
    }

    @Test
    public void testMessageIsEmpty() {
        PaymentException paymentException = new PaymentException("");
        assertTrue(paymentException.getMessage() != null);
        assertEquals("Unzer responded with 0 when calling . ", paymentException.getMessage());
    }

    @Test
    public void testPaymentErrorListEmptyAndMessageNotEmpty() {
        PaymentException paymentException = new PaymentException(new ArrayList<PaymentError>(), "An Error occurred!");
        assertTrue(paymentException.getMessage() != null);
        assertEquals("An Error occurred!", paymentException.getMessage());
    }

    @Test
    public void testPaymentErrorListEmptyAndMessageIsEmpty() {
        PaymentException paymentException = new PaymentException(new ArrayList<PaymentError>(), "");
        assertTrue(paymentException.getMessage() != null);
        assertEquals("Unzer responded with 0 when calling . ", paymentException.getMessage());
    }

    @Test
    public void testPaymentErrorListNotEmptyAndMessageIsEmpty() {
        PaymentError paymentError = new PaymentError();
        paymentError.setCode("0");
        paymentError.setCustomerMessage("This is a Customer Error Message!");
        paymentError.setMerchantMessage("This is a Merchant Error Message!");

        List<PaymentError> paymentErrorList = new ArrayList<PaymentError>();
        paymentErrorList.add(paymentError);

        PaymentException paymentException = new PaymentException(paymentErrorList, "");
        assertTrue(paymentException.getMessage() != null);
        assertEquals("This is a Merchant Error Message!", paymentException.getMessage());
    }

    @Test
    public void testMessageIsNull() {
        PaymentException paymentException = new PaymentException(null);
        assertTrue(paymentException.getMessage() != null);
        assertEquals("Unzer responded with 0 when calling . ", paymentException.getMessage());
    }

}
package com.unzer.payment;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentExceptionTest {


    @Test
    void testMessageIsNotNull() {
        PaymentException paymentException = new PaymentException("An Error occurred!");
        assertNotNull(paymentException.getMessage());
        assertEquals("An Error occurred!", paymentException.getMessage());
    }

    @Test
    void testMessageIsEmpty() {
        PaymentException paymentException = new PaymentException("");
        assertNotNull(paymentException.getMessage());
        assertEquals("Unzer responded with 0 when calling . ", paymentException.getMessage());
    }

    @Test
    void testPaymentErrorListEmptyAndMessageNotEmpty() {
        PaymentException paymentException = new PaymentException(new ArrayList<PaymentError>(), "An Error occurred!");
        assertNotNull(paymentException.getMessage());
        assertEquals("An Error occurred!", paymentException.getMessage());
    }

    @Test
    void testPaymentErrorListEmptyAndMessageIsEmpty() {
        PaymentException paymentException = new PaymentException(new ArrayList<PaymentError>(), "");
        assertNotNull(paymentException.getMessage());
        assertEquals("Unzer responded with 0 when calling . ", paymentException.getMessage());
    }

    @Test
    void testPaymentErrorListNotEmptyAndMessageIsEmpty() {
        PaymentError paymentError = new PaymentError();
        paymentError.setCode("0");
        paymentError.setCustomerMessage("This is a Customer Error Message!");
        paymentError.setMerchantMessage("This is a Merchant Error Message!");

        List<PaymentError> paymentErrorList = new ArrayList<PaymentError>();
        paymentErrorList.add(paymentError);

        PaymentException paymentException = new PaymentException(paymentErrorList, "");
        assertNotNull(paymentException.getMessage());
        assertEquals("This is a Merchant Error Message!", paymentException.getMessage());
    }

    @Test
    void testMessageIsNull() {
        PaymentException paymentException = new PaymentException(null);
        assertNotNull(paymentException.getMessage());
        assertEquals("Unzer responded with 0 when calling . ", paymentException.getMessage());
    }

}

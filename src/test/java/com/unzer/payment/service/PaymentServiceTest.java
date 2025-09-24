package com.unzer.payment.service;


import com.unzer.payment.Charge;
import com.unzer.payment.PaymentError;
import com.unzer.payment.PaymentException;
import com.unzer.payment.TestData;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationMockUtils;
import com.unzer.payment.communication.UnzerRestCommunication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentServiceTest {


    public PaymentService setUpPaymentService(String json, int status) {
        UnzerRestCommunication rest = HttpCommunicationMockUtils.withFixedResponse(json, status);
        Unzer unzer = new Unzer(rest, "anykey");
        return new PaymentService(unzer, rest);
    }

    @Test
    void testChargeInCaseOfCoreException() {
        PaymentService paymentService = setUpPaymentService(TestData.errorJson(), 500);
        PaymentException exception = null;
        try {
            paymentService.charge(new Charge());
        } catch (PaymentException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(1, exception.getPaymentErrorList().size());
        PaymentError error = exception.getPaymentErrorList().get(0);
        assertEquals("COR.400.100.101", error.getCode());
        assertEquals("Address untraceable", error.getMerchantMessage());
        assertEquals("The provided address is invalid. Please check your input and try agian.", error.getCustomerMessage());
    }

    @Test
    void test200WithErrorJsonIsConvertedToPaymentException() {
        PaymentService paymentService = setUpPaymentService(TestData.errorJson(), 200);
        PaymentException exception = null;
        try {
            paymentService.charge(new Charge());
        } catch (PaymentException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(1, exception.getPaymentErrorList().size());
        PaymentError error = exception.getPaymentErrorList().get(0);
        assertEquals("COR.400.100.101", error.getCode());
        assertEquals("Address untraceable", error.getMerchantMessage());
        assertEquals("The provided address is invalid. Please check your input and try agian.", error.getCustomerMessage());
    }


}

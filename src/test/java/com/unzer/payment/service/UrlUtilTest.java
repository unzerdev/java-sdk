package com.unzer.payment.service;


import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlUtilTest extends AbstractPaymentTest {

    @Test
    public void testGetUrlForPaymentTypeInvoiceSecured() throws HttpCommunicationException {
        String minimalRestUrl = "https://api.unzer.com/v1/types";
        String maximumRestUrl = "https://api.unzer.com/v1/types/invoice-secured";

        Unzer unzer = getUnzer();
        UrlUtil urlUtil = new UrlUtil(unzer.getEndPoint());
        InvoiceSecured invoiceSecured = createPaymentTypeInvoiceSecured();

        assertEquals(String.format("%s/%s", minimalRestUrl, invoiceSecured.getId()), urlUtil.getHttpGetUrl(invoiceSecured.getId()));
        assertEquals(String.format("%s/%s", maximumRestUrl, invoiceSecured.getId()), urlUtil.getHttpGetUrl(invoiceSecured, invoiceSecured.getId()));
    }

    @Test
    public void testGetUrlForPaymentTypeInstallmentSecured() throws HttpCommunicationException {
        String minimalRestUrl = "https://api.unzer.com/v1/types";
        String maximumRestUrl = "https://api.unzer.com/v1/types/installment-secured";

        Unzer unzer = getUnzer();
        UrlUtil urlUtil = new UrlUtil(unzer.getEndPoint());
        InstallmentSecuredRatePlan installmentSecuredRatePlan = createPaymentTypeInstallmentSecuredRatePlan();

        assertEquals(String.format("%s/%s", minimalRestUrl, installmentSecuredRatePlan.getId()), urlUtil.getHttpGetUrl(installmentSecuredRatePlan.getId()));
        assertEquals(String.format("%s/%s", maximumRestUrl, installmentSecuredRatePlan.getId()), urlUtil.getHttpGetUrl(installmentSecuredRatePlan, installmentSecuredRatePlan.getId()));
    }

    @Test
    public void testGetUrlForPaymentTypeSepaDirectDebitSecured() throws HttpCommunicationException {
        String minimalRestUrl = "https://api.unzer.com/v1/types";
        String maximumRestUrl = "https://api.unzer.com/v1/types/sepa-direct-debit-secured";

        Unzer unzer = getUnzer();
        UrlUtil urlUtil = new UrlUtil(unzer.getEndPoint());
        SepaDirectDebitSecured sepaDirectDebitSecured = createPaymentTypeSepaDirectDebitSecured("DE89370400440532013000");

        assertEquals(String.format("%s/%s", minimalRestUrl, sepaDirectDebitSecured.getId()), urlUtil.getHttpGetUrl(sepaDirectDebitSecured.getId()));
        assertEquals(String.format("%s/%s", maximumRestUrl, sepaDirectDebitSecured.getId()), urlUtil.getHttpGetUrl(sepaDirectDebitSecured, sepaDirectDebitSecured.getId()));
    }
}

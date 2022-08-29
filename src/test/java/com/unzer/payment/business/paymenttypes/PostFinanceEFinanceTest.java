package com.unzer.payment.business.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.PostFinanceEFinance;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostFinanceEFinanceTest extends AbstractPaymentTest {

    @Test
    public void testCreatePostFinanceEFinanceMandatoryType() throws HttpCommunicationException {
        PostFinanceEFinance pfEFinance = new PostFinanceEFinance();
        pfEFinance = getUnzer().createPaymentType(pfEFinance);
        assertNotNull(pfEFinance.getId());
    }

    @Test
    public void testChargePostFinanceEFinanceType() throws HttpCommunicationException, MalformedURLException {
        PostFinanceEFinance pfEFinance = getUnzer().createPaymentType(getPostFinanceEFinance());
        Charge charge = pfEFinance.charge(BigDecimal.ONE, Currency.getInstance("CHF"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchPostFinanceEFinanceType() throws HttpCommunicationException {
        PostFinanceEFinance pfEFinance = getUnzer().createPaymentType(getPostFinanceEFinance());
        assertNotNull(pfEFinance.getId());
        PostFinanceEFinance fetchedPostFinanceEFinance = (PostFinanceEFinance) getUnzer().fetchPaymentType(pfEFinance.getId());
        assertNotNull(fetchedPostFinanceEFinance.getId());
    }


    private PostFinanceEFinance getPostFinanceEFinance() {
        PostFinanceEFinance pfEFinance = new PostFinanceEFinance();
        return pfEFinance;
    }


}

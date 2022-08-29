package com.unzer.payment.business.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.PostFinanceCard;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostFinanceCardTest extends AbstractPaymentTest {

    @Test
    public void testCreatePostFinanceCardMandatoryType() throws HttpCommunicationException {
        PostFinanceCard pfCard = new PostFinanceCard();
        pfCard = getUnzer().createPaymentType(pfCard);
        assertNotNull(pfCard.getId());
    }

    @Test
    public void testChargePostFinanceCardType() throws HttpCommunicationException, MalformedURLException {
        PostFinanceCard pfCard = getUnzer().createPaymentType(getPostFinanceCard());
        Charge charge = pfCard.charge(BigDecimal.ONE, Currency.getInstance("CHF"), new URL("https://www.google.at"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchPostFinanceCardType() throws HttpCommunicationException {
        PostFinanceCard pfCard = getUnzer().createPaymentType(getPostFinanceCard());
        assertNotNull(pfCard.getId());
        PostFinanceCard fetchedPostFinanceCard = (PostFinanceCard) getUnzer().fetchPaymentType(pfCard.getId());
        assertNotNull(fetchedPostFinanceCard.getId());
    }


    private PostFinanceCard getPostFinanceCard() {
        PostFinanceCard pfCard = new PostFinanceCard();
        return pfCard;
    }


}

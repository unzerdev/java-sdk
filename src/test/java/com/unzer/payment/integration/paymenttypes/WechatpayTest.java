package com.unzer.payment.integration.paymenttypes;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Wechatpay;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class WechatpayTest extends AbstractPaymentTest {

    @Test
    public void testCreateWechatpayMandatoryType() throws HttpCommunicationException {
        Wechatpay wechatpay = new Wechatpay();
        wechatpay = getUnzer().createPaymentType(wechatpay);
        assertNotNull(wechatpay.getId());
    }

    @Test
    public void testChargeWechatpayType() throws HttpCommunicationException, MalformedURLException {
        Wechatpay wechatpay = getUnzer().createPaymentType(new Wechatpay());
        Charge charge = wechatpay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchWechatpayType() throws HttpCommunicationException {
        Wechatpay wechatpay = getUnzer().createPaymentType(new Wechatpay());
        assertNotNull(wechatpay.getId());
        Wechatpay fetchedWechatpay = (Wechatpay) getUnzer().fetchPaymentType(wechatpay.getId());
        assertNotNull(fetchedWechatpay.getId());
    }



}

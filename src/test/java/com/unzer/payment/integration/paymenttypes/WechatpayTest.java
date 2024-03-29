package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Wechatpay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WechatpayTest extends AbstractPaymentTest {

    @Test
    public void testCreateWechatpayMandatoryType() {
        Wechatpay wechatpay = new Wechatpay();
        wechatpay = getUnzer().createPaymentType(wechatpay);
        assertNotNull(wechatpay.getId());
    }

    @Test
    public void testChargeWechatpayType() {
        Wechatpay wechatpay = getUnzer().createPaymentType(new Wechatpay());
        Charge charge = wechatpay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    public void testFetchWechatpayType() {
        Wechatpay wechatpay = getUnzer().createPaymentType(new Wechatpay());
        assertNotNull(wechatpay.getId());
        Wechatpay fetchedWechatpay = (Wechatpay) getUnzer().fetchPaymentType(wechatpay.getId());
        assertNotNull(fetchedWechatpay.getId());
    }


}

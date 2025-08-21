package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Alipay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class AlipayTest extends AbstractPaymentTest {

    @Test
    void testCreateAlipayManatoryType() {
        Alipay alipay = new Alipay();
        alipay = getUnzer().createPaymentType(alipay);
        assertNotNull(alipay.getId());
    }

    @Test
    void testChargeAlipayType() {
        Unzer unzer = getUnzer();
        Alipay alipay = unzer.createPaymentType(new Alipay());
        Charge charge = unzer.charge(
                BigDecimal.ONE,
                Currency.getInstance("EUR"),
                alipay.getId(),
                unsafeUrl("https://www.unzer.com")
        );
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    @Test
    void testFetchAlipayType() {
        Alipay alipay = getUnzer().createPaymentType(new Alipay());
        assertNotNull(alipay.getId());
        Alipay fetchedAlipay = (Alipay) getUnzer().fetchPaymentType(alipay.getId());
        assertNotNull(fetchedAlipay.getId());
    }
}

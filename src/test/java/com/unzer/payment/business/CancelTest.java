package com.unzer.payment.business;


import com.unzer.payment.*;
import com.unzer.payment.paymenttypes.Card;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CancelTest extends AbstractPaymentTest {

    @Test
    public void testFetchCancelAuthorizationWithUnzer() {
        Authorization authorize = getUnzer().authorize(
                getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Cancel cancelInit = authorize.cancel();
        Cancel cancel = getUnzer().fetchCancel(authorize.getPaymentId(), cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals("COR.000.100.112", cancel.getMessage().getCode());
        assertNotNull(cancel.getMessage().getCustomer());
        assertCancelEquals(cancelInit, cancel);
    }

    @Test
    public void test_fetch_cancel_charge() {
        Unzer unzer = getUnzer();
        Card card = unzer.createPaymentType(new Card("4711100000000000", "03/30").setCvc("123"));
        assertNotNull(card.getId());
        Charge charge = unzer.charge(BigDecimal.ONE, Currency.getInstance("EUR"), card.getId(), unsafeUrl("https://www.unzer.com"), false);
        assertNotNull(charge.getId());
        assertEquals(BaseTransaction.Status.SUCCESS, charge.getStatus());

        // Cancel
        Cancel cancel = unzer.cancelCharge(charge.getPaymentId(), charge.getId());
        assertNotNull(cancel.getId());
        assertEquals(BaseTransaction.Status.SUCCESS, cancel.getStatus());

        // Fetch cancel
        Cancel fetchedCancel = unzer.fetchCancel(cancel.getPaymentId(), cancel.getId());
        assertEquals(cancel.getId(), fetchedCancel.getId());
        assertEquals(Cancel.TransactionType.CHARGES, fetchedCancel.getTransactionType());
    }

    @Test
    public void testFetchCancelAuthorizationWithPayment() {
        Authorization authorize = getUnzer().authorize(
                getAuthorization(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), false));
        Cancel cancelInit = authorize.cancel();
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        List<Cancel> cancelList = cancelInit.getPayment().getCancelList();
        assertNotNull(cancelList);
        assertEquals(1, cancelList.size());
        assertCancelEquals(cancelInit, cancelList.get(0));
    }

    @Test
    public void testFetchCancelChargeWithUnzer() {
        Charge initCharge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"),
                createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
                unsafeUrl("https://www.unzer.com"), false);
        Cancel cancelInit = initCharge.cancel();
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        Cancel cancel =
                getUnzer().fetchCancel(initCharge.getPaymentId(), initCharge.getId(), cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertCancelEquals(cancelInit, cancel);
    }

    @Test
    public void testCancelChargeWithPayment() {
        Charge initCharge = getUnzer()
                .charge(BigDecimal.ONE, Currency.getInstance("EUR"),
                        createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
                        unsafeUrl("https://www.unzer.com"), false);
        Cancel cancelInit = initCharge.cancel();
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId())
                .getCancel(cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertCancelEquals(cancelInit, cancel);
    }

    @Test
    public void testCancelChargeWithPaymentReference() {
        Charge initCharge = getUnzer()
                .charge(BigDecimal.ONE, Currency.getInstance("EUR"),
                        createPaymentTypeCard(getUnzer(), "4711100000000000").getId(),
                        unsafeUrl("https://www.unzer.com"), false);
        Cancel cancelReq = new Cancel();
        cancelReq.setPaymentReference("pmt-ref");
        cancelReq.setAmount(new BigDecimal("1.0"));
        Cancel cancelInit =
                getUnzer().cancelCharge(initCharge.getPaymentId(), initCharge.getId(), cancelReq);
        assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
        assertNotNull(cancelInit.getMessage().getCustomer());
        assertEquals("pmt-ref", cancelInit.getPaymentReference());
        assertEquals(new BigDecimal("1.0000").setScale(4), cancelInit.getAmount());
        Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId())
                .getCancel(cancelInit.getId());
        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertCancelEquals(cancelInit, cancel);
        assertEquals("pmt-ref", cancel.getPaymentReference());
        assertEquals(new BigDecimal("1.0000").setScale(4), cancel.getAmount());
    }

}

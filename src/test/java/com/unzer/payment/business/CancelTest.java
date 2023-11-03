package com.unzer.payment.business;


import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Authorization;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;
import org.junit.jupiter.api.Test;

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
    cancelReq.setAmount(new BigDecimal(1.0));
    Cancel cancelInit =
        getUnzer().cancelCharge(initCharge.getPaymentId(), initCharge.getId(), cancelReq);
    assertEquals("COR.000.100.112", cancelInit.getMessage().getCode());
    assertNotNull(cancelInit.getMessage().getCustomer());
    assertEquals("pmt-ref", cancelInit.getPaymentReference());
    assertEquals(new BigDecimal(1.0000).setScale(4), cancelInit.getAmount());
    Cancel cancel = cancelInit.getPayment().getCharge(initCharge.getId())
        .getCancel(cancelInit.getId());
    assertNotNull(cancel);
    assertNotNull(cancel.getId());
    assertCancelEquals(cancelInit, cancel);
    assertEquals("pmt-ref", cancel.getPaymentReference());
    assertEquals(new BigDecimal(1.0000).setScale(4), cancel.getAmount());
  }

}

package com.unzer.payment.integration.paymenttypes;


import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.Bancontact;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;


public class BancontactTest extends AbstractPaymentTest {

  @Test
  public void testCreateBancontactWithoutHolder() {
    Bancontact bacncontact = new Bancontact();
    bacncontact = getUnzer().createPaymentType(bacncontact);
    assertNotNull(bacncontact.getId());
  }

  @Test
  public void testCreateBancontactWithHolder() {
    Bancontact bancontact = new Bancontact("test holder");
    bancontact = getUnzer().createPaymentType(bancontact);
    assertNotNull(bancontact.getId());
    assertEquals("test holder", bancontact.getHolder());
  }

  @Test
  public void testChargeBancontactType() {
    Unzer unzer = getUnzer();
    Bancontact bancontact = unzer.createPaymentType(new Bancontact());
    Charge charge = unzer.charge(
        BigDecimal.ONE,
        Currency.getInstance("EUR"),
        bancontact.getId(),
        unsafeUrl("https://www.unzer.com")
    );
    assertNotNull(charge);
    assertNotNull(charge.getId());
    assertNotNull(charge.getRedirectUrl());
  }

  @Test
  public void testFetchBancontactType() {
    Bancontact bancontact = getUnzer().createPaymentType(new Bancontact());
    assertNotNull(bancontact.getId());
    Bancontact fetchedBancontact = (Bancontact) getUnzer().fetchPaymentType(bancontact.getId());
    assertNotNull(fetchedBancontact.getId());
  }
}

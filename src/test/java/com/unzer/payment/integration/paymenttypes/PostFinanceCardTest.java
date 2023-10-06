package com.unzer.payment.integration.paymenttypes;


import static com.unzer.payment.business.Keys.ALT_LEGACY_PRIVATE_KEY;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.PostFinanceCard;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class PostFinanceCardTest extends AbstractPaymentTest {

  @Test
  public void testCreatePostFinanceCardMandatoryType() throws HttpCommunicationException {
    PostFinanceCard pfCard = new PostFinanceCard();
    pfCard = getUnzer().createPaymentType(pfCard);
    assertNotNull(pfCard.getId());
  }

  @Test
  public void testChargePostFinanceCardType()
      throws HttpCommunicationException, MalformedURLException {
    Unzer unzer = getUnzer();
    PostFinanceCard pfCard = unzer.createPaymentType(getPostFinanceCard());
    Charge charge = pfCard.charge(BigDecimal.ONE, Currency.getInstance("CHF"),
        new URL("https://www.google.at"));
    assertNotNull(charge);
    assertNotNull(charge.getId());
    assertNotNull(charge.getRedirectUrl());
  }

  private PostFinanceCard getPostFinanceCard() {
    PostFinanceCard pfCard = new PostFinanceCard();
    return pfCard;
  }

  @Test
  public void testFetchPostFinanceCardType() throws HttpCommunicationException {
    Unzer unzer = getUnzer();
    PostFinanceCard pfCard = unzer.createPaymentType(getPostFinanceCard());
    assertNotNull(pfCard.getId());
    PostFinanceCard fetchedPostFinanceCard =
        (PostFinanceCard) unzer.fetchPaymentType(pfCard.getId());
    assertNotNull(fetchedPostFinanceCard.getId());
  }


}

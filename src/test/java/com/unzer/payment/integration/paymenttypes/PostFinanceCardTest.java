package com.unzer.payment.integration.paymenttypes;


import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.PostFinanceCard;
import java.math.BigDecimal;
import java.util.Currency;
import org.junit.jupiter.api.Test;

public class PostFinanceCardTest extends AbstractPaymentTest {

  @Test
  public void testCreatePostFinanceCardMandatoryType() {
    PostFinanceCard pfCard = new PostFinanceCard();
    pfCard = getUnzer().createPaymentType(pfCard);
    assertNotNull(pfCard.getId());
  }

  @Test
  public void testChargePostFinanceCardType() {
    Unzer unzer = getUnzer();
    PostFinanceCard pfCard = unzer.createPaymentType(getPostFinanceCard());
    Charge charge = pfCard.charge(BigDecimal.ONE, Currency.getInstance("CHF"),
        unsafeUrl("https://www.google.at"));
    assertNotNull(charge);
    assertNotNull(charge.getId());
    assertNotNull(charge.getRedirectUrl());
  }

  private PostFinanceCard getPostFinanceCard() {
    PostFinanceCard pfCard = new PostFinanceCard();
    return pfCard;
  }

  @Test
  public void testFetchPostFinanceCardType() {
    Unzer unzer = getUnzer();
    PostFinanceCard pfCard = unzer.createPaymentType(getPostFinanceCard());
    assertNotNull(pfCard.getId());
    PostFinanceCard fetchedPostFinanceCard =
        (PostFinanceCard) unzer.fetchPaymentType(pfCard.getId());
    assertNotNull(fetchedPostFinanceCard.getId());
  }


}

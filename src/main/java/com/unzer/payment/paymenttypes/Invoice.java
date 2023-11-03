package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Invoice business object
 *
 * @author Unzer E-Com GmbH
 * @deprecated use {@link PaylaterInvoice} instead
 */
@Deprecated
public class Invoice extends BasePaymentType {

  @Override
  public String getResourceUrl() {
    return "/v1/types/invoice/<resourceId>";
  }

  @Override
  public PaymentType map(PaymentType invoice, ApiObject jsonId) {
    ((Invoice) invoice).setId(jsonId.getId());
    ((Invoice) invoice).setRecurring(((ApiIdObject) jsonId).getRecurring());
    return invoice;
  }

  @Deprecated
  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
      throws HttpCommunicationException {
    return charge(amount, currency, returnUrl, null);
  }

  @Deprecated
  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl, customer);
  }

}

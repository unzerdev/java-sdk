package com.unzer.payment.paymenttypes;

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiIdeal;
import com.unzer.payment.communication.json.ApiObject;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Ideal business object
 *
 * @author Unzer E-Com GmbH
 */
public class Ideal extends AbstractPaymentType implements PaymentType {

  private String bic;

  @Override
  public String getTypeUrl() {
    return "types/ideal";
  }

  @Override
  public PaymentType map(PaymentType ideal, ApiObject jsonIdeal) {
    ((Ideal) ideal).setId(jsonIdeal.getId());
    ((Ideal) ideal).setBic(((ApiIdeal) jsonIdeal).getBankName());
    ((Ideal) ideal).setRecurring(((ApiIdeal) jsonIdeal).getRecurring());
    return ideal;
  }

  public String getBic() {
    return bic;
  }

  public Ideal setBic(String bic) {
    this.bic = bic;
    return this;
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl);
  }

  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer)
      throws HttpCommunicationException {
    return getUnzer().charge(amount, currency, this, returnUrl, customer);
  }

}

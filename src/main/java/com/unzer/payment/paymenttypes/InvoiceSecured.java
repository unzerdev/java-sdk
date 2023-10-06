package com.unzer.payment.paymenttypes;

import com.unzer.payment.Basket;
import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiObject;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

/**
 * Invoice secured is an Invoice payment with guarantee for the Merchant
 */
@Deprecated
public class InvoiceSecured extends AbstractPaymentType implements PaymentType {

  @Override
  public String getTypeUrl() {
    return "types/invoice-secured";
  }

  @Override
  public PaymentType map(PaymentType invoiceSecured, ApiObject jsonId) {
    if (invoiceSecured instanceof InvoiceSecured && jsonId instanceof ApiIdObject) {
      ((InvoiceSecured) invoiceSecured).setId(jsonId.getId());
      ((InvoiceSecured) invoiceSecured).setRecurring(((ApiIdObject) jsonId).getRecurring());
    }
    return invoiceSecured;
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

  @Deprecated
  public Charge charge(BigDecimal amount, Currency currency, URL returnUrl, Customer customer,
                       Basket basket, String invoiceId) throws HttpCommunicationException {
    return getUnzer().charge(
        getCharge(amount, currency, this, returnUrl, customer, basket, invoiceId));
  }

  @Deprecated
  private Charge getCharge(BigDecimal amount, Currency currency, InvoiceSecured invoiceSecured,
                           URL returnUrl,
                           Customer customer, Basket basket, String invoiceId)
      throws HttpCommunicationException {
    return (Charge) new Charge()
        .setAmount(amount)
        .setCurrency(currency)
        .setTypeId(getUnzer().createPaymentType(invoiceSecured).getId())
        .setReturnUrl(returnUrl)
        .setCustomerId(getUnzer().createCustomerIfPresent(customer).getId())
        .setBasketId(getUnzer().createBasket(basket).getId())
        .setInvoiceId(invoiceId);
  }
}

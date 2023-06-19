/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unzer.payment.service;

import com.unzer.payment.*;
import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.json.*;
import com.unzer.payment.communication.json.paylater.JsonInstallmentPlans;
import com.unzer.payment.communication.mapper.JsonToBusinessClassMapper;
import com.unzer.payment.models.PaylaterInvoiceConfig;
import com.unzer.payment.models.PaylaterInvoiceConfigRequest;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentService {
  protected static final String TRANSACTION_TYPE_AUTHORIZATION = "authorize";
  protected static final String TRANSACTION_TYPE_CHARGE = "charge";
  protected static final String TRANSACTION_TYPE_PAYOUT = "payout";
  protected static final String TRANSACTION_TYPE_CANCEL_AUTHORIZE = "cancel-authorize";
  protected static final String TRANSACTION_TYPE_CANCEL_CHARGE = "cancel-charge";

  protected UnzerRestCommunication restCommunication;

  protected UrlUtil urlUtil;
  protected JsonToBusinessClassMapper jsonToBusinessClassMapper = new JsonToBusinessClassMapper();
  protected Unzer unzer;
  protected JsonParser jsonParser;

  /**
   * Creates the {@code PaymentService} with the given {@code Unzer} facade,
   * bound to the given {@code UnzerRestCommunication} implementation used for
   * http-communication.
   *
   * @param unzer             - the {@code Unzer} Facade
   * @param restCommunication - the implementation of
   *                          {@code UnzerRestCommunication} to be used for
   *                          network communication.
   */
  public PaymentService(Unzer unzer, UnzerRestCommunication restCommunication) {
    super();
    this.unzer = unzer;
    this.urlUtil = new UrlUtil(unzer.getPrivateKey());
    this.restCommunication = restCommunication;
    this.jsonParser = new JsonParser();
  }

    public PaylaterInstallmentPlans fetchPaylaterInstallmentPlans(InstallmentPlansRequest installmentPlansRequest) throws HttpCommunicationException {
        String url = this.urlUtil.getApiEndpoint() + installmentPlansRequest.getRequestUrl();

        String response = restCommunication.httpGet(
                url,
                unzer.getPrivateKey());

        JsonInstallmentPlans json = jsonParser.fromJson(response,
                JsonInstallmentPlans.class);
        return jsonToBusinessClassMapper.mapToBusinessObject(new PaylaterInstallmentPlans(), json);
    }

  public List<InstallmentSecuredRatePlan> installmentSecuredPlan(BigDecimal amount,
                                                                 Currency currency,
                                                                 BigDecimal effectiveInterestRate,
                                                                 Date orderDate)
      throws HttpCommunicationException {
    String response = restCommunication.httpGet(
        urlUtil.getHirePurchaseRateUrl(amount, currency, effectiveInterestRate, orderDate),
        unzer.getPrivateKey());
    JsonInstallmentSecuredRatePlanList json = jsonParser.fromJson(response,
        JsonInstallmentSecuredRatePlanList.class);
    return json.getEntity();
  }

  public <T extends PaymentType> T createPaymentType(T paymentType)
      throws HttpCommunicationException {
    String response =
        restCommunication.httpPost(urlUtil.getRestUrl(paymentType), unzer.getPrivateKey(),
            paymentType);
    JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
    return fetchPaymentType(jsonResponse.getId());
  }

  @SuppressWarnings("unchecked")
  public <T extends PaymentType> T fetchPaymentType(String typeId)
      throws HttpCommunicationException {
    AbstractPaymentType paymentType = getPaymentTypeFromTypeId(typeId);
    paymentType.setUnzer(unzer);
    String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(typeId),
        unzer.getPrivateKey());
    JsonIdObject jsonPaymentType =
        jsonParser.fromJson(response, getJsonObjectFromTypeId(typeId).getClass());
    return (T) jsonToBusinessClassMapper.mapToBusinessObject(paymentType, jsonPaymentType);
  }

  private AbstractPaymentType getPaymentTypeFromTypeId(String typeId) {
    if (typeId.length() < 5) {
      throw new PaymentException("TypeId '" + typeId + "' is invalid");
    }
    String paymentType = getTypeIdentifier(typeId);

    PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnumByShortName(paymentType);
    switch (paymentTypeEnum) {
      case CARD:
        return new Card("", "");
      case EPS:
        return new Eps();
      case GIROPAY:
        return new Giropay();
      case IDEAL:
        return new Ideal();
      case INVOICE:
        return new Invoice();
      case INVOICE_GUARANTEED:
      case INVOICE_FACTORING:
      case INVOICE_SECURED:
        return new InvoiceSecured();
      case PAYPAL:
        return new Paypal();
      case PREPAYMENT:
        return new Prepayment();
      case PRZELEWY24:
        return new Przelewy24();
      case SEPA_DIRECT_DEBIT:
        return new SepaDirectDebit("");
      case SEPA_DIRECT_DEBIT_GUARANTEED:
      case SEPA_DIRECT_DEBIT_SECURED:
        return new SepaDirectDebitSecured("");
      case SOFORT:
        return new Sofort();
      case PIS:
        return new Pis();
      case ALIPAY:
        return new Alipay();
      case WECHATPAY:
        return new Wechatpay();
      case APPLEPAY:
        return new Applepay(null, null, null, null);
      case HIRE_PURCHASE_RATE_PLAN:
      case INSTALLMENT_SECURED_RATE_PLAN:
        return new InstallmentSecuredRatePlan();
      case BANCONTACT:
        return new Bancontact("");
      case PF_CARD:
        return new PostFinanceCard();
      case PF_EFINANCE:
        return new PostFinanceEFinance();
      case UNZER_PAYLATER_INVOICE:
        return new PaylaterInvoice();
      case KLARNA:
        return new Klarna();
      case PAYLATER_INSTALLMENT:
        return new PaylaterInstallment();
      default:
        throw new PaymentException("Type '" + typeId + "' is currently not supported by the SDK");
    }
  }

  private JsonIdObject getJsonObjectFromTypeId(String typeId) {
    String paymentType = getTypeIdentifier(typeId);
    PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnumByShortName(paymentType);
    switch (paymentTypeEnum) {
      case EPS:
      case GIROPAY:
      case INVOICE:
      case INVOICE_GUARANTEED:
      case INVOICE_FACTORING:
      case INVOICE_SECURED:
      case PREPAYMENT:
      case PRZELEWY24:
      case SOFORT:
      case ALIPAY:
      case WECHATPAY:
      case PF_CARD:
      case PF_EFINANCE:
      case UNZER_PAYLATER_INVOICE:
      case KLARNA:
        return new JsonIdObject();
      case PAYPAL:
        return new JsonPaypal();
      case CARD:
        return new JsonCard();
      case IDEAL:
        return new JsonIdeal();
      case SEPA_DIRECT_DEBIT:
      case SEPA_DIRECT_DEBIT_GUARANTEED:
      case SEPA_DIRECT_DEBIT_SECURED:
        return new JsonSepaDirectDebit();
      case PIS:
        return new JsonPis();
      case APPLEPAY:
        return new JsonApplepayResponse();
      case HIRE_PURCHASE_RATE_PLAN:
      case INSTALLMENT_SECURED_RATE_PLAN:
        return new JsonInstallmentSecuredRatePlan();
      case BANCONTACT:
        return new JsonBancontact();
      case PAYLATER_INSTALLMENT:
        return new JsonPaylaterInstallment();
      default:
        throw new PaymentException("Type '" + typeId + "' is currently not supported by the SDK");
    }
  }

  private String getTypeIdentifier(String typeId) {
    return typeId.substring(2, 5);
  }

  public <T extends PaymentType> T updatePaymentType(T paymentType)
      throws HttpCommunicationException {
    String url = urlUtil.getRestUrl(paymentType);
    url = addId(url, paymentType.getId());
    String response = restCommunication.httpPut(url, unzer.getPrivateKey(), paymentType);
    JsonIdObject jsonResponse = jsonParser.fromJson(response, JsonIdObject.class);
    return fetchPaymentType(jsonResponse.getId());
  }

  private String addId(String url, String id) {
    if (!url.endsWith("/")) {
      url += "/";
    }
    return url + id;
  }

  public Customer createCustomer(Customer customer) throws HttpCommunicationException {
    String response =
        restCommunication.httpPost(urlUtil.getRestUrl(customer), unzer.getPrivateKey(),
            jsonToBusinessClassMapper.map(customer));
    JsonIdObject jsonId = jsonParser.fromJson(response, JsonIdObject.class);
    return fetchCustomer(jsonId.getId());
  }

  public Customer fetchCustomer(String customerId)
      throws HttpCommunicationException, PaymentException {
    Customer customer = new Customer("", "");
    customer.setId(customerId);
    String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(customer, customer.getId()),
        unzer.getPrivateKey());
    JsonCustomer json = jsonParser.fromJson(response, JsonCustomer.class);
    return jsonToBusinessClassMapper.mapToBusinessObject(new Customer("", ""), json);
  }

  public Customer updateCustomer(String id, Customer customer) throws HttpCommunicationException {
    restCommunication.httpPut(urlUtil.getHttpGetUrl(customer, id), unzer.getPrivateKey(),
        jsonToBusinessClassMapper.map(customer));
    return fetchCustomer(id);
  }

  public Basket updateBasket(String id, Basket basket) throws HttpCommunicationException {
    restCommunication.httpPut(urlUtil.getHttpGetUrl(basket, id), unzer.getPrivateKey(), basket);
    return fetchBasket(id);
  }

  public Basket fetchBasket(String id) throws HttpCommunicationException {
    String response;

    try {
      // Try fetch Basket version 2
      response = restCommunication.httpGet(
          urlUtil.getHttpGetUrl(new Basket().setTotalValueGross(BigDecimal.ONE), id),
          // basket v2 has totalvaluegross. this object is not sent to api
          unzer.getPrivateKey()
      );
    } catch (PaymentException ex) {
      // ... or Basket version 1
      if (ex.getStatusCode() == 404) { // not found
        response = restCommunication.httpGet(
            urlUtil.getHttpGetUrl(new Basket(), id),
            unzer.getPrivateKey()
        );
      } else {
        throw ex;
      }
    }

    Basket basket = jsonParser.fromJson(response, Basket.class);
    basket.setId(id);
    return basket;
  }

  public Metadata createMetadata(Metadata metadata) throws HttpCommunicationException {
    String response =
        restCommunication.httpPost(urlUtil.getRestUrl(metadata), unzer.getPrivateKey(),
            metadata.getMetadataMap());
    Metadata metadataJson = jsonParser.fromJson(response, Metadata.class);
    metadata.setUnzer(unzer);
    metadata.setId(metadataJson.getId());
    return metadata;
  }

  public Metadata fetchMetadata(String id) throws HttpCommunicationException {
    Metadata metadata = new Metadata();
    metadata.setId(id);
    String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(metadata, metadata.getId()),
        unzer.getPrivateKey());
    Map<String, String> metadataMap = jsonParser.fromJson(response, HashMap.class);
    metadata.setMetadataMap(metadataMap);
    return metadata;
  }

  public Basket createBasket(Basket basket) throws HttpCommunicationException {
    String response =
        restCommunication.httpPost(urlUtil.getRestUrl(basket), unzer.getPrivateKey(), basket);
    Basket jsonBasket = jsonParser.fromJson(response, Basket.class);
    basket.setId(jsonBasket.getId());
    return basket;
  }

  /**
   * Execute an normal authorization.
   *
   * @param authorization refers to normal authorization request.
   * @return Authorization refers to an authorization response with id, paymentId,
   * etc.
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public Authorization authorize(Authorization authorization) throws HttpCommunicationException {
    String response =
        restCommunication.httpPost(urlUtil.getRestUrl(authorization), unzer.getPrivateKey(),
            jsonToBusinessClassMapper.map(authorization));
    JsonAuthorization jsonAuthorization = jsonParser.fromJson(response, JsonAuthorization.class);
    authorization = (Authorization) jsonToBusinessClassMapper.mapToBusinessObject(authorization,
        jsonAuthorization);
    authorization.setPayment(fetchPayment(jsonAuthorization.getResources().getPaymentId()));
    authorization.setUnzer(unzer);
    return authorization;
  }

  public Payment fetchPayment(String paymentId) throws HttpCommunicationException {
    Payment payment = new Payment(unzer);
    return fetchPayment(payment, paymentId);
  }

  private Payment fetchPayment(Payment payment, String paymentId)
      throws HttpCommunicationException {
    payment.setId(paymentId);

    String response = getPayment(payment);
    JsonPayment jsonPayment = jsonParser.fromJson(response, JsonPayment.class);
    payment = jsonToBusinessClassMapper.mapToBusinessObject(payment, jsonPayment);
    payment.setCancelList(
        fetchCancelList(payment, getCancelsFromTransactions(jsonPayment.getTransactions())));
    payment.setAuthorization(
        fetchAuthorization(payment, getAuthorizationFromTransactions(jsonPayment.getTransactions()))
    );
    payment.setChargesList(
        fetchChargeList(payment, getChargesFromTransactions(jsonPayment.getTransactions())));
    payment.setPayoutList(
        fetchPayoutList(payment, getPayoutFromTransactions(jsonPayment.getTransactions())));
    return payment;
  }

  protected <T extends AbstractPayment> String getPayment(T payment)
      throws HttpCommunicationException {
    return restCommunication.httpGet(urlUtil.getHttpGetUrl(payment, payment.getId()),
        unzer.getPrivateKey());
  }

  private List<Cancel> fetchCancelList(Payment payment,
                                       List<JsonTransaction> jsonChargesTransactionList)
      throws HttpCommunicationException {
    List<Cancel> cancelList = new ArrayList<Cancel>();

    if (jsonChargesTransactionList != null && !jsonChargesTransactionList.isEmpty()) {
      for (JsonTransaction jsonTransaction : jsonChargesTransactionList) {
        Cancel cancel = fetchCancel(payment, new Cancel(unzer), jsonTransaction.getUrl());
        cancel.setType(jsonTransaction.getType());
        cancelList.add(cancel);
      }
    }
    return cancelList;
  }

  protected List<JsonTransaction> getCancelsFromTransactions(List<JsonTransaction> transactions) {
    List<JsonTransaction> cancelsList = new ArrayList<JsonTransaction>();
    for (JsonTransaction jsonTransaction : transactions) {
      if (TRANSACTION_TYPE_CANCEL_AUTHORIZE.equalsIgnoreCase(jsonTransaction.getType())
          || TRANSACTION_TYPE_CANCEL_CHARGE.equalsIgnoreCase(jsonTransaction.getType())) {
        cancelsList.add(jsonTransaction);
      }
    }
    return cancelsList;
  }

  private Authorization fetchAuthorization(Payment payment, JsonTransaction jsonTransaction)
      throws HttpCommunicationException {
    if (jsonTransaction == null) {
      return null;
    }
    Authorization authorization =
        fetchAuthorization(payment, new Authorization(), jsonTransaction.getUrl());
    authorization.setPayment(payment);
    authorization.setResourceUrl(jsonTransaction.getUrl());
    authorization.setType(jsonTransaction.getType());
    authorization.setCancelList(getCancelListForAuthorization(payment.getCancelList()));
    authorization.setBasketId(payment.getBasketId());
    return authorization;
  }

  private JsonTransaction getAuthorizationFromTransactions(List<JsonTransaction> transactions) {
    List<JsonTransaction> authorizeList = getAuthorizationsFromTransactions(transactions);
    return !authorizeList.isEmpty() ? authorizeList.get(0) : null;
  }

  private List<Charge> fetchChargeList(Payment payment,
                                       List<JsonTransaction> jsonChargesTransactionList)
      throws HttpCommunicationException {
    if (jsonChargesTransactionList == null || jsonChargesTransactionList.isEmpty()) {
      return new ArrayList<>();
    }
    List<Charge> chargesList = new ArrayList<>();
    for (JsonTransaction jsonTransaction : jsonChargesTransactionList) {
      Charge charge = fetchCharge(payment, new Charge(unzer), jsonTransaction.getUrl());
      charge.setCancelList(getCancelListForCharge(charge.getId(), payment.getCancelList()));
      charge.setType(jsonTransaction.getType());
      charge.setBasketId(payment.getBasketId());
      charge.setCustomerId(payment.getCustomerId());
      charge.setMetadataId(payment.getMetadataId());
      chargesList.add(charge);
    }
    return chargesList;
  }

  protected List<JsonTransaction> getChargesFromTransactions(List<JsonTransaction> transactions) {
    List<JsonTransaction> chargesList = new ArrayList<JsonTransaction>();
    for (JsonTransaction jsonTransaction : transactions) {
      if (TRANSACTION_TYPE_CHARGE.equalsIgnoreCase(jsonTransaction.getType())) {
        chargesList.add(jsonTransaction);
      }
    }
    return chargesList;
  }

  private List<Payout> fetchPayoutList(Payment payment, List<JsonTransaction> jsonTransactionList)
      throws HttpCommunicationException {
    if (jsonTransactionList == null || jsonTransactionList.isEmpty()) {
      return new ArrayList<>();
    }
    List<Payout> payoutList = new ArrayList<>();
    for (JsonTransaction jsonTransaction : jsonTransactionList) {
      Payout payout = fetchPayout(payment, new Payout(unzer), jsonTransaction.getUrl());
      payout.setType(jsonTransaction.getType());
      payout.setBasketId(payment.getBasketId());
      payoutList.add(payout);
    }
    return payoutList;
  }

  private List<JsonTransaction> getPayoutFromTransactions(List<JsonTransaction> transactions) {
    List<JsonTransaction> payoutList = new ArrayList<JsonTransaction>();
    for (JsonTransaction jsonTransaction : transactions) {
      if (TRANSACTION_TYPE_PAYOUT.equalsIgnoreCase(jsonTransaction.getType())) {
        payoutList.add(jsonTransaction);
      }
    }
    return payoutList;
  }

  private Cancel fetchCancel(Payment payment, Cancel cancel, URL url)
      throws HttpCommunicationException {
    String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
    JsonCancel jsonCancel = jsonParser.fromJson(response, JsonCancel.class);
    cancel = (Cancel) jsonToBusinessClassMapper.mapToBusinessObject(cancel, jsonCancel);
    cancel.setPayment(payment);
    cancel.setResourceUrl(url);
    return cancel;
  }

  private Authorization fetchAuthorization(Payment payment, Authorization authorization, URL url)
      throws HttpCommunicationException {
    String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
    JsonAuthorization jsonAuthorization = jsonParser.fromJson(response, JsonAuthorization.class);
    authorization = (Authorization) jsonToBusinessClassMapper.mapToBusinessObject(authorization,
        jsonAuthorization);
    authorization.setCancelList(getCancelListForAuthorization(payment.getCancelList()));
    authorization.setUnzer(unzer);
    return authorization;
  }

  private List<Cancel> getCancelListForAuthorization(List<Cancel> cancelList) {
    if (cancelList == null) {
      return new ArrayList<Cancel>();
    }
    List<Cancel> authorizationCancelList = new ArrayList<Cancel>();
    for (Cancel cancel : cancelList) {
      if (TRANSACTION_TYPE_CANCEL_AUTHORIZE.equalsIgnoreCase(cancel.getType())) {
        authorizationCancelList.add(cancel);
      }
    }
    return authorizationCancelList;

  }

  protected List<JsonTransaction> getAuthorizationsFromTransactions(
      List<JsonTransaction> transactions) {
    List<JsonTransaction> authorizeList = new ArrayList<JsonTransaction>();
    for (JsonTransaction jsonTransaction : transactions) {
      if (TRANSACTION_TYPE_AUTHORIZATION.equalsIgnoreCase(jsonTransaction.getType())) {
        authorizeList.add(jsonTransaction);
      }
    }
    return authorizeList;
  }

  private Charge fetchCharge(Payment payment, Charge charge, URL url)
      throws HttpCommunicationException {
    String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
    JsonCharge jsonCharge = jsonParser.fromJson(response, JsonCharge.class);
    charge = (Charge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
    charge.setInvoiceId(jsonCharge.getInvoiceId());
    charge.setPayment(payment);
    charge.setResourceUrl(url);
    return charge;
  }

  private List<Cancel> getCancelListForCharge(String chargeId, List<Cancel> cancelList) {
    if (cancelList == null) {
      return new ArrayList<Cancel>();
    }
    List<Cancel> chargeCancelList = new ArrayList<Cancel>();
    for (Cancel cancel : cancelList) {
      if (TRANSACTION_TYPE_CANCEL_CHARGE.equalsIgnoreCase(cancel.getType())
          && containsChargeId(cancel.getResourceUrl(), chargeId)) {
        chargeCancelList.add(cancel);
      }
    }
    return chargeCancelList;
  }

  private Payout fetchPayout(Payment payment, Payout payout, URL url)
      throws HttpCommunicationException {
    String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
    JsonPayout jsonPayout = jsonParser.fromJson(response, JsonPayout.class);
    payout = (Payout) jsonToBusinessClassMapper.mapToBusinessObject(payout, jsonPayout);
    payout.setPayment(payment);
    payout.setResourceUrl(url);
    return payout;
  }

  private boolean containsChargeId(URL resourceUrl, String chargeId) {
    return resourceUrl.toString().contains(chargeId);
  }

  /**
   * Update an Authorization transaction with PATCH method
   * and returns the resulting Authorization resource.
   *
   * @param authorization The Authorization object containing transaction specific information.
   * @return Authorization The resulting object of the Authorization resource.
   */
  public Authorization updateAuthorization(Authorization authorization)
      throws HttpCommunicationException {
    return this.updateAuthorization(authorization,
        urlUtil.getPaymentUrl(authorization, authorization.getPaymentId()));
  }

  private Authorization updateAuthorization(Authorization authorization, String url)
      throws HttpCommunicationException {
    String response = restCommunication.httpPatch(url, unzer.getPrivateKey(),
        jsonToBusinessClassMapper.map(authorization));
    JsonAuthorization jsonCharge = jsonParser.fromJson(response, JsonAuthorization.class);
    authorization =
        (Authorization) jsonToBusinessClassMapper.mapToBusinessObject(authorization, jsonCharge);
    authorization.setPayment(fetchPayment(jsonCharge.getResources().getPaymentId()));
    authorization.setPaymentId(jsonCharge.getResources().getPaymentId());
    authorization.setUnzer(unzer);
    return authorization;
  }

  public Charge charge(Charge charge) throws HttpCommunicationException {
    return charge(charge, urlUtil.getRestUrl(charge));
  }

  private Charge charge(Charge charge, String url) throws HttpCommunicationException {
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
        jsonToBusinessClassMapper.map(charge));
    JsonCharge jsonCharge = jsonParser.fromJson(response, JsonCharge.class);
    charge = (Charge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
    charge.setInvoiceId(jsonCharge.getInvoiceId());
    charge.setPayment(fetchPayment(jsonCharge.getResources().getPaymentId()));
    charge.setPaymentId(jsonCharge.getResources().getPaymentId());
    charge.setUnzer(unzer);
    return charge;
  }

  /**
   * Update a Charge transaction with PATCH method and returns the resulting Charge resource.
   *
   * @param charge The Charge object containing transaction specific information
   * @return Charge The resulting object of the Charge resource.
   */
  public Charge updateCharge(Charge charge) throws HttpCommunicationException {
    return updateCharge(charge, urlUtil.getPaymentUrl(charge, charge.getPaymentId()));
  }

  private Charge updateCharge(Charge charge, String url) throws HttpCommunicationException {
    String response = restCommunication.httpPatch(url, unzer.getPrivateKey(),
        jsonToBusinessClassMapper.map(charge));
    JsonCharge jsonCharge = jsonParser.fromJson(response, JsonCharge.class);
    charge = (Charge) jsonToBusinessClassMapper.mapToBusinessObject(charge, jsonCharge);
    charge.setInvoiceId(jsonCharge.getInvoiceId());
    charge.setPayment(fetchPayment(jsonCharge.getResources().getPaymentId()));
    charge.setPaymentId(jsonCharge.getResources().getPaymentId());
    charge.setUnzer(unzer);
    return charge;
  }

  public Payout payout(Payout payout) throws HttpCommunicationException {
    return payout(payout, urlUtil.getRestUrl(payout));
  }

  private Payout payout(Payout payout, String url) throws HttpCommunicationException {
    JsonObject json = jsonToBusinessClassMapper.map(payout);
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(), json);
    JsonPayout jsonPayout = jsonParser.fromJson(response, JsonPayout.class);
    payout = (Payout) jsonToBusinessClassMapper.mapToBusinessObject(payout, jsonPayout);
    payout.setPayment(fetchPayment(jsonPayout.getResources().getPaymentId()));
    payout.setPaymentId(jsonPayout.getResources().getPaymentId());
    payout.setUnzer(unzer);
    return payout;
  }

  public Charge chargeAuthorization(Charge charge) throws HttpCommunicationException {
    return charge(charge, urlUtil.getPaymentUrl(charge, charge.getPaymentId()));
  }

  public Cancel cancelAuthorization(String paymentId) throws HttpCommunicationException {
    Cancel cancel = new Cancel();
    return cancelAuthorization(paymentId, cancel);
  }

  public Cancel cancelAuthorization(String paymentId, Cancel cancel)
      throws HttpCommunicationException {
    return cancel(cancel, urlUtil.getPaymentUrl(cancel, paymentId));
  }

  private Cancel cancel(Cancel cancel, String url) throws HttpCommunicationException {
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
        jsonToBusinessClassMapper.map(cancel));
    JsonCancel jsonCancel = jsonParser.fromJson(response, JsonCancel.class);
    cancel = (Cancel) jsonToBusinessClassMapper.mapToBusinessObject(cancel, jsonCancel);
    cancel.setPayment(fetchPayment(jsonCancel.getResources().getPaymentId()));
    cancel.setUnzer(unzer);
    return cancel;
  }

  public Cancel cancelAuthorization(String paymentId, BigDecimal amount)
      throws HttpCommunicationException {
    Cancel cancel = new Cancel();
    cancel.setAmount(amount);
    return cancelAuthorization(paymentId, cancel);
  }

  public Cancel cancelCharge(String paymentId, String chargeId) throws HttpCommunicationException {
    Cancel cancel = new Cancel();
    return cancelCharge(paymentId, chargeId, cancel);
  }

  public Cancel cancelCharge(String paymentId, String chargeId, Cancel cancel)
      throws HttpCommunicationException {
    return cancel(cancel, urlUtil.getRefundUrl(paymentId, chargeId));
  }

  public Cancel cancelCharge(String paymentId, String chargeId, BigDecimal amount)
      throws HttpCommunicationException {
    Cancel cancel = new Cancel();
    cancel.setAmount(amount);
    return cancelCharge(paymentId, chargeId, cancel);
  }

  public Shipment shipment(String paymentId, String invoiceId, String orderId)
      throws HttpCommunicationException {
    return shipment(new Shipment(invoiceId, orderId),
        urlUtil.getPaymentUrl(new Shipment(), paymentId));
  }

  private Shipment shipment(Shipment shipment, String url) throws HttpCommunicationException {
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(), shipment);
    JsonShipment jsonShipment = jsonParser.fromJson(response, JsonShipment.class);
    shipment = jsonToBusinessClassMapper.mapToBusinessObject(shipment, jsonShipment);
    shipment.setPayment(fetchPayment(jsonShipment.getResources().getPaymentId()));
    shipment.setUnzer(unzer);
    return shipment;
  }

  public Shipment doShipment(Shipment shipment, String paymentId)
      throws HttpCommunicationException {
    return shipment(shipment, urlUtil.getPaymentUrl(new Shipment(), paymentId));
  }

  public Recurring recurring(Recurring recurring) throws HttpCommunicationException {
    String url = urlUtil.getRecurringUrl(recurring);
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
        jsonToBusinessClassMapper.map(recurring));
    JsonRecurring json = jsonParser.fromJson(response, JsonRecurring.class);
    recurring = jsonToBusinessClassMapper.mapToBusinessObject(recurring, json);
    recurring.setUnzer(unzer);
    return recurring;

  }

  public String deleteCustomer(String customerId) throws HttpCommunicationException {
    String response =
        restCommunication.httpDelete(urlUtil.getHttpGetUrl(new Customer("a", "b"), customerId),
            unzer.getPrivateKey());
    if (response == null || response.isEmpty()) {
      throw new PaymentException("Customer '" + customerId + "' cannot be deleted");
    }
    JsonIdObject idResponse = new JsonParser().fromJson(response, JsonIdObject.class);
    return idResponse.getId();
  }

  public Authorization fetchAuthorization(String paymentId) throws HttpCommunicationException {
    return fetchPayment(paymentId).getAuthorization();
  }

  public Charge fetchCharge(String paymentId, String chargeId) throws HttpCommunicationException {
    return fetchPayment(paymentId).getCharge(chargeId);
  }

  public Payout fetchPayout(String paymentId, String payoutId) throws HttpCommunicationException {
    return fetchPayment(paymentId).getPayout(payoutId);
  }

  public Cancel fetchCancel(String paymentId, String cancelId) throws HttpCommunicationException {
    return fetchPayment(paymentId).getCancel(cancelId);
  }

  public Cancel fetchCancel(String paymentId, String chargeId, String cancelId)
      throws HttpCommunicationException {
    return fetchPayment(paymentId).getCharge(chargeId).getCancel(cancelId);
  }

  public PaylaterInvoiceConfig fetchPaymentTypeConfig(PaylaterInvoiceConfigRequest configRequest) throws HttpCommunicationException {
    String url = this.urlUtil.getApiEndpoint() + configRequest.getRequestUrl();
    String response = this.restCommunication.httpGet(url, unzer.getPrivateKey());
    return this.jsonParser.fromJson(response, PaylaterInvoiceConfig.class);
  }
}
package com.unzer.payment.service.marketplace;

import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.json.ApiPayment;
import com.unzer.payment.communication.json.ApiTransaction;
import com.unzer.payment.communication.json.ApiAuthorization;
import com.unzer.payment.communication.json.ApiCancel;
import com.unzer.payment.communication.json.ApiCharge;
import com.unzer.payment.marketplace.MarketplaceAuthorization;
import com.unzer.payment.marketplace.MarketplaceCancel;
import com.unzer.payment.marketplace.MarketplaceCharge;
import com.unzer.payment.marketplace.MarketplacePayment;
import com.unzer.payment.service.PaymentService;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MarketplacePaymentService extends PaymentService {

  public MarketplacePaymentService(Unzer unzer, UnzerRestCommunication restCommunication) {
    super(unzer, restCommunication);
  }

  /**
   * Execute a marketplace authorization.
   *
   * @param authorization refers to normal authorization request.
   * @return MarketplaceAuthorization refers to an authorization response with id, paymentId, etc.
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplaceAuthorization marketplaceAuthorize(MarketplaceAuthorization authorization)
      throws HttpCommunicationException {
    String response =
        restCommunication.httpPost(urlUtil.getRestUrl(authorization), unzer.getPrivateKey(),
            apiToSdkMapper.map(authorization));
    ApiAuthorization jsonAuthorization = jsonParser.fromJson(response, ApiAuthorization.class);
    authorization =
        (MarketplaceAuthorization) apiToSdkMapper.mapToBusinessObject(jsonAuthorization,
            authorization
        );
    authorization.setPayment(
        fetchMarketplacePayment(jsonAuthorization.getResources().getPaymentId()));
    authorization.setUnzer(unzer);
    return authorization;
  }

  /**
   * Execute a marketplace payment fetch action.
   *
   * @param paymentId refers to payment to be fetched.
   * @return MarketplacePayment
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplacePayment fetchMarketplacePayment(String paymentId)
      throws HttpCommunicationException {
    MarketplacePayment payment = new MarketplacePayment(unzer);
    payment.setId(paymentId);
    String response = getPayment(payment);
    ApiPayment apiPayment = jsonParser.fromJson(response, ApiPayment.class);
    payment = apiToSdkMapper.mapToBusinessObject(apiPayment, payment);

    payment.setCancelList(
        fetchCancelList(payment, getCancelsFromTransactions(apiPayment.getTransactions())
        )
    );

    payment.setAuthorizationsList(
        fetchAuthorizationList(
            payment,
            getTypedTransactions(apiPayment.getTransactions(), TransactionType.AUTHORIZE)
        )
    );

    payment.setChargesList(
        fetchChargeList(
            payment,
            getTypedTransactions(apiPayment.getTransactions(), TransactionType.CHARGE)
        )
    );
    return payment;
  }

  private List<MarketplaceCancel> fetchCancelList(MarketplacePayment payment,
                                                  List<ApiTransaction> jsonChargesTransactionList)
      throws HttpCommunicationException {
    List<MarketplaceCancel> cancelList = new ArrayList<MarketplaceCancel>();

    if (jsonChargesTransactionList != null && !jsonChargesTransactionList.isEmpty()) {
      for (ApiTransaction apiTransaction : jsonChargesTransactionList) {
        MarketplaceCancel cancel =
            fetchCancel(payment, new MarketplaceCancel(unzer), apiTransaction.getUrl());
        cancel.setType(apiTransaction.getType());
        cancelList.add(cancel);
      }
    }
    return cancelList;
  }

  private List<MarketplaceAuthorization> fetchAuthorizationList(
      MarketplacePayment payment,
      List<ApiTransaction> apiTransaction
  )
      throws HttpCommunicationException {
    List<MarketplaceAuthorization> authorizationsList =
        new ArrayList<MarketplaceAuthorization>(apiTransaction.size());
    if (!apiTransaction.isEmpty()) {
      for (ApiTransaction json : apiTransaction) {
        MarketplaceAuthorization authorization =
            fetchAuthorization(payment, new MarketplaceAuthorization(unzer), json.getUrl());

        authorization.setCancelList(
            getCancelListByParentId(
                payment.getCancelList(),
                TransactionType.CANCEL_AUTHORIZE.apiName(),
                authorization.getId())
        );
        authorization.setType(json.getType());
        authorization.setBasketId(payment.getBasketId());
        authorization.setCustomerId(payment.getCustomerId());
        authorization.setMetadataId(payment.getMetadataId());
        authorizationsList.add(authorization);
      }
    }

    return authorizationsList;
  }

  private List<MarketplaceCharge> fetchChargeList(MarketplacePayment payment,
                                                  List<ApiTransaction> jsonChargesTransactionList)
      throws HttpCommunicationException {
    List<MarketplaceCharge> chargesList = new ArrayList<MarketplaceCharge>();

    if (jsonChargesTransactionList != null && !jsonChargesTransactionList.isEmpty()) {
      for (ApiTransaction apiTransaction : jsonChargesTransactionList) {
        MarketplaceCharge charge =
            fetchCharge(payment, new MarketplaceCharge(unzer), apiTransaction.getUrl());
        charge.setCancelList(
            getCancelListByParentId(
                payment.getCancelList(),
                TransactionType.CANCEL_CHARGE.apiName(),
                charge.getId())
        );
        charge.setType(apiTransaction.getType());
        charge.setBasketId(payment.getBasketId());
        charge.setCustomerId(payment.getCustomerId());
        charge.setMetadataId(payment.getMetadataId());
        chargesList.add(charge);
      }
    }
    return chargesList;
  }

  private MarketplaceCancel fetchCancel(MarketplacePayment payment, MarketplaceCancel cancel,
                                        URL url) throws HttpCommunicationException {
    String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
    ApiCancel jsonCancel = jsonParser.fromJson(response, ApiCancel.class);
    cancel = (MarketplaceCancel) apiToSdkMapper.mapToBusinessObject(jsonCancel, cancel);
    cancel.setPayment(payment);
    cancel.setResourceUrl(url);
    return cancel;
  }

  private MarketplaceAuthorization fetchAuthorization(MarketplacePayment payment,
                                                      MarketplaceAuthorization authorization,
                                                      URL url) throws HttpCommunicationException {
    String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
    ApiAuthorization jsonAuthorization = jsonParser.fromJson(response,
        ApiAuthorization.class);
    authorization =
        (MarketplaceAuthorization) apiToSdkMapper.mapToBusinessObject(jsonAuthorization,
            authorization
        );
    authorization.setCancelList(
        getCancelListByParentId(
            payment.getCancelList(),
            TransactionType.CANCEL_AUTHORIZE.apiName(),
            authorization.getId()));
    authorization.setUnzer(unzer);
    return authorization;
  }

  private List<MarketplaceCancel> getCancelListByParentId(List<MarketplaceCancel> cancelList,
                                                          String cancelType, String authorizeId) {
    List<MarketplaceCancel> authorizationCancelList = new ArrayList<MarketplaceCancel>();

    if (cancelList != null) {
      for (MarketplaceCancel cancel : cancelList) {
        if (cancelType.equalsIgnoreCase(cancel.getType())
            && cancel.getResourceUrl().toString().contains(authorizeId)) {
          authorizationCancelList.add(cancel);
        }
      }
    }
    return authorizationCancelList;

  }

  private MarketplaceCharge fetchCharge(MarketplacePayment payment, MarketplaceCharge charge,
                                        URL url) throws HttpCommunicationException {
    String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
    ApiCharge jsonCharge = jsonParser.fromJson(response, ApiCharge.class);
    charge = (MarketplaceCharge) apiToSdkMapper.mapToBusinessObject(jsonCharge, charge);
    charge.setInvoiceId(jsonCharge.getInvoiceId());
    charge.setPayment(payment);
    charge.setResourceUrl(url);
    return charge;
  }

  /**
   * Execute a marketplace charge.
   *
   * @param charge refers to normal charge request.
   * @return MarketplaceCharge
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplaceCharge marketplaceCharge(MarketplaceCharge charge)
      throws HttpCommunicationException {
    String response = restCommunication.httpPost(urlUtil.getRestUrl(charge), unzer.getPrivateKey(),
        apiToSdkMapper.map(charge));
    ApiCharge jsonCharge = jsonParser.fromJson(response, ApiCharge.class);
    charge = (MarketplaceCharge) apiToSdkMapper.mapToBusinessObject(jsonCharge, charge);
    charge.setInvoiceId(jsonCharge.getInvoiceId());
    charge.setPayment(fetchMarketplacePayment(jsonCharge.getResources().getPaymentId()));
    charge.setPaymentId(jsonCharge.getResources().getPaymentId());
    charge.setUnzer(unzer);
    return charge;
  }

  /**
   * Execute a marketplace charge for one authorization.
   *
   * @param paymentId   refers to marketplace payment.
   * @param authorizeId refers to marketplace authorization to be charged.
   * @param charge      refers to charge request with amount, payment reference, etc.
   * @return
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplaceCharge marketplaceChargeAuthorization(String paymentId, String authorizeId,
                                                          MarketplaceCharge charge)
      throws HttpCommunicationException {
    String url = urlUtil.getRestUrl().concat("/")
        .concat(charge.getChargeAuthorizationUrl(paymentId, authorizeId));
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
        apiToSdkMapper.map(charge));
    ApiCharge jsonCharge = jsonParser.fromJson(response, ApiCharge.class);
    charge = (MarketplaceCharge) apiToSdkMapper.mapToBusinessObject(jsonCharge, charge);
    charge.setInvoiceId(jsonCharge.getInvoiceId());
    charge.setPayment(fetchMarketplacePayment(jsonCharge.getResources().getPaymentId()));
    charge.setPaymentId(jsonCharge.getResources().getPaymentId());
    charge.setUnzer(unzer);
    return charge;
  }

  /**
   * Execute a marketplace full charge for single or multiple authorization(s).
   *
   * @param paymentId        refers to marketplace payment.
   * @param paymentReference refers to marketplace payment note.
   * @return MarketplacePayment
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplacePayment marketplaceFullChargeAuthorizations(String paymentId,
                                                                String paymentReference)
      throws HttpCommunicationException {
    MarketplaceCharge charge = new MarketplaceCharge();
    charge.setPaymentReference(paymentReference);

    String url =
        urlUtil.getRestUrl().concat("/").concat(charge.getFullChargeAuthorizationsUrl(paymentId));
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
        apiToSdkMapper.map(charge));
    ApiPayment apiPayment = jsonParser.fromJson(response, ApiPayment.class);

    MarketplacePayment paymentResponse = new MarketplacePayment(this.unzer);
    paymentResponse.setId(paymentId);

    paymentResponse = apiToSdkMapper.mapToBusinessObject(apiPayment, paymentResponse);
    paymentResponse.setCancelList(
        fetchCancelList(
            paymentResponse,
            getCancelsFromTransactions(apiPayment.getTransactions())
        )
    );

    paymentResponse.setAuthorizationsList(
        fetchAuthorizationList(
            paymentResponse,
            getTypedTransactions(apiPayment.getTransactions(), TransactionType.AUTHORIZE)
        )
    );

    paymentResponse.setChargesList(
        fetchChargeList(
            paymentResponse,
            getTypedTransactions(apiPayment.getTransactions(), TransactionType.CHARGE)
        )
    );
    return paymentResponse;
  }

  /**
   * Execute a marketplace charge fetch action.
   *
   * @param paymentId refers to payment to be fetched.
   * @param chargeId  refers to charge to be fetched.
   * @return MarketplaceCharge
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplaceCharge fetchMarketplaceCharge(String paymentId, String chargeId)
      throws HttpCommunicationException {
    MarketplaceCharge charge = new MarketplaceCharge(unzer);
    charge.setId(chargeId);
    String response = restCommunication.httpGet(urlUtil.getPaymentUrl(charge, paymentId, chargeId),
        unzer.getPrivateKey());
    ApiCharge jsonCharge = jsonParser.fromJson(response, ApiCharge.class);
    charge = (MarketplaceCharge) apiToSdkMapper.mapToBusinessObject(jsonCharge, charge);
    charge.setPayment(fetchMarketplacePayment(jsonCharge.getResources().getPaymentId()));
    return charge;
  }

  /**
   * Execute a marketplace authorization fetch action.
   *
   * @param paymentId   refers to payment to be fetched.
   * @param authorizeId refers to authorization to be fetched.
   * @return MarketplaceAuthorization
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplaceAuthorization fetchMarketplaceAuthorization(String paymentId,
                                                                String authorizeId)
      throws HttpCommunicationException {
    MarketplaceAuthorization authorization = new MarketplaceAuthorization(unzer);
    authorization.setId(authorizeId);
    String response =
        restCommunication.httpGet(urlUtil.getPaymentUrl(authorization, paymentId, authorizeId),
            unzer.getPrivateKey());
    ApiAuthorization jsonAuthorization = jsonParser.fromJson(response, ApiAuthorization.class);
    authorization =
        (MarketplaceAuthorization) apiToSdkMapper.mapToBusinessObject(jsonAuthorization,
            authorization
        );
    authorization.setPayment(
        fetchMarketplacePayment(jsonAuthorization.getResources().getPaymentId()));
    return authorization;
  }

  /**
   * Execute a marketplace full cancel for single or multiple authorization(s).
   *
   * @param paymentId refers to payment to be cancelled.
   * @param cancel    refers to cancel request.
   * @return MarketplacePayment
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplacePayment marketplaceFullAuthorizationsCancel(String paymentId,
                                                                MarketplaceCancel cancel)
      throws HttpCommunicationException {
    String url = urlUtil.getRestUrl().concat(cancel.getFullAuthorizeCancelUrl(paymentId));
    return marketplaceFullCancel(paymentId, url, cancel);
  }

  private MarketplacePayment marketplaceFullCancel(String paymentId, String url,
                                                   MarketplaceCancel cancel)
      throws HttpCommunicationException {
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
        apiToSdkMapper.map(cancel));
    ApiPayment apiPayment = jsonParser.fromJson(response, ApiPayment.class);
    MarketplacePayment paymentResponse = new MarketplacePayment(this.unzer);
    paymentResponse.setId(paymentId);
    paymentResponse = apiToSdkMapper.mapToBusinessObject(apiPayment, paymentResponse);

    paymentResponse.setCancelList(
        fetchCancelList(
            paymentResponse,
            getCancelsFromTransactions(apiPayment.getTransactions())
        )
    );

    paymentResponse.setAuthorizationsList(
        fetchAuthorizationList(
            paymentResponse,
            getTypedTransactions(apiPayment.getTransactions(), TransactionType.AUTHORIZE)
        )
    );

    paymentResponse.setChargesList(
        fetchChargeList(
            paymentResponse,
            getTypedTransactions(apiPayment.getTransactions(), TransactionType.CHARGE)
        )
    );
    return paymentResponse;
  }

  /**
   * Execute a marketplace full cancel for single or multiple charge(s).
   *
   * @param paymentId refers to payment to be cancelled.
   * @param cancel    refers to cancel request.
   * @return MarketplacePayment
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplacePayment marketplaceFullChargesCancel(String paymentId, MarketplaceCancel cancel)
      throws HttpCommunicationException {
    String url = urlUtil.getRestUrl().concat(cancel.getFullChargesCancelUrl(paymentId));
    return marketplaceFullCancel(paymentId, url, cancel);
  }

  /**
   * Execute a marketplace cancel for one authorization.
   *
   * @param paymentId   refers to payment to be cancelled.
   * @param authorizeId refers to authorization to be cancelled.
   * @param cancel      refers to cancel request.
   * @return MarketplaceCancel
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplaceCancel marketplaceAuthorizationCancel(String paymentId, String authorizeId,
                                                          MarketplaceCancel cancel)
      throws HttpCommunicationException {
    String url =
        urlUtil.getRestUrl().concat(cancel.getPartialAuthorizeCancelUrl(paymentId, authorizeId));
    return marketplaceCancel(paymentId, url, cancel);
  }

  private MarketplaceCancel marketplaceCancel(
      String paymentId,
      String url,
      MarketplaceCancel cancel
  )
      throws HttpCommunicationException {
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
        apiToSdkMapper.map(cancel));
    ApiCancel jsonCancel = jsonParser.fromJson(response, ApiCancel.class);
    cancel = (MarketplaceCancel) apiToSdkMapper.mapToBusinessObject(jsonCancel, cancel);
    cancel.setPayment(fetchMarketplacePayment(paymentId));
    cancel.setUnzer(unzer);
    return cancel;
  }

  /**
   * Execute a marketplace cancel for one charge.
   *
   * @param paymentId refers to payment to be cancelled.
   * @param cancel    refers to cancel request.
   * @return MarketplaceCancel
   * @throws HttpCommunicationException generic Payment API communication error
   */
  public MarketplaceCancel marketplaceChargeCancel(String paymentId, String chargeId,
                                                   MarketplaceCancel cancel)
      throws HttpCommunicationException {
    String url = urlUtil.getRestUrl().concat(cancel.getPartialChargeCancelUrl(paymentId, chargeId));
    return marketplaceCancel(paymentId, url, cancel);
  }
}

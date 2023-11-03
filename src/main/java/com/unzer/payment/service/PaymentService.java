package com.unzer.payment.service;

import com.unzer.payment.*;
import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.json.*;
import com.unzer.payment.communication.json.paylater.ApiInstallmentPlans;
import com.unzer.payment.communication.mapper.ApiToSdkConverter;
import com.unzer.payment.models.PaylaterInvoiceConfig;
import com.unzer.payment.models.PaylaterInvoiceConfigRequest;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaymentService {
    protected UnzerRestCommunication restCommunication;
    protected UrlUtil urlUtil;
    protected ApiToSdkConverter apiToSdkMapper = new ApiToSdkConverter();
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

    public PaylaterInstallmentPlans fetchPaylaterInstallmentPlans(
            InstallmentPlansRequest installmentPlansRequest
    ) throws HttpCommunicationException {
        String response = restCommunication.httpGet(
                this.urlUtil.getInstallmentPlanUrl(installmentPlansRequest),
                unzer.getPrivateKey()
        );

        ApiInstallmentPlans json = jsonParser.fromJson(response,
                ApiInstallmentPlans.class);
        return apiToSdkMapper.mapToBusinessObject(new PaylaterInstallmentPlans(), json);
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

    public <T extends PaymentType> T createPaymentType(T type)
            throws HttpCommunicationException {
        String response = restCommunication.httpPost(
                urlUtil.getUrl(type),
                unzer.getPrivateKey(),
                type
        );
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        return fetchPaymentType(jsonResponse.getId());
    }

    @SuppressWarnings("unchecked")
    public <T extends PaymentType> T fetchPaymentType(String typeId)
            throws HttpCommunicationException {
        PaymentType paymentType = getPaymentTypeFromTypeId(typeId);
        ((BasePaymentType) paymentType).setUnzer(unzer);
        String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(typeId),
                unzer.getPrivateKey());
        ApiIdObject jsonPaymentType =
                jsonParser.fromJson(response, getJsonObjectFromTypeId(typeId).getClass());
        return (T) apiToSdkMapper.mapToBusinessObject(paymentType, jsonPaymentType);
    }

    private PaymentType getPaymentTypeFromTypeId(String typeId) {
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
            case PAYU:
                return new PayU();
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
            case PAYLATER_DIRECT_DEBIT:
                return new PaylaterDirectDebit();
            default:
                throw new PaymentException("Type '" + typeId + "' is currently not supported by the SDK");
        }
    }

    private ApiIdObject getJsonObjectFromTypeId(String typeId) {
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
            case PAYU:
            case SOFORT:
            case ALIPAY:
            case WECHATPAY:
            case PF_CARD:
            case PF_EFINANCE:
            case UNZER_PAYLATER_INVOICE:
            case KLARNA:
                return new ApiIdObject();
            case PAYPAL:
                return new ApiPaypal();
            case CARD:
                return new ApiCard();
            case IDEAL:
                return new ApiIdeal();
            case SEPA_DIRECT_DEBIT:
            case SEPA_DIRECT_DEBIT_GUARANTEED:
            case SEPA_DIRECT_DEBIT_SECURED:
                return new ApiSepaDirectDebit();
            case PIS:
                return new ApiPis();
            case APPLEPAY:
                return new ApiApplepayResponse();
            case HIRE_PURCHASE_RATE_PLAN:
            case INSTALLMENT_SECURED_RATE_PLAN:
                return new ApiInstallmentSecuredRatePlan();
            case BANCONTACT:
                return new ApiBancontact();
            case PAYLATER_INSTALLMENT:
                return new ApiPaylaterInstallment();
            case PAYLATER_DIRECT_DEBIT:
                return new ApiSepaDirectDebit();
            default:
                throw new PaymentException("Type '" + typeId + "' is currently not supported by the SDK");
        }
    }

    private String getTypeIdentifier(String typeId) {
        return typeId.substring(2, 5);
    }

    public <T extends PaymentType> T updatePaymentType(T paymentType)
            throws HttpCommunicationException {
        String url = urlUtil.getUrl(paymentType);
        String response = restCommunication.httpPut(url, unzer.getPrivateKey(), paymentType);
        ApiIdObject jsonResponse = jsonParser.fromJson(response, ApiIdObject.class);
        return fetchPaymentType(jsonResponse.getId());
    }

    public Customer createCustomer(Customer customer) throws HttpCommunicationException {
        String response =
                restCommunication.httpPost(urlUtil.getUrl(customer), unzer.getPrivateKey(),
                        apiToSdkMapper.map(customer));
        ApiIdObject jsonId = jsonParser.fromJson(response, ApiIdObject.class);
        return fetchCustomer(jsonId.getId());
    }

    public Customer fetchCustomer(String customerId)
            throws HttpCommunicationException, PaymentException {
        Customer customer = new Customer("", "");
        customer.setId(customerId);
        String response = restCommunication.httpGet(
                urlUtil.getUrl(customer),
                unzer.getPrivateKey()
        );
        ApiCustomer json = jsonParser.fromJson(response, ApiCustomer.class);
        return apiToSdkMapper.mapToBusinessObject(json, new Customer("", ""));
    }

    public Customer updateCustomer(String id, Customer customer) throws HttpCommunicationException {
        customer.setId(id);
        restCommunication.httpPut(urlUtil.getUrl(customer), unzer.getPrivateKey(),
                apiToSdkMapper.map(customer));
        return fetchCustomer(id);
    }

    public Basket updateBasket(Basket basket) throws HttpCommunicationException {
        restCommunication.httpPut(urlUtil.getUrl(basket), unzer.getPrivateKey(), basket);
        return fetchBasket(basket.getId());
    }

    public Basket fetchBasket(String id) throws HttpCommunicationException {
        String response;

        try {
            // Try fetch Basket version 2

            // basket v2 has totalvaluegross. this object is not sent to api
            Basket basket = new Basket()
                    .setId(id)
                    .setTotalValueGross(BigDecimal.ONE);
            response = restCommunication.httpGet(
                    urlUtil.getUrl(basket),
                    unzer.getPrivateKey()
            );
        } catch (PaymentException ex) {
            // ... or Basket version 1
            if (ex.getStatusCode() == 404) { // not found
                response = restCommunication.httpGet(
                        urlUtil.getUrl(new Basket().setId(id)),
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
        String response = restCommunication.httpPost(
                urlUtil.getUrl(metadata),
                unzer.getPrivateKey(),
                metadata.getMetadataMap()
        );
        Metadata metadataJson = jsonParser.fromJson(response, Metadata.class);
        metadata.setUnzer(unzer);
        metadata.setId(metadataJson.getId());
        return metadata;
    }

    public Metadata fetchMetadata(String id) throws HttpCommunicationException {
        Metadata metadata = new Metadata();
        metadata.setId(id);
        String response = restCommunication.httpGet(
                urlUtil.getUrl(metadata),
                unzer.getPrivateKey()
        );
        Map<String, String> metadataMap = jsonParser.fromJson(response, HashMap.class);
        metadata.setMetadataMap(metadataMap);
        return metadata;
    }

    public Basket createBasket(Basket basket) throws HttpCommunicationException {
        String response = restCommunication.httpPost(
                urlUtil.getUrl(basket),
                unzer.getPrivateKey(),
                basket
        );
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
        String response = restCommunication.httpPost(
                urlUtil.getUrl(authorization), unzer.getPrivateKey(),
                apiToSdkMapper.map(authorization));
        ApiAuthorization jsonAuthorization = jsonParser.fromJson(response, ApiAuthorization.class);
        authorization = (Authorization) apiToSdkMapper.mapToBusinessObject(jsonAuthorization,
                authorization
        );
        authorization.setPayment(fetchPayment(jsonAuthorization.getResources().getPaymentId()));
        authorization.setUnzer(unzer);
        return authorization;
    }

    public Payment fetchPayment(String paymentId) throws HttpCommunicationException {
        Payment payment = new Payment(unzer);
        return fetchPayment(payment, paymentId);
    }

    private Payment fetchPayment(Payment sdkPayment, String paymentId)
            throws HttpCommunicationException {
        sdkPayment.setId(paymentId);

        String response = getPayment(sdkPayment);
        ApiPayment apiPayment = jsonParser.fromJson(response, ApiPayment.class);
        sdkPayment = apiToSdkMapper.mapToBusinessObject(apiPayment, sdkPayment);


        sdkPayment.setCancelList(
                fetchCancelList(
                        sdkPayment,
                        getCancelsFromTransactions(apiPayment.getTransactions())
                )
        );

        sdkPayment.setAuthorization(
                fetchAuthorization(
                        sdkPayment,
                        getAuthorizationFromTransactions(apiPayment.getTransactions())
                )
        );

        sdkPayment.setChargesList(
                fetchChargeList(
                        sdkPayment,
                        getTypedTransactions(apiPayment.getTransactions(), TransactionType.CHARGE)
                )
        );

        sdkPayment.setPayoutList(
                fetchPayoutList(
                        sdkPayment,
                        getTypedTransactions(apiPayment.getTransactions(), TransactionType.PAYOUT)
                )
        );

        sdkPayment.setChargebackList(
                fetchChargebackList(
                        sdkPayment,
                        getTypedTransactions(apiPayment.getTransactions(), TransactionType.CHARGEBACK)
                )
        );
        return sdkPayment;
    }

    protected <T extends BasePayment> String getPayment(T payment)
            throws HttpCommunicationException {
        return restCommunication.httpGet(
                urlUtil.getUrl(payment),
                unzer.getPrivateKey()
        );
    }

    private List<Cancel> fetchCancelList(
            Payment sdkPayment,
            List<ApiTransaction> apiChargeTransactions
    )
            throws HttpCommunicationException {
        List<Cancel> cancelList = new ArrayList<Cancel>();

        if (apiChargeTransactions != null && !apiChargeTransactions.isEmpty()) {
            for (ApiTransaction apiTransaction : apiChargeTransactions) {
                Cancel cancel = fetchCancel(sdkPayment, new Cancel(unzer), apiTransaction.getUrl());
                cancel.setType(apiTransaction.getType());
                cancelList.add(cancel);
            }
        }
        return cancelList;
    }

    protected List<ApiTransaction> getCancelsFromTransactions(List<ApiTransaction> transactions) {
        return Stream.concat(
                        getTypedTransactions(transactions, TransactionType.CANCEL_AUTHORIZE).stream(),
                        getTypedTransactions(transactions, TransactionType.CANCEL_CHARGE).stream())
                .collect(Collectors.toList());
    }

    private Authorization fetchAuthorization(Payment payment, ApiTransaction apiTransaction)
            throws HttpCommunicationException {
        if (apiTransaction == null) {
            return null;
        }
        Authorization authorization =
                fetchAuthorization(payment, new Authorization(), apiTransaction.getUrl());
        authorization.setPayment(payment);
        authorization.setResourceUrl(apiTransaction.getUrl());
        authorization.setType(apiTransaction.getType());
        authorization.setCancelList(
                payment.getCancelList()
                        .parallelStream()
                        .filter(c -> c.getType().equalsIgnoreCase(TransactionType.CANCEL_AUTHORIZE.apiName()))
                        .collect(Collectors.toList())
        );
        authorization.setBasketId(payment.getBasketId());
        return authorization;
    }

    private ApiTransaction getAuthorizationFromTransactions(List<ApiTransaction> transactions) {
        List<ApiTransaction> authorizeList =
                getTypedTransactions(transactions, TransactionType.AUTHORIZE);
        return authorizeList.isEmpty() ? null : authorizeList.get(0);
    }

    private List<Charge> fetchChargeList(
            Payment payment,
            List<ApiTransaction> jsonChargesTransactionList
    ) throws HttpCommunicationException {
        if (jsonChargesTransactionList == null || jsonChargesTransactionList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Charge> chargesList = new ArrayList<>();
        for (ApiTransaction apiTransaction : jsonChargesTransactionList) {
            Charge charge = fetchCharge(payment, new Charge(unzer), apiTransaction.getUrl());
            charge.setCancelList(getCancelListForCharge(payment.getCancelList(), charge.getId()));
            charge.setType(apiTransaction.getType());
            charge.setBasketId(payment.getBasketId());
            charge.setCustomerId(payment.getCustomerId());
            charge.setMetadataId(payment.getMetadataId());
            chargesList.add(charge);
        }
        return chargesList;
    }

    protected List<ApiTransaction> getTypedTransactions(
            List<ApiTransaction> transactions,
            TransactionType type
    ) {
        return transactions
                .parallelStream()
                .filter(t -> t.getType().equalsIgnoreCase(type.apiName()))
                .collect(Collectors.toList());
    }

    private List<Payout> fetchPayoutList(Payment payment, List<ApiTransaction> apiTransactionList)
            throws HttpCommunicationException {
        if (apiTransactionList == null || apiTransactionList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Payout> payoutList = new ArrayList<>();
        for (ApiTransaction apiTransaction : apiTransactionList) {
            Payout payout = fetchPayout(payment, new Payout(unzer), apiTransaction.getUrl());
            payout.setType(apiTransaction.getType());
            payout.setBasketId(payment.getBasketId());
            payoutList.add(payout);
        }
        return payoutList;
    }

    private List<Chargeback> fetchChargebackList(Payment payment, List<ApiTransaction> chargebacks) {
        return chargebacks.parallelStream()
                .map(t -> fetchChargeback(payment, t.getUrl()))
                .collect(Collectors.toList());
    }

    private Cancel fetchCancel(Payment payment, Cancel cancel, URL url)
            throws HttpCommunicationException {
        String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
        ApiCancel jsonCancel = jsonParser.fromJson(response, ApiCancel.class);
        cancel = (Cancel) apiToSdkMapper.mapToBusinessObject(jsonCancel, cancel);
        cancel.setPayment(payment);
        cancel.setResourceUrl(url);
        return cancel;
    }

    private Authorization fetchAuthorization(Payment payment, Authorization authorization, URL url)
            throws HttpCommunicationException {
        String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
        ApiAuthorization jsonAuthorization = jsonParser.fromJson(response, ApiAuthorization.class);
        authorization = (Authorization) apiToSdkMapper.mapToBusinessObject(
                jsonAuthorization,
                authorization
        );
        authorization.setCancelList(payment.getCancelList().parallelStream()
                .filter(c -> c.getType().equalsIgnoreCase(TransactionType.CANCEL_AUTHORIZE.apiName()))
                .collect(
                        Collectors.toList()
                )
        );
        authorization.setUnzer(unzer);
        return authorization;
    }

    private Charge fetchCharge(Payment payment, Charge charge, URL url)
            throws HttpCommunicationException {
        String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
        ApiCharge jsonCharge = jsonParser.fromJson(response, ApiCharge.class);
        charge = (Charge) apiToSdkMapper.mapToBusinessObject(jsonCharge, charge);
        charge.setInvoiceId(jsonCharge.getInvoiceId());
        charge.setPayment(payment);
        charge.setResourceUrl(url);
        return charge;
    }

    private List<Cancel> getCancelListForCharge(List<Cancel> list, String chargeId) {
        return list.parallelStream()
                .filter(c -> c.getType().equalsIgnoreCase(TransactionType.CANCEL_CHARGE.apiName()))
                .filter(c -> c.getResourceUrl().toString().contains(chargeId))
                .collect(Collectors.toList());
    }

    private Payout fetchPayout(Payment payment, Payout payout, URL url)
            throws HttpCommunicationException {
        String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
        ApiPayout jsonPayout = jsonParser.fromJson(response, ApiPayout.class);
        payout = (Payout) apiToSdkMapper.mapToBusinessObject(jsonPayout, payout);
        payout.setPayment(payment);
        payout.setResourceUrl(url);
        return payout;
    }

    private Chargeback fetchChargeback(Payment payment, URL url) {
        String response = restCommunication.httpGet(url.toString(), unzer.getPrivateKey());
        ApiChargeback apiChargeback = jsonParser.fromJson(response, ApiChargeback.class);
        Chargeback chargeback = (Chargeback) apiToSdkMapper
                .mapToBusinessObject(
                        apiChargeback, new Chargeback()
                );
        chargeback.setPayment(payment);
        chargeback.setResourceUrl(url);
        return chargeback;
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
        return this.updateAuthorization(
                authorization,
                urlUtil.getUrl(authorization)
        );
    }

    private Authorization updateAuthorization(Authorization authorization, String url)
            throws HttpCommunicationException {
        String response = restCommunication.httpPatch(url, unzer.getPrivateKey(),
                apiToSdkMapper.map(authorization));
        ApiAuthorization jsonCharge = jsonParser.fromJson(response, ApiAuthorization.class);
        authorization = (Authorization) apiToSdkMapper.mapToBusinessObject(jsonCharge, authorization);
        authorization.setPayment(fetchPayment(jsonCharge.getResources().getPaymentId()));
        authorization.setPaymentId(jsonCharge.getResources().getPaymentId());
        authorization.setUnzer(unzer);
        return authorization;
    }

    public Charge charge(Charge charge) throws HttpCommunicationException {
        return charge(charge, urlUtil.getUrl(charge));
    }

    private Charge charge(Charge charge, String url) throws HttpCommunicationException {
        String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
                apiToSdkMapper.map(charge));
        ApiCharge jsonCharge = jsonParser.fromJson(response, ApiCharge.class);
        charge = (Charge) apiToSdkMapper.mapToBusinessObject(jsonCharge, charge);
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
        return updateCharge(charge, urlUtil.getUrl(charge));
    }

    private Charge updateCharge(Charge charge, String url) throws HttpCommunicationException {
        String response = restCommunication.httpPatch(url, unzer.getPrivateKey(),
                apiToSdkMapper.map(charge));
        ApiCharge jsonCharge = jsonParser.fromJson(response, ApiCharge.class);
        charge = (Charge) apiToSdkMapper.mapToBusinessObject(jsonCharge, charge);
        charge.setInvoiceId(jsonCharge.getInvoiceId());
        charge.setPayment(fetchPayment(jsonCharge.getResources().getPaymentId()));
        charge.setPaymentId(jsonCharge.getResources().getPaymentId());
        charge.setUnzer(unzer);
        return charge;
    }

    public Payout payout(Payout payout) throws HttpCommunicationException {
        ApiObject json = apiToSdkMapper.map(payout);
        String response =
                restCommunication.httpPost(urlUtil.getUrl(payout), unzer.getPrivateKey(), json);
        ApiPayout jsonPayout = jsonParser.fromJson(response, ApiPayout.class);
        payout = (Payout) apiToSdkMapper.mapToBusinessObject(jsonPayout, payout);
        payout.setPayment(fetchPayment(jsonPayout.getResources().getPaymentId()));
        payout.setPaymentId(jsonPayout.getResources().getPaymentId());
        payout.setUnzer(unzer);
        return payout;
    }

    public Charge chargeAuthorization(Charge charge) throws HttpCommunicationException {
        return charge(charge, urlUtil.getUrl(charge));
    }

    public Cancel cancelAuthorization(String paymentId) throws HttpCommunicationException {
        Cancel cancel = new Cancel();
        return cancelAuthorization(paymentId, cancel);
    }

    public Cancel cancelAuthorization(String paymentId, Cancel cancel)
            throws HttpCommunicationException {
        cancel.setPaymentId(paymentId);
        return cancel(cancel, urlUtil.getUrl(cancel));
    }

    private Cancel cancel(Cancel cancel, String url) throws HttpCommunicationException {
        String response = restCommunication.httpPost(
                url, unzer.getPrivateKey(),
                apiToSdkMapper.map(cancel)
        );
        ApiCancel jsonCancel = jsonParser.fromJson(response, ApiCancel.class);
        cancel = (Cancel) apiToSdkMapper.mapToBusinessObject(jsonCancel, cancel);
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
        return shipment(
                new Shipment(invoiceId, orderId),
                urlUtil.getUrl(new Shipment().setPaymentId(paymentId))
        );
    }

    private Shipment shipment(Shipment shipment, String url) throws HttpCommunicationException {
        String response = restCommunication.httpPost(url, unzer.getPrivateKey(), shipment);
        ApiShipment jsonShipment = jsonParser.fromJson(response, ApiShipment.class);
        shipment = apiToSdkMapper.mapToBusinessObject(jsonShipment, shipment);
        shipment.setPayment(fetchPayment(jsonShipment.getResources().getPaymentId()));
        shipment.setUnzer(unzer);
        return shipment;
    }

    public Shipment doShipment(Shipment shipment, String paymentId)
            throws HttpCommunicationException {
        return shipment(shipment, urlUtil.getUrl(new Shipment().setPaymentId(paymentId)));
    }

    public Recurring recurring(Recurring recurring) throws HttpCommunicationException {
        String url = urlUtil.getUrl(recurring);
        String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
                apiToSdkMapper.map(recurring));
        ApiRecurring json = jsonParser.fromJson(response, ApiRecurring.class);
        recurring = apiToSdkMapper.mapToBusinessObject(recurring, json);
        recurring.setUnzer(unzer);
        return recurring;

    }

    public String deleteCustomer(String customerId) throws HttpCommunicationException {
        Customer customer = new Customer("a", "b");
        customer.setId(customerId);
        customer.setCustomerId(customerId);
        String response =
                restCommunication.httpDelete(
                        urlUtil.getUrl(customer),
                        unzer.getPrivateKey()
                );
        if (response == null || response.isEmpty()) {
            throw new PaymentException("Customer '" + customerId + "' cannot be deleted");
        }
        ApiIdObject idResponse = new JsonParser().fromJson(response, ApiIdObject.class);
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

    public PaylaterInvoiceConfig fetchPaymentTypeConfig(PaylaterInvoiceConfigRequest configRequest)
            throws HttpCommunicationException {
        String response = this.restCommunication.httpGet(
                this.urlUtil.getPaymentTypeConfigUrl(configRequest),
                unzer.getPrivateKey()
        );
        return this.jsonParser.fromJson(response, PaylaterInvoiceConfig.class);
    }

    protected enum TransactionType {
        AUTHORIZE, CHARGE, CHARGEBACK, PAYOUT, CANCEL_AUTHORIZE, CANCEL_CHARGE;

        public String apiName() {
            return this.name().toLowerCase().replaceAll("_", "-");
        }
    }
}
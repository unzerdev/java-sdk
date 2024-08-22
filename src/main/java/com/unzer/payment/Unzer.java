package com.unzer.payment;

import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.enums.RecurrenceType;
import com.unzer.payment.marketplace.MarketplaceAuthorization;
import com.unzer.payment.marketplace.MarketplaceCancel;
import com.unzer.payment.marketplace.MarketplaceCharge;
import com.unzer.payment.marketplace.MarketplacePayment;
import com.unzer.payment.models.*;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.PaylaterInstallment;
import com.unzer.payment.paymenttypes.PaymentType;
import com.unzer.payment.service.*;
import com.unzer.payment.service.marketplace.MarketplacePaymentService;
import com.unzer.payment.util.JwtHelper;
import com.unzer.payment.webhook.Webhook;
import com.unzer.payment.webhook.WebhookList;
import lombok.Getter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * {@code Unzer} is a facade to the Unzer REST Api. The facade is
 * initialized with your privateKey. For having no forced dependencies you can
 * inject an appropriate implementation of {@code UnzerRestCommunication}
 * implementing the http-communication Layer. If you are fine with apache's
 * httpClient you can choose the {@code HttpClientBasedRestCommunication}.
 *
 * @see UnzerRestCommunication for details of the http-layer abstraction.
 */
public class Unzer {
    @Getter
    private final String privateKey;
    @Getter
    private String jwtToken;

    private final transient PaymentService paymentService;
    private final transient MarketplacePaymentService marketplacePaymentService;
    private final transient PaypageService paypageService;
    private final transient LinkpayService linkpayService;
    private final transient WebhookService webhookService;
    private final transient TokenService tokenService;

    public Unzer(String privateKey) {
        this(privateKey, null, null);
    }

    public Unzer(String privateKey, Locale locale, String endPoint) {
        this(privateKey, locale, endPoint, null);
    }

    /**
     * Creates an instance of the {@code Unzer}-facade.
     *
     * @param clientIp   -  sets CLIENT_IP header for Payment API request
     * @param privateKey - your private key as generated within the unzer Intelligence Platform (hIP)
     * @param endPoint   - the endPoint for the outgoing connection, in case of null,
     *                   the value of unzer.properties will be considered
     */
    public Unzer(String privateKey, Locale locale, String endPoint, String clientIp) {
        this(new HttpClientBasedRestCommunication(locale, clientIp), privateKey, endPoint);
    }

    /**
     * Creates an instance of the {@code Unzer}-facade.
     *
     * @param restCommunication an appropriate implementation of {@code UnzerRestCommunication}.
     *                          If you are fine with apache's httpCLient you might choose
     *                          {@code HttpClientBasedRestCommunication}.
     * @param privateKey        private key as generated within the unzer Intelligence Platform (hIP)
     * @param endPoint          the endPoint for the outgoing connection, in case of null,
     *                          the value of unzer.properties will be considered
     */
    public Unzer(UnzerRestCommunication restCommunication, String privateKey, String endPoint) {
        assert privateKey != null && !privateKey.isEmpty() : "privateKey must be non-empty";
        this.privateKey = privateKey;
        this.paymentService = new PaymentService(this, restCommunication);
        this.marketplacePaymentService = new MarketplacePaymentService(this, restCommunication);
        this.paypageService = new PaypageService(this, restCommunication);
        this.linkpayService = new LinkpayService(this, restCommunication);
        this.webhookService = new WebhookService(this, restCommunication);
        this.tokenService = new TokenService(this, restCommunication);
    }

    public Unzer(String privateKey, Locale locale) {
        this(privateKey, locale, null);
    }

    /**
     * Creates an instance of the {@code Unzer}-facade.
     *
     * @param restCommunication an appropriate implementation of {@code UnzerRestCommunication}.
     *                          If you are fine with apache's httpCLient you might choose
     *                          {@code HttpClientBasedRestCommunication}.
     * @param privateKey        your private key as generated within the Unzer Intelligence Platform
     */
    public Unzer(UnzerRestCommunication restCommunication, String privateKey) {
        this(restCommunication, privateKey, null);
    }

    /**
     * Update a customers data. All parameters that are null are ignored. Only the
     * non null values are updated.
     *
     * @param id       Customer id to be updated
     * @param customer Object ti be used for the update
     * @return Customer updated customer
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Customer updateCustomer(String id, Customer customer) throws HttpCommunicationException {
        return paymentService.updateCustomer(id, customer);
    }

    /**
     * Delete a customer at Unzer
     *
     * @param customerId Customer id to be deleted
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public String deleteCustomer(String customerId) throws HttpCommunicationException {
        return paymentService.deleteCustomer(customerId);
    }

    public Metadata createMetadata(Metadata metadata) throws HttpCommunicationException {
        return paymentService.createMetadata(metadata);
    }

    public Metadata fetchMetadata(String id) throws HttpCommunicationException {
        return paymentService.fetchMetadata(id);
    }

    public Basket fetchBasket(String id) throws HttpCommunicationException {
        return paymentService.fetchBasket(id);
    }

    @Deprecated
    public Basket updateBasket(Basket basket, String id) throws HttpCommunicationException {
        basket.setId(id);
        return paymentService.updateBasket(basket);
    }

    public <T extends PaymentType> T updatePaymentType(T paymentType)
            throws HttpCommunicationException {
        if (paymentType != null && paymentType.getId() == null) {
            return paymentService.createPaymentType(paymentType);
        } else if (paymentType != null && paymentType.getId() != null) {
            return paymentService.updatePaymentType(paymentType);
        } else {
            return null;
        }
    }

    /**
     * Authorize call for redirect payments with a returnUrl and existed payment type.
     *
     * @param amount    Amount used for the authorization
     * @param currency  Currency used for the authorization
     * @param typeId    Payment type id used for the authorization
     * @param returnUrl ReturnURL where after the payment was finished
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl)
            throws HttpCommunicationException {
        return authorize(amount, currency, typeId, returnUrl, null, null);
    }

    /**
     * Authorize call for redirect payments with returnUrl, existed payment type and existed customer.
     * <br>
     * <b>Note:</b> Keypair must have either 3ds channel (card3ds=true)
     * or non-3ds channel (card3ds=false) or both
     *
     * @param amount     Amount used for the authorization
     * @param currency   Currency used for the authorization
     * @param typeId     Payment type id used for the authorization
     * @param returnUrl  ReturnURL where after the payment was finished
     * @param customerId used for the authorization
     * @param card3ds    Flag to specify whether to force 3ds or not
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                                   String customerId, Boolean card3ds)
            throws HttpCommunicationException {
        return authorize(
                getAuthorization(amount, currency, typeId, returnUrl, customerId, card3ds, null));
    }

    /**
     * Authorize call with an Authorization object. The Authorization object must
     * have at least an amount, a currency, a typeId.
     *
     * @param authorization Authorization object.
     *                      <br>
     *                      <b>Note:</b> even if <code>MarketplaceAuthorization</code> is used,
     *                      (<code>MarketplaceAuthorization extends Authorization</code>),
     *                      a normal authorization will be executed.
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(Authorization authorization) throws HttpCommunicationException {
        return paymentService.authorize(authorization);
    }

    private Authorization getAuthorization(BigDecimal amount, Currency currency, String paymentTypeId,
                                           URL returnUrl, String customerId, Boolean card3ds,
                                           RecurrenceType recurrenceType) {
        Authorization authorization = (Authorization) new Authorization(this)
                .setAmount(amount)
                .setCurrency(currency)
                .setReturnUrl(returnUrl)
                .setTypeId(paymentTypeId)
                .setCustomerId(customerId)
                .setCard3ds(card3ds);
        setRecurrenceType(authorization, recurrenceType);
        return authorization;
    }

    private void setRecurrenceType(BaseTransaction transaction, RecurrenceType recurrenceType) {
        if (recurrenceType == null) {
            return;
        }

        if (transaction.getAdditionalTransactionData() == null) {
            transaction.setAdditionalTransactionData(new AdditionalTransactionData());
        }

        if (transaction.getAdditionalTransactionData().getCard() == null) {
            transaction.getAdditionalTransactionData().setCard(new CardTransactionData());
        }

        transaction.getAdditionalTransactionData().getCard().setRecurrenceType(recurrenceType);

    }

    /**
     * Authorize call for redirect payments with a returnUrl and existed payment type.
     *
     * @param amount    Amount used for the authorization
     * @param currency  Currency used for the authorization
     * @param typeId    Payment type id used for the authorization
     * @param returnUrl ReturnURL where after the payment was finished
     * @param card3ds   Flag to specify whether to force 3ds or not
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                                   Boolean card3ds)
            throws HttpCommunicationException {
        return authorize(amount, currency, typeId, returnUrl, null, card3ds);
    }

    /**
     * Authorize call for redirect payments with returnUrl and existed payment type.
     * The customer must not exist. The customer will be created before executing auhtorization.
     *
     * @param amount    Amount used for the authorization
     * @param currency  Currency used for the authorization
     * @param typeId    Payment type id used for the authorization
     * @param returnUrl ReturnURL where after the payment was finished
     * @param customer  Customer used for the authorization
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                                   Customer customer) throws HttpCommunicationException {
        return authorize(amount, currency, typeId, returnUrl,
                getCustomerId(createCustomerIfPresent(customer)), null);
    }

    private String getCustomerId(Customer customer) {
        if (customer == null) {
            return null;
        }
        return customer.getId();
    }

    /**
     * Create a customer if it is not null. In case the customer is null it will
     * return null
     *
     * @param customer used customer for creation
     * @return Customer with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Customer createCustomerIfPresent(Customer customer) throws HttpCommunicationException {
        if (customer == null) {
            return null;
        }
        return createCustomer(customer);
    }

    /**
     * Create a customer. This is only possible for new customers. If the customer
     * is already on the system (has an id) this method will throw an Exception.
     *
     * @param customer Create customer
     * @return Customer with Id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Customer createCustomer(Customer customer) throws HttpCommunicationException {
        if (customer == null) {
            throw new IllegalArgumentException("Customer must not be null");
        }
        if (customer.getId() != null) {
            throw new PaymentException(
                    "Customer has an id set. createCustomer can only be called without Customer.id. "
                            + "Please use updateCustomer or remove the id from Customer.");
        }
        return paymentService.createCustomer(customer);
    }

    /**
     * Authorize call for redirect payments with returnUrl.
     * New payment type will be created before executing authorization.
     *
     * @param amount      Amount used for the authorization
     * @param currency    Currency used for the authorization
     * @param paymentType Payment type used for the authorization
     * @param returnUrl   ReturnURL where after the payment was finished
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType,
                                   URL returnUrl)
            throws HttpCommunicationException {
        return authorize(amount, currency, paymentType, returnUrl, null, null);
    }

    /**
     * Authorize call for redirect payments with returnUrl in 3ds or non-3ds.
     * New paymentType and new customer will be created before executing authorization.
     * * <br>
     * <b>Note:</b> Keypair must have either 3ds channel (card3ds=true)
     * or non-3ds channel (card3ds=false) or both
     *
     * @param amount      Amount used for the authorization
     * @param currency    Currency used for the authorization
     * @param paymentType Payment type used for the authorization
     * @param returnUrl   ReturnURL where after the payment was finished
     * @param customer    used for the authorization
     * @param card3ds     Flag to specify whether to force 3ds or not
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType,
                                   URL returnUrl,
                                   Customer customer, Boolean card3ds)
            throws HttpCommunicationException {
        return authorize(amount, currency, createPaymentType(paymentType).getId(), returnUrl,
                getCustomerId(createCustomerIfPresent(customer)), card3ds);
    }

    /**
     * Create a new PaymentType at Unzer. This can be any Object which
     * implements the Interface PaymentType
     *
     * @param <T>         type of PaymentType object
     * @param paymentType object used for creating a payment type
     * @return PaymentType Object with an id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public <T extends PaymentType> T createPaymentType(T paymentType)
            throws HttpCommunicationException {
        return paymentService.createPaymentType(paymentType);
    }

    /**
     * Authorize call for redirect payments with returnUrl in 3ds or non-3ds.
     * New payment type will be created before executing authorization.
     * <br>
     * <b>Note:</b> Keypair must have either 3ds channel (card3ds=true)
     * or non-3ds channel (card3ds=false) or both
     *
     * @param amount      Amount used for the authorization
     * @param currency    Currency used for the authorization
     * @param paymentType Payment type used for the authorization
     * @param returnUrl   ReturnURL where after the payment was finished
     * @param card3ds     Flag to specify whether to force 3ds or not
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType,
                                   URL returnUrl, Boolean card3ds)
            throws HttpCommunicationException {
        return authorize(amount, currency, paymentType, returnUrl, null, card3ds);
    }

    /**
     * Authorize call for redirect payments with returnUrl.
     * New paymentType and new customer will be created before executing authorization.
     *
     * @param amount      Amount used for the authorization
     * @param currency    Currency used for the authorization
     * @param paymentType Payment type used for the authorization
     * @param returnUrl   ReturnURL where after the payment was finished
     * @param customer    used for the authorization
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType,
                                   URL returnUrl, Customer customer)
            throws HttpCommunicationException {
        if (paymentType.getId() == null) {
            paymentType = createPaymentType(paymentType);
        }
        return authorize(amount, currency, paymentType.getId(), returnUrl,
                getCustomerId(createCustomerIfPresent(customer)), null);
    }

    /**
     * Authorize call for redirect payments with returnUrl, existed payment type and existed customer.
     *
     * @param amount     Amount used for the authorization
     * @param currency   Currency used for the authorization
     * @param typeId     Payment type id used for the authorization
     * @param returnUrl  ReturnURL where after the payment was finished
     * @param customerId used for the authorization
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                                   String customerId) throws HttpCommunicationException {
        return authorize(amount, currency, typeId, returnUrl, customerId, null);
    }

    /**
     * Authorize call for redirect payments with returnUrl, existed payment type and existed customer.
     * <br>
     * <b>Note:</b> Keypair must have either 3ds channel (card3ds=true)
     * or non-3ds channel (card3ds=false) or both
     *
     * @param amount     Amount used for the authorization
     * @param currency   Currency used for the authorization
     * @param typeId     Payment type id used for the authorization
     * @param returnUrl  ReturnURL where after the payment was finished
     * @param customerId used for the authorization
     * @param card3ds    Flag to specify whether to force 3ds or not
     * @return Authorization with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                                   String customerId, Boolean card3ds, RecurrenceType recurrenceType)
            throws HttpCommunicationException {
        return authorize(
                getAuthorization(amount, currency, typeId, returnUrl, customerId, card3ds, recurrenceType));
    }

    /**
     * Authorize call with an MarketplaceAuthorization object. The Authorization object must
     * have at least an amount, a currency, a typeId and basket with participantId.
     *
     * @param authorization MarketplaceAuthorization request model.
     * @return MarketplaceAuthorization with paymentId and authorize id in pending status.
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public MarketplaceAuthorization marketplaceAuthorize(MarketplaceAuthorization authorization)
            throws HttpCommunicationException {
        return marketplacePaymentService.marketplaceAuthorize(authorization);
    }

    /**
     * Update an Authorization transaction with PATCH method and returns the resulting
     * Authorization resource.
     *
     * @param authorization The Authorization object containing transaction specific information.
     * @return Authorization The resulting object of the Authorization resource.
     */
    public Authorization updateAuthorization(Authorization authorization)
            throws HttpCommunicationException {
        return paymentService.updateAuthorization(authorization);
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     *
     * @param amount   Amount usd for the charge
     * @param currency Currency used for the charge
     * @param typeId   Payment type id used for the charge
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId)
            throws HttpCommunicationException {
        return charge(amount, currency, typeId, (URL) null);
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * for redirect payments
     *
     * @param amount    Amount usd for the charge
     * @param currency  Currency used for the charge
     * @param typeId    Payment type id used for the charge
     * @param returnUrl ReturnURL where after the payment was finished
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl)
            throws HttpCommunicationException {
        return charge(amount, currency, typeId, returnUrl, (String) null);
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * for redirect payments
     *
     * @param amount     Amount usd for the charge
     * @param currency   Currency used for the charge
     * @param typeId     Payment type id used for the charge
     * @param returnUrl  ReturnURL where after the payment was finished
     * @param customerId used for the charge
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                         String customerId)
            throws HttpCommunicationException {
        return charge(amount, currency, typeId, returnUrl, customerId, null);
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * for redirect payments
     *
     * @param amount     Amount usd for the charge
     * @param currency   Currency used for the charge
     * @param typeId     Payment type id used for the charge
     * @param returnUrl  ReturnURL where after the payment was finished
     * @param customerId used for the charge
     * @param card3ds    Flag to specify whether to force 3ds or not
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                         String customerId, Boolean card3ds)
            throws HttpCommunicationException {
        return charge(getCharge(amount, currency, typeId, returnUrl, customerId, null, card3ds, null));
    }

    /**
     * Charge call with a Charge object. At least amount, currency and paymentTypeId
     * are mandatory
     *
     * @param charge Charge object
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(Charge charge) throws HttpCommunicationException {
        return paymentService.charge(charge);
    }

    private Charge getCharge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                             String customerId, String basketId, Boolean card3ds,
                             RecurrenceType recurrenceType) {
        Charge charge = (Charge) new Charge()
                .setAmount(amount)
                .setCurrency(currency)
                .setTypeId(typeId)
                .setCustomerId(customerId)
                .setBasketId(basketId)
                .setReturnUrl(returnUrl)
                .setCard3ds(card3ds);
        setRecurrenceType(charge, recurrenceType);
        return charge;
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * and a customerId
     *
     * @param amount     Amount usd for the charge
     * @param currency   Currency used for the charge
     * @param typeId     Payment type id used for the charge
     * @param customerId used for the charge
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId)
            throws HttpCommunicationException {
        return charge(amount, currency, typeId, null, customerId);
    }

    /**
     * Charge call. The PaymentType will be created within this method
     *
     * @param amount      Amount usd for the charge
     * @param currency    Currency used for the charge
     * @param paymentType Payment type used for the charge
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType)
            throws HttpCommunicationException {
        return charge(amount, currency, createPaymentType(paymentType).getId(), null, (String) null);
    }

    /**
     * Charge call for redirect payments. The PaymentType will be created within
     * this method
     *
     * @param amount      Amount usd for the charge
     * @param currency    Currency used for the charge
     * @param paymentType Payment type used for the charge
     * @param returnUrl   ReturnURL where after the payment was finished
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType,
                         URL returnUrl)
            throws HttpCommunicationException {
        if (paymentType.getId() == null) {
            paymentType = createPaymentType(paymentType);
        }
        return charge(amount, currency, paymentType.getId(), returnUrl, "");
    }

    /**
     * Charge call for redirect payments. The PaymentType will be created within
     * this method
     *
     * @param amount      Amount usd for the charge
     * @param currency    Currency used for the charge
     * @param paymentType Payment type used for the charge
     * @param returnUrl   ReturnURL where after the payment was finished
     * @param card3ds     Flag to specify whether to force 3ds or not
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType,
                         URL returnUrl,
                         Boolean card3ds)
            throws HttpCommunicationException {
        return charge(amount, currency, createPaymentType(paymentType).getId(), returnUrl, card3ds);
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * for redirect payments
     *
     * @param amount    Amount usd for the charge
     * @param currency  Currency used for the charge
     * @param typeId    Payment type id used for the charge
     * @param returnUrl ReturnURL where after the payment was finished
     * @param card3ds   Flag to specify whether to force 3ds or not
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                         Boolean card3ds)
            throws HttpCommunicationException {
        return charge(amount, currency, typeId, returnUrl, null, card3ds);
    }

    /**
     * Charge call for redirect payments. The PaymentType will be created within
     * this method
     *
     * @param amount      Amount usd for the charge
     * @param currency    Currency used for the charge
     * @param paymentType Payment type used for the charge
     * @param returnUrl   ReturnURL where after the payment was finished
     * @param customer    used for the charge
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType,
                         URL returnUrl,
                         Customer customer) throws HttpCommunicationException {
        if (paymentType.getId() == null) {
            paymentType = createPaymentType(paymentType);
        }
        return charge(amount, currency, paymentType.getId(), returnUrl,
                getCustomerId(createCustomerIfPresent(customer)));
    }

    /**
     * Charge call for redirect payments. The PaymentType will be created within
     * this method
     *
     * @param amount      Amount usd for the charge
     * @param currency    Currency used for the charge
     * @param paymentType Payment type used for the charge
     * @param returnUrl   ReturnURL where after the payment was finished
     * @param customer    used for the charge
     * @param basket      used for the charge
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType,
                         URL returnUrl,
                         Customer customer, Basket basket) throws HttpCommunicationException {
        if (paymentType.getId() == null) {
            paymentType = createPaymentType(paymentType);
        }
        return charge(amount, currency, paymentType.getId(), returnUrl,
                getCustomerId(createCustomerIfPresent(customer)), getBasketId(createBasket(basket)), null);
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * for redirect payments
     *
     * @param amount     Amount usd for the charge
     * @param currency   Currency used for the charge
     * @param typeId     Payment type id used for the charge
     * @param returnUrl  ReturnURL where after the payment was finished
     * @param customerId used for the charge
     * @param basketId   used for the charge
     * @param card3ds    Flag to specify whether to force 3ds or not
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                         String customerId, String basketId, Boolean card3ds)
            throws HttpCommunicationException {
        return charge(
                getCharge(amount, currency, typeId, returnUrl, customerId, basketId, card3ds, null));
    }

    private String getBasketId(Basket basket) {
        if (basket != null) {
            return basket.getId();
        } else {
            return null;
        }
    }

    public Basket createBasket(Basket basket) throws HttpCommunicationException {
        return paymentService.createBasket(basket);
    }

    /**
     * Charge call for redirect payments. The PaymentType will be created within
     * this method
     *
     * @param amount      Amount usd for the charge
     * @param currency    Currency used for the charge
     * @param paymentType Payment type used for the charge
     * @param returnUrl   ReturnURL where after the payment was finished
     * @param customer    used for the charge
     * @param card3ds     Flag to specify whether to force 3ds or not
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType,
                         URL returnUrl,
                         Customer customer, Boolean card3ds) throws HttpCommunicationException {
        return charge(amount, currency, createPaymentType(paymentType).getId(), returnUrl,
                getCustomerId(createCustomerIfPresent(customer)), card3ds);
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * for redirect payments
     *
     * @param amount     Amount usd for the charge
     * @param currency   Currency used for the charge
     * @param typeId     Payment type id used for the charge
     * @param returnUrl  ReturnURL where after the payment was finished
     * @param customerId used for the charge
     * @param basketId   used for the charge
     * @param card3ds    Flag to specify whether to force 3ds or not
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                         String customerId, String basketId, Boolean card3ds,
                         RecurrenceType recurrenceType)
            throws HttpCommunicationException {
        return charge(getCharge(amount, currency, typeId, returnUrl, customerId, basketId, card3ds,
                recurrenceType));
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * for redirect payments
     *
     * @param amount     Amount usd for the charge
     * @param currency   Currency used for the charge
     * @param typeId     Payment type id used for the charge
     * @param returnUrl  ReturnURL where after the payment was finished
     * @param customerId used for the charge
     * @param card3ds    Flag to specify whether to force 3ds or not
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                         String customerId, Boolean card3ds, RecurrenceType recurrenceType)
            throws HttpCommunicationException {
        return charge(
                getCharge(amount, currency, typeId, returnUrl, customerId, null, card3ds, recurrenceType));
    }

    /**
     * Charge call with a typeId that was created using the Javascript or Mobile SDK
     * for redirect payments
     *
     * @param amount    Amount usd for the charge
     * @param currency  Currency used for the charge
     * @param typeId    Payment type id used for the charge
     * @param returnUrl ReturnURL where after the payment was finished
     * @param card3ds   Flag to specify whether to force 3ds or not
     * @return Charge with paymentId and authorize id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl,
                         Boolean card3ds, RecurrenceType recurrenceType)
            throws HttpCommunicationException {
        return charge(
                getCharge(amount, currency, typeId, returnUrl, null, null, card3ds, recurrenceType));
    }

    /**
     * Charge call with an MarketplaceCharge object. The MarketplaceCharge object must
     * have at least an amount, a currency, a typeId and basket with participantId.
     *
     * @param charge refers to a MarketplaceCharge request model.
     * @return MarketplaceCharge with paymentId and charge id in pending status.
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public MarketplaceCharge marketplaceCharge(MarketplaceCharge charge)
            throws HttpCommunicationException {
        return marketplacePaymentService.marketplaceCharge(charge);
    }

    /**
     * Update a Charge transaction with PATCH method and returns the resulting Charge resource.
     *
     * @param charge The Authorization object containing transaction specific information.
     * @return Charge The resulting object of the Charge resource.
     */
    public Charge updateCharge(Charge charge) throws HttpCommunicationException {
        return paymentService.updateCharge(charge);
    }

    /**
     * Charge call with MarketplaceCharge for a MarketplaceAuthorization.
     * The MarketplaceCharge object must have at least an amount.
     *
     * @param paymentId   refers to a MarketplacePayment id.
     * @param authorizeId refers to a MarketplaceAuthorization id.
     * @param charge      refers to a MarketplaceCharge request model.
     * @return MarketplaceCharge.
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public MarketplaceCharge marketplaceChargeAuthorization(String paymentId, String authorizeId,
                                                            MarketplaceCharge charge)
            throws HttpCommunicationException {
        return marketplacePaymentService.marketplaceChargeAuthorization(paymentId, authorizeId, charge);
    }

    /**
     * Pay out money to the customer.
     *
     * @param amount    Amount usd for the payout
     * @param currency  Currency used for the payout
     * @param typeId    Payment type id used for the payout
     * @param returnUrl ReturnURL where after the payment was finished
     * @return Payout object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Payout payout(BigDecimal amount, Currency currency, String typeId, URL returnUrl)
            throws HttpCommunicationException {
        return payout(getPayout(amount, currency, typeId, returnUrl));
    }

    public Payout payout(Payout payout) throws HttpCommunicationException {
        return paymentService.payout(payout);
    }

    private Payout getPayout(BigDecimal amount, Currency currency, String typeId, URL returnUrl) {
        Payout payout = new Payout();
        payout.setAmount(amount);
        payout.setCurrency(currency);
        payout.setTypeId(typeId);
        payout.setReturnUrl(returnUrl);
        return payout;
    }

    /**
     * Charge (Capture) the full amount that was authorized
     *
     * @param paymentId used for the charge of an authorization
     * @return Charge with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge chargeAuthorization(String paymentId) throws HttpCommunicationException {
        return chargeAuthorization(
                (Charge) new Charge().setPaymentId(paymentId)
        );
    }

    /**
     * Charge (Capture) with custom Charge parameters
     *
     * @param charge used for the charge of an authorization.
     * @return Charge with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge chargeAuthorization(Charge charge) throws HttpCommunicationException {
        return paymentService.chargeAuthorization(charge);
    }

    /**
     * Charge (Capture) partial amount of Authorization. As there is only one Authorization
     * for a Payment id you only need to provide a paymentId
     *
     * @param paymentId used for the charge of an authorization
     * @param amount    used for the charge of an authorization
     * @return Charge with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge chargeAuthorization(String paymentId, BigDecimal amount)
            throws HttpCommunicationException {
        return chargeAuthorization(
                (Charge) new Charge()
                        .setPaymentId(paymentId)
                        .setAmount(amount)
        );
    }

    /**
     * Charge (Capture) with amount and paymentReference
     *
     * @param paymentId        used for the charge of an authorization
     * @param amount           used for the charge of an authorization
     * @param paymentReference used for the charge of an authorization
     * @return Charge with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge chargeAuthorization(String paymentId, BigDecimal amount, String paymentReference)
            throws HttpCommunicationException {
        return chargeAuthorization(
                (Charge) new Charge()
                        .setPaymentId(paymentId)
                        .setAmount(amount)
                        .setPaymentReference(paymentReference)
        );
    }

    /**
     * Charge (Capture) with custom Charge parameters
     *
     * @param paymentId id of a payment to charge
     * @param charge    used for the charge of an authorization.
     * @return Charge with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge chargeAuthorization(String paymentId, Charge charge)
            throws HttpCommunicationException {
        return chargeAuthorization((Charge) charge.setPaymentId(paymentId));
    }

    /**
     * Cancel (Reverse) the full Authorization. As there is only one Authorization for a
     * Payment id you only need to provide a paymentId
     *
     * @param paymentId used for the cancel of an authorization
     * @return Cancel object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel cancelAuthorization(String paymentId) throws HttpCommunicationException {
        return paymentService.cancelAuthorization(paymentId);
    }

    /**
     * Cancel (Reverse) partial amount of Authorization. As there is only one Authorization
     * for a Payment id you only need to provide a paymentId
     *
     * @param paymentId used for the cancel of an authorization
     * @param amount    used for the cancel of an authorization
     * @return Cancel with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel cancelAuthorization(String paymentId, BigDecimal amount)
            throws HttpCommunicationException {
        return paymentService.cancelAuthorization(paymentId, amount);
    }

    /**
     * Cancel (Reverse) Authorize with Cancel object
     *
     * @param paymentId used for the cancel of an authorization
     * @param cancel    object used for the cancelation
     * @return Cancel with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel cancelAuthorization(String paymentId, Cancel cancel)
            throws HttpCommunicationException {
        return paymentService.cancelAuthorization(paymentId, cancel);
    }

    /**
     * Cancel (Refund) full Charge
     *
     * @param paymentId used for the cancel of a charge
     * @param chargeId  used for the cancel of a charge
     * @return Cancel with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel cancelCharge(String paymentId, String chargeId) throws HttpCommunicationException {
        return paymentService.cancelCharge(paymentId, chargeId);
    }

    /**
     * Fully cancel for marketplace authorization(s).
     * <b>Note:</b>: <code>amount</code> will be ignored due to fully cancel.
     * Only <code>paymentReference</code> is processed.
     *
     * @param paymentId refers to the payment.
     * @param cancel    refers to MarketplaceCancel.
     * @return MarketplacePayment
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public MarketplacePayment marketplaceFullAuthorizationsCancel(String paymentId,
                                                                  MarketplaceCancel cancel)
            throws HttpCommunicationException {
        return marketplacePaymentService.marketplaceFullAuthorizationsCancel(paymentId, cancel);
    }

    /**
     * Fully cancel for marketplace
     * <b>Note:</b>: <code>amount</code> will be ignored due to fully cancel.
     * Only <code>paymentReference</code> is processed.
     *
     * @param paymentId refers to the payment.
     * @param cancel    refers to MarketplaceCancel.
     * @return MarketplacePayment
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public MarketplacePayment marketplaceFullChargesCancel(String paymentId, MarketplaceCancel cancel)
            throws HttpCommunicationException {
        return marketplacePaymentService.marketplaceFullChargesCancel(paymentId, cancel);
    }

    /**
     * Cancel for one marketplace authorization.
     *
     * @param paymentId   refers to the payment.
     * @param authorizeId refers to authorization id to be cancelled.
     * @param cancel      refers to MarketplaceCancel.
     * @return MarketplaceCancel
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public MarketplaceCancel marketplaceAuthorizationCancel(String paymentId, String authorizeId,
                                                            MarketplaceCancel cancel)
            throws HttpCommunicationException {
        return marketplacePaymentService.marketplaceAuthorizationCancel(paymentId, authorizeId, cancel);
    }

    /**
     * Cancel for one marketplace charge.
     *
     * @param paymentId refers to the payment.
     * @param chargeId  refers to charge id to be cancelled.
     * @param cancel    refers to MarketplaceCancel.
     * @return MarketplaceCancel
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public MarketplaceCancel marketplaceChargeCancel(String paymentId, String chargeId,
                                                     MarketplaceCancel cancel)
            throws HttpCommunicationException {
        return marketplacePaymentService.marketplaceChargeCancel(paymentId, chargeId, cancel);
    }

    /**
     * Fully charge for marketplace authorization(s).
     * <b>Note:</b>: <code>amount</code> will be ignored due to fully charge.
     * Only <code>paymentReference</code> is processed.
     *
     * @param paymentId refers to the payment.
     * @return MarketplacePayment
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public MarketplacePayment marketplaceFullChargeAuthorizations(String paymentId,
                                                                  String paymentReference)
            throws HttpCommunicationException {
        return marketplacePaymentService.marketplaceFullChargeAuthorizations(paymentId,
                paymentReference);
    }

    /**
     * Cancel (Refund) partial Charge
     *
     * @param paymentId used for the cancel of a charge
     * @param chargeId  used for the cancel of a charge
     * @param amount    used for the cancel of a charge
     * @return Cancel with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel cancelCharge(String paymentId, String chargeId, BigDecimal amount)
            throws HttpCommunicationException {
        return paymentService.cancelCharge(paymentId, chargeId, amount);
    }

    /**
     * Cancel (Refund) partial Charge
     *
     * @param paymentId used for the cancel of a charge
     * @param amount    used for the cancel of a charge
     * @return Cancel with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel cancelCharge(String paymentId, BigDecimal amount)
            throws HttpCommunicationException {
        return paymentService.cancelCharge(paymentId, null, amount);
    }

    /**
     * Cancel (Refund) charge with Cancel object
     *
     * @param paymentId used for the cancel of a charge
     * @param chargeId  used for the cancel of a charge
     * @param cancel    used for the cancel of a charge
     * @return Cancel with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel cancelCharge(String paymentId, String chargeId, Cancel cancel)
            throws HttpCommunicationException {
        return paymentService.cancelCharge(paymentId, chargeId, cancel);
    }

    /**
     * Inform about a shipment of goods. From this time the insurance start.
     *
     * @param paymentId used for the shipment of a payment
     * @return Shipment with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Shipment shipment(String paymentId) throws HttpCommunicationException {
        return paymentService.shipment(paymentId, null, null);
    }

    public Shipment shipment(Shipment shipment, String paymentId) throws HttpCommunicationException {
        return paymentService.doShipment(shipment, paymentId);
    }

    /**
     * Inform about a shipment of goods and provide invoiceId. From this time the insurance start.
     *
     * @param paymentId used for the shipment of a payment
     * @param invoiceId used for the shipment of a payment
     * @return Shipment with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Shipment shipment(String paymentId, String invoiceId) throws HttpCommunicationException {
        return paymentService.shipment(paymentId, invoiceId, null);
    }

    /**
     * Inform about a shipment of goods and provide invoiceId. From this time the insurance start.
     *
     * @param paymentId used for the shipment of a payment
     * @param invoiceId used for the shipment of a payment
     * @param orderId   used for the shipment of a payment
     * @return Shipment with id
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Shipment shipment(String paymentId, String invoiceId, String orderId)
            throws HttpCommunicationException {
        return paymentService.shipment(paymentId, invoiceId, orderId);
    }

    /**
     * Load the whole Payment Object for the given paymentId.
     * <br><br>
     * If this method is being used to fetch marketplace payment,
     * it may cause unexpected response data.
     *
     * @param paymentId used for fetching a payment
     * @return Payment object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Payment fetchPayment(String paymentId) throws HttpCommunicationException {
        return paymentService.fetchPayment(paymentId);
    }

    /**
     * Load the whole Payment Object for the given marketplace paymentId
     * <br><br>
     * If this method is being used to fetch normal payment, it may cause unexpected response data.
     *
     * @param paymentId used for fetching a marketplace payment.
     * @return Payment object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public MarketplacePayment fetchMarketplacePayment(String paymentId)
            throws HttpCommunicationException {
        return marketplacePaymentService.fetchMarketplacePayment(paymentId);
    }

    /**
     * Load the Payout Object
     *
     * @param paymentId used for fetching a payout
     * @param payoutId  used for fetching a payout
     * @return Payout object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Payout fetchPayout(String paymentId, String payoutId) throws HttpCommunicationException {
        return paymentService.fetchPayout(paymentId, payoutId);
    }

    /**
     * Load the Authorization for the given paymentId. As there is only one
     * Authorization for a Payment the Authorization id is not needed.
     *
     * @param paymentId used for fetching an authorization
     * @return Authorization object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Authorization fetchAuthorization(String paymentId) throws HttpCommunicationException {
        return paymentService.fetchAuthorization(paymentId);
    }

    /**
     * Load the Marketplace Authorization for the given paymentId.
     *
     * @param paymentId used for fetching an authorization
     * @return MarketplaceAuthorization object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public MarketplaceAuthorization fetchMarketplaceAuthorization(String paymentId,
                                                                  String authorizeId)
            throws HttpCommunicationException {
        return marketplacePaymentService.fetchMarketplaceAuthorization(paymentId, authorizeId);
    }

    /**
     * Load the Marketplace Charge for the given paymentId.
     *
     * @param paymentId used for fetching a charge
     * @return MarketplaceCharge object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public MarketplaceCharge fetchMarketplaceCharge(String paymentId, String chargeId)
            throws HttpCommunicationException {
        return marketplacePaymentService.fetchMarketplaceCharge(paymentId, chargeId);
    }

    /**
     * Load the Charge for the given paymentId and charge Id.
     *
     * @param paymentId used for fetching a charge
     * @param chargeId  used for fetching a charge
     * @return Charge
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Charge fetchCharge(String paymentId, String chargeId) throws HttpCommunicationException {
        return paymentService.fetchCharge(paymentId, chargeId);
    }

    /**
     * Load the Cancel for the given paymentId and cancelId
     *
     * @param paymentId used for fetching a cancel
     * @param cancelId  used for fetching a cancel
     * @return Cancel
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel fetchCancel(String paymentId, String cancelId) throws HttpCommunicationException {
        return paymentService.fetchCancel(paymentId, cancelId);
    }

    /**
     * Load the Cancel for the given paymentId, chargeId and cancelId
     *
     * @param paymentId used for fetching a cancel
     * @param chargeId  used for fetching a cancel
     * @param cancelId  used for fetching a cancel
     * @return Cancel
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Cancel fetchCancel(String paymentId, String chargeId, String cancelId)
            throws HttpCommunicationException {
        return paymentService.fetchCancel(paymentId, chargeId, cancelId);
    }

    /**
     * Load the Customer data for the given customerId
     *
     * @param customerId used for fetching a customer
     * @return Customer object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Customer fetchCustomer(String customerId)
            throws HttpCommunicationException, PaymentException {
        return paymentService.fetchCustomer(customerId);
    }

    /**
     * Returns links to legal documents (Terms and Conditions, etc).
     *
     * @param customerType Customer type either B2B or B2C. Mandatory
     * @param country      Country in ISO 3166-2 format. Optional
     */
    public PaylaterInvoiceConfig fetchPaylaterConfig(CustomerType customerType, Locale country)
            throws HttpCommunicationException {
        return paymentService.fetchPaymentTypeConfig(
                new PaylaterInvoiceConfigRequest(customerType, country));
    }

    /**
     * Load the PaymentType
     *
     * @param typeId used for fetching a payment type
     * @return PaymentType object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public PaymentType fetchPaymentType(String typeId) throws HttpCommunicationException {
        return paymentService.fetchPaymentType(typeId);
    }

    /**
     * Initiates a paypage and returns the redirectUrl and an id to the paypage. The id will be
     * used for embedded paypage within Javascript components, the redirectUrl will be used
     * for hosted paypage to redirect customer to this url.
     *
     * @param paypage used for initializing a paypage
     * @return PayPage object
     * @throws HttpCommunicationException in case communication to Unzer didn't work
     */
    public Paypage paypage(Paypage paypage) throws HttpCommunicationException {
        if (Objects.isNull(paypage.getAction())) {
            paypage.setAction(BasePaypage.Action.CHARGE);
        }
        return paypageService.initialize(paypage);
    }

    /**
     * Retrieves created Paypage by id
     *
     * @param id paypage id
     * @return paypage
     */
    public Paypage fetchPaypage(String id) {
        return paypageService.fetch(id);
    }

    public Linkpay linkpay(Linkpay linkpay) throws HttpCommunicationException {
        if (Objects.isNull(linkpay.getAction())) {
            linkpay.setAction(BasePaypage.Action.CHARGE);
        }
        return linkpayService.initialize(linkpay);
    }

    /**
     * Retrieves created Linkpay by id
     *
     * @param id paypage id
     * @return paypage
     */
    public Linkpay fetchLinkpay(String id) {
        return linkpayService.fetch(id);
    }

    public Recurring recurring(String typeId, String customerId, String metadataId, URL returnUrl,
                               RecurrenceType recurrenceType) throws HttpCommunicationException {
        return paymentService.recurring(
                getRecurring(typeId, customerId, metadataId, returnUrl, recurrenceType));
    }

    private Recurring getRecurring(String typeId, String customerId, String metadataId, URL returnUrl,
                                   RecurrenceType recurrenceType) {
        Recurring recurring = new Recurring();
        recurring.setCustomerId(customerId);
        recurring.setTypeId(typeId);
        recurring.setReturnUrl(returnUrl);
        recurring.setMetadataId(metadataId);
        setRecurrenceType(recurring, recurrenceType);
        return recurring;
    }

    public Recurring recurring(String typeId, URL returnUrl) throws HttpCommunicationException {
        return recurring(typeId, null, returnUrl);
    }

    public Recurring recurring(String typeId, String customerId, URL returnUrl)
            throws HttpCommunicationException {
        return recurring(typeId, customerId, null, returnUrl);
    }

    public Recurring recurring(String typeId, String customerId, String metadataId, URL returnUrl)
            throws HttpCommunicationException {
        return paymentService.recurring(getRecurring(typeId, customerId, metadataId, returnUrl));
    }

    private Recurring getRecurring(String typeId, String customerId, String metadataId,
                                   URL returnUrl) {
        Recurring recurring = new Recurring();
        recurring.setCustomerId(customerId);
        recurring.setTypeId(typeId);
        recurring.setReturnUrl(returnUrl);
        recurring.setMetadataId(metadataId);
        return recurring;
    }

    /**
     * @param installmentPlansRequest Contains information for requested plan.
     * @return Response contains a list of available installment plans the customer can choose from.
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public PaylaterInstallmentPlans fetchPaylaterInstallmentPlans(
            InstallmentPlansRequest installmentPlansRequest) throws HttpCommunicationException {
        return paymentService.fetchPaylaterInstallmentPlans(installmentPlansRequest);
    }

    /**
     * @deprecated Use {@link Unzer#fetchPaylaterInstallmentPlans(InstallmentPlansRequest)} together
     * with payment type {@link PaylaterInstallment} instead.
     */
    public List<InstallmentSecuredRatePlan> installmentSecuredRates(
            BigDecimal amount,
            Currency currency,
            BigDecimal effectiveInterestRate,
            Date orderDate
    ) throws HttpCommunicationException {
        return paymentService.installmentSecuredPlan(
                amount,
                currency,
                effectiveInterestRate,
                orderDate);
    }


    /**
     * Register single webhook. It may response error if <code><b>event</b></code> &
     * <code><b>eventList</b></code> are both defined.
     */
    public Webhook registerSingleWebhook(Webhook webhookRequest) throws HttpCommunicationException {
        return webhookService.registerSingleWebhook(webhookRequest);
    }

    /**
     * Register single webhook. It may response error if <code><b>event</b></code> &
     * <code><b>eventList</b></code> are both defined.
     * The URL will be registered to all webhook events.
     */
    public WebhookList registerMultiWebhooks(Webhook webhookRequest)
            throws HttpCommunicationException {
        return webhookService.registerMultiWebhooks(webhookRequest);
    }

    /**
     * Delete single webhook.
     *
     * @param webhookId refers to id of a webhook.
     * @return WebhookList refers to list of ids of which webhooks have been deleted.
     * <br>
     * Response example:
     * <pre>
     * {
     *   "id": "s-whk-61873"
     * }
     * <pre>
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public Webhook deleteSingleWebhook(String webhookId) throws HttpCommunicationException {
        return webhookService.deleteSingleWebhook(webhookId);
    }

    /**
     * Delete list of webhooks. It may return empty list if there is no webhooks to delete.
     *
     * @return WebhookList refers to list of ids of which webhooks have been deleted.
     * <br>
     * Response example:
     * <pre>
     * {
     *    "events": [
     *      {
     *         "id": "s-whk-61873"
     *      }
     *    ]
     * }
     * <pre>
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public WebhookList deleteMultiWebhook() throws HttpCommunicationException {
        return webhookService.deleteMultiWebhook();
    }

    /**
     * Update URl of a webhook. Only URL is able to be updated
     *
     * @param updateId:      webhook id to be updated
     * @param updateWebhook: update request
     * @return
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public Webhook updateSingleWebhook(String updateId, Webhook updateWebhook)
            throws HttpCommunicationException {
        return webhookService.updateSingleWebhook(updateId, updateWebhook);
    }

    /**
     * Get list of webhooks. It may response empty list if there is no webhook.
     *
     * @return WebhookList refers to list of webhooks have been created.
     * * <br>
     * Response example:
     * <pre>
     * {
     *    "events":[{
     *       "id": "s-whk-61873",
     *       "url": "https://domain.com",
     *       "event": "types"
     *    },
     *    {
     *       "id": "s-whk-61874",
     *       "url": "https://domain.com",
     *       "event": "payments"
     *    }]
     * }
     * <pre>
     * @throws HttpCommunicationException generic Payment API communication error
     */
    public WebhookList getWebhooks() throws HttpCommunicationException {
        return webhookService.getWebhooks();
    }

    public AuthToken createAuthToken() throws HttpCommunicationException {
        return tokenService.create();
    }

    public void prepareJwtToken() throws HttpCommunicationException {
        if (this.jwtToken != null && JwtHelper.validateExpiryDate(this.jwtToken)) {
            return;
        }
        this.jwtToken = tokenService.create().getAccessToken();
    }
}

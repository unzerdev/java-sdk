package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.List;

import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.PaymentType;

/**
 * Business object for a Payment. A Payment is the object that holds toghether several 
 * requests over time. This means that a payment belongs to one offer from the merchant.
 * 
 *  Within the Payment you also find the list of Charges, Cancels and the Authorization object.
 *  
 * @author rene.felder
 *
 */
public class Payment extends AbstractPayment {
	
	public enum State {
		COMPLETED, PENDING, CANCELED, PARTLY, PAYMENT_REVIEW, CHARGEBACK
	}
	
	private State paymentState;
	private BigDecimal amountTotal;
	private BigDecimal amountCharged;
	private BigDecimal amountCanceled;
	private BigDecimal amountRemaining;
	
	private Currency currency;
	private String orderId;
	
	private String customerId;
	private Customer customer;
	private String paymentTypeId;
	private PaymentType paymentType;
	private String metadataId;
	private Metadata metadata;
	private String basketId;
	private Basket basket;
	
	private Authorization authorization;
	private List<Charge> chargesList;
	private List<Cancel> cancelList;
	private List<Payout> payoutList;

	public Payment(Heidelpay heidelpay) {
		super(heidelpay);
	}
	
	public Charge charge() throws HttpCommunicationException {
		return getHeidelpay().chargeAuthorization(getId());
	}

	public Charge charge(BigDecimal amount) throws HttpCommunicationException {
		return getHeidelpay().chargeAuthorization(getId(), amount);
	}
	
	public Charge charge(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, typeId);
	}

	public Charge charge(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, typeId, customerId);
	}

	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, paymentType);
	}

	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, typeId, returnUrl);
	}

	public Charge charge(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, typeId, returnUrl, customerId);
	}

	public Charge charge(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().charge(amount, currency, paymentType, returnUrl, customer);
	}
	
	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, String customerId) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, customerId);
	}

	public Authorization authorize(BigDecimal amount, Currency currency, String typeId) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId);
	}

	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, paymentType);
	}

	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, returnUrl);
	}

	public Authorization authorize(BigDecimal amount, Currency currency, String typeId, URL returnUrl, String customerId) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, typeId, returnUrl, customerId);
	}

	public Authorization authorize(BigDecimal amount, Currency currency, PaymentType paymentType, URL returnUrl, Customer customer) throws HttpCommunicationException {
		return getHeidelpay().authorize(amount, currency, paymentType, returnUrl, customer);
	}

	public Cancel cancel() throws HttpCommunicationException {
		if (getAuthorization() == null) {
			throw new PaymentException("Cancel is only possible for an Authorization", "Payment cancelation not possible", "", "");
		}
		return getAuthorization().cancel();
	}

	public Cancel cancel(BigDecimal amount) throws HttpCommunicationException {
		if (getAuthorization() == null) {
			throw new PaymentException("Cancel is only possible for an Authorization", "Payment cancelation not possible", "", "");
		}
		return getAuthorization().cancel(amount);
	}
	
	public Authorization getAuthorization() {
		return authorization;
	}

	public Charge getCharge(String chargeId) {
		if (chargesList == null) return null;
		for (Charge charge : chargesList) {
			if (chargeId.equalsIgnoreCase(charge.getId())) {
				return charge;
			}
		}
		return null;
	}

	public Charge getCharge(int index) {
		return getChargesList().get(index);
	}

	public Payout getPayout(String payoutId) {
		if (payoutList == null) return null;
		for (Payout payout : payoutList) {
			if (payoutId.equalsIgnoreCase(payout.getId())) {
				return payout;
			}
		}
		return null;
	}

	public List<Cancel> getCancelList() {
		return cancelList;
	}

	public Cancel getCancel(String cancelId) {
		if (getCancelList() == null) return null;
		for (Cancel cancel : getCancelList()) {
			if (cancelId.equalsIgnoreCase(cancel.getId())) {
				return cancel;
			}
		}
		return null;
	}

	public Cancel getCancel() {
		return new Cancel();
	}

	public PaymentType getPaymentType() throws HttpCommunicationException {
		if (paymentType == null) {
			paymentType = fetchPaymentType(getPaymentTypeId());
		}
		return paymentType;
	}

	private PaymentType fetchPaymentType(String paymentTypeId) throws HttpCommunicationException {
		return getHeidelpay().fetchPaymentType(paymentTypeId);
	}

	public Customer getCustomer() throws HttpCommunicationException {
		if (customer == null && isNotEmpty(getCustomerId())) {
			customer = fetchCustomer(getCustomerId());
		}
		return customer;
	}

	protected boolean isNotEmpty(String value) {
		return value != null && !"".equalsIgnoreCase(value.trim());
	}

	private Customer fetchCustomer(String customerId) throws HttpCommunicationException {
		return getHeidelpay().fetchCustomer(customerId);
	}

	private Metadata fetchMetadata(String metadataId) throws HttpCommunicationException {
		return getHeidelpay().fetchMetadata(metadataId);
	}

	@Override
	public String getTypeUrl() {
		return "payments";
	}

	public State getPaymentState() {
		return paymentState;
	}

	public void setPaymentState(State paymentState) {
		this.paymentState = paymentState;
	}

	public BigDecimal getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(BigDecimal amountTotal) {
		this.amountTotal = amountTotal;
	}

	public BigDecimal getAmountCharged() {
		return amountCharged;
	}

	public void setAmountCharged(BigDecimal amountCharged) {
		this.amountCharged = amountCharged;
	}

	public BigDecimal getAmountCanceled() {
		return amountCanceled;
	}

	public void setAmountCanceled(BigDecimal amountCanceled) {
		this.amountCanceled = amountCanceled;
	}

	public BigDecimal getAmountRemaining() {
		return amountRemaining;
	}

	public void setAmountRemaining(BigDecimal amountRemaining) {
		this.amountRemaining = amountRemaining;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<Charge> getChargesList() {
		return chargesList;
	}

	public void setChargesList(List<Charge> chargesList) {
		this.chargesList = chargesList;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setAuthorization(Authorization authorization) {
		this.authorization = authorization;
	}

	public void setCancelList(List<Cancel> cancelList) {
		this.cancelList = cancelList;
	}

	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}

	public Metadata getMetadata() throws HttpCommunicationException {
		if (metadata == null && isNotEmpty(getMetadataId())) {
			metadata = fetchMetadata(getMetadataId());
		}
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getBasketId() {
		return basketId;
	}

	public void setBasketId(String basketId) {
		this.basketId = basketId;
	}

	public Basket getBasket() {
		return basket;
	}

	public void setBasket(Basket basket) {
		this.basket = basket;
	}

	public List<Payout> getPayoutList() {
		return payoutList;
	}

	public void setPayoutList(List<Payout> payoutList) {
		this.payoutList = payoutList;
	}

}

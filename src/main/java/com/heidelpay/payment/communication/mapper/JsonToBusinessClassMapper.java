package com.heidelpay.payment.communication.mapper;

import com.heidelpay.payment.AbstractInitPayment;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.Processing;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.UnsupportedPaymentTypeException;
import com.heidelpay.payment.communication.json.JsonApplepayResponse;
/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
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
import com.heidelpay.payment.communication.json.JsonCancel;
import com.heidelpay.payment.communication.json.JsonCard;
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonIdeal;
import com.heidelpay.payment.communication.json.JsonInitPayment;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.communication.json.JsonPayment;
import com.heidelpay.payment.communication.json.JsonPaypage;
import com.heidelpay.payment.communication.json.JsonProcessing;
import com.heidelpay.payment.communication.json.JsonResources;
import com.heidelpay.payment.communication.json.JsonSepaDirectDebit;
import com.heidelpay.payment.communication.json.JsonShipment;
import com.heidelpay.payment.communication.json.JsonState;
import com.heidelpay.payment.paymenttypes.Alipay;
import com.heidelpay.payment.paymenttypes.Applepay;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.Eps;
import com.heidelpay.payment.paymenttypes.Giropay;
import com.heidelpay.payment.paymenttypes.Ideal;
import com.heidelpay.payment.paymenttypes.Invoice;
import com.heidelpay.payment.paymenttypes.InvoiceFactoring;
import com.heidelpay.payment.paymenttypes.InvoiceGuaranteed;
import com.heidelpay.payment.paymenttypes.PaymentType;
import com.heidelpay.payment.paymenttypes.Paypal;
import com.heidelpay.payment.paymenttypes.Pis;
import com.heidelpay.payment.paymenttypes.Prepayment;
import com.heidelpay.payment.paymenttypes.Przelewy24;
import com.heidelpay.payment.paymenttypes.SepaDirectDebit;
import com.heidelpay.payment.paymenttypes.SepaDirectDebitGuaranteed;
import com.heidelpay.payment.paymenttypes.Sofort;
import com.heidelpay.payment.paymenttypes.Wechatpay;

public class JsonToBusinessClassMapper {

	public JsonObject map(AbstractInitPayment abstractInitPayment) {
		JsonInitPayment json = new JsonInitPayment();
		json.setAmount(abstractInitPayment.getAmount());
		json.setCurrency(abstractInitPayment.getCurrency());
		json.setReturnUrl(abstractInitPayment.getReturnUrl());
		json.setOrderId(abstractInitPayment.getOrderId());
		json.setResources(getResources(abstractInitPayment));
		json.setCard3ds(abstractInitPayment.getCard3ds());

		if(abstractInitPayment instanceof Charge) {
			json = new JsonCharge(json);
			((JsonCharge) json).setInvoiceId(((Charge) abstractInitPayment).getInvoiceId());
		}

		return json;
	}

	public JsonObject map(Cancel cancel) {
		JsonCharge json = new JsonCharge();
		json.setAmount(cancel.getAmount());
		return json;
	}
	
	public JsonObject map(Paypage paypage) {
		JsonPaypage json = new JsonPaypage();
		json.setAmount(paypage.getAmount());
		json.setContactUrl(paypage.getContactUrl());
		json.setCurrency(paypage.getCurrency());
		json.setDescriptionMain(paypage.getDescriptionMain());
		json.setDescriptionSmall(paypage.getDescriptionSmall());
		json.setFullPageImage(paypage.getFullPageImage());
		json.setHelpUrl(paypage.getHelpUrl());
		json.setId(paypage.getId());
		json.setImpressumUrl(paypage.getImpressumUrl());
		json.setLogoImage(paypage.getLogoImage());
		json.setOrderId(paypage.getOrderId());
		json.setPrivacyPolicyUrl(paypage.getPrivacyPolicyUrl());
		json.setReturnUrl(paypage.getReturnUrl());
		json.setShopName(paypage.getShopName());
		json.setTermsAndConditionUrl(paypage.getTermsAndConditionUrl());
		json.setResources(getResources(paypage));
		return json;
	}

	private JsonResources getResources(AbstractInitPayment abstractInitPayment) {
		JsonResources json = new JsonResources();
		json.setCustomerId(abstractInitPayment.getCustomerId());
		json.setMetadataId(abstractInitPayment.getMetadataId());
		json.setTypeId(abstractInitPayment.getTypeId());
		json.setRiskId(abstractInitPayment.getRiskId());
		json.setBasketId(abstractInitPayment.getBasketId());
		return json;
	}
	
	private JsonResources getResources(Paypage paypage) {
		JsonResources json = new JsonResources();
		json.setCustomerId(paypage.getCustomerId());
		json.setMetadataId(paypage.getMetadataId());
		json.setBasketId(paypage.getBasketId());
		return json;
	}

	public Paypage mapToBusinessObject(Paypage paypage, JsonPaypage json) {
		paypage.setAmount(json.getAmount());
		paypage.setContactUrl(json.getContactUrl());
		paypage.setCurrency(json.getCurrency());
		paypage.setDescriptionMain(json.getDescriptionMain());
		paypage.setDescriptionSmall(json.getDescriptionSmall());
		paypage.setFullPageImage(json.getFullPageImage());
		paypage.setHelpUrl(json.getHelpUrl());
		paypage.setId(json.getId());
		paypage.setImpressumUrl(json.getImpressumUrl());
		paypage.setLogoImage(json.getLogoImage());
		paypage.setOrderId(json.getOrderId());
		paypage.setPrivacyPolicyUrl(json.getPrivacyPolicyUrl());
		paypage.setReturnUrl(json.getReturnUrl());
		paypage.setShopName(json.getShopName());
		paypage.setTermsAndConditionUrl(json.getTermsAndConditionUrl());
		paypage.setRedirectUrl(json.getRedirectUrl());

		if (json.getResources() != null) {
			paypage.setBasketId(json.getResources().getBasketId());
			paypage.setCustomerId(json.getResources().getCustomerId());
			paypage.setMetadataId(json.getResources().getMetadataId());
			paypage.setPaymentId(json.getResources().getPaymentId());
		}
		return paypage;
	}
	public AbstractInitPayment mapToBusinessObject(AbstractInitPayment abstractInitPayment, JsonInitPayment json) {
		abstractInitPayment.setId(json.getId());
		abstractInitPayment.setAmount(json.getAmount());
		abstractInitPayment.setCurrency(json.getCurrency());
		abstractInitPayment.setOrderId(json.getOrderId());
		abstractInitPayment.setCard3ds(json.getCard3ds());
		if (json.getResources() != null) {
			abstractInitPayment.setCustomerId(json.getResources().getCustomerId());
			abstractInitPayment.setMetadataId(json.getResources().getMetadataId());
			abstractInitPayment.setPaymentId(json.getResources().getPaymentId());
			abstractInitPayment.setRiskId(json.getResources().getRiskId());
			abstractInitPayment.setTypeId(json.getResources().getTypeId());
		}
		abstractInitPayment.setReturnUrl(json.getReturnUrl());
		abstractInitPayment.setProcessing(getProcessing(json.getProcessing()));
		abstractInitPayment.setRedirectUrl(json.getRedirectUrl());
		abstractInitPayment.setMessage(json.getMessage());

		abstractInitPayment.setDate(json.getDate());

		setStatus(abstractInitPayment, json);
		return abstractInitPayment;
	}

	private void setStatus(AbstractInitPayment abstractInitPayment, JsonInitPayment json) {
		if (json.getIsSuccess()) {
			abstractInitPayment.setStatus(com.heidelpay.payment.AbstractInitPayment.Status.SUCCESS);
		} else if (json.getIsPending()) {
			abstractInitPayment.setStatus(com.heidelpay.payment.AbstractInitPayment.Status.PENDING);
		} else if (json.getIsError()) {
			abstractInitPayment.setStatus(com.heidelpay.payment.AbstractInitPayment.Status.ERRROR);
		}
	
	}

	public Cancel mapToBusinessObject(Cancel cancel, JsonCancel json) {
		cancel.setId(json.getId());
		cancel.setAmount(json.getAmount());
		cancel.setProcessing(getProcessing(json.getProcessing()));
		cancel.setMessage(json.getMessage());
		cancel.setDate(json.getDate());
		return cancel;
	}

	public Shipment mapToBusinessObject(Shipment shipment, JsonShipment json) {
		shipment.setId(json.getId());
		shipment.setMessage(json.getMessage());
		shipment.setDate(json.getDate());
		return shipment;
	}

	private Processing getProcessing(JsonProcessing json) {
		Processing processing = new Processing();
		processing.setUniqueId(json.getUniqueId());
		processing.setShortId(json.getShortId());
		return processing;
	}

	public Payment mapToBusinessObject(Payment payment, JsonPayment json) {
		payment.setAmountTotal(json.getAmount().getTotal());
		payment.setAmountCanceled(json.getAmount().getCanceled());
		payment.setAmountCharged(json.getAmount().getCharged());
		payment.setAmountRemaining(json.getAmount().getRemaining());
		payment.setOrderId(json.getOrderId());
		payment.setPaymentState(getPaymentState(json.getState()));
		payment.setId(json.getId());
		if (json.getResources() != null) { 
			payment.setPaymentTypeId(json.getResources().getTypeId());
			payment.setCustomerId(json.getResources().getCustomerId());
			payment.setMetadataId(json.getResources().getMetadataId());
			payment.setBasketId(json.getResources().getBasketId());
		}
		return payment;
	}

	private Payment.State getPaymentState(JsonState state) {
		if (state == null) return null;
		if (state.getId() == 0) return Payment.State.pending;
		if (state.getId() == 1) return Payment.State.completed;
		if (state.getId() == 2) return Payment.State.canceled;
		if (state.getId() == 3) return Payment.State.partly;
		if (state.getId() == 4) return Payment.State.payment_review;
		if (state.getId() == 5) return Payment.State.chargeback;
		return null;
	}

	public PaymentType mapToBusinessObject(PaymentType paymentType, JsonIdObject jsonPaymentType) {
		if (paymentType instanceof Card) {
			return map((Card) paymentType, (JsonCard) jsonPaymentType);
		} else if (paymentType instanceof SepaDirectDebitGuaranteed) {
			return map((SepaDirectDebitGuaranteed) paymentType, (JsonSepaDirectDebit) jsonPaymentType);
		} else if (paymentType instanceof SepaDirectDebit) {
			return map((SepaDirectDebit) paymentType, (JsonSepaDirectDebit) jsonPaymentType);
		} else if (paymentType instanceof Eps) {
			return map((Eps) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Giropay) {
			return map((Giropay) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Ideal) {
			return map((Ideal) paymentType, (JsonIdeal) jsonPaymentType);
		} else if (paymentType instanceof Invoice) {
			return map((Invoice) paymentType, jsonPaymentType);
		} else if (paymentType instanceof InvoiceFactoring) {
			return map((InvoiceFactoring) paymentType, jsonPaymentType);
		} else if (paymentType instanceof InvoiceGuaranteed) {
			return map((InvoiceGuaranteed) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Paypal) {
			return map((Paypal) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Prepayment) {
			return map((Prepayment) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Przelewy24) {
			return map((Przelewy24) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Sofort) {
			return map((Sofort) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Pis) {
			return map((Pis) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Alipay) {
			return map((Alipay) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Wechatpay) {
			return map((Wechatpay) paymentType, jsonPaymentType);
		} else if (paymentType instanceof Applepay) {
			return map((Applepay) paymentType, (JsonApplepayResponse) jsonPaymentType);
		} else {
			throw new UnsupportedPaymentTypeException(
					"Type '" + paymentType.getClass().getName() + "' is currently now supported by the SDK");
		}
	}

	private PaymentType map(Card card, JsonCard jsonCard) {
		card.setCvc(jsonCard.getCvc());
		card.setExpiryDate(jsonCard.getExpiryDate());
		card.setNumber(jsonCard.getNumber());
		card.setId(jsonCard.getId());
		card.set3ds(jsonCard.get3ds());
		return card;
	}

	private PaymentType map(Applepay applepay, JsonApplepayResponse jsonApplePay) {
		applepay.setId(jsonApplePay.getId());
		applepay.setExpiryDate(jsonApplePay.getApplicationExpirationDate());
		applepay.setNumber(jsonApplePay.getApplicationPrimaryAccountNumber());
		applepay.setCurrencyCode(jsonApplePay.getCurrencyCode());
		applepay.setTransactionAmount(jsonApplePay.getTransactionAmount());
		return applepay;
	}

	private PaymentType map(SepaDirectDebit sdd, JsonSepaDirectDebit jsonSdd) {
		sdd.setId(jsonSdd.getId());
		sdd.setBic(jsonSdd.getBic());
		sdd.setIban(jsonSdd.getIban());
		sdd.setHolder(jsonSdd.getHolder());
		return sdd;
	}

	private PaymentType map(Eps eps, JsonIdObject jsonId) {
		eps.setId(jsonId.getId());
		return eps;
	}

	private PaymentType map(Giropay giropay, JsonIdObject jsonId) {
		giropay.setId(jsonId.getId());
		return giropay;
	}
	
	private PaymentType map(Ideal ideal, JsonIdeal jsonIdeal) {
		ideal.setId(jsonIdeal.getId());
		ideal.setBic(jsonIdeal.getBankName());
		return ideal;
	}
	
	private PaymentType map(Invoice invoice, JsonIdObject jsonId) {
		invoice.setId(jsonId.getId());
		return invoice;
	}
	
	private PaymentType map(InvoiceGuaranteed invoice, JsonIdObject jsonId) {
		invoice.setId(jsonId.getId());
		return invoice;
	}
	
	private PaymentType map(InvoiceFactoring invoice, JsonIdObject jsonId) {
		invoice.setId(jsonId.getId());
		return invoice;
	}


	private PaymentType map(Paypal paypal, JsonIdObject jsonId) {
		paypal.setId(jsonId.getId());
		return paypal;
	}
	
	private PaymentType map(Prepayment prepayment, JsonIdObject jsonId) {
		prepayment.setId(jsonId.getId());
		return prepayment;
	}
	
	private PaymentType map(Przelewy24 p24, JsonIdObject jsonId) {
		p24.setId(jsonId.getId());
		return p24;
	}
	
	private PaymentType map(Alipay alipay, JsonIdObject jsonId) {
		alipay.setId(jsonId.getId());
		return alipay;
	}

	private PaymentType map(Wechatpay wechatpay, JsonIdObject jsonId) {
		wechatpay.setId(jsonId.getId());
		return wechatpay;
	}

	private PaymentType map(Sofort sofort, JsonIdObject jsonId) {
		sofort.setId(jsonId.getId());
		return sofort;
	}
	
	private PaymentType map(Pis pis, JsonIdObject jsonId) {
		pis.setId(jsonId.getId());
		return pis;
	}
}

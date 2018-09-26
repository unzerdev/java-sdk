package com.heidelpay.payment.communication.mapper;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.apache.log4j.Logger;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Processing;
import com.heidelpay.payment.communication.json.JsonAuthorization;
import com.heidelpay.payment.communication.json.JsonCancel;
import com.heidelpay.payment.communication.json.JsonCard;
import com.heidelpay.payment.communication.json.JsonCardFetch;
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonIdeal;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.communication.json.JsonPayment;
import com.heidelpay.payment.communication.json.JsonProcessing;
import com.heidelpay.payment.communication.json.JsonResources;
import com.heidelpay.payment.communication.json.JsonSepaDirectDebit;
import com.heidelpay.payment.communication.json.JsonState;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.Eps;
import com.heidelpay.payment.paymenttypes.Giropay;
import com.heidelpay.payment.paymenttypes.Ideal;
import com.heidelpay.payment.paymenttypes.Invoice;
import com.heidelpay.payment.paymenttypes.InvoiceGuaranteed;
import com.heidelpay.payment.paymenttypes.PaymentType;
import com.heidelpay.payment.paymenttypes.Paypal;
import com.heidelpay.payment.paymenttypes.Prepayment;
import com.heidelpay.payment.paymenttypes.Przelewy24;
import com.heidelpay.payment.paymenttypes.SepaDirectDebit;
import com.heidelpay.payment.paymenttypes.SepaDirectDebitGuaranteed;
import com.heidelpay.payment.paymenttypes.Sofort;

public class JsonToBusinessClassMapper {
	public final static Logger logger = Logger.getLogger(JsonToBusinessClassMapper.class);

	public JsonObject map(Authorization authorization) {
		JsonAuthorization json = new JsonAuthorization();
		json.setAmount(authorization.getAmount());
		json.setCurrency(authorization.getCurrency());
		json.setReturnUrl(authorization.getReturnUrl());
		json.setResources(getResources(authorization));
		return json;
	}

	public JsonObject map(Charge charge) {
		JsonCharge json = new JsonCharge();
		json.setAmount(charge.getAmount());
		json.setCurrency(charge.getCurrency());
		json.setReturnUrl(charge.getReturnUrl());
		json.setResources(getResources(charge));
		return json;
	}

	public JsonObject map(Cancel cancel) {
		JsonCharge json = new JsonCharge();
		json.setAmount(cancel.getAmount());
		return json;
	}

	private JsonResources getResources(Charge charge) {
		JsonResources json = new JsonResources();
		json.setCustomerId(charge.getCustomerId());
		json.setMetadataId(charge.getMetadataId());
		json.setTypeId(charge.getTypeId());
		json.setRiskId(charge.getRiskId());
		return json;
	}

	private JsonResources getResources(Authorization authorization) {
		JsonResources json = new JsonResources();
		json.setCustomerId(authorization.getCustomerId());
		json.setMetadataId(authorization.getMetadataId());
		json.setTypeId(authorization.getTypeId());
		json.setRiskId(authorization.getRiskId());
		return json;
	}

	public Authorization mapToBusinessObject(Authorization authorization, JsonAuthorization json) {
		authorization.setId(json.getId());
		authorization.setAmount(json.getAmount());
		authorization.setCurrency(json.getCurrency());
		if (json.getResources() != null) {
			authorization.setCustomerId(json.getResources().getCustomerId());
			authorization.setMetadataId(json.getResources().getMetadataId());
			authorization.setPaymentId(json.getResources().getPaymentId());
			authorization.setRiskId(json.getResources().getRiskId());
			authorization.setTypeId(json.getResources().getTypeId());
		}
		authorization.setReturnUrl(json.getReturnUrl());
		authorization.setProcessing(getProcessing(json.getProcessing()));
		authorization.setRedirectUrl(json.getRedirectUrl());
		return authorization;
	}

	public Charge mapToBusinessObject(Charge charge, JsonCharge json) {
		charge.setId(json.getId());
		charge.setAmount(json.getAmount());
		charge.setCurrency(json.getCurrency());
		if (json.getResources() != null) {
			charge.setCustomerId(json.getResources().getCustomerId());
			charge.setMetadataId(json.getResources().getMetadataId());
			charge.setPaymentId(json.getResources().getPaymentId());
			charge.setRiskId(json.getResources().getRiskId());
			charge.setTypeId(json.getResources().getTypeId());
		}
		charge.setReturnUrl(json.getReturnUrl());
		charge.setProcessing(getProcessing(json.getProcessing()));
		charge.setRedirectUrl(json.getRedirectUrl());
		return charge;
	}

	public Cancel mapToBusinessObject(Cancel cancel, JsonCancel json) {
		cancel.setId(json.getId());
		cancel.setAmount(json.getAmount());
		cancel.setProcessing(getProcessing(json.getProcessing()));
		return cancel;
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
			// workaround for Bug AHC-265
			if (jsonPaymentType instanceof JsonCardFetch) {
				return map((Card) paymentType, (JsonCardFetch) jsonPaymentType);
			} else {
				return map((Card) paymentType, (JsonCard) jsonPaymentType);
			}
		} else if (paymentType instanceof SepaDirectDebitGuaranteed) {
			return map((SepaDirectDebitGuaranteed) paymentType, (JsonSepaDirectDebit) jsonPaymentType);
		} else if (paymentType instanceof SepaDirectDebit) {
			return map((SepaDirectDebit) paymentType, (JsonSepaDirectDebit) jsonPaymentType);
		} else if (paymentType instanceof Eps) {
			return map((Eps) paymentType, (JsonIdObject) jsonPaymentType);
		} else if (paymentType instanceof Giropay) {
			return map((Giropay) paymentType, (JsonIdObject) jsonPaymentType);
		} else if (paymentType instanceof Ideal) {
			return map((Ideal) paymentType, (JsonIdeal) jsonPaymentType);
		} else if (paymentType instanceof Invoice) {
			return map((Invoice) paymentType, (JsonIdObject) jsonPaymentType);
		} else if (paymentType instanceof InvoiceGuaranteed) {
			return map((InvoiceGuaranteed) paymentType, (JsonIdObject) jsonPaymentType);
		} else if (paymentType instanceof Paypal) {
			return map((Paypal) paymentType, (JsonIdObject) jsonPaymentType);
		} else if (paymentType instanceof Prepayment) {
			return map((Prepayment) paymentType, (JsonIdObject) jsonPaymentType);
		} else if (paymentType instanceof Przelewy24) {
			return map((Przelewy24) paymentType, (JsonIdObject) jsonPaymentType);
		} else if (paymentType instanceof Sofort) {
			return map((Sofort) paymentType, (JsonIdObject) jsonPaymentType);
		} else {
			throw new PaymentException(
					"Type '" + paymentType.getClass().getName() + "' is currently now supported by the SDK");
		}
	}

	private PaymentType map(Card card, JsonCardFetch jsonCard) {
		card.setCvc(jsonCard.getCvv());
		card.setExpiryDate(jsonCard.getExpiry());
		card.setNumber(jsonCard.getNumber());
		card.setId(jsonCard.getId());
		return card;
		
	}
	private PaymentType map(Card card, JsonCard jsonCard) {
		card.setCvc(jsonCard.getCvc());
		card.setExpiryDate(jsonCard.getExpiryDate());
		card.setNumber(jsonCard.getNumber());
		card.setId(jsonCard.getId());
		return card;
	}

	private PaymentType map(SepaDirectDebit sdd, JsonSepaDirectDebit jsonSdd) {
		sdd.setId(jsonSdd.getId());
		sdd.setBic(jsonSdd.getBic());
		sdd.setIban(jsonSdd.getIban());
		sdd.setHolder(jsonSdd.getHolder());
		return sdd;
	}

	private PaymentType map(SepaDirectDebitGuaranteed ddg, JsonSepaDirectDebit jsonSdd) {
		ddg.setId(jsonSdd.getId());
		ddg.setBic(jsonSdd.getBic());
		ddg.setIban(jsonSdd.getIban());
		ddg.setHolder(jsonSdd.getHolder());
		return ddg;
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
	
	private PaymentType map(Sofort sofort, JsonIdObject jsonId) {
		sofort.setId(jsonId.getId());
		return sofort;
	}
	
}

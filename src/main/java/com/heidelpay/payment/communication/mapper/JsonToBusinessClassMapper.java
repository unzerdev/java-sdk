package com.heidelpay.payment.communication.mapper;

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
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.communication.json.JsonPayment;
import com.heidelpay.payment.communication.json.JsonProcessing;
import com.heidelpay.payment.communication.json.JsonResources;
import com.heidelpay.payment.paymenttypes.Card;
import com.heidelpay.payment.paymenttypes.PaymentType;

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
		authorization.setCustomerId(json.getResources().getCustomerId());
		authorization.setMetadataId(json.getResources().getMetadataId());
		authorization.setPaymentId(json.getResources().getPaymentId());
		authorization.setReturnUrl(json.getReturnUrl());
		authorization.setRiskId(json.getResources().getRiskId());
		authorization.setTypeId(json.getResources().getTypeId());
		authorization.setProcessing(getProcessing(json.getProcessing()));
		return authorization;
	}

	public Charge mapToBusinessObject(Charge charge, JsonCharge json) {
		charge.setId(json.getId());
		charge.setAmount(json.getAmount());
		charge.setCurrency(json.getCurrency());
		charge.setCustomerId(json.getResources().getCustomerId());
		charge.setMetadataId(json.getResources().getMetadataId());
		charge.setPaymentId(json.getResources().getPaymentId());
		charge.setReturnUrl(json.getReturnUrl());
		charge.setRiskId(json.getResources().getRiskId());
		charge.setTypeId(json.getResources().getTypeId());
		charge.setProcessing(getProcessing(json.getProcessing()));
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
		payment.setId(json.getId());
		payment.setPaymentTypeId(json.getResources().getTypeId());
		return payment;
	}

	public PaymentType mapToBusinessObject(PaymentType paymentType, JsonIdObject jsonPaymentType) {
		if (paymentType instanceof Card) {
			return map((Card) paymentType, (JsonCard) jsonPaymentType);
		} else {
			throw new PaymentException(
					"Type '" + paymentType.getClass().getName() + "' is currently now supported by the SDK");
		}
	}

	private PaymentType map(Card card, JsonCard jsonCard) {
		card.setCvc(jsonCard.getCvc());
		card.setExpiryDate(jsonCard.getExpiryDate());
		card.setNumber(jsonCard.getNumber());
		card.setId(jsonCard.getId());
		return card;
	}

}

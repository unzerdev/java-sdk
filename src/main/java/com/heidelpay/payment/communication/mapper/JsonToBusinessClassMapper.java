package com.heidelpay.payment.communication.mapper;

import com.heidelpay.payment.AbstractInitPayment;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.CommercialSector;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.CustomerCompanyData;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.Payout;
import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.Processing;
import com.heidelpay.payment.Recurring;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.UnsupportedPaymentTypeException;
import com.heidelpay.payment.communication.json.JSonCompanyInfo;
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
import com.heidelpay.payment.communication.json.JsonCustomer;
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonIdeal;
import com.heidelpay.payment.communication.json.JsonInitPayment;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.communication.json.JsonPayment;
import com.heidelpay.payment.communication.json.JsonPayout;
import com.heidelpay.payment.communication.json.JsonPaypage;
import com.heidelpay.payment.communication.json.JsonPis;
import com.heidelpay.payment.communication.json.JsonProcessing;
import com.heidelpay.payment.communication.json.JsonRecurring;
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
		json.setPaymentReference(abstractInitPayment.getPaymentReference());

		if(abstractInitPayment instanceof Charge) {
			json = new JsonCharge(json);
			((JsonCharge) json).setInvoiceId(((Charge) abstractInitPayment).getInvoiceId());
		}

		return json;
	}

	public JsonObject map(Recurring recurring) {
		JsonRecurring json = new JsonRecurring();
		json.setReturnUrl(recurring.getReturnUrl());
		json.setResources(getResources(recurring));
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
	
	private JsonResources getResources(Recurring recurring) {
		JsonResources json = new JsonResources();
		json.setCustomerId(recurring.getCustomerId());
		json.setMetadataId(recurring.getMetadataId());
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
	public Recurring mapToBusinessObject(Recurring recurring, JsonRecurring json) {
		recurring.setDate(json.getDate());
		recurring.setMessage(json.getMessage());
		if (json.getResources() != null) {
			recurring.setMetadataId(json.getResources().getMetadataId());
			recurring.setCustomerId(json.getResources().getCustomerId());
		}
		recurring.setProcessing(getProcessing(json.getProcessing()));
		recurring.setRedirectUrl(json.getRedirectUrl());
		recurring.setReturnUrl(json.getReturnUrl());
		setStatus(recurring, json);
		return recurring;
	}
	public AbstractInitPayment mapToBusinessObject(AbstractInitPayment abstractInitPayment, JsonInitPayment json) {
		abstractInitPayment.setId(json.getId());
		abstractInitPayment.setAmount(json.getAmount());
		abstractInitPayment.setCurrency(json.getCurrency());
		abstractInitPayment.setOrderId(json.getOrderId());
		abstractInitPayment.setCard3ds(json.getCard3ds());
		abstractInitPayment.setPaymentReference(json.getPaymentReference());
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
	private JSonCompanyInfo getCompanyInfo(CustomerCompanyData customer, String company) {
		if (customer == null) return null;
		JSonCompanyInfo json =  new JSonCompanyInfo();
		if (company != null) {
			mapRegisteredCompany(customer, json);
		} else {
			mapUnregisteredCompany(customer, json);
		}
		return json;
	}

	private void mapUnregisteredCompany(CustomerCompanyData customer, JSonCompanyInfo json) {
		json.setFunction("OWNER");
		json.setRegistrationType("not_registered");
		if (customer.getCommercialSector() != null) {
			json.setCommercialSector(customer.getCommercialSector().toString());
		}
	}

	private void mapRegisteredCompany(CustomerCompanyData customer, JSonCompanyInfo json) {
		json.setRegistrationType("registered");
		json.setCommercialRegisterNumber(customer.getCommercialRegisterNumber());
	}

	public JsonCustomer map(Customer customer) {
		JsonCustomer json = new JsonCustomer();
		json.setFirstname(customer.getFirstname());
		json.setLastname(customer.getLastname());
		json.setCompany(customer.getCompany());
		json.setCustomerId(customer.getCustomerId());
		json.setEmail(customer.getEmail());
		json.setMobile(customer.getMobile());
		json.setPhone(customer.getPhone());
		json.setSalutation(customer.getSalutation());
		json.setBirthDate(customer.getBirthDate());
		
		json.setBillingAddress(customer.getBillingAddress());
		json.setShippingAddress(customer.getShippingAddress());
		json.setCompanyInfo(getCompanyInfo(customer.getCompanyData(), customer.getCompany()));
		return json;
	}
	

	public Customer mapToBusinessObject(Customer customer, JsonCustomer json) {
		customer.setId(json.getId());
		customer.setFirstname(json.getFirstname());
		customer.setLastname(json.getLastname());
		customer.setCompany(json.getCompany());
		customer.setCustomerId(json.getCustomerId());
		customer.setEmail(json.getEmail());
		customer.setMobile(json.getMobile());
		customer.setPhone(json.getPhone());
		customer.setSalutation(json.getSalutation());
		customer.setBirthDate(json.getBirthDate());
		
		customer.setBillingAddress(json.getBillingAddress());
		customer.setShippingAddress(json.getShippingAddress());
		customer.setCompanyData(getCompanyInfo(json.getCompanyInfo()));
		return customer;
	}

	private CustomerCompanyData getCompanyInfo(JSonCompanyInfo json) {
		if (json == null) return null;
		if (allFieldsNull(json)) return null;
		CustomerCompanyData company = new CustomerCompanyData();
		company.setCommercialRegisterNumber(json.getCommercialRegisterNumber());
		if (json.getCommercialSector() != null) {
			company.setCommercialSector(CommercialSector.valueOf(json.getCommercialSector()));
		}
		if (json.getRegistrationType() != null) {
			company.setRegistrationType(CustomerCompanyData.RegistrationType.valueOf(json.getRegistrationType().toUpperCase()));
		}
		return company;
	}



	private boolean allFieldsNull(JSonCompanyInfo json) {
		if (json.getCommercialRegisterNumber() != null) return false;
		if (json.getCommercialSector() != null) return false;
		if (json.getFunction() != null) return false;
		if (json.getRegistrationType() != null) return false;
		return true;
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
	private void setStatus(Recurring recurring, JsonRecurring json) {
		if (json.getIsSuccess()) {
			recurring.setStatus(com.heidelpay.payment.Recurring.Status.SUCCESS);
		} else if (json.getIsPending()) {
			recurring.setStatus(com.heidelpay.payment.Recurring.Status.PENDING);
		} else if (json.getIsError()) {
			recurring.setStatus(com.heidelpay.payment.Recurring.Status.ERRROR);
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
		processing.setBic(json.getBic());
		processing.setDescriptor(json.getDescriptor());
		processing.setHolder(json.getHolder());
		processing.setIban(json.getIban());
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
			return map((Pis) paymentType, (JsonPis)jsonPaymentType);
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
		card.setRecurring(jsonCard.getRecurring());
		return card;
	}

	private PaymentType map(Applepay applepay, JsonApplepayResponse jsonApplePay) {
		applepay.setId(jsonApplePay.getId());
		applepay.setExpiryDate(jsonApplePay.getApplicationExpirationDate());
		applepay.setNumber(jsonApplePay.getApplicationPrimaryAccountNumber());
		applepay.setCurrencyCode(jsonApplePay.getCurrencyCode());
		applepay.setTransactionAmount(jsonApplePay.getTransactionAmount());
		applepay.setRecurring(jsonApplePay.getRecurring());
		return applepay;
	}

	private PaymentType map(SepaDirectDebit sdd, JsonSepaDirectDebit jsonSdd) {
		sdd.setId(jsonSdd.getId());
		sdd.setBic(jsonSdd.getBic());
		sdd.setIban(jsonSdd.getIban());
		sdd.setHolder(jsonSdd.getHolder());
		sdd.setRecurring(jsonSdd.getRecurring());
		return sdd;
	}

	private PaymentType map(Eps eps, JsonIdObject jsonId) {
		eps.setId(jsonId.getId());
		eps.setRecurring(jsonId.getRecurring());
		return eps;
	}

	private PaymentType map(Giropay giropay, JsonIdObject jsonId) {
		giropay.setId(jsonId.getId());
		giropay.setRecurring(jsonId.getRecurring());
		return giropay;
	}
	
	private PaymentType map(Ideal ideal, JsonIdeal jsonIdeal) {
		ideal.setId(jsonIdeal.getId());
		ideal.setBic(jsonIdeal.getBankName());
		ideal.setRecurring(jsonIdeal.getRecurring());
		return ideal;
	}
	
	private PaymentType map(Invoice invoice, JsonIdObject jsonId) {
		invoice.setId(jsonId.getId());
		invoice.setRecurring(jsonId.getRecurring());
		return invoice;
	}
	
	private PaymentType map(InvoiceGuaranteed invoice, JsonIdObject jsonId) {
		invoice.setId(jsonId.getId());
		invoice.setRecurring(jsonId.getRecurring());
		return invoice;
	}
	
	private PaymentType map(InvoiceFactoring invoice, JsonIdObject jsonId) {
		invoice.setId(jsonId.getId());
		invoice.setRecurring(jsonId.getRecurring());
		return invoice;
	}


	private PaymentType map(Paypal paypal, JsonIdObject jsonId) {
		paypal.setId(jsonId.getId());
		paypal.setRecurring(jsonId.getRecurring());
		return paypal;
	}
	
	private PaymentType map(Prepayment prepayment, JsonIdObject jsonId) {
		prepayment.setId(jsonId.getId());
		prepayment.setRecurring(jsonId.getRecurring());
		return prepayment;
	}
	
	private PaymentType map(Przelewy24 p24, JsonIdObject jsonId) {
		p24.setId(jsonId.getId());
		p24.setRecurring(jsonId.getRecurring());
		return p24;
	}
	
	private PaymentType map(Alipay alipay, JsonIdObject jsonId) {
		alipay.setId(jsonId.getId());
		alipay.setRecurring(jsonId.getRecurring());
		return alipay;
	}

	private PaymentType map(Wechatpay wechatpay, JsonIdObject jsonId) {
		wechatpay.setId(jsonId.getId());
		wechatpay.setRecurring(jsonId.getRecurring());
		return wechatpay;
	}

	private PaymentType map(Sofort sofort, JsonIdObject jsonId) {
		sofort.setId(jsonId.getId());
		sofort.setRecurring(jsonId.getRecurring());
		return sofort;
	}
	
	private PaymentType map(Pis pis, JsonPis jsonPis) {
		pis.setId(jsonPis.getId());
		pis.setRecurring(jsonPis.getRecurring());
		pis.setBic(jsonPis.getBic());
		pis.setIban(jsonPis.getIban());
		pis.setHolder(jsonPis.getHolder());
		return pis;
	}



}

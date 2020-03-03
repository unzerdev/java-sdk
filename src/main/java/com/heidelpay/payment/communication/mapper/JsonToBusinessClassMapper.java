package com.heidelpay.payment.communication.mapper;

import com.heidelpay.payment.AbstractInitPayment;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.CommercialSector;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.CustomerCompanyData;
import com.heidelpay.payment.Linkpay;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.Payout;
import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.Processing;
import com.heidelpay.payment.Recurring;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.UnsupportedPaymentTypeException;
import com.heidelpay.payment.business.paymenttypes.HirePurchaseRatePlan;
import com.heidelpay.payment.communication.json.JSonCompanyInfo;
import com.heidelpay.payment.communication.json.JsonApplepayResponse;
import com.heidelpay.payment.communication.json.JsonAuthorization;
import com.heidelpay.payment.communication.json.JsonCancel;
import com.heidelpay.payment.communication.json.JsonCard;
import com.heidelpay.payment.communication.json.JsonCardDetails;
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonCustomer;
import com.heidelpay.payment.communication.json.JsonHirePurchaseRatePlan;
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonIdeal;
import com.heidelpay.payment.communication.json.JsonInitPayment;
import com.heidelpay.payment.communication.json.JsonLinkpay;
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
import com.heidelpay.payment.paymenttypes.CardDetails;
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
		} else if (abstractInitPayment instanceof Payout) {
			json = new JsonPayout(json);
		} else if(abstractInitPayment instanceof Authorization) {
			json = new JsonAuthorization(json);
			json.setEffectiveInterestRate(((Authorization) abstractInitPayment).getEffectiveInterestRate());
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
		json.setPaymentReference(cancel.getPaymentReference());
		return json;
	}
	
	public JsonObject map(Paypage paypage) {
		JsonPaypage json = new JsonPaypage();
		json.setId(paypage.getId());
		json.setAmount(paypage.getAmount());
		json.setCurrency(paypage.getCurrency());
		json.setReturnUrl(paypage.getReturnUrl());
		json.setLogoImage(paypage.getLogoImage());
		json.setFullPageImage(paypage.getFullPageImage());
		json.setShopName(paypage.getShopName());
		json.setShopDescription(paypage.getShopDescription());
		json.setTagline(paypage.getTagline());
		json.setCss(paypage.getCss());
		json.setTermsAndConditionUrl(paypage.getTermsAndConditionUrl());
		json.setPrivacyPolicyUrl(paypage.getPrivacyPolicyUrl());
		json.setImpressumUrl(paypage.getImpressumUrl());
		json.setImprintUrl(paypage.getImprintUrl());
		json.setHelpUrl(paypage.getHelpUrl());
		json.setContactUrl(paypage.getContactUrl());
		json.setInvoiceId(paypage.getInvoiceId());
		json.setOrderId(paypage.getOrderId());
		json.setCard3ds(paypage.getCard3ds());
		json.setBillingAddressRequired(paypage.getBillingAddressRequired());
		json.setShippingAddressRequired(paypage.getShippingAddressRequired());
		json.setAdditionalAttributes(paypage.getAdditionalAttributes());
		json.setResources(getResources(paypage));
		json.setExcludeTypes(paypage.getExcludeTypes());
		return json;
	}

	public JsonObject map(Linkpay linkpay) {
		JsonLinkpay json = new JsonLinkpay();
		json.setId(linkpay.getId());
		json.setAmount(linkpay.getAmount());
		json.setCurrency(linkpay.getCurrency());
		json.setReturnUrl(linkpay.getReturnUrl());
		json.setLogoImage(linkpay.getLogoImage());
		json.setFullPageImage(linkpay.getFullPageImage());
		json.setShopName(linkpay.getShopName());
		json.setTermsAndConditionUrl(linkpay.getTermsAndConditionUrl());
		json.setPrivacyPolicyUrl(linkpay.getPrivacyPolicyUrl());
		json.setHelpUrl(linkpay.getHelpUrl());
		json.setContactUrl(linkpay.getContactUrl());
		json.setOrderId(linkpay.getOrderId());
		json.setResources(getResources(linkpay));
		json.setShopDescription(linkpay.getShopDescription());
		json.setTagline(linkpay.getTagline());
		json.setImprintUrl(linkpay.getImprintUrl());
		json.setInvoiceId(linkpay.getInvoiceId());
		json.setCard3ds(linkpay.getCard3ds());
		json.setBillingAddressRequired(linkpay.getBillingAddressRequired());
		json.setShippingAddressRequired(linkpay.getShippingAddressRequired());
		json.setAdditionalAttributes(linkpay.getAdditionalAttributes());
		json.setAction(linkpay.getAction());
		json.setExcludeTypes(linkpay.getExcludeTypes());
		json.setCss(linkpay.getCss());
		json.setAlias(linkpay.getAlias());
		json.setExpires(linkpay.getExpires());
		json.setIntention(linkpay.getIntention());
		json.setPaymentReference(linkpay.getPaymentReference());
		json.setOrderIdRequired(linkpay.getOrderIdRequired());
		json.setInvoiceIdRequired(linkpay.getInvoiceIdRequired());
		json.setOneTimeUse(linkpay.getOneTimeUse());
		json.setSuccessfullyProcessed(linkpay.getSuccessfullyProcessed());
		json.setRedirectUrl(linkpay.getRedirectUrl());
		json.setVersion(linkpay.getVersion());
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

	private JsonResources getResources(Linkpay linkpay) {
		JsonResources json = new JsonResources();
		json.setCustomerId(linkpay.getCustomerId());
		json.setMetadataId(linkpay.getMetadataId());
		json.setBasketId(linkpay.getBasketId());
		return json;
	}

	public Linkpay mapToBusinessObject(Linkpay linkpay, JsonLinkpay json) {
		linkpay.setId(json.getId());
		linkpay.setAmount(json.getAmount());
		linkpay.setCurrency(json.getCurrency());
		linkpay.setReturnUrl(json.getReturnUrl());
		linkpay.setLogoImage(json.getLogoImage());
		linkpay.setFullPageImage(json.getFullPageImage());
		linkpay.setShopName(json.getShopName());
		linkpay.setShopDescription(json.getShopDescription());
		linkpay.setTagline(json.getTagline());
		linkpay.setCss(json.getCss());
		linkpay.setAlias(json.getAlias());
		linkpay.setTermsAndConditionUrl(json.getTermsAndConditionUrl());
		linkpay.setPrivacyPolicyUrl(json.getPrivacyPolicyUrl());
		linkpay.setImprintUrl(json.getImprintUrl());
		linkpay.setHelpUrl(json.getHelpUrl());
		linkpay.setContactUrl(json.getContactUrl());
		linkpay.setVersion(json.getVersion());
		linkpay.setRedirectUrl(json.getRedirectUrl());
		linkpay.setAction(json.getAction());
		linkpay.setCard3ds(json.getCard3ds());
		linkpay.setExpires(json.getExpires());
		linkpay.setOrderId(json.getOrderId());
		linkpay.setInvoiceId(json.getInvoiceId());
		linkpay.setBillingAddressRequired(json.getBillingAddressRequired());
		linkpay.setShippingAddressRequired(json.getShippingAddressRequired());
		linkpay.setAdditionalAttributes(json.getAdditionalAttributes());
		linkpay.setIntention(json.getIntention());
		linkpay.setOrderIdRequired(json.getOrderIdRequired());
		linkpay.setInvoiceIdRequired(json.getInvoiceIdRequired());
		linkpay.setOneTimeUse(json.getOneTimeUse());
		linkpay.setSuccessfullyProcessed(json.getSuccessfullyProcessed());
		linkpay.setExcludeTypes(json.getExcludeTypes());
		linkpay.setPaymentReference(json.getPaymentReference());
		if(json.getResources() != null) {
			JsonResources jsonResources = json.getResources();
			linkpay.setCustomerId(jsonResources.getCustomerId());
			linkpay.setMetadataId(jsonResources.getMetadataId());
			linkpay.setPaymentId(jsonResources.getPaymentId());
			linkpay.setBasketId(jsonResources.getBasketId());
		}
		return linkpay;
	}

	public Paypage mapToBusinessObject(Paypage paypage, JsonPaypage json) {
		paypage.setId(json.getId());
		paypage.setAmount(json.getAmount());
		paypage.setCurrency(json.getCurrency());
		paypage.setReturnUrl(json.getReturnUrl());
		paypage.setLogoImage(json.getLogoImage());
		paypage.setFullPageImage(json.getFullPageImage());
		paypage.setShopName(json.getShopName());
		paypage.setShopDescription(json.getShopDescription());
		paypage.setTagline(json.getTagline());
		paypage.setCss(json.getCss());
		paypage.setTermsAndConditionUrl(json.getTermsAndConditionUrl());
		paypage.setPrivacyPolicyUrl(json.getPrivacyPolicyUrl());
		paypage.setImpressumUrl(json.getImpressumUrl());
		paypage.setImprintUrl(json.getImprintUrl());
		paypage.setHelpUrl(json.getHelpUrl());
		paypage.setContactUrl(json.getContactUrl());
		paypage.setInvoiceId(json.getInvoiceId());
		paypage.setOrderId(json.getOrderId());
		paypage.setCard3ds(json.getCard3ds());
		paypage.setBillingAddressRequired(json.getBillingAddressRequired());
		paypage.setShippingAddressRequired(json.getShippingAddressRequired());
		paypage.setAdditionalAttributes(json.getAdditionalAttributes());
		paypage.setRedirectUrl(json.getRedirectUrl());
		paypage.setAction(json.getAction());
		paypage.setExcludeTypes(json.getExcludeTypes());

		if(json.getResources() != null) {
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
		return json.getRegistrationType() == null;
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
	private void setStatus(Cancel cancel, JsonCancel json) {
		if (json.isSuccess()) {
			cancel.setStatus(Cancel.Status.SUCCESS);
		} else if (json.isPending()) {
			cancel.setStatus(Cancel.Status.PENDING);
		} else if (json.isError()) {
			cancel.setStatus(Cancel.Status.ERRROR);
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
		cancel.setPaymentReference(json.getPaymentReference());
		setStatus(cancel, json);
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
		processing.setPdfLink(json.getPdfLink());
		processing.setExternalOrderId(json.getExternalOrderId());
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
		} else if (paymentType instanceof HirePurchaseRatePlan) {
			return map((HirePurchaseRatePlan) paymentType, (JsonHirePurchaseRatePlan) jsonPaymentType);
		} else {
			throw new UnsupportedPaymentTypeException(
					"Type '" + paymentType.getClass().getName() + "' is currently now supported by the SDK");
		}
	}

	private HirePurchaseRatePlan map(HirePurchaseRatePlan paymentType, JsonHirePurchaseRatePlan jsonPaymentType) {
		paymentType.setAccountHolder(jsonPaymentType.getAccountHolder());
		paymentType.setBic(jsonPaymentType.getBic());
		paymentType.setEffectiveInterestRate(jsonPaymentType.getEffectiveInterestRate());
		paymentType.setFeeFirstRate(jsonPaymentType.getFeeFirstRate());
		paymentType.setFeePerRate(jsonPaymentType.getFeePerRate());
		paymentType.setIban(jsonPaymentType.getIban());
		paymentType.setId(jsonPaymentType.getId());
		paymentType.setInvoiceDate(jsonPaymentType.getInvoiceDate());
		paymentType.setInvoiceDueDate(jsonPaymentType.getInvoiceDueDate());
		paymentType.setLastRate(jsonPaymentType.getLastRate());
		paymentType.setMonthlyRate(jsonPaymentType.getMonthlyRate());
		paymentType.setNominalInterestRate(jsonPaymentType.getNominalInterestRate());
		paymentType.setNumberOfRates(jsonPaymentType.getNumberOfRates());
		paymentType.setOrderDate(jsonPaymentType.getOrderDate());
		paymentType.setRateList(jsonPaymentType.getRateList());
		paymentType.setRecurring(jsonPaymentType.getRecurring());
		paymentType.setTotalAmount(jsonPaymentType.getTotalAmount());
		paymentType.setTotalInterestAmount(jsonPaymentType.getTotalInterestAmount());
		paymentType.setTotalPurchaseAmount(jsonPaymentType.getTotalPurchaseAmount());
		return paymentType;
	}

	private PaymentType map(Card card, JsonCard jsonCard) {
		card.setCvc(jsonCard.getCvc());
		card.setExpiryDate(jsonCard.getExpiryDate());
		card.setNumber(jsonCard.getNumber());
		card.setId(jsonCard.getId());
		card.set3ds(jsonCard.get3ds());
		card.setRecurring(jsonCard.getRecurring());
		card.setBrand(jsonCard.getBrand());
		card.setMethod(jsonCard.getMethod());
		card.setCardHolder(jsonCard.getCardHolder());
		CardDetails cardDetails = mapCardDetails(jsonCard.getCardDetails());
		card.setCardDetails(cardDetails);
		return card;
	}

	private CardDetails mapCardDetails(JsonCardDetails jsonCardDetails) {
		CardDetails cardDetails = new CardDetails();
		cardDetails.setAccount(jsonCardDetails.getAccount());
		cardDetails.setCardType(jsonCardDetails.getCardType());
		cardDetails.setCountryIsoA2(jsonCardDetails.getCountryIsoA2());
		cardDetails.setCountryName(jsonCardDetails.getCountryName());
		cardDetails.setIssuerName(jsonCardDetails.getIssuerName());
		cardDetails.setIssuerPhoneNumber(jsonCardDetails.getIssuerPhoneNumber());
		cardDetails.setIssuerUrl(jsonCardDetails.getIssuerUrl());
		return cardDetails;
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

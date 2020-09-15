package com.heidelpay.payment.communication.mapper;

import com.heidelpay.payment.AbstractPayment;
import com.heidelpay.payment.AbstractTransaction;
import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Cancel;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.CommercialSector;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.CustomerCompanyData;
import com.heidelpay.payment.Linkpay;
import com.heidelpay.payment.Payout;
import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.Processing;
import com.heidelpay.payment.Recurring;
import com.heidelpay.payment.Shipment;
import com.heidelpay.payment.communication.json.JSonCompanyInfo;
import com.heidelpay.payment.communication.json.JsonAuthorization;
import com.heidelpay.payment.communication.json.JsonCancel;
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonCustomer;
import com.heidelpay.payment.communication.json.JsonIdObject;
import com.heidelpay.payment.communication.json.JsonInitPayment;
import com.heidelpay.payment.communication.json.JsonLinkpay;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.communication.json.JsonPayment;
import com.heidelpay.payment.communication.json.JsonPayout;
import com.heidelpay.payment.communication.json.JsonPaypage;
import com.heidelpay.payment.communication.json.JsonProcessing;
import com.heidelpay.payment.communication.json.JsonRecurring;
import com.heidelpay.payment.communication.json.JsonResources;
import com.heidelpay.payment.communication.json.JsonShipment;
import com.heidelpay.payment.communication.json.JsonState;
import com.heidelpay.payment.marketplace.MarketplaceCancel;
import com.heidelpay.payment.paymenttypes.PaymentType;

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

	public JsonObject map(AbstractTransaction<? extends AbstractPayment> abstractInitPayment) {
		JsonInitPayment json = new JsonInitPayment();
		json.setAmount(abstractInitPayment.getAmount());
		json.setCurrency(abstractInitPayment.getCurrency());
		json.setReturnUrl(abstractInitPayment.getReturnUrl());
		json.setOrderId(abstractInitPayment.getOrderId());
		json.setResources(getResources(abstractInitPayment));
		json.setCard3ds(abstractInitPayment.getCard3ds());
		json.setPaymentReference(abstractInitPayment.getPaymentReference());

		if (abstractInitPayment instanceof Charge) {
			json = new JsonCharge(json);
			((JsonCharge) json).setInvoiceId(((Charge) abstractInitPayment).getInvoiceId());
		} else if (abstractInitPayment instanceof Payout) {
			json = new JsonPayout(json);
		} else if (abstractInitPayment instanceof Authorization) {
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

	public JsonObject map(MarketplaceCancel cancel) {
		JsonCancel json = new JsonCancel();
		json.setPaymentReference(cancel.getPaymentReference());
		json.setCanceledBasket(cancel.getCanceledBasket());
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
		json.setExcludeTypes(paypage.getExcludeTypes());
		json.setResources(getResources(paypage));
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

	private JsonResources getResources(AbstractTransaction<? extends AbstractPayment> abstractInitPayment) {
		JsonResources json = new JsonResources();
		json.setCustomerId(abstractInitPayment.getCustomerId());
		json.setMetadataId(abstractInitPayment.getMetadataId());
		json.setTypeId(abstractInitPayment.getTypeId());
		json.setRiskId(abstractInitPayment.getRiskId());
		json.setBasketId(abstractInitPayment.getBasketId());
		json.setTraceId(abstractInitPayment.getTraceId());
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
		if (json.getResources() != null) {
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
		paypage.setExcludeTypes(json.getExcludeTypes());
		paypage.setRedirectUrl(json.getRedirectUrl());
		paypage.setAction(json.getAction());

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
		setStatus(recurring, json.getIsSuccess(), json.getIsPending(), json.getIsError());
		return recurring;
	}

	public <T extends AbstractPayment> AbstractTransaction<T> mapToBusinessObject(AbstractTransaction<T> abstractInitTransaction, JsonInitPayment json) {
		abstractInitTransaction.setId(json.getId());
		abstractInitTransaction.setAmount(json.getAmount());
		abstractInitTransaction.setCurrency(json.getCurrency());
		abstractInitTransaction.setOrderId(json.getOrderId());
		abstractInitTransaction.setCard3ds(json.getCard3ds());
		abstractInitTransaction.setPaymentReference(json.getPaymentReference());
		if (json.getResources() != null) {
			abstractInitTransaction.setCustomerId(json.getResources().getCustomerId());
			abstractInitTransaction.setMetadataId(json.getResources().getMetadataId());
			abstractInitTransaction.setPaymentId(json.getResources().getPaymentId());
			abstractInitTransaction.setRiskId(json.getResources().getRiskId());
			abstractInitTransaction.setTypeId(json.getResources().getTypeId());
			abstractInitTransaction.setTraceId(json.getResources().getTraceId());
		}
		abstractInitTransaction.setReturnUrl(json.getReturnUrl());
		abstractInitTransaction.setProcessing(getProcessing(json.getProcessing()));
		abstractInitTransaction.setRedirectUrl(json.getRedirectUrl());
		abstractInitTransaction.setMessage(json.getMessage());
		abstractInitTransaction.setDate(json.getDate());

		setStatus(abstractInitTransaction, json.getIsSuccess(), json.getIsPending(), json.getIsError());
		return abstractInitTransaction;
	}

	private JSonCompanyInfo getCompanyInfo(CustomerCompanyData customer, String company) {
		if (customer == null)
			return null;
		JSonCompanyInfo json = new JSonCompanyInfo();
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
		if (json == null)
			return null;
		if (allFieldsNull(json))
			return null;
		CustomerCompanyData company = new CustomerCompanyData();
		company.setCommercialRegisterNumber(json.getCommercialRegisterNumber());
		if (json.getCommercialSector() != null) {
			company.setCommercialSector(CommercialSector.valueOf(json.getCommercialSector()));
		}
		if (json.getRegistrationType() != null) {
			company.setRegistrationType(
					CustomerCompanyData.RegistrationType.valueOf(json.getRegistrationType().toUpperCase()));
		}
		return company;
	}

	private boolean allFieldsNull(JSonCompanyInfo json) {
		if (json.getCommercialRegisterNumber() != null)
			return false;
		if (json.getCommercialSector() != null)
			return false;
		if (json.getFunction() != null)
			return false;
		return json.getRegistrationType() == null;
	}
	
	private <T extends AbstractPayment> void setStatus(AbstractTransaction<T> transaction, boolean isSuccess, boolean isPending, boolean isError) {
		if (isSuccess) {
			transaction.setStatus(AbstractTransaction.Status.SUCCESS);
		} else if (isPending) {
			transaction.setStatus(AbstractTransaction.Status.PENDING);
		} else if (isError) {
			transaction.setStatus(AbstractTransaction.Status.ERRROR);
		}
	}

	public <T extends AbstractPayment> AbstractTransaction<T> mapToBusinessObject(AbstractTransaction<T> cancel, JsonCancel json) {
		cancel.setId(json.getId());
		cancel.setAmount(json.getAmount());
		cancel.setProcessing(getProcessing(json.getProcessing()));
		cancel.setMessage(json.getMessage());
		cancel.setDate(json.getDate());
		cancel.setPaymentReference(json.getPaymentReference());
		setStatus(cancel, json.isSuccess(), json.isPending(), json.isError());
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
		processing.setZgReferenceId(json.getZgReferenceId());
		processing.setCreatorId(json.getCreatorId());
		processing.setIdentification(json.getIdentification());
		processing.setTraceId(json.getTraceId());
		processing.setParticipantId(json.getParticipantId());
		return processing;
	}

	public <T extends AbstractPayment> T mapToBusinessObject(T payment, JsonPayment json) {
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

	private AbstractPayment.State getPaymentState(JsonState state) {
		if (state == null)
			return null;
		if (state.getId() == 0)
			return AbstractPayment.State.PENDING;
		if (state.getId() == 1)
			return AbstractPayment.State.COMPLETED;
		if (state.getId() == 2)
			return AbstractPayment.State.CANCELED;
		if (state.getId() == 3)
			return AbstractPayment.State.PARTLY;
		if (state.getId() == 4)
			return AbstractPayment.State.PAYMENT_REVIEW;
		if (state.getId() == 5)
			return AbstractPayment.State.CHARGEBACK;
		return null;
	}

	public PaymentType mapToBusinessObject(PaymentType paymentType, JsonIdObject jsonPaymentType) {
		return paymentType.map(paymentType, jsonPaymentType);
	}
}

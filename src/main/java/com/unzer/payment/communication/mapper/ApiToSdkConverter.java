package com.unzer.payment.communication.mapper;

import com.unzer.payment.AbstractPayment;
import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Authorization;
import com.unzer.payment.Cancel;
import com.unzer.payment.CommercialSector;
import com.unzer.payment.Customer;
import com.unzer.payment.CustomerCompanyData;
import com.unzer.payment.Linkpay;
import com.unzer.payment.PaylaterInstallmentPlans;
import com.unzer.payment.Payout;
import com.unzer.payment.Paypage;
import com.unzer.payment.Processing;
import com.unzer.payment.Recurring;
import com.unzer.payment.Shipment;
import com.unzer.payment.communication.json.ApiAuthorization;
import com.unzer.payment.communication.json.ApiCancel;
import com.unzer.payment.communication.json.ApiCustomer;
import com.unzer.payment.communication.json.ApiIdObject;
import com.unzer.payment.communication.json.ApiInitPayment;
import com.unzer.payment.communication.json.ApiLinkpay;
import com.unzer.payment.communication.json.ApiObject;
import com.unzer.payment.communication.json.ApiPayment;
import com.unzer.payment.communication.json.ApiPayout;
import com.unzer.payment.communication.json.ApiPaypage;
import com.unzer.payment.communication.json.ApiRecurring;
import com.unzer.payment.communication.json.ApiShipment;
import com.unzer.payment.communication.json.JsonCompanyInfo;
import com.unzer.payment.communication.json.JsonProcessing;
import com.unzer.payment.communication.json.JsonResources;
import com.unzer.payment.communication.json.JsonState;
import com.unzer.payment.communication.json.TransactionStatus;
import com.unzer.payment.communication.json.paylater.ApiInstallmentPlans;
import com.unzer.payment.marketplace.MarketplaceCancel;
import com.unzer.payment.paymenttypes.PaymentType;
import java.util.Locale;
import java.util.Optional;

public class ApiToSdkConverter {

  public ApiObject map(AbstractTransaction<? extends AbstractPayment> src) {
    ApiInitPayment out = new ApiInitPayment();
    out.setAmount(src.getAmount());
    out.setCurrency(src.getCurrency());
    out.setReturnUrl(src.getReturnUrl());
    out.setOrderId(src.getOrderId());
    out.setInvoiceId(src.getInvoiceId());
    out.setResources(getResources(src));
    out.setCard3ds(src.getCard3ds());
    out.setPaymentReference(src.getPaymentReference());
    out.setAdditionalTransactionData(src.getAdditionalTransactionData());

    if (src instanceof Payout) {
      out = new ApiPayout(out);
    } else if (src instanceof Authorization) {
      out = new ApiAuthorization(out);
      out.setEffectiveInterestRate(
          ((Authorization) src).getEffectiveInterestRate());
    }

    return out;
  }

  private JsonResources getResources(
      AbstractTransaction<? extends AbstractPayment> src
  ) {
    JsonResources out = new JsonResources();
    out.setCustomerId(src.getCustomerId());
    out.setMetadataId(src.getMetadataId());
    out.setTypeId(src.getTypeId());
    out.setRiskId(src.getRiskId());
    out.setBasketId(src.getBasketId());
    out.setTraceId(src.getTraceId());
    out.setPayPageId(src.getPaypageId());
    return out;
  }

  public ApiRecurring map(Recurring src) {
    ApiRecurring out = new ApiRecurring();
    out.setReturnUrl(src.getReturnUrl());
    out.setResources(getResources(src));
    out.setAdditionalTransactionData(src.getAdditionalTransactionData());
    return out;
  }

  private JsonResources getResources(Recurring src) {
    JsonResources out = new JsonResources();
    out.setCustomerId(src.getCustomerId());
    out.setMetadataId(src.getMetadataId());
    return out;
  }

  public ApiCancel map(Cancel src) {
    ApiCancel out = new ApiCancel();
    out.setAmount(src.getAmount());
    out.setOrderId(src.getOrderId());
    out.setInvoiceId(src.getInvoiceId());
    out.setPaymentReference(src.getPaymentReference());

    if (src.getReasonCode() != null) {
      out.setReasonCode(src.getReasonCode().toString());
    }
    return out;
  }

  public ApiObject map(MarketplaceCancel src) {
    ApiCancel out = new ApiCancel();
    out.setPaymentReference(src.getPaymentReference());
    out.setCanceledBasket(src.getCanceledBasket());
    return out;
  }

  public ApiObject map(Paypage src) {
    ApiPaypage out = new ApiPaypage();
    out.setId(src.getId());
    out.setAmount(src.getAmount());
    out.setCurrency(src.getCurrency());
    out.setReturnUrl(src.getReturnUrl());
    out.setLogoImage(src.getLogoImage());
    out.setFullPageImage(src.getFullPageImage());
    out.setShopName(src.getShopName());
    out.setShopDescription(src.getShopDescription());
    out.setTagline(src.getTagline());
    out.setCss(src.getCss());
    out.setTermsAndConditionUrl(src.getTermsAndConditionUrl());
    out.setPrivacyPolicyUrl(src.getPrivacyPolicyUrl());
    out.setImprintUrl(src.getImprintUrl());
    out.setHelpUrl(src.getHelpUrl());
    out.setContactUrl(src.getContactUrl());
    out.setInvoiceId(src.getInvoiceId());
    out.setOrderId(src.getOrderId());
    out.setCard3ds(src.getCard3ds());
    out.setBillingAddressRequired(src.getBillingAddressRequired());
    out.setShippingAddressRequired(src.getShippingAddressRequired());
    out.setAdditionalAttributes(src.getAdditionalAttributes());
    out.setExcludeTypes(src.getExcludeTypes());
    out.setResources(getResources(src));
    return out;
  }

  private JsonResources getResources(Paypage src) {
    JsonResources out = new JsonResources();
    out.setCustomerId(src.getCustomerId());
    out.setMetadataId(src.getMetadataId());
    out.setBasketId(src.getBasketId());
    return out;
  }

  public ApiObject map(Linkpay src) {
    ApiLinkpay out = new ApiLinkpay();
    out.setId(src.getId());
    out.setAmount(src.getAmount());
    out.setCurrency(src.getCurrency());
    out.setReturnUrl(src.getReturnUrl());
    out.setLogoImage(src.getLogoImage());
    out.setFullPageImage(src.getFullPageImage());
    out.setShopName(src.getShopName());
    out.setTermsAndConditionUrl(src.getTermsAndConditionUrl());
    out.setPrivacyPolicyUrl(src.getPrivacyPolicyUrl());
    out.setHelpUrl(src.getHelpUrl());
    out.setContactUrl(src.getContactUrl());
    out.setOrderId(src.getOrderId());
    out.setResources(getResources(src));
    out.setShopDescription(src.getShopDescription());
    out.setTagline(src.getTagline());
    out.setImprintUrl(src.getImprintUrl());
    out.setInvoiceId(src.getInvoiceId());
    out.setCard3ds(src.getCard3ds());
    out.setBillingAddressRequired(src.getBillingAddressRequired());
    out.setShippingAddressRequired(src.getShippingAddressRequired());
    out.setAdditionalAttributes(src.getAdditionalAttributes());
    out.setAction(src.getAction());
    out.setExcludeTypes(src.getExcludeTypes());
    out.setCss(src.getCss());
    out.setAlias(src.getAlias());
    out.setExpires(src.getExpires());
    out.setIntention(src.getIntention());
    out.setPaymentReference(src.getPaymentReference());
    out.setOrderIdRequired(src.getOrderIdRequired());
    out.setInvoiceIdRequired(src.getInvoiceIdRequired());
    out.setOneTimeUse(src.getOneTimeUse());
    out.setSuccessfullyProcessed(src.getSuccessfullyProcessed());
    out.setRedirectUrl(src.getRedirectUrl());
    out.setVersion(src.getVersion());
    return out;
  }

  private JsonResources getResources(Linkpay linkpay) {
    JsonResources out = new JsonResources();
    out.setCustomerId(linkpay.getCustomerId());
    out.setMetadataId(linkpay.getMetadataId());
    out.setBasketId(linkpay.getBasketId());
    return out;
  }

  public Linkpay mapToBusinessObject(Linkpay out, ApiLinkpay src) {
    out.setId(src.getId());
    out.setAmount(src.getAmount());
    out.setCurrency(src.getCurrency());
    out.setReturnUrl(src.getReturnUrl());
    out.setLogoImage(src.getLogoImage());
    out.setFullPageImage(src.getFullPageImage());
    out.setShopName(src.getShopName());
    out.setShopDescription(src.getShopDescription());
    out.setTagline(src.getTagline());
    out.setCss(src.getCss());
    out.setAlias(src.getAlias());
    out.setTermsAndConditionUrl(src.getTermsAndConditionUrl());
    out.setPrivacyPolicyUrl(src.getPrivacyPolicyUrl());
    out.setImprintUrl(src.getImprintUrl());
    out.setHelpUrl(src.getHelpUrl());
    out.setContactUrl(src.getContactUrl());
    out.setVersion(src.getVersion());
    out.setRedirectUrl(src.getRedirectUrl());
    out.setAction(src.getAction());
    out.setCard3ds(src.getCard3ds());
    out.setExpires(src.getExpires());
    out.setOrderId(src.getOrderId());
    out.setInvoiceId(src.getInvoiceId());
    out.setBillingAddressRequired(src.getBillingAddressRequired());
    out.setShippingAddressRequired(src.getShippingAddressRequired());
    out.setAdditionalAttributes(src.getAdditionalAttributes());
    out.setIntention(src.getIntention());
    out.setOrderIdRequired(src.getOrderIdRequired());
    out.setInvoiceIdRequired(src.getInvoiceIdRequired());
    out.setOneTimeUse(src.getOneTimeUse());
    out.setSuccessfullyProcessed(src.getSuccessfullyProcessed());
    out.setExcludeTypes(src.getExcludeTypes());
    out.setPaymentReference(src.getPaymentReference());
    if (src.getResources() != null) {
      JsonResources outResources = src.getResources();
      out.setCustomerId(outResources.getCustomerId());
      out.setMetadataId(outResources.getMetadataId());
      out.setPaymentId(outResources.getPaymentId());
      out.setBasketId(outResources.getBasketId());
    }
    return out;
  }

  public Paypage mapToBusinessObject(Paypage out, ApiPaypage src) {
    out.setId(src.getId());
    out.setAmount(src.getAmount());
    out.setCurrency(src.getCurrency());
    out.setReturnUrl(src.getReturnUrl());
    out.setLogoImage(src.getLogoImage());
    out.setFullPageImage(src.getFullPageImage());
    out.setShopName(src.getShopName());
    out.setShopDescription(src.getShopDescription());
    out.setTagline(src.getTagline());
    out.setCss(src.getCss());
    out.setTermsAndConditionUrl(src.getTermsAndConditionUrl());
    out.setPrivacyPolicyUrl(src.getPrivacyPolicyUrl());
    out.setImprintUrl(src.getImprintUrl());
    out.setHelpUrl(src.getHelpUrl());
    out.setContactUrl(src.getContactUrl());
    out.setInvoiceId(src.getInvoiceId());
    out.setOrderId(src.getOrderId());
    out.setCard3ds(src.getCard3ds());
    out.setBillingAddressRequired(src.getBillingAddressRequired());
    out.setShippingAddressRequired(src.getShippingAddressRequired());
    out.setAdditionalAttributes(src.getAdditionalAttributes());
    out.setExcludeTypes(src.getExcludeTypes());
    out.setRedirectUrl(src.getRedirectUrl());
    out.setAction(src.getAction());

    if (src.getResources() != null) {
      out.setBasketId(src.getResources().getBasketId());
      out.setCustomerId(src.getResources().getCustomerId());
      out.setMetadataId(src.getResources().getMetadataId());
      out.setPaymentId(src.getResources().getPaymentId());
    }
    return out;
  }

  public Recurring mapToBusinessObject(Recurring out, ApiRecurring src) {
    out.setDate(src.getDate());
    out.setMessage(src.getMessage());
    if (src.getResources() != null) {
      out.setMetadataId(src.getResources().getMetadataId());
      out.setCustomerId(src.getResources().getCustomerId());
    }
    out.setProcessing(getProcessing(src.getProcessing()));
    out.setRedirectUrl(src.getRedirectUrl());
    out.setReturnUrl(src.getReturnUrl());
    out.setAdditionalTransactionData(src.getAdditionalTransactionData());
    out.setStatus(extractStatus(src));
    return out;
  }

  private Processing getProcessing(JsonProcessing src) {
    Processing out = new Processing();
    out.setUniqueId(src.getUniqueId());
    out.setShortId(src.getShortId());
    out.setBic(src.getBic());
    out.setDescriptor(src.getDescriptor());
    out.setHolder(src.getHolder());
    out.setIban(src.getIban());
    out.setPdfLink(src.getPdfLink());
    out.setExternalOrderId(src.getExternalOrderId());
    out.setZgReferenceId(src.getZgReferenceId());
    out.setCreatorId(src.getCreatorId());
    out.setIdentification(src.getIdentification());
    out.setTraceId(src.getTraceId());
    out.setParticipantId(src.getParticipantId());
    return out;
  }

  private AbstractTransaction.Status extractStatus(TransactionStatus src) {
    // Resumed has to be the first,
    // because currently Payment API returns several statuses if isResumed set
    if (Optional.ofNullable(src.getResumed()).orElse(false)) {
      return AbstractTransaction.Status.RESUMED;
    }

    if (src.getSuccess()) {
      return AbstractTransaction.Status.SUCCESS;
    } else if (src.getPending()) {
      return AbstractTransaction.Status.PENDING;
    } else if (src.getError()) {
      return AbstractTransaction.Status.ERROR;
    }

    return null;
  }

  public <T extends AbstractPayment> AbstractTransaction<T> mapToBusinessObject(
      ApiInitPayment src, AbstractTransaction<T> out
  ) {
    out.setId(src.getId());
    out.setAmount(src.getAmount());
    out.setCurrency(src.getCurrency());
    out.setOrderId(src.getOrderId());
    out.setCard3ds(src.getCard3ds());
    out.setInvoiceId(src.getInvoiceId());
    out.setOrderId(src.getOrderId());
    out.setPaymentReference(src.getPaymentReference());
    if (src.getResources() != null) {
      out.setCustomerId(src.getResources().getCustomerId());
      out.setMetadataId(src.getResources().getMetadataId());
      out.setPaymentId(src.getResources().getPaymentId());
      out.setRiskId(src.getResources().getRiskId());
      out.setTypeId(src.getResources().getTypeId());
      out.setTraceId(src.getResources().getTraceId());
      out.setPaypageId(src.getResources().getPayPageId());
    }
    out.setReturnUrl(src.getReturnUrl());
    out.setProcessing(getProcessing(src.getProcessing()));
    out.setRedirectUrl(src.getRedirectUrl());
    out.setMessage(src.getMessage());
    out.setDate(src.getDate());
    out.setAdditionalTransactionData(src.getAdditionalTransactionData());

    out.setStatus(extractStatus(src));
    return out;
  }

  public ApiCustomer map(Customer src) {
    ApiCustomer out = new ApiCustomer();
    out.setFirstname(src.getFirstname());
    out.setLastname(src.getLastname());
    out.setCompany(src.getCompany());

    if (src.getLanguage() != null) {
      out.setLanguage(src.getLanguage().getLanguage());
    }

    out.setCustomerId(src.getCustomerId());
    out.setEmail(src.getEmail());
    out.setMobile(src.getMobile());
    out.setPhone(src.getPhone());
    out.setSalutation(src.getSalutation());
    out.setBirthDate(src.getBirthDate());

    out.setBillingAddress(src.getBillingAddress());
    out.setShippingAddress(src.getShippingAddress());
    out.setCompanyInfo(getCompanyInfo(src.getCompanyData(), src.getCompany()));
    return out;
  }

  private JsonCompanyInfo getCompanyInfo(CustomerCompanyData src, String company) {
    if (src == null) {
      return null;
    }
    JsonCompanyInfo out = new JsonCompanyInfo();
    if (company != null) {
      mapRegisteredCompany(src, out);
    } else {
      mapUnregisteredCompany(src, out);
    }
    return out;
  }

  private void mapRegisteredCompany(CustomerCompanyData src, JsonCompanyInfo out) {
    out.setRegistrationType("registered");
    out.setCommercialRegisterNumber(src.getCommercialRegisterNumber());
  }

  private void mapUnregisteredCompany(CustomerCompanyData src, JsonCompanyInfo out) {
    out.setFunction("OWNER");
    out.setRegistrationType("not_registered");
    if (src.getCommercialSector() != null) {
      out.setCommercialSector(src.getCommercialSector().toString());
    }
  }

  public Customer mapToBusinessObject(ApiCustomer src, Customer out) {
    out.setId(src.getId());
    out.setFirstname(src.getFirstname());
    out.setLastname(src.getLastname());
    out.setCompany(src.getCompany());
    out.setCustomerId(src.getCustomerId());

    if (src.getLanguage() != null && !src.getLanguage().isEmpty()) {
      out.setLanguage(new Locale(src.getLanguage()));
    }

    out.setEmail(src.getEmail());
    out.setMobile(src.getMobile());
    out.setPhone(src.getPhone());
    out.setSalutation(src.getSalutation());
    out.setBirthDate(src.getBirthDate());

    out.setBillingAddress(src.getBillingAddress());
    out.setShippingAddress(src.getShippingAddress());
    out.setCompanyData(getCompanyInfo(src.getCompanyInfo()));
    return out;
  }

  private CustomerCompanyData getCompanyInfo(JsonCompanyInfo src) {
    if (src == null) {
      return null;
    }
    if (allFieldsNull(src)) {
      return null;
    }
    CustomerCompanyData out = new CustomerCompanyData();
    out.setCommercialRegisterNumber(src.getCommercialRegisterNumber());
    if (src.getCommercialSector() != null) {
      out.setCommercialSector(CommercialSector.valueOf(src.getCommercialSector()));
    }
    if (src.getRegistrationType() != null) {
      out.setRegistrationType(
          CustomerCompanyData.RegistrationType.valueOf(src.getRegistrationType().toUpperCase()));
    }
    return out;
  }

  private boolean allFieldsNull(JsonCompanyInfo src) {
    if (src.getCommercialRegisterNumber() != null) {
      return false;
    }
    if (src.getCommercialSector() != null) {
      return false;
    }
    if (src.getFunction() != null) {
      return false;
    }
    return src.getRegistrationType() == null;
  }

  public <T extends AbstractPayment> AbstractTransaction<T> mapToBusinessObject(
      ApiCancel src, AbstractTransaction<T> out
  ) {
    out.setId(src.getId());
    out.setAmount(src.getAmount());
    out.setProcessing(getProcessing(src.getProcessing()));
    out.setMessage(src.getMessage());
    out.setDate(src.getDate());
    out.setPaymentReference(src.getPaymentReference());
    out.setInvoiceId(src.getInvoiceId());
    out.setOrderId(src.getOrderId());
    out.setStatus(extractStatus(src));
    return out;
  }

  public Shipment mapToBusinessObject(ApiShipment src, Shipment out) {
    out.setId(src.getId());
    out.setMessage(src.getMessage());
    out.setDate(src.getDate());
    return out;
  }

  public <T extends AbstractPayment> T mapToBusinessObject(ApiPayment src, T out) {
    out.setAmountTotal(src.getAmount().getTotal());
    out.setAmountCanceled(src.getAmount().getCanceled());
    out.setAmountCharged(src.getAmount().getCharged());
    out.setAmountRemaining(src.getAmount().getRemaining());
    out.setOrderId(src.getOrderId());
    out.setPaymentState(getPaymentState(src.getState()));
    out.setId(src.getId());

    JsonResources resources = src.getResources();
    if (resources != null) {
      out.setPaymentTypeId(resources.getTypeId());
      out.setCustomerId(resources.getCustomerId());
      out.setMetadataId(resources.getMetadataId());
      out.setBasketId(resources.getBasketId());
      out.setPaypageId(resources.getPayPageId());
    }

    return out;
  }

  private AbstractPayment.State getPaymentState(JsonState src) {
    if (src == null) {
      return null;
    }

    if (src.getId() >= 0 && src.getId() <= 5) {
      return AbstractPayment.State.values()[src.getId()];
    }

    return null;
  }

  public PaymentType mapToBusinessObject(PaymentType out, ApiIdObject src) {
    return out.map(out, src);
  }

  public PaylaterInstallmentPlans mapToBusinessObject(PaylaterInstallmentPlans installmentPlans,
                                                      ApiInstallmentPlans jsonInstallmentPlans) {
    installmentPlans.setStatus(extractStatus(jsonInstallmentPlans));
    return installmentPlans.map(installmentPlans, jsonInstallmentPlans);
  }
}

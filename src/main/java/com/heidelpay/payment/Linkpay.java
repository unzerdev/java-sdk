package com.heidelpay.payment;

import com.heidelpay.payment.paymenttypes.PaymentType;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Currency;
import java.util.Map;

public class Linkpay implements PaymentType {

	private String id;

	private String redirectUrl;

	private String version;

	private String alias;

	private String orderId;

	private String invoiceId;

	private BigDecimal amount;

	private Currency currency;

	private URL returnUrl;

	private String logoImage;

	private String fullPageImage;

	private String shopName;

	private String shopDescription;

	private String tagline;

	private Map<String, String> css;

	private URL termsAndConditionUrl;

	private URL privacyPolicyUrl;

	private URL imprintUrl;

	private URL helpUrl;

	private URL contactUrl;

	private String card3ds;

	private String billingAddressRequired;

	private String shippingAddressRequired;

	private String expires;

	private String intention;

	private String paymentReference;

	private Map<String, String> additionalAttributes;

	private String orderIdRequired;

	private String invoiceIdRequired;

	private String oneTimeUse;

	private String successfullyProcessed;

	private String[] excludeTypes = new String[] {};

	private String action;

	private String customerId;
	private String metadataId;
	private String paymentId;
	private String basketId;

	@Override
	public String getTypeUrl() {
		return "linkpay/charge";
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * @return the returnUrl
	 */
	public URL getReturnUrl() {
		return returnUrl;
	}

	/**
	 * @param returnUrl the returnUrl to set
	 */
	public void setReturnUrl(URL returnUrl) {
		this.returnUrl = returnUrl;
	}

	/**
	 * @return the logoImage
	 */
	public String getLogoImage() {
		return logoImage;
	}

	/**
	 * @param logoImage the logoImage to set
	 */
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}

	/**
	 * @return the fullPageImage
	 */
	public String getFullPageImage() {
		return fullPageImage;
	}

	/**
	 * @param fullPageImage the fullPageImage to set
	 */
	public void setFullPageImage(String fullPageImage) {
		this.fullPageImage = fullPageImage;
	}

	/**
	 * @return the shopName
	 */
	public String getShopName() {
		return shopName;
	}

	/**
	 * @param shopName the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	/**
	 * @return the shopDescription
	 */
	public String getShopDescription() {
		return shopDescription;
	}

	/**
	 * @param shopDescription the shopDescription to set
	 */
	public void setShopDescription(String shopDescription) {
		this.shopDescription = shopDescription;
	}

	/**
	 * @return the tagline
	 */
	public String getTagline() {
		return tagline;
	}

	/**
	 * @param tagline the tagline to set
	 */
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public Map<String, String> getCss() {
		return css;
	}

	public void setCss(Map<String, String> css) {
		this.css = css;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public URL getTermsAndConditionUrl() {
		return termsAndConditionUrl;
	}

	public void setTermsAndConditionUrl(URL termsAndConditionUrl) {
		this.termsAndConditionUrl = termsAndConditionUrl;
	}

	public URL getPrivacyPolicyUrl() {
		return privacyPolicyUrl;
	}

	public void setPrivacyPolicyUrl(URL privacyPolicyUrl) {
		this.privacyPolicyUrl = privacyPolicyUrl;
	}

	public URL getImprintUrl() {
		return imprintUrl;
	}

	public void setImprintUrl(URL imprintUrl) {
		this.imprintUrl = imprintUrl;
	}

	public URL getHelpUrl() {
		return helpUrl;
	}

	public void setHelpUrl(URL helpUrl) {
		this.helpUrl = helpUrl;
	}

	public URL getContactUrl() {
		return contactUrl;
	}

	public void setContactUrl(URL contactUrl) {
		this.contactUrl = contactUrl;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the redirectUrl
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * @param redirectUrl the redirectUrl to set
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the card3ds
	 */
	public String getCard3ds() {
		return card3ds;
	}

	/**
	 * @param card3ds the card3ds to set
	 */
	public void setCard3ds(String card3ds) {
		this.card3ds = card3ds;
	}

	/**
	 * @param expires the expires to set
	 */
	public void setExpires(String expires) {
		this.expires = expires;
	}

	/**
	 * @return the expires
	 */
	public String getExpires() {
		return expires;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param invoiceId the invoiceId to set
	 */
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * @return the billingAddressRequired
	 */
	public String getBillingAddressRequired() {
		return billingAddressRequired;
	}

	/**
	 * @param billingAddressRequired the billingAddressRequired to set
	 */
	public void setBillingAddressRequired(String billingAddressRequired) {
		this.billingAddressRequired = billingAddressRequired;
	}

	/**
	 * @return the billingAddressRequired
	 */
	public String getShippingAddressRequired() {
		return shippingAddressRequired;
	}

	/**
	 * @param shippingAddressRequired the shippingAddressRequired to set
	 */
	public void setShippingAddressRequired(String shippingAddressRequired) {
		this.shippingAddressRequired = shippingAddressRequired;
	}

	/**
	 * @return the additionalAttributes
	 */
	public Map<String, String> getAdditionalAttributes() {
		return additionalAttributes;
	}

	/**
	 * @param additionalAttributes the additionalAttributes to set
	 */
	public void setAdditionalAttributes(Map<String, String> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}

	/**
	 * @return the intention
	 */
	public String getIntention() {
		return intention;
	}

	/**
	 * @param intention the intention to set
	 */
	public void setIntention(String intention) {
		this.intention = intention;
	}

	public String getOrderIdRequired() {
		return orderIdRequired;
	}

	public void setOrderIdRequired(String orderIdRequired) {
		this.orderIdRequired = orderIdRequired;
	}

	public String getInvoiceIdRequired() {
		return invoiceIdRequired;
	}

	public void setInvoiceIdRequired(String invoiceIdRequired) {
		this.invoiceIdRequired = invoiceIdRequired;
	}

	public String getOneTimeUse() {
		return oneTimeUse;
	}

	public void setOneTimeUse(String oneTimeUse) {
		this.oneTimeUse = oneTimeUse;
	}

	public String getSuccessfullyProcessed() {
		return successfullyProcessed;
	}

	public void setSuccessfullyProcessed(String successfullyProcessed) {
		this.successfullyProcessed = successfullyProcessed;
	}

	/**
	 * @return the excludeTypes
	 */
	public String[] getExcludeTypes() {
		return excludeTypes;
	}

	/**
	 * @param excludeTypes the excludeTypes to set
	 */
	public void setExcludeTypes(String[] excludeTypes) {
		this.excludeTypes = excludeTypes;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	@Override
	public String toString() {
		return "\nnet.hpcsoft.mgw.papi.api.linkpay.model.LinkPayResponse{" +
				"\n\tid='" + id + '\'' +
				", \n\tredirectUrl='" + redirectUrl + '\'' +
				", \n\tversion='" + version + '\'' +
				", \n\talias='" + alias + '\'' +
				", \n\torderId='" + orderId + '\'' +
				", \n\tinvoiceId='" + invoiceId + '\'' +
				", \n\tamount='" + amount + '\'' +
				", \n\tcurrency='" + currency + '\'' +
				", \n\treturnUrl='" + returnUrl + '\'' +
				", \n\tlogoImage='" + logoImage + '\'' +
				", \n\tfullPageImage='" + fullPageImage + '\'' +
				", \n\tshopName='" + shopName + '\'' +
				", \n\tshopDescription='" + shopDescription + '\'' +
				", \n\ttagline='" + tagline + '\'' +
				", \n\tcss=" + css +
				", \n\ttermsAndConditionUrl='" + termsAndConditionUrl + '\'' +
				", \n\tprivacyPolicyUrl='" + privacyPolicyUrl + '\'' +
				", \n\timprintUrl='" + imprintUrl + '\'' +
				", \n\thelpUrl='" + helpUrl + '\'' +
				", \n\tcontactUrl='" + contactUrl + '\'' +
				", \n\tcard3ds='" + card3ds + '\'' +
				", \n\tbillingAddressRequired='" + billingAddressRequired + '\'' +
				", \n\tshippingAddressRequired='" + shippingAddressRequired + '\'' +
				", \n\texpires='" + expires + '\'' +
				", \n\tintention='" + intention + '\'' +
				", \n\tadditionalAttributes=" + additionalAttributes +
				", \n\torderIdRequired='" + orderIdRequired + '\'' +
				", \n\tinvoiceIdRequired='" + invoiceIdRequired + '\'' +
				", \n\toneTimeUse='" + oneTimeUse + '\'' +
				", \n\tsuccessfullyProcessed='" + successfullyProcessed + '\'' +
				", \n\texcludeTypes=" + Arrays.toString(excludeTypes) +
				", \n\taction=" + action +
				"\n}";
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

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getBasketId() {
		return basketId;
	}

	public void setBasketId(String basketId) {
		this.basketId = basketId;
	}

}

package com.unzer.payment;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Map;

public abstract class BasePaypage extends BaseResource {
    private final static String ACTION_TOKEN = "<action>";

    private String id;
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
    private String invoiceId;
    private String orderId;
    private String card3ds;
    private String billingAddressRequired;
    private String shippingAddressRequired;
    private Map<String, String> additionalAttributes;
    private String[] excludeTypes;
    private Status status;
    private String action;
    private String redirectUrl;
    private String customerId;
    private String metadataId;
    private String paymentId;
    private String basketId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public URL getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(URL returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public String getFullPageImage() {
        return fullPageImage;
    }

    public void setFullPageImage(String fullPageImage) {
        this.fullPageImage = fullPageImage;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
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

    /**
     * @return the css
     */
    public Map<String, String> getCss() {
        return css;
    }

    /**
     * @param css the css to set
     */
    public void setCss(Map<String, String> css) {
        this.css = css;
    }

    /**
     * @return the imprintUrl
     */
    public URL getImprintUrl() {
        return imprintUrl;
    }

    /**
     * @param imprintUrl the imprintUrl to set
     */
    public void setImprintUrl(URL imprintUrl) {
        this.imprintUrl = imprintUrl;
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
     * @return the shippingAddressRequired
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

    @Override
    public String getUrl() {
        String preprocessedUrl = getAction() == null
                ? getResourceUrl().replaceAll(ACTION_TOKEN + "/", "")
                : getResourceUrl().replaceAll(ACTION_TOKEN, getAction().toLowerCase());

        return preprocessedUrl.replaceAll(RESOURCE_ID_TOKEN, getId() == null ? "" : getId());
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public enum Status {SUCCESS, PENDING, ERROR}

    public interface Action {
        String CHARGE = "CHARGE";
        String AUTHORIZE = "AUTHORIZE";
    }
}

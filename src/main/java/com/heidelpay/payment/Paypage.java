package com.heidelpay.payment;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Heidelpay GmbH
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

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;

import com.heidelpay.payment.paymenttypes.PaymentType;

public class Paypage implements PaymentType {
	public enum Status {SUCCESS, PENDING, ERRROR}

	private String id;
	private BigDecimal amount;
	private Currency currency;
	private URL returnUrl;

	private String logoImage;
	private String basketImage;
	private String fullPageImage;
	private String shopName;
	private String descriptionMain;
	private String descriptionSmall;
	private URL termsAndConditionUrl;
	private URL privacyPolicyUrl;
	private URL impressumUrl;
	private URL helpUrl;
	private URL contactUrl;

	private String orderId;
	
	private String customerId;
	private String metadataId;
	private String paymentId;
	private String basketId;

	private Status status;
	
	private String redirectUrl;
	
	public Paypage() {
	}
	
	@Override
	public String getTypeUrl() {
		return "paypage/charge";
	}
	
	
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
	public String getBasketImage() {
		return basketImage;
	}
	public void setBasketImage(String basketImage) {
		this.basketImage = basketImage;
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
	public String getDescriptionMain() {
		return descriptionMain;
	}
	public void setDescriptionMain(String descriptionMain) {
		this.descriptionMain = descriptionMain;
	}
	public String getDescriptionSmall() {
		return descriptionSmall;
	}
	public void setDescriptionSmall(String descriptionSmall) {
		this.descriptionSmall = descriptionSmall;
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
	public URL getImpressumUrl() {
		return impressumUrl;
	}
	public void setImpressumUrl(URL impressumUrl) {
		this.impressumUrl = impressumUrl;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


}

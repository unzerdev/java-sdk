package com.heidelpay.payment;

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

import java.math.BigDecimal;
import java.net.URL;

public class BasketItem {

	private String basketItemReferenceId;
	private Integer quantity;
	private Integer vat;
	private BigDecimal amountDiscount;
	private BigDecimal amountGross;
	private BigDecimal amountVat;
	private BigDecimal amountPerUnit;
	private BigDecimal amountNet;
	private String unit;
	private String title;
	private String subTitle;
	private URL imageUrl;
	private String type;
	
	public BasketItem() {
	}


	public String getBasketItemReferenceId() {
		return basketItemReferenceId;
	}


	public BasketItem setBasketItemReferenceId(String basketItemReferenceId) {
		this.basketItemReferenceId = basketItemReferenceId;
		return this;
	}


	public Integer getQuantity() {
		return quantity;
	}


	public BasketItem setQuantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}


	public Integer getVat() {
		return vat;
	}


	public BasketItem setVat(Integer vat) {
		this.vat = vat;
		return this;
	}


	public BigDecimal getAmountDiscount() {
		return amountDiscount;
	}


	public BasketItem setAmountDiscount(BigDecimal amountDiscount) {
		this.amountDiscount = amountDiscount;
		return this;
	}


	public BigDecimal getAmountGross() {
		return amountGross;
	}


	public BasketItem setAmountGross(BigDecimal amountGross) {
		this.amountGross = amountGross;
		return this;
	}


	public BigDecimal getAmountVat() {
		return amountVat;
	}


	public BasketItem setAmountVat(BigDecimal amountVat) {
		this.amountVat = amountVat;
		return this;
	}


	public BigDecimal getAmountPerUnit() {
		return amountPerUnit;
	}


	public BasketItem setAmountPerUnit(BigDecimal amountPerUnit) {
		this.amountPerUnit = amountPerUnit;
		return this;
	}


	public BigDecimal getAmountNet() {
		return amountNet;
	}


	public BasketItem setAmountNet(BigDecimal amountNet) {
		this.amountNet = amountNet;
		return this;
	}


	public String getUnit() {
		return unit;
	}


	public BasketItem setUnit(String unit) {
		this.unit = unit;
		return this;
	}


	public String getTitle() {
		return title;
	}


	public BasketItem setTitle(String title) {
		this.title = title;
		return this;
	}


	public String getSubTitle() {
		return subTitle;
	}


	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}


	public URL getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(URL imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

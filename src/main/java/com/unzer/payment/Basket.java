package com.unzer.payment;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Basket implements PaymentType {

	private String id;
	private BigDecimal amountTotalGross;
	private BigDecimal amountTotalVat;
	private BigDecimal amountTotalDiscount;
	private Currency currencyCode;
	private String orderId;
	private String note;
	private List<BasketItem> basketItems = new ArrayList<BasketItem>();
	

	public BigDecimal getAmountTotalDiscount() {
		return amountTotalDiscount;
	}

	public Basket setAmountTotalDiscount(BigDecimal amountTotalDiscount) {
		this.amountTotalDiscount = amountTotalDiscount;
		return this;
	}

	public Currency getCurrencyCode() {
		return currencyCode;
	}

	public Basket setCurrencyCode(Currency currencyCode) {
		this.currencyCode = currencyCode;
		return this;
	}

	public String getOrderId() {
		return orderId;
	}

	public Basket setOrderId(String orderId) {
		this.orderId = orderId;
		return this;
	}

	public String getNote() {
		return note;
	}

	public Basket setNote(String note) {
		this.note = note;
		return this;
	}

	public List<BasketItem> getBasketItems() {
		return basketItems;
	}

	public Basket setBasketItems(List<BasketItem> basketItems) {
		this.basketItems = basketItems;
		return this;
	}
	
	public Basket addBasketItem(BasketItem basketItem) {
		getBasketItems().add(basketItem);
		return this;
	}

	@Override
	public String getTypeUrl() {
		return "baskets";
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
		return null;
	}

	public Basket setId(String id) {
		this.id = id;
		return this;
	}

	public BigDecimal getAmountTotalVat() {
		return amountTotalVat;
	}

	public void setAmountTotalVat(BigDecimal amountTotalVat) {
		this.amountTotalVat = amountTotalVat;
	}

	public BigDecimal getAmountTotalGross() {
		return amountTotalGross;
	}

	public Basket setAmountTotalGross(BigDecimal amountTotalGross) {
		this.amountTotalGross = amountTotalGross;
		return this;
	}

}

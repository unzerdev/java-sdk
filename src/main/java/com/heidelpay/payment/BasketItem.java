package com.heidelpay.payment;

import java.math.BigDecimal;

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

}

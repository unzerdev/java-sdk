/*
 * Copyright 2021 Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment;

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Basket implements PaymentType {

    private String id;
    private Currency currencyCode;
    private String orderId;
    private BigDecimal totalValueGross;
    private List<BasketItem> basketItems = new ArrayList();

    private String note;
    @Deprecated
    private BigDecimal amountTotalGross;
    @Deprecated
    private BigDecimal amountTotalVat;
    @Deprecated
    private BigDecimal amountTotalDiscount;

    /**
     * Get generated unique id
     */
    public String getId() {
        return id;
    }

    /**
     * Get basket id
     */
    public Basket setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Currency code in ISO_4217 format
     */
    public Currency getCurrencyCode() {
        return currencyCode;
    }

    public Basket setCurrencyCode(Currency currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    /**
     * A basket or shop reference ID sent from the shop’s back end.
     * Must be equal to the orderID used for the authorization and/or charge trx belonging to the basket.
     * If not value is provided, an order ID is automatically generated by the Unzer system.
     */
    public String getOrderId() {
        return orderId;
    }

    public Basket setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    /**
     * The total basket value (including VAT) of all basket items reduced/deducted by all discounts and vouchers.
     */
    public BigDecimal getTotalValueGross() {
        return totalValueGross;
    }

    /**
     * The total basket value (including VAT) of all basket items reduced/deducted by all discounts and vouchers.
     */
    public Basket setTotalValueGross(BigDecimal totalValueGross) {
        this.totalValueGross = totalValueGross;
        return this;
    }

    /**
     * Get additional details of the basket.
     */
    public String getNote() {
        return note;
    }


    /**
     * Set additional details for the basket.
     */
    public Basket setNote(String note) {
        this.note = note;
        return this;
    }


    /**
     * List of items in the basket
     */
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

    @Deprecated
    public BigDecimal getAmountTotalDiscount() {
        return amountTotalDiscount;
    }

    @Deprecated
    public Basket setAmountTotalDiscount(BigDecimal amountTotalDiscount) {
        this.amountTotalDiscount = amountTotalDiscount;
        return this;
    }

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }

    @Deprecated
    public BigDecimal getAmountTotalVat() {
        return amountTotalVat;
    }

    @Deprecated
    public Basket setAmountTotalVat(BigDecimal amountTotalVat) {
        this.amountTotalVat = amountTotalVat;
        return this;
    }

    @Deprecated
    public BigDecimal getAmountTotalGross() {
        return amountTotalGross;
    }

    @Deprecated
    public Basket setAmountTotalGross(BigDecimal amountTotalGross) {
        this.amountTotalGross = amountTotalGross;
        return this;
    }

    @Deprecated
    public boolean isV2() {
        return this.totalValueGross != null; // mandatory v2 attribute is not null
    }
}

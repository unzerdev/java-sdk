package com.unzer.payment;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.net.URL;

public class BasketItem {
    private String title;
    private String subTitle;
    private URL imageUrl;
    private String basketItemReferenceId;
    private Integer quantity;
    private BigDecimal amountPerUnitGross;
    private BigDecimal vat;
    private Type type;
    private String unit;
    private BigDecimal amountDiscountPerUnitGross;
    private String participantId;
    @Deprecated
    private BigDecimal amountDiscount;
    @Deprecated
    private BigDecimal amountGross;
    @Deprecated
    private BigDecimal amountVat;
    @Deprecated
    private BigDecimal amountPerUnit;
    @Deprecated
    private BigDecimal amountNet;

    /**
     * The title of the basket item
     */
    public String getTitle() {
        return title;
    }

    /**
     * The title of the basket item
     */
    public BasketItem setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * The subtitle for the basket item
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * The subtitle for the basket item
     */
    public BasketItem setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    /**
     * URL to the basket item image, only used for the payment page.
     */
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * URL to the basket item image, only used for the payment page.
     */
    public BasketItem setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    /**
     * Unique basket item reference ID (within the basket).
     */
    public String getBasketItemReferenceId() {
        return basketItemReferenceId;
    }

    /**
     * Unique basket item reference ID (within the basket).
     */
    public BasketItem setBasketItemReferenceId(String basketItemReferenceId) {
        this.basketItemReferenceId = basketItemReferenceId;
        return this;
    }

    /**
     * Quantity of the basket item
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Quantity of the basket item
     */
    public BasketItem setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * Amount per unit including VAT.
     * <p>
     * Note: Discount is not included. Don’t include discounts here!
     */
    public BigDecimal getAmountPerUnitGross() {
        return amountPerUnitGross;
    }

    /**
     * Amount per unit including VAT.
     * <p>
     * Note: Discount is not included. Don’t include discounts here!
     */
    public BasketItem setAmountPerUnitGross(BigDecimal amountPerUnitGross) {
        this.amountPerUnitGross = amountPerUnitGross;
        return this;
    }

    /**
     * The VAT value for the basket item in percentage
     */
    public BigDecimal getVat() {
        return vat;
    }

    /**
     * The VAT value for the basket item in percentage
     */
    public BasketItem setVat(BigDecimal vat) {
        this.vat = vat;
        return this;
    }

    /**
     * The VAT value for the basket item in percentage.
     *
     * @deprecated Use {@link #setVat(BigDecimal)} instead
     */
    @Deprecated
    public BasketItem setVat(Integer vat) {
        this.vat = BigDecimal.valueOf(vat);
        return this;
    }

    /**
     * The type of item in the basket. The valid values are goods, voucher, and shipment.
     */
    public Type getType() {
        return type;
    }

    /**
     * The type of item in the basket. The valid values are goods, voucher, and shipment.
     */
    public BasketItem setType(Type type) {
        this.type = type;
        return this;
    }

    /**
     * @deprecated Use {@link #setType(Type)} instead
     */
    @Deprecated
    public BasketItem setType(String type) {
        this.type = Type.valueOf(type.toUpperCase());
        return this;
    }

    /**
     * The measuring unit for the basket item, e.g. pieces/Pc., kilogram/kg
     */
    public String getUnit() {
        return unit;
    }

    /**
     * The measuring unit for the basket item, e.g. pieces/Pc., kilogram/kg
     */
    public BasketItem setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    /**
     * Discount (incl. VAT) granted on the basketItems.amountGross.
     * Positive amount is expected.
     */
    public BigDecimal getAmountDiscountPerUnitGross() {
        return amountDiscountPerUnitGross;
    }

    /**
     * Discount (incl. VAT) granted on the basketItems.amountGross.
     * Positive amount is expected.
     */
    public BasketItem setAmountDiscountPerUnitGross(BigDecimal amountDiscountPerUnitGross) {
        this.amountDiscountPerUnitGross = amountDiscountPerUnitGross;
        return this;
    }

    /**
     * @deprecated Use getAmountDiscountPerUnitGross instead
     */
    @Deprecated
    public BigDecimal getAmountDiscount() {
        return amountDiscount;
    }

    /**
     * @deprecated Use setAmountDiscountPerUnitGross instead
     */
    @Deprecated
    public BasketItem setAmountDiscount(BigDecimal amountDiscount) {
        this.amountDiscount = amountDiscount;
        return this;
    }

    @Deprecated
    public BigDecimal getAmountGross() {
        return amountGross;
    }

    @Deprecated
    public BasketItem setAmountGross(BigDecimal amountGross) {
        this.amountGross = amountGross;
        return this;
    }

    @Deprecated
    public BigDecimal getAmountVat() {
        return amountVat;
    }

    @Deprecated
    public BasketItem setAmountVat(BigDecimal amountVat) {
        this.amountVat = amountVat;
        return this;
    }

    @Deprecated
    public BigDecimal getAmountPerUnit() {
        return amountPerUnit;
    }

    @Deprecated
    public BasketItem setAmountPerUnit(BigDecimal amountPerUnit) {
        this.amountPerUnit = amountPerUnit;
        return this;
    }

    @Deprecated
    public BigDecimal getAmountNet() {
        return amountNet;
    }

    @Deprecated
    public BasketItem setAmountNet(BigDecimal amountNet) {
        this.amountNet = amountNet;
        return this;
    }

    public String getParticipantId() {
        return participantId;
    }

    public BasketItem setParticipantId(String participantId) {
        this.participantId = participantId;
        return this;
    }

    public enum Type {
        @SerializedName("")
        EMPTY,

        @SerializedName("goods")
        GOODS,

        @SerializedName("voucher")
        VOUCHER,

        @SerializedName("shipment")
        SHIPMENT,

        @SerializedName("digital")
        DIGITAL
    }
}

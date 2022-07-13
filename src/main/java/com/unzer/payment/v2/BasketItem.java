package com.unzer.payment.v2;

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

    /**
     * Used only for serialization/deserialization. To create entity with values use Builder.
     */
    public BasketItem() {

    }

    /**
     * The title of the basket item
     */
    public String getTitle() {
        return title;
    }

    /**
     * The subtitle for the basket item
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * URL to the basket item image, only used for the payment page.
     */
    public URL getImageUrl() {
        return imageUrl;
    }

    /**
     * Unique basket item reference ID (within the basket).
     * If not provided, an ID is automatically generated by Unzer.
     */
    public String getBasketItemReferenceId() {
        return basketItemReferenceId;
    }

    /**
     * Quantity of the basket item
     */
    public Integer getQuantity() {
        return quantity;
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
     * The VAT value for the basket item in percentage
     */
    public BigDecimal getVat() {
        return vat;
    }

    /**
     * The type of item in the basket. The valid values are goods, voucher, and shipment.
     */
    public Type getType() {
        return type;
    }

    /**
     * The measuring unit for the basket item, e.g. pieces/Pc., kilogram/kg
     */
    public String getUnit() {
        return unit;
    }


    /**
     * Discount (incl. VAT) granted on the basketItems.amountGross.
     * Positive amount is expected.
     */
    public BigDecimal getAmountDiscountPerUnitGross() {
        return amountDiscountPerUnitGross;
    }

    public enum Type {
        @SerializedName("")
        EMPTY,

        @SerializedName("goods")
        GOODS,

        @SerializedName("voucher")
        VOUCHER,

        @SerializedName("shipment")
        SHIPMENT
    }

    /**
     * Builder is used for building BasketItem objects
     */
    public static class Builder {
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

        /**
         * Constructs Builder object. All parameters are mandatory.
         *
         * @param title              The title of the basket item
         * @param quantity           Quantity of the basket item
         * @param amountPerUnitGross Amount per unit including VAT. Note: Discount is not included. Don’t include discounts here!
         * @param vat                The VAT value for the basket item in percentage
         */
        public Builder(String title, Integer quantity, BigDecimal amountPerUnitGross, BigDecimal vat) {
            this.title = title;
            this.quantity = quantity;
            this.amountPerUnitGross = amountPerUnitGross;
            this.vat = vat;
            this.type = Type.EMPTY;
        }

        /**
         * The subtitle for the basket item
         */
        public Builder subTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        /**
         * URL to the basket item image, only used for the payment page.
         */
        public Builder imageUrl(URL imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        /**
         * Unique basket item reference ID (within the basket).
         * If not provided, an ID is automatically generated by Unzer.
         */
        public Builder basketItemReferenceId(String basketItemReferenceId) {
            this.basketItemReferenceId = basketItemReferenceId;
            return this;
        }

        /**
         * The type of item in the basket. The valid values are goods, voucher, and shipment.
         */
        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        /**
         * The measuring unit for the basket item, e.g. pieces/Pc., kilogram/kg
         */
        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        /**
         * Discount (incl. VAT) granted on the basketItems.amountGross.
         * Positive amount is expected.
         */
        public Builder amountDiscountPerUnitGross(BigDecimal amountDiscountPerUnitGross) {
            this.amountDiscountPerUnitGross = amountDiscountPerUnitGross;
            return this;
        }

        public BasketItem build() {
            BasketItem basketItem = new BasketItem();
            basketItem.title = this.title;
            basketItem.subTitle = this.subTitle;
            basketItem.imageUrl = this.imageUrl;
            basketItem.basketItemReferenceId = this.basketItemReferenceId;
            basketItem.quantity = this.quantity;
            basketItem.amountPerUnitGross = this.amountPerUnitGross;
            basketItem.vat = this.vat;
            basketItem.type = this.type;
            basketItem.unit = this.unit;
            basketItem.amountDiscountPerUnitGross = this.amountDiscountPerUnitGross;
            return basketItem;
        }
    }
}

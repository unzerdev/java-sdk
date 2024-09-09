package com.unzer.payment.resources;

import com.google.gson.annotations.SerializedName;
import com.unzer.payment.BaseResource;
import com.unzer.payment.models.paypage.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;

@Data
@NoArgsConstructor
public class PaypageV2 extends BaseResource {

    private static String URI = "/v2/merchant/paypage";

    @SerializedName("paypageId")
    private String id;
    private String checkoutType;
    private String invoiceId;
    private String logoImage;
    private String orderId;
    private String paymentReference;
    private String recurrenceType;
    private String shopName;
    private String type;
    private BigDecimal amount;
    private String currency;

    /**
     * The preferred transaction mode of the paypage. "charge" or "authorize".
     */
    private String mode;

    private Urls urls;
    private Style style;
    private Resources resources;
    private HashMap<String, PaymentMethodConfig> paymentMethodsConfigs;
    private Risk risk;

    // Response Parameter
    @Setter(AccessLevel.PRIVATE)
    private String redirectUrl;
    @Setter(AccessLevel.PRIVATE)
    private PaypagePayment[] payments;
    @Setter(AccessLevel.PRIVATE)
    private Integer total;

    public PaypageV2(BigDecimal amount, String currency, String mode) {
        this.amount = amount;
        this.currency = currency;
        this.mode = mode;
    }

    public PaypageV2(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
        this.mode = "charge";
    }

    @Override
    protected String getResourceUrl() {
        return URI;
    }

    @Override
    public String getId() {
        return id;
    }

    public enum MethodName {
        DEFAULT("default"),
        CARDS("cards"),
        PAYPAL("paypal"),
        PAYLATER_INSTALLMENT("paylaterInstallment"),
        GOOGLEPAY("googlepay"),
        APPLEPAY("applepay"),
        KLARNA("klarna"),
        SEPA_DIRECT_DEBIT("sepaDirectDebit"),
        EPS("eps"),
        PAYLATER_INVOICE("paylaterInvoice"),
        PAYLATER_DIRECT_DEBIT("paylaterDirectDebit"),
        PREPAYMENT("prepayment"),
        PAYU("payu"),
        IDEAL("ideal"),
        PRZELEWY24("przelewy24"),
        ALIPAY("alipay"),
        WECHATPAY("wechatpay"),
        BANCONTACT("bancontact"),
        PFCARD("pfcard"),
        PFEFINANCE("pfefinance"),
        TWINT("twint");

        private final String name;

        MethodName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}

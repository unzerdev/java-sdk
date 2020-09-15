package com.heidelpay.payment.paymenttypes;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2020 Heidelpay GmbH
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

public enum PaymentTypeEnum {
    CARD("crd"),
	EPS("eps"),
	GIROPAY("gro"),
	IDEAL("idl"),
	INVOICE("ivc"),
    INVOICE_GUARANTEED("ivg"),
	INVOICE_FACTORING("ivf"),
	INVOICE_SECURED("ivs"),
	PAYPAL("ppl"),
	PREPAYMENT("ppy"),
	PRZELEWY24("p24"),
    SEPA_DIRECT_DEBIT("sdd"),
	SEPA_DIRECT_DEBIT_GUARANTEED("ddg"),
	SEPA_DIRECT_DEBIT_SECURED("dds"),
	SOFORT("sft"),
	PIS("pis"),
	ALIPAY("ali"),
    WECHATPAY("wcp"),
	APPLEPAY("apl"),
	HIRE_PURCHASE_RATE_PLAN("hdd"),
	INSTALLMENT_SECURED_RATE_PLAN("ins"),
	BANCONTACT("bct"),
	PF_CARD("pfc"),
	PF_EFINANCE("pfe"),
    UNKNOWN("unknown");

    private String shortName;

    private PaymentTypeEnum(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public static PaymentTypeEnum getPaymentTypeEnumByShortName(String shortName) {
        for (PaymentTypeEnum typeEnum : PaymentTypeEnum.values()) {
            if (typeEnum.getShortName().equals(shortName)) {
                return typeEnum;
            }
        }

        return PaymentTypeEnum.UNKNOWN;
    }
}

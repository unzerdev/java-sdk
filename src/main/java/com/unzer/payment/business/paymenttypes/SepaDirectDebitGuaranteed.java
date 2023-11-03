package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.paymenttypes.PaymentType;
import com.unzer.payment.paymenttypes.SepaDirectDebit;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;

@Deprecated
public class SepaDirectDebitGuaranteed extends SepaDirectDebitSecured {

    public SepaDirectDebitGuaranteed(String iban) {
        super(iban);
    }

    @Override
    public String getResourceUrl() {
        return "/v1/types/sepa-direct-debit-guaranteed/<resourceId>";
    }

}

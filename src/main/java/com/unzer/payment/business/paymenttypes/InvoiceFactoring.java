package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.paymenttypes.InvoiceSecured;

@Deprecated
public class InvoiceFactoring extends InvoiceSecured {
    @Override
    public String getResourceUrl() {
        return "/v1/types/invoice-factoring/<resourceId>";
    }
}

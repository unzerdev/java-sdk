package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.paymenttypes.InvoiceSecured;

@Deprecated
public class InvoiceGuaranteed extends InvoiceSecured {
    @Override
    public String getResourceUrl() {
        return "/v1/types/invoice-guaranteed/<resourceId>";
    }
}

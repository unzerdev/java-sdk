/*
 * Unzer Java SDK
 *
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.unzer.payment.marketplace;

import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.util.ArrayList;
import java.util.List;

public class MarketplaceCharge extends AbstractTransaction<MarketplacePayment> {

    private static final String MARKETPLACE_AUTHORIZATION_CHARGES = "marketplace/payments/%1$s/authorize/%2$s/charges";

    private static final String MARKETPLACE_FULL_AUTHORIZATIONS_CHARGES = "marketplace/payments/%1$s/authorize/charges";

    private static final String MARKETPLACE_DIRECT_CHARGES = "marketplace/payments/<paymentId>/charges";

    private String invoiceId;

    private List<MarketplaceCancel> cancelList;

    public MarketplaceCharge() {
        super();
        setCancelList(new ArrayList<MarketplaceCancel>());
    }

    public MarketplaceCharge(Unzer unzer) {
        super(unzer);
        setCancelList(new ArrayList<MarketplaceCancel>());
    }

    @Override
    public String getTypeUrl() {
        return MARKETPLACE_DIRECT_CHARGES;
    }

    public String getFullChargeAuthorizationsUrl(String paymentId) {
        return String.format(MARKETPLACE_FULL_AUTHORIZATIONS_CHARGES, paymentId);
    }

    public String getChargeAuthorizationUrl(String paymentId, String authorizeId) {
        return String.format(MARKETPLACE_AUTHORIZATION_CHARGES, paymentId, authorizeId);
    }

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public MarketplaceCharge setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public List<MarketplaceCancel> getCancelList() {
        return cancelList;
    }

    public void setCancelList(List<MarketplaceCancel> cancelList) {
        this.cancelList = cancelList;
    }

    /**
     * Cancel for this charge.
     *
     * @param cancel refers to MarketplaceCancel.
     * @return MarketplaceCancel
     * @throws HttpCommunicationException
     */
    public MarketplaceCancel cancel(MarketplaceCancel cancel) throws HttpCommunicationException {
        return getUnzer().marketplaceChargeCancel(this.getPaymentId(), this.getId(), cancel);
    }
}

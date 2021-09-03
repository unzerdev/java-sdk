package com.unzer.payment.marketplace;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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

import com.unzer.payment.AbstractTransaction;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.math.BigDecimal;

public class MarketplaceCancel extends AbstractTransaction<MarketplacePayment> {
	
	private static final String FULL_AUTHORIZE_CANCEL_URL = "marketplace/payments/%1$s/authorize/cancels";
	private static final String FULL_CHARGES_CANCEL_URL = "marketplace/payments/%1$s/charges/cancels";
	private static final String PARTIAL_AUTHORIZE_CANCEL_URL = "marketplace/payments/%1$s/authorize/%2$s/cancels";
	private static final String PARTIAL_CHARGE_CANCEL_URL = "marketplace/payments/%1$s/charges/%2$s/cancels";
	
	private BigDecimal amountGross;
	private BigDecimal amountNet;
	private BigDecimal amountVat;
	private MarketplaceCancelBasket canceledBasket;
	
	public MarketplaceCancel() {
		super();
	}
	
	public MarketplaceCancel(Unzer unzer) {
		super(unzer);
	}
	
	public String getFullAuthorizeCancelUrl(String paymentId) {
		return String.format(FULL_AUTHORIZE_CANCEL_URL, paymentId);
	}
	
	public String getFullChargesCancelUrl(String paymentId) {
		return String.format(FULL_CHARGES_CANCEL_URL, paymentId);
	}
	
	public String getPartialAuthorizeCancelUrl(String paymentId, String authorizeId) {
		return String.format(PARTIAL_AUTHORIZE_CANCEL_URL, paymentId, authorizeId);
	}
	
	public String getPartialChargeCancelUrl(String paymentId, String chargeId) {
		return String.format(PARTIAL_CHARGE_CANCEL_URL, paymentId, chargeId);
	}

	@Override
	public String getTypeUrl() {
		throw new UnsupportedOperationException("Unsupport method. Please use other specific get URL methods.");
	}

	@Override
	public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
		return null;
	}

	public BigDecimal getAmountGross() {
		return amountGross;
	}

	public void setAmountGross(BigDecimal amountGross) {
		this.amountGross = amountGross;
	}

	public BigDecimal getAmountNet() {
		return amountNet;
	}

	public void setAmountNet(BigDecimal amountNet) {
		this.amountNet = amountNet;
	}

	public BigDecimal getAmountVat() {
		return amountVat;
	}

	public void setAmountVat(BigDecimal amountVat) {
		this.amountVat = amountVat;
	}

	public MarketplaceCancelBasket getCanceledBasket() {
		return canceledBasket;
	}

	public void setCanceledBasket(MarketplaceCancelBasket canceledBasket) {
		this.canceledBasket = canceledBasket;
	}
}

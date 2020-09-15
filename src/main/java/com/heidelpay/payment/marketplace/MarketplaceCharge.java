package com.heidelpay.payment.marketplace;

import java.util.ArrayList;
import java.util.List;

import com.heidelpay.payment.AbstractTransaction;
import com.heidelpay.payment.Heidelpay;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.communication.json.JsonObject;
import com.heidelpay.payment.paymenttypes.PaymentType;

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
	public MarketplaceCharge(Heidelpay heidelpay) {
		super(heidelpay);
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
		return getHeidelpay().marketplaceChargeCancel(this.getPaymentId(), this.getId(), cancel);
	}
}

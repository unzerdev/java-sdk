package com.unzer.payment.marketplace;

import com.unzer.payment.AbstractPayment;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.util.List;

public class MarketplacePayment extends AbstractPayment {

	private List<MarketplaceAuthorization> authorizationsList;
	private List<MarketplaceCharge> chargesList;
	private List<MarketplaceCancel> cancelList;
	
	public MarketplacePayment() {
		super();
	}

	public MarketplacePayment(Unzer unzer) {
		super(unzer);
	}

	@Override
	public String getTypeUrl() {
		return "marketplace/payments";
	}

	public List<MarketplaceCharge> getChargesList() {
		return chargesList;
	}

	public MarketplaceCharge getCharge(String chargeId) {
		if (chargesList == null)
			return null;
		for (MarketplaceCharge charge : chargesList) {
			if (chargeId.equalsIgnoreCase(charge.getId())) {
				return charge;
			}
		}
		return null;
	}

	public MarketplaceCharge getCharge(int index) {
		return getChargesList().get(index);
	}

	public void setChargesList(List<MarketplaceCharge> chargesList) {
		this.chargesList = chargesList;
	}

	public List<MarketplaceCancel> getCancelList() {
		return cancelList;
	}

	public MarketplaceCancel getCancel(String cancelId) {
		if (getCancelList() == null)
			return null;
		for (MarketplaceCancel cancel : getCancelList()) {
			if (cancelId.equalsIgnoreCase(cancel.getId())) {
				return cancel;
			}
		}
		return null;
	}

	public void setCancelList(List<MarketplaceCancel> cancelList) {
		this.cancelList = cancelList;
	}

	public List<MarketplaceAuthorization> getAuthorizationsList() {
		return authorizationsList;
	}

	public MarketplaceAuthorization getAuthorization(String authorizeId) {
		if (authorizationsList == null)
			return null;
		for (MarketplaceAuthorization charge : authorizationsList) {
			if (authorizeId.equalsIgnoreCase(charge.getId())) {
				return charge;
			}
		}
		return null;
	}

	public MarketplaceAuthorization getAuthorization(int index) {
		return getAuthorizationsList().get(index);
	}

	public void setAuthorizationsList(List<MarketplaceAuthorization> authorizationsList) {
		this.authorizationsList = authorizationsList;
	}

	@Override
	public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
		return null;
	}
	
	/**
	 * Fully cancel for Marketplace Authorization(s).
	 * <b>Note:</b>: <code>amount</code> will be ignored due to fully cancel. Only <code>paymentReference</code> is processed.
	 * 
	 * @param cancel must refer to sub type of MarketplaceCancel
	 * @return MarketplacePayment
	 * @throws HttpCommunicationException
	 */
	public MarketplacePayment marketplaceFullAuthorizeCancel(String paymentReference) throws HttpCommunicationException {
		MarketplaceCancel cancel = new MarketplaceCancel();
		cancel.setPaymentReference(paymentReference);
		return getUnzer().marketplaceFullAuthorizationsCancel(this.getId(), cancel);
	}
	
	/**
	 * Fully cancel for Marketplace Charge(s).
	 * <b>Note:</b>: <code>amount</code> will be ignored due to fully cancel. Only <code>paymentReference</code> is processed.
	 * 
	 * @param cancel must refer to sub type of MarketplaceCancel
	 * @return MarketplacePayment
	 * @throws HttpCommunicationException
	 */
	public MarketplacePayment marketplaceFullChargesCancel(String paymentReference) throws HttpCommunicationException {
		MarketplaceCancel cancel = new MarketplaceCancel();
		cancel.setPaymentReference(paymentReference);
		return getUnzer().marketplaceFullChargesCancel(this.getId(), cancel);
	}
	
	/**
	 * Fully charge for Marketplace Authorization(s).
	 * <b>Note:</b>: <code>amount</code> will be ignored due to fully charge. Only <code>paymentReference</code> is processed.
	 * 
	 * @return MarketplacePayment
	 * @throws HttpCommunicationException
	 */
	public MarketplacePayment fullChargeAuthorizations(String paymentReference) throws HttpCommunicationException {
		return getUnzer().marketplaceFullChargeAuthorizations(this.getId(), paymentReference);
	}
}
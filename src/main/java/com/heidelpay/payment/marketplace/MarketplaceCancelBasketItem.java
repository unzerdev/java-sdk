package com.heidelpay.payment.marketplace;

import java.math.BigDecimal;

/**
 * This DTO is part of marketplace cancel request, to request cancel for basket item(s) of one marketplace participant.
 * @author ngokienthuan
 *
 */
public class MarketplaceCancelBasketItem {
	private String participantId;
	private String basketItemReferenceId;
	private int quantity;
	private BigDecimal amountGross;

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public String getBasketItemReferenceId() {
		return basketItemReferenceId;
	}

	public void setBasketItemReferenceId(String basketItemReferenceId) {
		this.basketItemReferenceId = basketItemReferenceId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getAmountGross() {
		return amountGross;
	}

	public void setAmountGross(BigDecimal amountGross) {
		this.amountGross = amountGross;
	}

}

package com.heidelpay.payment.marketplace;

import java.util.List;

/**
 * This DTO is part of marketplace cancel request, to request cancel for list of basket item(s) of one marketplace participant.
 * <br>
 * The basket item(s) list can contains basket item(s) of single or multiple marketplace participant(s) to be cancelled.
 * <br>
 * <b>Note: <code>basketItemReferenceId</code> must be unique among basket items.</b>
 * @author ngokienthuan
 *
 */
public class MarketplaceCancelBasket {
	private List<MarketplaceCancelBasketItem> items;

	public List<MarketplaceCancelBasketItem> getItems() {
		return items;
	}

	public void setItems(List<MarketplaceCancelBasketItem> items) {
		this.items = items;
	}

}

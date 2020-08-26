package com.heidelpay.payment.webhook;

public enum WebhookEventEnum {
	ALL("all"), AUTHORIZE("authorize"), AUTHORIZE_SUCCEEDED("authorize.succeeded"),
	AUTHORIZE_FAILED("authorize.failed"), AUTHORIZE_PENDING("authorize.pending"),
	AUTHORIZE_EXPIRED("authorize.expired"), AUTHORIZE_CANCELED("authorize.canceled"), CHARGE("charge"),
	CHARGE_SUCCEEDED("charge.succeeded"), CHARGE_FAILED("charge.failed"), CHARGE_PENDING("charge.pending"),
	CHARGE_EXPIRED("charge.expired"), CHARGE_CANCELED("charge.canceled"), CHARGEBACK("chargeback"), PAYOUT("payout"),
	PAYOUT_SUCCEEDED("payout.succeeded"), PAYOUT_FAILED("payout.failed"), TYPES("types"), CUSTOMER("customer"),
	CUSTOMER_CREATED("customer.created"), CUSTOMER_DELETED("customer.deleted"), CUSTOMER_UPDATED("customer.updated"),
	PAYMENT("payment"), PAYMENT_PENDING("payment.pending"), PAYMENT_COMPLETED("payment.completed"),
	PAYMENT_CANCELED("payment.canceled"), PAYMENT_PARTLY("payment.partly"), PAYMENT_REVIEW("payment.payment_review"),
	PAYMENT_CHARGEBACK("payment.chargeback"), SHIPMENT("shipment"), BASKET("basket"), BASKET_CREATE("basket.created"),
	BASKET_UPDATED("basket.updated"), BASKET_USED("basket.used");

	private String eventName;

	private WebhookEventEnum(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	public static WebhookEventEnum fromEventName(String eventName) {
		for (WebhookEventEnum value : values()) {
			if (value.getEventName().equalsIgnoreCase(eventName)) {
				return value;
			}
		}
		return null;
	}
}

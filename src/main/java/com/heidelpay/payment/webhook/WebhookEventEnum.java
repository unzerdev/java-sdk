package com.heidelpay.payment.webhook;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2020 Heidelpay GmbH
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

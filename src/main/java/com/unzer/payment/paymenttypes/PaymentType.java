package com.unzer.payment.paymenttypes;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2018 Unzer GmbH
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

import com.unzer.payment.communication.json.JsonObject;

public interface PaymentType {
	public String getTypeUrl();
	public String getId();
	public PaymentType map(PaymentType paymentType, JsonObject jsonObject);
}

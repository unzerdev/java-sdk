package com.unzer.payment.webhook;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import com.unzer.payment.business.AbstractPaymentTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unzer.payment.communication.HttpCommunicationException;

public class WebhookTest extends AbstractPaymentTest {
	
	@Before
	public void clearDataBeforeTest() throws HttpCommunicationException {
		WebhookList deleteResult = getUnzer().deleteMultiWebhook();
		assertNotNull(deleteResult);
	}
	
	@After
	public void clearDataAfterTest() throws HttpCommunicationException {
		WebhookList deleteResult = getUnzer().deleteMultiWebhook();
		assertNotNull(deleteResult);
	}

	@Test
	public void testRegisterSingleWebhookWithValidEvent() throws HttpCommunicationException {
		Webhook registerRequest = new Webhook("https://domain.com", WebhookEventEnum.AUTHORIZE_CANCELED);
		Webhook registerResult = getUnzer().registerSingleWebhook(registerRequest);
		assertNotNull(registerResult);
		assertNotNull(registerResult.getId());
		assertEquals(registerRequest.getEvent(), registerResult.getEvent());
	}
	
	@Test
	public void testRegisterMultiWebhookWithValidEvent() throws HttpCommunicationException {
		List<WebhookEventEnum> listWebhookEvents = Arrays.asList(WebhookEventEnum.CHARGE_CANCELED, WebhookEventEnum.AUTHORIZE_CANCELED, WebhookEventEnum.BASKET_CREATE);
		Webhook registerRequest = new Webhook("https://domain.com", listWebhookEvents);
		WebhookList registerResult = getUnzer().registerMultiWebhooks(registerRequest);
		assertNotNull(registerResult);
		assertEquals(registerRequest.getEventList().size(), registerResult.getEvents().size());
		
		WebhookList getResult = getUnzer().getWebhooks();
		assertNotNull(getResult);
		assertEquals(registerResult.getEvents().size(), getResult.getEvents().size());
	}
	
	@Test
	public void testDeleteSingleWebhook() throws HttpCommunicationException {
		Webhook registerRequest = new Webhook("https://domain.com", WebhookEventEnum.AUTHORIZE_CANCELED);
		Webhook registerResult = getUnzer().registerSingleWebhook(registerRequest);
		assertNotNull(registerResult);
		assertNotNull(registerResult.getId());
		assertEquals(registerRequest.getEvent(), registerResult.getEvent());
		
		Webhook deleteResult = getUnzer().deleteSingleWebhook(registerResult.getId());
		assertNotNull(deleteResult);
		assertEquals(registerResult.getId(), deleteResult.getId());
	}
	
	@Test
	public void updateSingleWebhook() throws HttpCommunicationException {
		Webhook registerRequest = new Webhook("https://domain.com", WebhookEventEnum.AUTHORIZE_CANCELED);
		Webhook registerResult = getUnzer().registerSingleWebhook(registerRequest);
		assertNotNull(registerResult);
		assertNotNull(registerResult.getId());
		assertEquals(registerRequest.getEvent(), registerResult.getEvent());
		
		registerRequest.setUrl("https://new_domain.com");
		registerRequest.setEvent(null);
		
		Webhook updateResult = getUnzer().updateSingleWebhook(registerResult.getId(), registerRequest);
		assertNotNull(updateResult);
		assertEquals(registerResult.getId(), updateResult.getId());
		assertEquals(registerResult.getEvent(), registerResult.getEvent());
	}
}

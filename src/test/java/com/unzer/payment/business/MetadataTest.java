package com.unzer.payment.business;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

import com.unzer.payment.Authorization;
import com.unzer.payment.Charge;
import com.unzer.payment.Metadata;
import com.unzer.payment.Payment;
import com.unzer.payment.communication.HttpCommunicationException;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MetadataTest extends AbstractPaymentTest {

	@Test
	public void testCreateFetchMetadata() throws MalformedURLException, HttpCommunicationException {
		Metadata metadata = getUnzer().createMetadata(getTestMetadata());
		Metadata metadataFetched = getUnzer().fetchMetadata(metadata.getId());
		assertNotNull(metadataFetched);
		assertNotNull(metadataFetched.getId());
		assertMapEquals(getTestMetadata().getMetadataMap(), metadata.getMetadataMap());
	}

	@Test
	public void testSortedMetadata() throws MalformedURLException, HttpCommunicationException {
		Metadata metadataRequest = getTestMetadata(true);
		Metadata metadata = getUnzer().createMetadata(metadataRequest);
		assertEquals("delivery-date", metadata.getMetadataMap().keySet().toArray()[0]);
		
		Metadata metadataFetched = getUnzer().fetchMetadata(metadata.getId());
		assertNotNull(metadataFetched);
		assertNotNull(metadataFetched.getId());
		assertMapEquals(metadataRequest.getMetadataMap(), metadata.getMetadataMap());
	}

	@Test
	public void testAuthorizationWithMetadata() throws MalformedURLException, HttpCommunicationException {
		String metadataId = createTestMetadata().getId();
		Authorization authorize = getUnzer().authorize(getAuthorization(createPaymentTypeCard().getId(), null, metadataId));
		Payment payment = getUnzer().fetchPayment(authorize.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getAuthorization());
		assertNotNull(payment.getAuthorization().getId());
		assertEquals(metadataId, payment.getMetadataId());
		assertEquals(metadataId, payment.getAuthorization().getMetadataId());
		assertNotNull(payment.getMetadata().getMetadataMap());
	}

	@Test
	public void testChargeWithMetadata() throws MalformedURLException, HttpCommunicationException {
		String metadataId = createTestMetadata().getId();
		Charge charge = getUnzer().charge(getCharge(createPaymentTypeCard().getId(), null, null, metadataId, null, null));
		Payment payment = getUnzer().fetchPayment(charge.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getCharge(0));
		assertNotNull(payment.getCharge(0).getId());
		assertEquals(metadataId, payment.getMetadataId());
		assertEquals(metadataId, payment.getCharge(0).getMetadataId());
		assertNotNull(payment.getMetadata().getMetadataMap());
	}


}

package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
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

import java.net.MalformedURLException;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Metadata;
import com.heidelpay.payment.Payment;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class MetadataTest extends AbstractPaymentTest {

	@Test
	public void testCreateFetchMetadata() throws MalformedURLException, HttpCommunicationException {
		Metadata metadata = getHeidelpay().createMetadata(getTestMetadata());
		Metadata metadataFetched = getHeidelpay().fetchMetadata(metadata.getId());
		assertNotNull(metadataFetched);
		assertNotNull(metadataFetched.getId());
		assertMapEquals(getTestMetadata().getMetadataMap(), metadata.getMetadataMap());
	}

	@Test
	public void testSortedMetadata() throws MalformedURLException, HttpCommunicationException {
		Metadata metadataRequest = getTestMetadata(true);
		Metadata metadata = getHeidelpay().createMetadata(metadataRequest);
		assertEquals("delivery-date", metadata.getMetadataMap().keySet().toArray()[0]);
		
		Metadata metadataFetched = getHeidelpay().fetchMetadata(metadata.getId());
		assertNotNull(metadataFetched);
		assertNotNull(metadataFetched.getId());
		assertMapEquals(metadataRequest.getMetadataMap(), metadata.getMetadataMap());
	}

	@Test
	public void testAuthorizationWithMetadata() throws MalformedURLException, HttpCommunicationException {
		String metadataId = createTestMetadata().getId();
		Authorization authorize = getHeidelpay().authorize(getAuthorization(createPaymentTypeCard().getId(), null, metadataId));
		Payment payment = getHeidelpay().fetchPayment(authorize.getPayment().getId());
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
		Charge charge = getHeidelpay().charge(getCharge(createPaymentTypeCard().getId(), null, null, metadataId, null));
		Payment payment = getHeidelpay().fetchPayment(charge.getPayment().getId());
		assertNotNull(payment);
		assertNotNull(payment.getId());
		assertNotNull(payment.getCharge(0));
		assertNotNull(payment.getCharge(0).getId());
		assertEquals(metadataId, payment.getMetadataId());
		assertEquals(metadataId, payment.getCharge(0).getMetadataId());
		assertNotNull(payment.getMetadata().getMetadataMap());
	}


}

package com.unzer.payment.business;


import com.unzer.payment.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetadataTest extends AbstractPaymentTest {

    @Test
    public void testCreateFetchMetadata() {
        Metadata metadata = getUnzer().createMetadata(getTestMetadata());
        Metadata metadataFetched = getUnzer().fetchMetadata(metadata.getId());
        assertNotNull(metadataFetched);
        assertNotNull(metadataFetched.getId());
        assertMapEquals(getTestMetadata().getMetadataMap(), metadata.getMetadataMap());
    }

    @Test
    public void testSortedMetadata() {
        Metadata metadataRequest = getTestMetadata(true);
        Metadata metadata = getUnzer().createMetadata(metadataRequest);
        assertEquals("delivery-date", metadata.getMetadataMap().keySet().toArray()[0]);

        Metadata metadataFetched = getUnzer().fetchMetadata(metadata.getId());
        assertNotNull(metadataFetched);
        assertNotNull(metadataFetched.getId());
        assertMapEquals(metadataRequest.getMetadataMap(), metadata.getMetadataMap());
    }

    @Test
    public void testAuthorizationWithMetadata() {
        Unzer unzer = getUnzer();
        String metadataId = unzer.createMetadata(getTestMetadata()).getId();
        Authorization authorize = unzer.authorize(
                getAuthorization(createPaymentTypeCard(unzer, "4711100000000000").getId(), null,
                        metadataId));
        Payment payment = unzer.fetchPayment(authorize.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getAuthorization());
        assertNotNull(payment.getAuthorization().getId());
        assertEquals(metadataId, payment.getMetadataId());
        assertEquals(metadataId, payment.getAuthorization().getMetadataId());
        assertNotNull(payment.getMetadata().getMetadataMap());
    }

    @Test
    public void testChargeWithMetadata() {
        Unzer unzer = getUnzer();
        String metadataId = unzer.createMetadata(getTestMetadata()).getId();
        Charge charge = unzer.charge(
                getCharge(createPaymentTypeCard(unzer, "4711100000000000").getId(), null, null,
                        metadataId, null, null));
        Payment payment = unzer.fetchPayment(charge.getPayment().getId());
        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertNotNull(payment.getCharge(0));
        assertNotNull(payment.getCharge(0).getId());
        assertEquals(metadataId, payment.getMetadataId());
        assertEquals(metadataId, payment.getCharge(0).getMetadataId());
        assertNotNull(payment.getMetadata().getMetadataMap());
    }


}

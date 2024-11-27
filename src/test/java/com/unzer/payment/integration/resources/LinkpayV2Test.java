package com.unzer.payment.integration.resources;


import com.unzer.payment.models.paypage.AmountSettings;
import com.unzer.payment.resources.PaypageV2;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Paypage V2 Test for linkpay specific features.
 */
class LinkpayV2Test extends PaypageV2BaseTest {

    @Test
    void minimumLinkpayCreation() {
        PaypageV2 paypage = new PaypageV2("EUR", "charge");
        paypage.setType("linkpay");

        testPaypageCreation(paypage);
    }

    @Test
    void maximumLinkpayCreation() {
        PaypageV2 paypage = new PaypageV2(new BigDecimal("99.99"), "EUR", "charge");
        paypage.setType("linkpay");
        paypage.setupLinkpay(new Date(), false, UUID.randomUUID().toString());

        assertNull(paypage.getRedirectUrl());
        assertNull(paypage.getId());

        testPaypageCreation(paypage);
    }

    @Test
    void LinkpayCreationWithAmountSettings() {
        PaypageV2 paypage = new PaypageV2("EUR", "charge");
        paypage.setType("linkpay");
        paypage.setAmountSettings(new AmountSettings().setMinimum(new BigDecimal("10.00")).setMaximum(new BigDecimal("100.00")));
        paypage.setupLinkpay(new Date(), false, UUID.randomUUID().toString());

        assertNull(paypage.getRedirectUrl());
        assertNull(paypage.getId());

        testPaypageCreation(paypage);
    }

    @Test
    void LinkpayUpdate() {
        PaypageV2 request = new PaypageV2(new BigDecimal("99.99"), "EUR", "charge");
        request.setType("linkpay");

        PaypageV2 paypage = unzer.createPaypage(request);
        assertNotNull(paypage.getId());

        // When
        BigDecimal updateAmount = new BigDecimal("66.66");
        paypage.setAmount(updateAmount);
        paypage.setExpiresAt(new Date());
        PaypageV2 updateResponse = unzer.updatePaypage(paypage);

        // Then
        assertNotSame(updateResponse, paypage);
        assertNumberEquals(updateAmount, updateResponse.getAmount());
        assertEquals("EUR", updateResponse.getCurrency());
        assertEquals(paypage.getExpiresAt(), updateResponse.getExpiresAt());
    }

    @Test
    void deleteLinkpay() {
        PaypageV2 paypage = new PaypageV2("EUR", "charge");
        paypage.setType("linkpay");

        PaypageV2 createdLinkpay = testPaypageCreation(paypage);
        unzer.deletePaypage(createdLinkpay);
    }

    @Test
    void LinkpayUpdateWithAmountSettings() {
        PaypageV2 request = new PaypageV2("EUR", "charge");
        request.setType("linkpay")
                .setAmountSettings(new AmountSettings(new BigDecimal("10.00"), new BigDecimal("100.00")));

        PaypageV2 paypage = unzer.createPaypage(request);
        assertNotNull(paypage.getId());

        // When
        paypage.setAmountSettings(new AmountSettings(new BigDecimal("22.00"), new BigDecimal("222.00")));
        PaypageV2 updateResponse = unzer.updatePaypage(paypage);

        // Then
        assertNotSame(updateResponse, paypage);
        assertNumberEquals(new BigDecimal("22.00"), updateResponse.getAmountSettings().getMinimum());
        assertNumberEquals(new BigDecimal("222.00"), updateResponse.getAmountSettings().getMaximum());
        assertEquals("EUR", updateResponse.getCurrency());
    }
}

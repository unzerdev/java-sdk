package com.unzer.payment.integration.resources;


import com.unzer.payment.Unzer;
import com.unzer.payment.business.Keys;
import com.unzer.payment.models.paypage.AmountSettings;
import com.unzer.payment.resources.PaypageV2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Paypage V2 Test for linkpay specific features.
 */
public class LinkpayV2Test extends PaypageV2BaseTest {

    @BeforeAll
    public void setUpBeforeAll() {
        // Setup single unzer instance for all class tests. -> reusing jwt token stored in unzer instance.
        unzer = new Unzer(Keys.DEFAULT);
    }

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
}

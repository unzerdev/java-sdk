package com.unzer.payment.integration.resources;

import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.resources.PaypageV2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BearerAuthBaseTest extends AbstractPaymentTest {
    protected Unzer unzer;

    @BeforeAll
    public void setUpBeforeAll() {
        unzer = new Unzer(Keys.DEFAULT);
    }

    protected void assertCreatedPaypage(PaypageV2 paypage) {
        String redirectUrl = paypage.getRedirectUrl();
        String id = paypage.getId();
        assertNotNull(redirectUrl);
        assertNotNull(id);
    }

    protected PaypageV2 testPaypageCreation(PaypageV2 paypage) {
        // when
        PaypageV2 response = unzer.createPaypage(paypage);

        // then
        assertCreatedPaypage(response);

        if (paypage.getType() != null && paypage.getType().equals("linkpay")) {
            assertNotNull(response.getQrCodeSvg());
        }

        if (paypage.getAlias() != null) {
            assertTrue(response.getRedirectUrl().contains(paypage.getAlias()));
        }

        return response;
    }
}

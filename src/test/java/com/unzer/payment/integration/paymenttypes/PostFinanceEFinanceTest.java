package com.unzer.payment.integration.paymenttypes;


import com.unzer.payment.Charge;
import com.unzer.payment.Unzer;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.paymenttypes.PostFinanceEFinance;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostFinanceEFinanceTest extends AbstractPaymentTest {

    @Test
    public void testCreatePostFinanceEFinanceMandatoryType() {
        PostFinanceEFinance pfEFinance = new PostFinanceEFinance();
        pfEFinance = getUnzer().createPaymentType(pfEFinance);
        assertNotNull(pfEFinance.getId());
    }

    @Test
    public void testChargePostFinanceEFinanceType() {
        Unzer unzer = getUnzer();
        PostFinanceEFinance pfEFinance = unzer.createPaymentType(getPostFinanceEFinance());
        Charge charge = pfEFinance.charge(BigDecimal.ONE, Currency.getInstance("CHF"),
                unsafeUrl("https://www.unzer.com"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertNotNull(charge.getRedirectUrl());
    }

    private PostFinanceEFinance getPostFinanceEFinance() {
        PostFinanceEFinance pfEFinance = new PostFinanceEFinance();
        return pfEFinance;
    }

    @Test
    public void testFetchPostFinanceEFinanceType() {
        Unzer unzer = getUnzer();
        PostFinanceEFinance pfEFinance = unzer.createPaymentType(getPostFinanceEFinance());
        assertNotNull(pfEFinance.getId());
        PostFinanceEFinance fetchedPostFinanceEFinance =
                (PostFinanceEFinance) unzer.fetchPaymentType(pfEFinance.getId());
        assertNotNull(fetchedPostFinanceEFinance.getId());
    }


}

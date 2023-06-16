/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.integration.paymenttypes;

import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.PaylaterInstallment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Currency;
import java.util.List;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.*;


public class PaylaterInstallmentTest extends AbstractPaymentTest {

    @Test
    public void testRateRetrievalUrl() {
        BigDecimal amount = new BigDecimal("33.33");
        InstallmentPlansRequest request = new InstallmentPlansRequest(amount, "EUR", "DE", CustomerType.B2C);

        String uri = request.getRequestUrl();
        assertEquals("/v1/types/paylater-invoice/plans?amount=33.33&currency=EUR&country=DE&customerType=B2C", uri);
    }

    @Test
    public void testFetchInstallmentPlans() {
        BigDecimal amount = new BigDecimal("99.99");
        InstallmentPlansRequest request = new InstallmentPlansRequest(amount, "EUR", "DE", CustomerType.B2C);

        PaylaterInstallmentPlans installmentPlans = getUnzer().fetchPaylaterInstallmentPlans(request);
        assertNotEquals("", installmentPlans.getId());
        assertNumberEquals(new BigDecimal("99.99"), installmentPlans.getAmount());
        assertEquals("EUR", installmentPlans.getCurrency());
        assertTrue(installmentPlans.isSuccess());
        assertFalse(installmentPlans.isError());
        assertFalse(installmentPlans.isPending());
        assertFalse(installmentPlans.isResumed());

        List<InstallmentPlan> plans = installmentPlans.getPlans();

        assertNotNull(plans);
        assertTrue(plans.size() > 0);
        assertPlans(plans);
    }

    @Test
    public void testCreatePaylaterInstallmentTypeWithAllParameter() throws HttpCommunicationException, ParseException {
        PaylaterInstallmentPlans installmentPlans = getPaylaterInstallmentPlans();
        InstallmentPlan selectedPlan = installmentPlans.getPlans().get(0);

        // when
        PaylaterInstallment paylaterInstallment = getUnzer().createPaymentType(
                new PaylaterInstallment()
                        .setInquiryId(installmentPlans.getId())
                        .setNumberOfRates(selectedPlan.getNumberOfRates())
                        .setHolder("Max Mustermann")
                        .setIban("DE89370400440532013000")
        );

        // then
        assertNotNull(paylaterInstallment.getId());
        assertNotNull(paylaterInstallment.getGeoLocation());
        assertFalse(paylaterInstallment.getRecurring());

        // API Response doesn't contain request fields.
        assertNull(paylaterInstallment.getInquiryId());
        assertNull(paylaterInstallment.getNumberOfRates());
        assertNull(paylaterInstallment.getCountry());
        assertNull(paylaterInstallment.getHolder());
    }

    private void assertPlans(List<InstallmentPlan> plans) {
        plans.forEach((plan) -> {
            assertNotNull(plan.getTotalAmount());
            assertNotNull(plan.getNumberOfRates());
            assertNotNull(plan.getNominalInterestRate());
            assertNotNull(plan.getEffectiveInterestRate());
            assertNotNull(plan.getInstallmentRates());
            assertNotNull(plan.getSecciUrl());
            assertEquals(plan.getNumberOfRates(), plan.getInstallmentRates().size());
        });
    }

    // TODO: TEST AUTHORIZE
    // TODO: TEST CHARGE
    // TODO: TEST CANCEL

    protected Authorization getAuthorization(String typeId, String customerId, String basketId, BigDecimal effectiveInterestRate) throws MalformedURLException {
        Authorization authorization = new Authorization();
        authorization
                .setAmount(getAmount())
                .setCurrency(Currency.getInstance("EUR"))
                .setTypeId(typeId)
                .setReturnUrl(unsafeUrl("https://www.unzer.com"))
                .setCustomerId(customerId)
                .setBasketId(basketId);
        authorization.setEffectiveInterestRate(effectiveInterestRate);
        return authorization;
    }

    private BigDecimal getAmount() {
        BigDecimal amount = new BigDecimal("370.4800");
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private PaylaterInstallment createPaylaterInstallmentType(PaylaterInstallment type) throws HttpCommunicationException {
        return getUnzer().createPaymentType(type);
    }

    private PaylaterInstallmentPlans getPaylaterInstallmentPlans() throws ParseException, HttpCommunicationException {
        BigDecimal amount = new BigDecimal("99.99");
        return getPaylaterInstallmentPlans(amount);
    }

    private PaylaterInstallmentPlans getPaylaterInstallmentPlans(BigDecimal amount) throws ParseException, HttpCommunicationException {
        return getUnzer().fetchPaylaterInstallmentPlans(
                new InstallmentPlansRequest(amount, "EUR", "DE", CustomerType.B2C)
        );
    }
}

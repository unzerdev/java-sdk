package com.unzer.payment.business.paymenttypes;


import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.unzer.payment.BaseTransaction;
import com.unzer.payment.PaylaterInstallmentPlans;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpClientMock;
import com.unzer.payment.models.CustomerType;
import com.unzer.payment.models.paylater.InstallmentPlan;
import com.unzer.payment.models.paylater.InstallmentPlanRate;
import com.unzer.payment.models.paylater.InstallmentPlansRequest;
import com.unzer.payment.paymenttypes.PaylaterInstallment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.unzer.payment.util.Types.date;
import static com.unzer.payment.util.Types.unsafeUrl;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8080)
class PaylaterInstallmentTest {
    private static String getPlansresponse(String response) {
        return new Scanner(Objects.requireNonNull(
                PaylaterInstallmentTest.class.getResourceAsStream(
                        "/api-response/paylaterInstallment/" + response))).useDelimiter("\\A")
                .next();
    }

    @Test
    void test_installment_plans() {

        List<InstallmentPlan> expectedPlans = Collections.singletonList(
                new InstallmentPlan()
                        .setNumberOfRates(3)
                        .setTotalAmount(new BigDecimal("101.3100"))
                        .setNominalInterestRate(new BigDecimal("9.9500"))
                        .setEffectiveInterestRate(new BigDecimal("10.4300"))
                        .setInstallmentRates(Arrays.asList(
                                new InstallmentPlanRate(date("2023-06-20"), new BigDecimal("33.7700")),
                                new InstallmentPlanRate(date("2023-07-20"), new BigDecimal("33.7700")),
                                new InstallmentPlanRate(date("2023-08-20"), new BigDecimal("33.7700"))
                        ))
                        .setSecciUrl(unsafeUrl("https://test-payment.paylater.unzer.com/payolution-payment/infoport/creditagreementdraft/Tx-xyz?duration=3), InstallmentPlan(totalAmount=102.6000, numberOfRates=6, nominalInterestRate=9.9500, effectiveInterestRate=10.5300, installmentRates=[InstallmentPlanRate(date=Tue Jun 20 02:00:00 CEST 2023, rate=17.1000), InstallmentPlanRate(date=Thu Jul 20 02:00:00 CEST 2023, rate=17.1000), InstallmentPlanRate(date=Sun Aug 20 02:00:00 CEST 2023, rate=17.1000), InstallmentPlanRate(date=Wed Sep 20 02:00:00 CEST 2023, rate=17.1000), InstallmentPlanRate(date=Fri Oct 20 02:00:00 CEST 2023, rate=17.1000), InstallmentPlanRate(date=Mon Nov 20 01:00:00 CET 2023, rate=17.1000)], secciUrl=https://test-payment.paylater.unzer.com/payolution-payment/infoport/creditagreementdraft/Tx-xyz?duration=6), InstallmentPlan(totalAmount=103.8600, numberOfRates=9, nominalInterestRate=9.9500, effectiveInterestRate=10.4600, installmentRates=[InstallmentPlanRate(date=Tue Jun 20 02:00:00 CEST 2023, rate=11.5400), InstallmentPlanRate(date=Thu Jul 20 02:00:00 CEST 2023, rate=11.5400), InstallmentPlanRate(date=Sun Aug 20 02:00:00 CEST 2023, rate=11.5400), InstallmentPlanRate(date=Wed Sep 20 02:00:00 CEST 2023, rate=11.5400), InstallmentPlanRate(date=Fri Oct 20 02:00:00 CEST 2023, rate=11.5400), InstallmentPlanRate(date=Mon Nov 20 01:00:00 CET 2023, rate=11.5400), InstallmentPlanRate(date=Wed Dec 20 01:00:00 CET 2023, rate=11.5400), InstallmentPlanRate(date=Sat Jan 20 01:00:00 CET 2024, rate=11.5400), InstallmentPlanRate(date=Tue Feb 20 01:00:00 CET 2024, rate=11.5400)], secciUrl=https://test-payment.paylater.unzer.com/payolution-payment/infoport/creditagreementdraft/Tx-xyz?duration=9), InstallmentPlan(totalAmount=105.1200, numberOfRates=12, nominalInterestRate=9.9500, effectiveInterestRate=10.4100, installmentRates=[InstallmentPlanRate(date=Tue Jun 20 02:00:00 CEST 2023, rate=8.7600), InstallmentPlanRate(date=Thu Jul 20 02:00:00 CEST 2023, rate=8.7600), InstallmentPlanRate(date=Sun Aug 20 02:00:00 CEST 2023, rate=8.7600), InstallmentPlanRate(date=Wed Sep 20 02:00:00 CEST 2023, rate=8.7600), InstallmentPlanRate(date=Fri Oct 20 02:00:00 CEST 2023, rate=8.7600), InstallmentPlanRate(date=Mon Nov 20 01:00:00 CET 2023, rate=8.7600), InstallmentPlanRate(date=Wed Dec 20 01:00:00 CET 2023, rate=8.7600), InstallmentPlanRate(date=Sat Jan 20 01:00:00 CET 2024, rate=8.7600), InstallmentPlanRate(date=Tue Feb 20 01:00:00 CET 2024, rate=8.7600), InstallmentPlanRate(date=Wed Mar 20 01:00:00 CET 2024, rate=8.7600), InstallmentPlanRate(date=Sat Apr 20 02:00:00 CEST 2024, rate=8.7600), InstallmentPlanRate(date=Mon May 20 02:00:00 CEST 2024, rate=8.7600)], secciUrl=https://test-payment.paylater.unzer.com/payolution-payment/infoport/creditagreementdraft/Tx-xyz?duration=12), InstallmentPlan(totalAmount=110.4000, numberOfRates=24, nominalInterestRate=9.9500, effectiveInterestRate=10.4800, installmentRates=[InstallmentPlanRate(date=Tue Jun 20 02:00:00 CEST 2023, rate=4.6000), InstallmentPlanRate(date=Thu Jul 20 02:00:00 CEST 2023, rate=4.6000), InstallmentPlanRate(date=Sun Aug 20 02:00:00 CEST 2023, rate=4.6000), InstallmentPlanRate(date=Wed Sep 20 02:00:00 CEST 2023, rate=4.6000), InstallmentPlanRate(date=Fri Oct 20 02:00:00 CEST 2023, rate=4.6000), InstallmentPlanRate(date=Mon Nov 20 01:00:00 CET 2023, rate=4.6000), InstallmentPlanRate(date=Wed Dec 20 01:00:00 CET 2023, rate=4.6000), InstallmentPlanRate(date=Sat Jan 20 01:00:00 CET 2024, rate=4.6000), InstallmentPlanRate(date=Tue Feb 20 01:00:00 CET 2024, rate=4.6000), InstallmentPlanRate(date=Wed Mar 20 01:00:00 CET 2024, rate=4.6000), InstallmentPlanRate(date=Sat Apr 20 02:00:00 CEST 2024, rate=4.6000), InstallmentPlanRate(date=Mon May 20 02:00:00 CEST 2024, rate=4.6000), InstallmentPlanRate(date=Thu Jun 20 02:00:00 CEST 2024, rate=4.6000), InstallmentPlanRate(date=Sat Jul 20 02:00:00 CEST 2024, rate=4.6000), InstallmentPlanRate(date=Tue Aug 20 02:00:00 CEST 2024, rate=4.6000), InstallmentPlanRate(date=Fri Sep 20 02:00:00 CEST 2024, rate=4.6000), InstallmentPlanRate(date=Sun Oct 20 02:00:00 CEST 2024, rate=4.6000), InstallmentPlanRate(date=Wed Nov 20 01:00:00 CET 2024, rate=4.6000), InstallmentPlanRate(date=Fri Dec 20 01:00:00 CET 2024, rate=4.6000), InstallmentPlanRate(date=Mon Jan 20 01:00:00 CET 2025, rate=4.6000), InstallmentPlanRate(date=Thu Feb 20 01:00:00 CET 2025, rate=4.6000), InstallmentPlanRate(date=Thu Mar 20 01:00:00 CET 2025, rate=4.6000), InstallmentPlanRate(date=Sun Apr 20 02:00:00 CEST 2025, rate=4.6000), InstallmentPlanRate(date=Tue May 20 02:00:00 CEST 2025, rate=4.6000)], secciUrl=https://test-payment.paylater.unzer.com/payolution-payment/infoport/creditagreementdraft/Tx-xyz?duration=24"))
        );

        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        String jsonBody = getPlansresponse("fetch-installment-plans.json");
        stubFor(
                get("/v1/types/paylater-installment/plans?amount=99.99&currency=EUR&country=DE&customerType=B2C").willReturn(
                        jsonResponse(jsonBody, 200)));

        BigDecimal amount = new BigDecimal("99.99");
        InstallmentPlansRequest request = new InstallmentPlansRequest(amount, "EUR", "DE", CustomerType.B2C);

        PaylaterInstallmentPlans installmentPlans = unzer.fetchPaylaterInstallmentPlans(request);

        assertEquals("Tx-xyz", installmentPlans.getId());
        assertEquals(Currency.getInstance("EUR"), installmentPlans.getCurrency());
        assertEquals(new BigDecimal("99.99").setScale(4), installmentPlans.getAmount());
        assertEquals(BaseTransaction.Status.valueOf("SUCCESS"), installmentPlans.getStatus());

        List<InstallmentPlan> plans = installmentPlans.getPlans();
        assertEquals(5, plans.size());
        assertNotNull(plans);
        assertEquals(expectedPlans.toString(), plans.toString());
    }

    @Test
    void test_installment_type_creation() {
        Unzer unzer = new Unzer(new HttpClientMock(), "s-private-key");
        String jsonBody = getPlansresponse("create-paylater-installment-type.json");
        stubFor(
                post("/v1/types/paylater-installment/").willReturn(
                        jsonResponse(jsonBody, 200)));
        stubFor(
                get("/v1/types/paylater-installment/s-pit-vs6pjunvuick").willReturn(
                        jsonResponse(jsonBody, 200)));

        PaylaterInstallment paylaterInstallment = unzer.createPaymentType(new PaylaterInstallment()
                .setInquiryId("tx-xyz")
                .setHolder("max mustermann")
                .setIban("Iban")
                .setNumberOfRates(3));

        assertNull(paylaterInstallment.getHolder());
        assertNull(paylaterInstallment.getIban());
        assertNull(paylaterInstallment.getIban());

        assertEquals("s-pit-vs6pjunvuick", paylaterInstallment.getId());
        assertEquals(false, paylaterInstallment.getRecurring());
        assertEquals("123.123.123.123", paylaterInstallment.getGeoLocation().getClientIp());
        assertEquals("US", paylaterInstallment.getGeoLocation().getCountryIsoA2());
    }
}
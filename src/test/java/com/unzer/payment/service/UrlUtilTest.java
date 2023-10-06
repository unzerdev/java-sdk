package com.unzer.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.Keys;
import com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.InvoiceSecured;
import com.unzer.payment.paymenttypes.PaymentType;
import com.unzer.payment.paymenttypes.SepaDirectDebitSecured;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;


public class UrlUtilTest extends AbstractPaymentTest {
    private static final String sbxTypesUrl = "https://sbx-api.unzer.com/v1/types/";
    private static final String id = "random-id";


    @TestFactory
    public Collection<DynamicTest> testGetApiEndpoint() {
        class KeyTestCase {
            final String name;
            final String key;
            final String env;
            final String expectedEndpoint;

            public KeyTestCase(String name, String key, String env, String expectedEndpoint) {
                this.name = name;
                this.key = key;
                this.env = env;
                this.expectedEndpoint = expectedEndpoint;
            }
        }

        return Stream.of(
                new KeyTestCase("whenProductionKey_returnsProductionEndpoint", "p-random-key", "", "https://api.unzer.com"),
                new KeyTestCase("whenSbxKeyDevEnv_returnsDevEndpoint", "s-random-key", "dev", "https://dev-api.unzer.com"),
                new KeyTestCase("whenSbxKeyStgEnv_returnsStgEndpoint", "s-random-key", "stg", "https://stg-api.unzer.com"),
                new KeyTestCase("whenSbxKey_returnsSbxEndpoint", "s-random-key", "", "https://sbx-api.unzer.com")
        ).map(tc -> dynamicTest(tc.name, () -> {
            EnvironmentVariables environmentVariables = new EnvironmentVariables("UNZER_PAPI_ENV", tc.env);

            environmentVariables.execute(() -> {
                assertEquals(tc.env, System.getenv("UNZER_PAPI_ENV"));
                assertEquals(tc.expectedEndpoint, new UrlUtil(tc.key).getApiEndpoint());
            });
        })).collect(Collectors.toList());
    }

    @Test
    public void testUnknownPaymentType() {
        assertEquals(
                sbxTypesUrl + id,
                new UrlUtil(Keys.KEY_WITHOUT_3DS).getHttpGetUrl(id)
        );
    }

    @Test
    public void testGetUrlForPaymentTypeInvoiceSecured() {
        runGetHttpGetUrlTest(new InvoiceSecured(), "invoice-secured/");
    }

    @Test
    public void testGetUrlForPaymentTypeInstallmentSecured() {
        runGetHttpGetUrlTest(new InstallmentSecuredRatePlan(), "installment-secured/");
    }

    @Test
    public void testGetUrlForPaymentTypeSepaDirectDebitSecured() throws HttpCommunicationException {
        runGetHttpGetUrlTest(new SepaDirectDebitSecured(""), "sepa-direct-debit-secured/");
    }

    private void runGetHttpGetUrlTest(PaymentType type, String typePartUrl) {
        assertEquals(
                sbxTypesUrl + typePartUrl + id,
                new UrlUtil(Keys.KEY_WITHOUT_3DS).getHttpGetUrl(type, id)
        );
    }
}

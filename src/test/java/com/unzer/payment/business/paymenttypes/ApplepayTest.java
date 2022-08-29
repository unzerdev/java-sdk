package com.unzer.payment.business.paymenttypes;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unzer.payment.Authorization;
import com.unzer.payment.Cancel;
import com.unzer.payment.Charge;
import com.unzer.payment.PaymentException;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Applepay;
import com.unzer.payment.paymenttypes.ApplepayHeader;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ApplepayTest extends AbstractPaymentTest {

    @Test
    public void testCreateApplePayTypeFromJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String applePayData = "{\"version\":\"EC_v1\",\"data\":\"+GfdFIUZV57HNDYWpfFpGnXXXXT7F9ZVRkEwxrulUfLUzUlRpV2XnEpVe9Kf6CZMYCvCAh8vB26lB9oZU+IdppNK9iZXXXXUq0G2X+jPCRxsuNp+eVBCQCLD5YHLcc9xCi7K62Gn2NOX80Q/hXeMVNpgIBmE9f7SjX6r6Oq8mD6ak4sUABRsD3efbKr9/kdFI+n0679p3ca1deqN91r3+XeOPLeErtexoJfwFRpIo85f/FhaoX/MgIzGVQ5KI9+khiR/Bez7aT0yOjMdTIJangkjpSqB9aj5UDDVO3jDryR7+fvWs3+Y9hGG3HoAXqKl3NSZvrSn7fqqJJUpXuaOtyEi9GTvYAq/7cQ34K8XvitAMTcHB17z/vwOlaIO62m6JnigKT2hh5ngFSpqZ6XR2Xxj01EJnJDZiTtauoEPKo27e5kE1H9vaQycYaVyrYxbsecqRSAtWKtGNVdkw927io14LuVEXcoYOTOzieX769MKpsU7lOemqBO8B0eXSsChOC7s3jncwuSGI/k5FyNBrtZawnI1yy39zCF4JtmpqaYuEa43QgKH4pE=\",\"signature\":\"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\",\"header\":{\"ephemeralPublicKey\":\"MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEMtiXUxCDL9zrWc7+rscObEhZy/ED/LvrXmdL3zdQOT/Mk/vvb6OjhXYUfmPgF17zhTAaJrk8jrYDRqHuI/FYMA==\",\"publicKeyHash\":\"zqO5Y3ldWWm4NnIkfGCvJILw30rp3y46Jsf21gE8CNg=\",\"transactionId\":\"4bffc6XXXXf11d3f60fffXXX2f98cbea13b1de7d2efXXXX2fbbb98b18ca6ff20\"}}";

        Applepay applepay = mapper.readValue(applePayData, Applepay.class);
        assertNotNull(applepay);
        assertEquals("EC_v1", applepay.getVersion());
        assertEquals("4bffc6XXXXf11d3f60fffXXX2f98cbea13b1de7d2efXXXX2fbbb98b18ca6ff20", applepay.getHeader().getTransactionId());
    }

    @Test
    public void testCreateApplepayType() throws HttpCommunicationException {
        Applepay applepay = getApplePay();

        Applepay response = getUnzer().createPaymentType(applepay);
        assertNotNull(response.getId());
        assertNotNull(response.getApplicationExpirationDate());
        assertNotNull(response.getApplicationPrimaryAccountNumber());
        assertEquals("520424******6937", response.getApplicationPrimaryAccountNumber());
        assertEquals("09/2022", response.getApplicationExpirationDate());

    }

    @Test
    public void testCreateApplepayTypeAndFetch() throws HttpCommunicationException {
        Applepay applepay = getApplePay();

        applepay = getUnzer().createPaymentType(applepay);

        assertNotNull(applepay.getId());
        assertNotNull(applepay.getApplicationExpirationDate());
        assertNotNull(applepay.getApplicationPrimaryAccountNumber());
        assertEquals("520424******6937", applepay.getApplicationPrimaryAccountNumber());
        assertEquals("09/2022", applepay.getApplicationExpirationDate());

        applepay = (Applepay) getUnzer().fetchPaymentType(applepay.getId());

        assertNotNull(applepay.getId());
        assertNotNull(applepay.getApplicationExpirationDate());
        assertNotNull(applepay.getApplicationPrimaryAccountNumber());
        assertEquals("520424******6937", applepay.getApplicationPrimaryAccountNumber());
        assertEquals("09/2022", applepay.getApplicationExpirationDate());
    }

    @Test
    public void testAuthorizeApplePayType() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Authorization authorization = applepay.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
    }

    @Test
    public void testAuthorizeApplePayTypeAndCancel() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Authorization authorization = applepay.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        Cancel cancel = authorization.cancel();

        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
    }

    @Test
    public void testAuthorizeApplePayTypeWithBigAmount() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Authorization authorization = applepay.authorize(BigDecimal.valueOf(10000L), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
    }

    @Test
    public void testAuthorizeApplePayTypeWithZeroAmount() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());

        try {
            applepay.authorize(BigDecimal.ZERO, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        } catch (PaymentException paymentException) {
            assertEquals(Integer.valueOf(400), paymentException.getStatusCode());
        }
    }

    @Test
    public void testAuthorizeApplePayTypeId() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
    }

    @Test
    public void testAuthorizeApplePayTypeIdAndCancel() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Authorization authorization = getUnzer().authorize(BigDecimal.ONE, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        Cancel cancel = getUnzer().cancelAuthorization(authorization.getPaymentId());

        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
    }

    @Test
    public void testAuthorizeApplePayTypeIdWithBigAmount() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Authorization authorization = getUnzer().authorize(BigDecimal.valueOf(10000L), Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        assertNotNull(authorization);
        assertNotNull(authorization.getId());
        assertEquals(Authorization.Status.SUCCESS, authorization.getStatus());
    }

    @Test
    public void testAuthorizeApplePayTypeIdWithZeroAmount() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());

        try {
            getUnzer().authorize(BigDecimal.ZERO, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        } catch (PaymentException paymentException) {
            assertEquals(Integer.valueOf(400), paymentException.getStatusCode());
        }
    }

    @Test
    public void testChargeApplePayType() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = applepay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertEquals(Authorization.Status.SUCCESS, charge.getStatus());
    }

    @Test
    public void testChargeApplePayTypeAndFullCancel() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = applepay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        Cancel cancel = getUnzer().cancelCharge(charge.getPaymentId(), charge.getId());

        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
    }

    @Test
    public void testChargeApplePayTypeAndPartialCancel() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = applepay.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        Cancel cancel = getUnzer().cancelCharge(charge.getPaymentId(), charge.getId(), BigDecimal.ONE);

        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
    }

    @Test
    public void testChargeApplePayTypeAndPartialAndFullCancel() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = applepay.charge(BigDecimal.TEN, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        Cancel partialCancel = getUnzer().cancelCharge(charge.getPaymentId(), charge.getId(), BigDecimal.ONE);
        Cancel remainingCancel = getUnzer().cancelCharge(charge.getPaymentId(), charge.getId());

        assertNotNull(partialCancel);
        assertNotNull(remainingCancel);
        assertNotNull(partialCancel.getId());
        assertNotNull(remainingCancel.getId());
        assertEquals(Cancel.Status.SUCCESS, partialCancel.getStatus());
        assertEquals(Cancel.Status.SUCCESS, remainingCancel.getStatus());
    }

    @Test
    public void testChargeApplePayTypeWithBigAmount() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = applepay.charge(BigDecimal.valueOf(10000L), Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertEquals(Authorization.Status.SUCCESS, charge.getStatus());
    }

    @Test
    public void testChargeApplePayTypeWithZeroAmount() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        try {
            applepay.charge(BigDecimal.ZERO, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
        } catch (PaymentException paymentException) {
            assertEquals(Integer.valueOf(400), paymentException.getStatusCode());
        }
    }

    @Test
    public void testChargeApplePayTypeId() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertEquals(Authorization.Status.SUCCESS, charge.getStatus());
    }

    @Test
    public void testChargeApplePayTypeIdAndFullCancel() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        Cancel cancel = getUnzer().cancelCharge(charge.getPaymentId(), charge.getId());

        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
    }

    @Test
    public void testChargeApplePayTypeIdAndPartialCancel() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = getUnzer().charge(BigDecimal.TEN, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        Cancel cancel = getUnzer().cancelCharge(charge.getPaymentId(), charge.getId(), BigDecimal.ONE);

        assertNotNull(cancel);
        assertNotNull(cancel.getId());
        assertEquals(Cancel.Status.SUCCESS, cancel.getStatus());
    }

    @Test
    public void testChargeApplePayTypeIdAndPartialAndFullCancel() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = getUnzer().charge(BigDecimal.TEN, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        Cancel partialCancel = getUnzer().cancelCharge(charge.getPaymentId(), charge.getId(), BigDecimal.ONE);
        Cancel fullCancel = getUnzer().cancelCharge(charge.getPaymentId(), charge.getId());

        assertNotNull(partialCancel);
        assertNotNull(fullCancel);
        assertNotNull(partialCancel.getId());
        assertNotNull(fullCancel.getId());
        assertEquals(Cancel.Status.SUCCESS, partialCancel.getStatus());
        assertEquals(Cancel.Status.SUCCESS, fullCancel.getStatus());
    }

    @Test
    public void testChargeApplePayTypeIdWithBigAmount() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        Charge charge = getUnzer().charge(BigDecimal.valueOf(10000L), Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        assertNotNull(charge);
        assertNotNull(charge.getId());
        assertEquals(Authorization.Status.SUCCESS, charge.getStatus());
    }

    @Test
    public void testChargeApplePayTypeIdWithZeroAmount() throws HttpCommunicationException, MalformedURLException {
        Applepay applepay = getUnzer().createPaymentType(getApplePay());
        try {
            getUnzer().charge(BigDecimal.ZERO, Currency.getInstance("EUR"), applepay.getId(), new URL("https://www.meinShop.de"));
        } catch (PaymentException paymentException) {
            assertEquals(Integer.valueOf(400), paymentException.getStatusCode());
        }
    }

    private Applepay getApplePay() {
        return new Applepay("EC_v1", "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", getApplePayHeader());
    }

    private ApplepayHeader getApplePayHeader() {
        return new ApplepayHeader("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEvObBjuehAktBfudn9rM3uUmK4S2Nq1h3O9FemVRDvjJf8c8ClVo7dHHaBrgKiYzR0vBS0Yk8f+u62cILj5yqEA==", "zqO5Y3ldWWm4NnIkfGCvJILw30rp3y46Jsf21gE8CNg=", "f4c9127c624f57cf4882911bcdaae3c279bda34bf28d9933bd5615932146ec1e");
    }

}

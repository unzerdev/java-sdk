package com.heidelpay.payment.business.errors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import com.heidelpay.payment.Authorization;
import com.heidelpay.payment.Charge;
import com.heidelpay.payment.Customer;
import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Card;

public class ErrorTest extends AbstractPaymentTest {

	@Test
	public void testKeyMissing() throws MalformedURLException, HttpCommunicationException {
		try {
			getHeidelpay(null).authorize(getAuthorization(""));
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.000.000.001", e.getPaymentErrorList().get(0).getCode());
			assertEquals("PrivateKey/PublicKey is missing", e.getPaymentErrorList().get(0).getMerchantMessage());
		}
	}

	// The given key something is unknown or invalid.
	// The key 's-priv-123' is invalid
	@Test
	public void testInvalidKey() throws MalformedURLException, HttpCommunicationException {
		try {
			getHeidelpay("s-priv-123").authorize(getAuthorization(""));
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.320.000.002", e.getPaymentErrorList().get(0).getCode());
			assertEquals("The given key s-priv-123 is unknown or invalid.",
					e.getPaymentErrorList().get(0).getMerchantMessage());
		}
	}

	// You do not have the permission to access this resource. This might happen you
	// are using either invalid credentials
	// Card resources can only be created directly with a valid PCI certification.
	// Please contact Heidelpay to grant permission for PCI level SAQ-D or SAQ-A EP
	@Test
	public void testPCILevelSaqA() throws MalformedURLException, HttpCommunicationException {
		try {
			getHeidelpay("s-priv-2a107CYZMp3UbyVPAuqWoxQHi9nFyeiW").createPaymentType(getPaymentTypeCard());
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.710.000.005", e.getPaymentErrorList().get(0).getCode());
			assertEquals(
					"You do not have the permission to access this resource. This might happen you are using either invalid credentials",
					e.getPaymentErrorList().get(0).getMerchantMessage());
		}
	}

	// You do not have the permission to access the paymentmethod with the id
	// s-crd-jy8xfchnfte2.
	// Payment type '/types/s-crd-jbrjthrghag2' not found
	@Test
	public void testInvalidAccess() throws MalformedURLException, HttpCommunicationException {
		Card card = createPaymentTypeCard();
		try {
			getHeidelpay("s-priv-2a107CYZMp3UbyVPAuqWoxQHi9nFyeiW").fetchPaymentType(card.getId());
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.710.300.003", e.getPaymentErrorList().get(0).getCode());
			assertEquals("You do not have the permission to access the paymentmethod with the id " + card.getId() + ".",
					e.getPaymentErrorList().get(0).getMerchantMessage());
		}
	}

	// Invalid return URL
	// Return URL is mandatory
	@Test
	public void testMissingReturnUrl() throws MalformedURLException, HttpCommunicationException {
		try {
			Authorization authorization = getAuthorization(createPaymentTypeCard().getId());
			authorization.setReturnUrl(null);
			getHeidelpay().authorize(authorization);
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.320.100.203", e.getPaymentErrorList().get(0).getCode());
			assertEquals("Return URL is missing", e.getPaymentErrorList().get(0).getMerchantMessage());
		}
	}

	@Test
	public void testPaymentTypeIdMissing() throws MalformedURLException, HttpCommunicationException {
		try {
			getHeidelpay().authorize(getAuthorization(""));
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.320.200.143", e.getPaymentErrorList().get(0).getCode());
			assertEquals("Resources type id is missing", e.getPaymentErrorList().get(0).getMerchantMessage());
		}
	}

	@Test
	public void testFetchNonExistingPayment() throws MalformedURLException, HttpCommunicationException {
		try {
			getHeidelpay().fetchAuthorization("");
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.310.000.006", e.getPaymentErrorList().get(0).getCode());
			assertEquals("Http GET method is not supported", e.getPaymentErrorList().get(0).getMerchantMessage());
		}
	}

	@Test
	public void testFetchNonExistingCharge() throws MalformedURLException, HttpCommunicationException {
		try {
			Charge charge = getHeidelpay().charge(getCharge());
			getHeidelpay().fetchCharge(charge.getPaymentId(), "s-chg-200");
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.310.000.006", e.getPaymentErrorList().get(0).getCode());
			assertEquals("Http GET method is not supported", e.getPaymentErrorList().get(0).getMerchantMessage());
		}
	}

	@Test
	public void testinvalidPutCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
		try {
			Customer customer = getHeidelpay().createCustomer(getMaximumCustomer(getRandomId()));
			Customer customerUpdate = new Customer(customer.getFirstname(), customer.getLastname());
			customerUpdate.setEmail("max");
			getHeidelpay().updateCustomer(customer.getId(), customerUpdate);
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() > 0);
			assertEquals("API.410.200.013", getCode("API.410.200.013", e.getPaymentErrorList()));
			assertEquals("Email max has invalid format", getMerchantMessage("API.410.200.013", e.getPaymentErrorList()));
		}
	}


	@Test
	public void testCreateInvalidCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
		try {
			Customer customer = getMinimumCustomer();
			customer.setBirthDate(getDate("01.01.1944"));
			customer.setEmail("max");
			customer.setMobile("xxx");
			customer.setFirstname("This is a very long first name because someone put the wrong content into the field");
			customer.setLastname("This is a very long last name because someone put the wrong content into the field");
			getHeidelpay().createCustomer(customer);
		} catch (PaymentException e) {
			assertNotNull(e.getPaymentErrorList());
			assertTrue(e.getPaymentErrorList().size() == 4);
			assertEquals("API.410.200.005", getCode("API.410.200.005", e.getPaymentErrorList()));
			assertEquals("First name This is a very long first name because someone put the wrong content into the field has invalid length", getMerchantMessage("API.410.200.005", e.getPaymentErrorList()));
			assertEquals("API.410.200.002", getCode("API.410.200.002", e.getPaymentErrorList()));
			assertEquals("Last name This is a very long last name because someone put the wrong content into the field has invalid length", getMerchantMessage("API.410.200.002", e.getPaymentErrorList()));
			assertEquals("API.410.200.015", getCode("API.410.200.015", e.getPaymentErrorList()));
			assertEquals("Phone xxx has invalid format", getMerchantMessage("API.410.200.015", e.getPaymentErrorList()));
			assertEquals("API.410.200.013", getCode("API.410.200.013", e.getPaymentErrorList()));
			assertEquals("Email max has invalid format", getMerchantMessage("API.410.200.013", e.getPaymentErrorList()));
		}
	}

	private String getCode(String value, List<PaymentError> paymentErrorList) {
		for (PaymentError paymentError : paymentErrorList) {
			if (value.equals(paymentError.getCode())) return paymentError.getCode();
		}
		return null;
	}
	private String getMerchantMessage(String value, List<PaymentError> paymentErrorList) {
		for (PaymentError paymentError : paymentErrorList) {
			if (value.equals(paymentError.getCode())) return paymentError.getMerchantMessage();
		}
		return null;
	}

}

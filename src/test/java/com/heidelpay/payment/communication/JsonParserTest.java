package com.heidelpay.payment.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.heidelpay.payment.PaymentError;
import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.TestData;
import com.heidelpay.payment.communication.json.JsonCharge;
import com.heidelpay.payment.communication.json.JsonErrorObject;

public class JsonParserTest {

	@Test
	public void given_an_error_message_then_payment_exception_is_thrown() {
		try {
			JsonParser<JsonCharge> parser = new JsonParser<JsonCharge>();
			parser.fromJson(TestData.errorJson(), JsonCharge.class);

			fail("Expected PaymentException");
		} catch (PaymentException exception) {
			assertBasicErrorAttributes(exception.getId(), exception.getUrl(), exception.getTimestamp());
			assertPaymentError(exception.getPaymentErrorList().get(0));
		}
	}

	@Test
	public void given_an_error_json_then_fromJson_returnes_jsonerrorobject() {
		assertJsonError(new JsonParser<JsonErrorObject>().fromJson(TestData.errorJson(), JsonErrorObject.class));
	}

	private void assertJsonError(JsonErrorObject expectedError) {
		assertBasicErrorAttributes(expectedError.getId(), expectedError.getUrl(), expectedError.getTimestamp());
		assertEquals(1, expectedError.getErrors().size());
		assertPaymentError(expectedError.getErrors().get(0));
	}

	private void assertBasicErrorAttributes(String id, String url, String timestamp) {
		assertEquals("s-err-f2ea241e5e8e4eb3b1513fab12c", id);
		assertEquals("https://api.heidelpay.com/v1/payments/charges", url);
		assertEquals("2019-01-09 15:42:24", timestamp);

	}

	private void assertPaymentError(PaymentError error) {
		assertEquals("COR.400.100.101", error.getCode());
		assertEquals("Address untraceable", error.getMerchantMessage());
		assertEquals("The provided address is invalid. Please check your input and try agian.",
				error.getCustomerMessage());
	}
}

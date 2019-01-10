package com.heidelpay.payment.communication;

/**
 * factory Methods for creating Mocks in a convenient way.
 * @author dirk.dorsch
 *
 */
public class HttpCommunicationMockUtils {

	public static MockHeidelpayRestCommunication withFixedResponse(String response, int status) {
		
		MockHeidelpayRestCommunication rest = new MockHeidelpayRestCommunication();
		rest.responseMockStatus = status;
		rest.responseMockContent = response;
		
		return rest;
	}
	
}

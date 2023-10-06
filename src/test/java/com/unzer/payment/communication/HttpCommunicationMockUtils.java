package com.unzer.payment.communication;


/**
 * factory Methods for creating Mocks in a convenient way.
 *
 * @author Unzer E-Com GmbH
 */
public class HttpCommunicationMockUtils {

    public static MockUnzerRestCommunication withFixedResponse(String response, int status) {

        MockUnzerRestCommunication rest = new MockUnzerRestCommunication();
        rest.responseMockStatus = status;
        rest.responseMockContent = response;

        return rest;
    }

}

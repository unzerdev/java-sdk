package com.unzer.payment.communication;

/**
 * Defines the rest-communication used by the {@code PaymentService}.
 * 
 * You should not implement this interface directly, the basic communication
 * flow is implemented in the {@code AbstractUnzerRestCommunication}.
 *
 */
public interface UnzerRestCommunication {

    /**
     * Executes a GET Request to the given {@code url} authenticated with the given
     * {@code privateKey}.
     *
	 * @param url
	 *            - the url to be called
	 * @param privateKey
	 *            - the private key of the key-pair to used
     * @return - the Response as application/json, UTF-8
	 * 
	 * @throws HttpCommunicationException
	 *             - thrown for any problems occurring in http-communication
     */
    String httpGet(String url, String privateKey) throws HttpCommunicationException;

    /**
     * Executes a POST Request to the given {@code url} authenticated with the given
     * {@code privateKey}.
     *
	 * @param url
	 *            - the url to be called
	 * @param privateKey
	 *            - the private key of the key-pair to used
     * @param data       - any data object as defined in the com.unzer.payment package
     * @return - the Response as application/json, UTF-8
	 * 
	 * @throws HttpCommunicationException
	 *             - thrown for any problems occurring in http-communication
     */
    String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException;

    /**
     * Executes a PUT Request to the given {@code url} authenticated with the given
     * {@code privateKey}.
     *
	 * @param url
	 *            - the url to be called
	 * @param privateKey
	 *            - the private key of the key-pair to used
     * @param data       - any data object as defined in the com.unzer.payment package
     * @return - the Response as application/json, UTF-8
	 * 
	 * @throws HttpCommunicationException
	 *             - thrown for any problems occurring in http-communication
     */
    String httpPut(String url, String privateKey, Object data) throws HttpCommunicationException;

    /**
     * Executes a DELETE Request to the given {@code url} authenticated with the given
     * {@code privateKey}.
     *
	 * @param url
	 *            - the url to be called
	 * @param privateKey
	 *            - the private key of the key-pair to used
     * @return - the Response as application/json, UTF-8
	 * 
	 * @throws HttpCommunicationException
	 *             - thrown for any problems occurring in http-communication
     */
    String httpDelete(String url, String privateKey) throws HttpCommunicationException;
}

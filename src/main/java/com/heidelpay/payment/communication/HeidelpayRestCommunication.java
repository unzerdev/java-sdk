package com.heidelpay.payment.communication;

import com.heidelpay.payment.PaymentException;

/**
 * Defines the rest-communication used by the {@code PaymentService}.
 * 
 * You should not implement this interface directly, the basic communication
 * flow is implemented in the {@code AbstractHeidelpayRestCommunication}.
 *
 */
public interface HeidelpayRestCommunication {

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
	 *             - thrown for any problems occuring in http-communication
	 * @throws PaymentException
	 *             - business error, e.g 4xx will be translated into a
	 *             {@code PaymentException} holding the {@code PaymentError} with
	 *             the details
	 */
	String httpGet(String url, String privateKey) throws HttpCommunicationException, PaymentException;

	/**
	 * Executes a POST Request to the given {@code url} authenticated with the given
	 * {@code privateKey}.
	 * 
	 * @param url
	 *            - the url to be called
	 * @param privateKey
	 *            - the private key of the key-pair to used
	 * @param data - any data object as defined in the com.heidelpay.payment package
	 * @return - the Response as application/json, UTF-8
	 * 
	 * @throws HttpCommunicationException
	 *             - thrown for any problems occuring in http-communication
	 * @throws PaymentException
	 *             - business error, e.g 4xx will be translated into a
	 *             {@code PaymentException} holding the {@code PaymentError} with
	 *             the details
	 */
	String httpPost(String url, String privateKey, Object data) throws HttpCommunicationException, PaymentException;

	/**
	 * Executes a PUT Request to the given {@code url} authenticated with the given
	 * {@code privateKey}.
	 * 
	 * @param url
	 *            - the url to be called
	 * @param privateKey
	 *            - the private key of the key-pair to used
	 * @param data - any data object as defined in the com.heidelpay.payment package
	 * @return - the Response as application/json, UTF-8
	 * 
	 * @throws HttpCommunicationException
	 *             - thrown for any problems occuring in http-communication
	 * @throws PaymentException
	 *             - business error, e.g 4xx will be translated into a
	 *             {@code PaymentException} holding the {@code PaymentError} with
	 *             the details
	 */
	String httpPut(String url, String privateKey, Object data) throws HttpCommunicationException, PaymentException;

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
	 *             - thrown for any problems occuring in http-communication
	 * @throws PaymentException
	 *             - business error, e.g 4xx will be translated into a
	 *             {@code PaymentException} holding the {@code PaymentError} with
	 *             the details
	 */
	String httpDelete(String url, String privateKey) throws HttpCommunicationException, PaymentException;
}

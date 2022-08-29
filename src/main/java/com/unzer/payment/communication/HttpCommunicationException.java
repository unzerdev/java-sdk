/**
 * HttpCommunicationException.java
 *
 * @license Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 * @copyright Copyright (C) 2016-present Heidelberger Payment GmbH. All rights reserved.
 * @link http://dev.unzer.com/unzer-php-api/
 * @author Unzer E-Com GmbH
 */
package com.unzer.payment.communication;

/**
 * Generic exception for HttpCommunication module
 */
public class HttpCommunicationException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor create instance without message
     */
    public HttpCommunicationException() {
        super();
    }

    /**
     * Constructor create instance with message
     * @param msg refers to the declared message
     */
    public HttpCommunicationException(String msg) {
        super(msg);
    }
}

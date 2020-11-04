/**
 * HttpCommunicationException.java
 *
 * @license Use of this software requires acceptance of the Evaluation License Agreement. See LICENSE file.
 * @copyright Copyright (C) 2016-present Heidelberger Payment GmbH. All rights reserved.
 *
 * @link  http://dev.unzer.com/unzer-php-api/
 *
 * @author vukhang
 *
 */
package com.unzer.payment.communication;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2018 Unzer GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

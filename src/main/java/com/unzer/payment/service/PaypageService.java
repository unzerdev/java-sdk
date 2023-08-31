/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unzer.payment.service;

import com.unzer.payment.Paypage;
import com.unzer.payment.Unzer;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.communication.JsonParser;
import com.unzer.payment.communication.UnzerRestCommunication;
import com.unzer.payment.communication.json.ApiPaypage;
import com.unzer.payment.communication.mapper.ApiToSdkConverter;

public class PaypageService {
  private final UnzerRestCommunication restCommunication;

  private final UrlUtil urlUtil;
  private final ApiToSdkConverter jsonToObjectMapper = new ApiToSdkConverter();
  private final Unzer unzer;

  /**
   * Creates the {@code PaymentService} with the given {@code Unzer} facade,
   * bound to the given {@code UnzerRestCommunication} implementation used for
   * http-communication.
   *
   * @param unzer             - the {@code Unzer} Facade
   * @param restCommunication - the implementation of {@code UnzerRestCommunication} to be
   *                          used for network communication.
   */
  public PaypageService(Unzer unzer, UnzerRestCommunication restCommunication) {
    super();
    this.unzer = unzer;
    this.urlUtil = new UrlUtil(unzer.getPrivateKey());
    this.restCommunication = restCommunication;
  }

  public Paypage initialize(Paypage paypage) throws HttpCommunicationException {
    return initialize(paypage, urlUtil.getInitPaypageUrl(paypage));
  }

  public Paypage initialize(Paypage paypage, String url) throws HttpCommunicationException {
    String response = restCommunication.httpPost(url, unzer.getPrivateKey(),
        jsonToObjectMapper.map(paypage));
    ApiPaypage apiPaypage = new JsonParser().fromJson(response, ApiPaypage.class);
    paypage = jsonToObjectMapper.mapToBusinessObject(paypage, apiPaypage);
    return paypage;
  }

  public Paypage fetch(String paypageId) {
    String response = restCommunication.httpGet(urlUtil.getHttpGetUrl(new Paypage(), paypageId),
        unzer.getPrivateKey());
    ApiPaypage apiPaypage = new JsonParser().fromJson(response, ApiPaypage.class);
    return jsonToObjectMapper.mapToBusinessObject(new Paypage(), apiPaypage);
  }
}

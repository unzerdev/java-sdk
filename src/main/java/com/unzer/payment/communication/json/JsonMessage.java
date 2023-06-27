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

package com.unzer.payment.communication.json;

import com.unzer.payment.Message;

public class JsonMessage implements Message {
  private String code;
  private String customer;
  private String merchant;

  @Override
  public int hashCode() {
    int result = getCode() != null ? getCode().hashCode() : 0;
    result = 31 * result + (getCustomer() != null ? getCustomer().hashCode() : 0);
    result = 31 * result + (getMerchant() != null ? getMerchant().hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    JsonMessage that = (JsonMessage) o;

    if (getCode() != null ? !getCode().equals(that.getCode()) : that.getCode() != null) {
      return false;
    }
    if (getCustomer() != null ? !getCustomer().equals(that.getCustomer()) :
        that.getCustomer() != null) {
      return false;
    }
    return getMerchant() != null ? getMerchant().equals(that.getMerchant()) :
        that.getMerchant() == null;
  }

  public String getCode() {
    return code;
  }

  public JsonMessage setCode(String code) {
    this.code = code;
    return this;
  }

  public String getCustomer() {
    return customer;
  }

  public JsonMessage setCustomer(String customer) {
    this.customer = customer;
    return this;
  }

  public String getMerchant() {
    return merchant;
  }

  public JsonMessage setMerchant(String merchant) {
    this.merchant = merchant;
    return this;
  }

}

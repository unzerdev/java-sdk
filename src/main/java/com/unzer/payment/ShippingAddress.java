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

package com.unzer.payment;

import com.google.gson.annotations.SerializedName;

public class ShippingAddress extends Address {
  private Type shippingType;

  /**
   * Creates ShippingAddress object from Address object.
   *
   * @param address Base address object
   * @param shippingType optional. could be null
   * @return ShippingAddress
   */
  public static ShippingAddress of(Address address, Type shippingType) {
    return (ShippingAddress) new ShippingAddress()
        .setShippingType(shippingType)
        .setName(address.getName())
        .setCountry(address.getCountry())
        .setStreet(address.getStreet())
        .setCity(address.getCity())
        .setZip(address.getZip())
        .setState(address.getState());
  }

  public Type getShippingType() {
    return shippingType;
  }

  public ShippingAddress setShippingType(Type shippingType) {
    this.shippingType = shippingType;
    return this;
  }

  public enum Type {
    @SerializedName("equals-billing")
    EQUALS_BILLING,
    @SerializedName("different-address")
    DIFFERENT_ADDRESS,
    @SerializedName("branch-pickup")
    BRANCH_PICKUP,
    @SerializedName("post-office-pickup")
    POST_OFFICE_PICKUP,
    @SerializedName("pack-station")
    PACK_STATION
  }
}

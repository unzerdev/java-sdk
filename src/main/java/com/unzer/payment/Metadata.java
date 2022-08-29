/*
 * Copyright 2021 Unzer E-Com GmbH
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

import com.unzer.payment.communication.json.JsonObject;
import com.unzer.payment.paymenttypes.PaymentType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Metadata implements PaymentType {
    private String id;
    private Map<String, String> metadataMap;
    private Unzer unzer;

    public Metadata() {
        this(false);
    }

    public Metadata(boolean sorted) {
        super();
        if (sorted) {
            metadataMap = new TreeMap<String, String>();
        } else {
            metadataMap = new LinkedHashMap<String, String>();
        }
    }

    public Metadata addMetadata(String key, String value) {
        getMetadataMap().put(key, value);
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public PaymentType map(PaymentType paymentType, JsonObject jsonObject) {
        return null;
    }

    @Override
    public String getTypeUrl() {
        return "metadata";
    }

    public Unzer getUnzer() {
        return unzer;
    }

    public void setUnzer(Unzer unzer) {
        this.unzer = unzer;
    }

    public Map<String, String> getMetadataMap() {
        return metadataMap;
    }

    public void setMetadataMap(Map<String, String> metadataMap) {
        this.metadataMap = metadataMap;
    }
}

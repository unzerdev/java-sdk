/*
 * Unzer Java SDK
 *
 * Copyright (C) 2020 - today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.unzer.payment.paymenttypes;

public class SepaDirectDebitSecured extends SepaDirectDebit implements PaymentType {

    public SepaDirectDebitSecured(String iban) {
        super(iban);
    }

    @Override
    public String getTypeUrl() {
        return "types/sepa-direct-debit-secured";
    }

}

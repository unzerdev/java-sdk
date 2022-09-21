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
package com.unzer.payment.business;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Keys {
    private static final String PUBLIC_KEY_ARG_NAME = "publickey1";
    private static final String PRIVATEKEY_1_ARG_NAME = "privatekey1";
    private static final String PRIVATEKEY_2_ARG_NAME = "privatekey2";
    private static final String PRIVATEKEY_3_ARG_NAME = "privatekey3";
    private static final String MARKETPLACE_PRIVATE_KEY_ARG_NAME = "marketplacePrivatekey";

    private static final Map<String, String> keys = new HashMap<>();
    static {
        Arrays.asList(PUBLIC_KEY_ARG_NAME, PRIVATEKEY_1_ARG_NAME, PRIVATEKEY_2_ARG_NAME, PRIVATEKEY_3_ARG_NAME, MARKETPLACE_PRIVATE_KEY_ARG_NAME)
                .forEach(envVar -> {
                    if (System.getProperty(envVar) != null) {
                        keys.put(envVar, System.getProperty(envVar));
                    }
                });
    }

    public static String PUBLIC_KEY = keys.get(PUBLIC_KEY_ARG_NAME);
    public static String KEY_WITHOUT_3DS = keys.get(PRIVATEKEY_1_ARG_NAME);
    public static String KEY_WITH_3DS = keys.get(PRIVATEKEY_2_ARG_NAME);
    public static String PRIVATE_KEY_3 = keys.get(PRIVATEKEY_3_ARG_NAME);
    public static String MARKETPLACE_KEY = keys.get(MARKETPLACE_PRIVATE_KEY_ARG_NAME);
}

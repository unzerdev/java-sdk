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
  private static final String DEFAULT_PUBLIC_KEY_ARG_NAME = "defaultPublicKey";
  private static final String DEFAULT_PRIVATE_KEY_ARG_NAME = "defaultPrivateKey";
  private static final String NO_3DS_PRIVATE_KEY_ARG_NAME = "no3dsPrivateKey";
  private static final String LEGACY_PRIVATE_KEY_ARG_NAME = "legacyPrivateKey";
  private static final String ALT_LEGACY_PRIVATE_KEY_ARG_NAME = "altLegacyPrivateKey";

  private static final Map<String, String> keys = new HashMap<>();
  public static String PUBLIC_KEY = getKey(DEFAULT_PUBLIC_KEY_ARG_NAME);
  public static String DEFAULT = getKey(DEFAULT_PRIVATE_KEY_ARG_NAME);
  public static String KEY_WITHOUT_3DS = getKey(NO_3DS_PRIVATE_KEY_ARG_NAME);
  public static String LEGACY_PRIVATE_KEY = getKey(LEGACY_PRIVATE_KEY_ARG_NAME);
  public static String ALT_LEGACY_PRIVATE_KEY = getKey(ALT_LEGACY_PRIVATE_KEY_ARG_NAME);
  public static String MARKETPLACE_KEY = getKey(ALT_LEGACY_PRIVATE_KEY_ARG_NAME);

  private static String getKey(String argName) {
    if (keys.isEmpty()) {
      init();
    }

    return keys.get(argName);
  }

  private static void init() {
    Arrays.asList(
            DEFAULT_PUBLIC_KEY_ARG_NAME,
            DEFAULT_PRIVATE_KEY_ARG_NAME,
            NO_3DS_PRIVATE_KEY_ARG_NAME,
            LEGACY_PRIVATE_KEY_ARG_NAME,
            ALT_LEGACY_PRIVATE_KEY_ARG_NAME)
        .forEach(envVar -> {
          if (System.getProperty(envVar) != null) {
            keys.put(envVar, System.getProperty(envVar));
          }
        });
  }
}

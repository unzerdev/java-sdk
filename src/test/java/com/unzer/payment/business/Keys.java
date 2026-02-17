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
    private static final String UNZER_ONE_PRIVATE_KEY_ARG_NAME = "unzerOnePrivateKey";

    private static final Map<String, String> keys = new HashMap<>();
    public static String PUBLIC_KEY = getKey(DEFAULT_PUBLIC_KEY_ARG_NAME);
    public static String DEFAULT = getKey(DEFAULT_PRIVATE_KEY_ARG_NAME);
    public static String KEY_WITHOUT_3DS = getKey(NO_3DS_PRIVATE_KEY_ARG_NAME);
    public static String LEGACY_PRIVATE_KEY = getKey(LEGACY_PRIVATE_KEY_ARG_NAME);
    public static String ALT_LEGACY_PRIVATE_KEY = getKey(ALT_LEGACY_PRIVATE_KEY_ARG_NAME);
    public static String UNZER_ONE_PRIVATE_KEY = getKey(UNZER_ONE_PRIVATE_KEY_ARG_NAME);
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
                        ALT_LEGACY_PRIVATE_KEY_ARG_NAME,
                        UNZER_ONE_PRIVATE_KEY_ARG_NAME)
                .forEach(envVar -> {
                    if (System.getProperty(envVar) != null) {
                        keys.put(envVar, System.getProperty(envVar));
                    }
                });
    }
}

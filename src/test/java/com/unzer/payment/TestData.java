package com.unzer.payment;


public class TestData {

    public static String errorJson() {
        return "{" +
                "  \"id\" : \"s-err-f2ea241e5e8e4eb3b1513fab12c\"," +
                "  \"url\" : \"https://api.unzer.com/v1/payments/charges\"," +
                "  \"timestamp\" : \"2019-01-09 15:42:24\"," +
                "  \"errors\" : [ {\r\n" +
                "    \"code\" : \"COR.400.100.101\"," +
                "    \"merchantMessage\" : \"Address untraceable\",\r\n" +
                "    \"customerMessage\" : \"The provided address is invalid. Please check your input and try agian.\"," +
                "    \"status\" : {" +
                "      \"successful\" : false," +
                "      \"processing\" : false," +
                "      \"pending\" : false" +
                "    }" +
                "  } ]" +
                "}";
    }

}

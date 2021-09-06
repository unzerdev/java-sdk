package com.unzer.payment;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 - today Unzer E-Com GmbH
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

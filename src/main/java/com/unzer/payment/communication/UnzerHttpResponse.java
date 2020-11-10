package com.unzer.payment.communication;

/*-
 * #%L
 * Unzer Java SDK
 * %%
 * Copyright (C) 2020 Unzer E-Com GmbH
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

/**
 * Minimal represenation of a http-response.
 */
public class UnzerHttpResponse {

	private String content;
	private int code;
	
	/**
	 * Creates the {@code UnzerHttpResponse} with the given content ond http-status code.
	 * @param content - the content of the response. will be application/son, UTF-8 in any cases
	 * @param code - the http-status code
	 */
	public UnzerHttpResponse(String content, int code) {
		this.content = content;
		this.code = code;
	}
	
	/**
	 * @return the http-status code
	 */
	public int getStatusCode() {
		return code;
	}

	/**
	 * @return the content of the response. will be application/son, UTF-8 in any cases
	 */
	public String getContent() {
		return this.content;
	}
	
}

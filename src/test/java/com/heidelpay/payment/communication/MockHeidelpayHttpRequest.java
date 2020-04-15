package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class MockHeidelpayHttpRequest implements HeidelpayHttpRequest {

	Map<String, String> headerMap = new HashMap<String, String>();
	private String uri;
	private HeidelpayHttpMethod method;
	String content, contentEncoding;

	MockHeidelpayHttpRequest(String uri, HeidelpayHttpMethod method) {
		this.uri = uri;
		this.method = method;
	}
	
	@Override
	public void addHeader(String header, String value) {
		headerMap.put(header, value);
	}

	@Override
	public URI getURI() {
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setContent(String content, String encoding) {
		this.content = content;
		this.contentEncoding = encoding;
	}

	@Override
	public HeidelpayHttpMethod getMethod() {
		return method;
	}

}

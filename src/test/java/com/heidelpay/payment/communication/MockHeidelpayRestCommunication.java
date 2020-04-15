package com.heidelpay.payment.communication;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 Heidelpay GmbH
 * %%
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

import com.heidelpay.payment.communication.HeidelpayHttpRequest.HeidelpayHttpMethod;

import java.util.Locale;

public class MockHeidelpayRestCommunication extends AbstractHeidelpayRestCommunication {

	MockHeidelpayHttpRequest request;
	
	String responseMockContent;
	int responseMockStatus;
	
	HeidelpayHttpResponse loggedResponse;
	HeidelpayHttpRequest loggedRequest;
	String loggedBody;

	public MockHeidelpayRestCommunication() {
		super(null);
	}

	public MockHeidelpayRestCommunication(Locale locale) {
		super(locale);
	}

	@Override
	protected HeidelpayHttpRequest createRequest(String url, HeidelpayHttpMethod method) {
		return new MockHeidelpayHttpRequest(url, method);
	}

	@Override
	protected HeidelpayHttpResponse doExecute(HeidelpayHttpRequest request) {
		this.request = (MockHeidelpayHttpRequest) request;
		return new HeidelpayHttpResponse(responseMockContent, responseMockStatus);
	}

	@Override
	protected void logRequest(HeidelpayHttpRequest request) {
		this.loggedRequest = request;
	}

	@Override
	protected void logRequestBody(String body) {
		this.loggedBody = body;
	}

	@Override
	protected void logResponse(HeidelpayHttpResponse response) {
		this.loggedResponse = response;
	}

	
	
}

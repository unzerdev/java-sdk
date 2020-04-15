package com.heidelpay.payment.business.paymenttypes;

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

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;

import org.junit.Test;

import com.heidelpay.payment.Charge;
import com.heidelpay.payment.business.AbstractPaymentTest;
import com.heidelpay.payment.communication.HttpCommunicationException;
import com.heidelpay.payment.paymenttypes.Pis;

public class PisTest extends AbstractPaymentTest {

	@Test
	public void testCreatePis() throws HttpCommunicationException {
		Pis pis = new Pis();
		pis = getHeidelpay().createPaymentType(pis);
		assertNotNull(pis.getId());
	}

	@Test
	public void testCreatePisWithIbanBic() throws HttpCommunicationException {
		Pis pis = new Pis("DE69545100670661762678", "SPFKAT2BXXX");
		pis = getHeidelpay().createPaymentType(pis);
		assertNotNull(pis.getId());
		assertNotNull(pis.getIban());
		assertNotNull(pis.getBic());

		Pis fetchedPis = (Pis)getHeidelpay().fetchPaymentType(pis.getId());
		assertNotNull(fetchedPis.getId());
		assertNotNull(fetchedPis.getIban());
		assertNotNull(fetchedPis.getBic());
	}

	@Test
	public void testAuthorizeType() throws HttpCommunicationException, MalformedURLException {
		Pis pis = getHeidelpay().createPaymentType(new Pis());
		Charge charge = pis.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new URL("https://www.meinShop.de"));
		assertNotNull(charge);
		assertNotNull(charge.getId());
		assertNotNull(charge.getRedirectUrl());
	}

	@Test
	public void testFetchPisType() throws HttpCommunicationException {
		Pis pis = getHeidelpay().createPaymentType(new Pis());
		assertNotNull(pis.getId());
		Pis fetchedPis = (Pis)getHeidelpay().fetchPaymentType(pis.getId());
		assertNotNull(fetchedPis.getId());
	}

	@Test
	public void testFetchPisTypeWithHolderBicIban() throws HttpCommunicationException {
		Pis fetchedPis = (Pis)getHeidelpay().fetchPaymentType("s-pis-tqiu0stu41qm");
		assertNotNull(fetchedPis.getId());
		assertNotNull(fetchedPis.getIban());
		assertNotNull(fetchedPis.getBic());
		assertNotNull(fetchedPis.getHolder());
	}
}

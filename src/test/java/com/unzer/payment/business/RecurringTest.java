package com.unzer.payment.business;

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

import com.unzer.payment.Charge;
import com.unzer.payment.Customer;
import com.unzer.payment.Metadata;
import com.unzer.payment.Recurring;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.paymenttypes.Card;
import com.unzer.payment.paymenttypes.Paypal;
import com.unzer.payment.paymenttypes.SepaDirectDebit;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;

import static org.junit.Assert.*;

public class RecurringTest extends AbstractSeleniumTest {

	@Test
	@Ignore("Does not work on Bamboo")
	public void testRecurringCardWithSelenium() throws MalformedURLException, HttpCommunicationException, ParseException {
		
		String typeId = createPaymentTypeCard("4711100000000000").getId();
		Recurring recurring = getUnzer().recurring(typeId, new URL("https://www.unzer.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		
		RemoteWebDriver driver = openUrl(recurring.getRedirectUrl().toString());
		WebElement sdd = driver.findElement(By.id("code"));

		sdd.sendKeys("secret3");
		WebElement submit = driver.findElement(By.name("submit"));
		submit.click();
		Card card = (Card) getUnzer().fetchPaymentType(typeId);
		assertEquals(true, card.getRecurring());
		close();
	}
	
	@Test
	public void testRecurringCardWithoutCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
		String typeId = createPaymentTypeCard().getId();
		Recurring recurring = getUnzer().recurring(typeId, new URL("https://www.unzer.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		
		Card type = (Card) getUnzer().fetchPaymentType(typeId);
		assertEquals(false, type.getRecurring());

	}

	@Test
	public void testRecurringCardWitCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getMaximumCustomer(getRandomId()));
		String typeId = createPaymentTypeCard().getId();
		Recurring recurring = getUnzer().recurring(typeId, customer.getId(), new URL("https://www.unzer.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		assertNotNull(recurring.getRedirectUrl());

		Card type = (Card) getUnzer().fetchPaymentType(typeId);
		assertEquals(false, type.getRecurring());
	}

	@Test
	public void testRecurringCardWitCustomerWithCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getMaximumCustomer(getRandomId()));
		Recurring recurring = getUnzer().recurring(createPaymentTypeCard().getId(), customer.getCustomerId(), new URL("https://www.unzer.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		assertNotNull(recurring.getRedirectUrl());
	}

	@Test
	public void testRecurringCardWitCustomerAndMetadata() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getMaximumCustomer(getRandomId()));
		Metadata metadata = getUnzer().createMetadata(getTestMetadata());
		Recurring recurring = getUnzer().recurring(createPaymentTypeCard().getId(), customer.getId(), metadata.getId(), new URL("https://www.unzer.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		assertNotNull(recurring.getRedirectUrl());
	}
	
	@Test
	@Ignore("Does not work on Bamboo")
	public void testRecurringPaypalWithSelenium() throws MalformedURLException, HttpCommunicationException, ParseException {
		Paypal paypal = new Paypal();
		paypal = (Paypal) getUnzer().createPaymentType(paypal);
		Recurring recurring = getUnzer().recurring(paypal.getId(), new URL("https://www.unzer.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);

		Paypal type = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
		assertEquals(false, type.getRecurring());

		RemoteWebDriver driver = openUrl(recurring.getRedirectUrl().toString());
		
		WebElement email = driver.findElement(By.id("email"));
		email.clear();
		email.sendKeys("paypal-customer@unzer.de");
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("unzer");
		WebElement submit = driver.findElement(By.name("btnLogin"));
		submit.click();
		
		type = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
		assertEquals(true, type.getRecurring());
		close();
	}

	
	@Test
	public void testRecurringPaypalWithoutCustomer() throws MalformedURLException, HttpCommunicationException, ParseException {
		Paypal paypal = new Paypal();
		paypal = (Paypal) getUnzer().createPaymentType(paypal);
		Recurring recurring = getUnzer().recurring(paypal.getId(), new URL("https://www.unzer.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);

		Paypal type = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
		assertEquals(false, type.getRecurring());
	}

	@Test
	public void testRecurringPaypalWitCustomerId() throws MalformedURLException, HttpCommunicationException, ParseException {
		Customer customer = getUnzer().createCustomer(getMaximumCustomer(getRandomId()));
		Paypal paypal = new Paypal();
		paypal = (Paypal) getUnzer().createPaymentType(paypal);
		Recurring recurring = getUnzer().recurring(paypal.getId(), customer.getId(), new URL("https://www.unzer.com"));
		assertRecurring(recurring, Recurring.Status.PENDING);
		assertNotNull(recurring.getRedirectUrl());

		Paypal type = (Paypal) getUnzer().fetchPaymentType(paypal.getId());
		assertEquals(false, type.getRecurring());
	}
	
	@Test
	public void testRecurringCardDuringCharge() throws MalformedURLException, HttpCommunicationException, ParseException {
		String typeId = createPaymentTypeCard().getId();
		Card type = (Card) getUnzer().fetchPaymentType(typeId);
		assertEquals(false, type.getRecurring());

		Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), typeId, new URL("https://www.unzer.com"), false);
		assertNull(charge.getRedirectUrl());
		
		type = (Card) getUnzer().fetchPaymentType(typeId);
		assertEquals(true, type.getRecurring());
	}

	@Test
	public void testRecurringSepaDirectDebitDuringCharge() throws MalformedURLException, HttpCommunicationException, ParseException {
		SepaDirectDebit sdd = createPaymentTypeSepaDirectDebit();
		sdd = (SepaDirectDebit) getUnzer().fetchPaymentType(sdd.getId());
		assertEquals(false, sdd.getRecurring());

		Charge charge = getUnzer().charge(BigDecimal.ONE, Currency.getInstance("EUR"), sdd.getId(), new URL("https://www.unzer.com"));
		assertNull(charge.getRedirectUrl());
		
		sdd = (SepaDirectDebit) getUnzer().fetchPaymentType(sdd.getId());
		assertEquals(true, sdd.getRecurring());
	}

	
	private void assertRecurring(Recurring recurring, Recurring.Status status) {
		assertNotNull(recurring);
		assertNotNull(recurring.getProcessing());
		assertNotNull(recurring.getProcessing().getUniqueId());
		
		assertNotNull(recurring.getUnzer());
		// Bug with Paypal that no date is returned in recurring call: https://heidelpay.atlassian.net/browse/AHC-1724
//		assertNotNull(recurring.getDate());
		assertNotNull(recurring.getRedirectUrl());
		assertEquals(status, recurring.getStatus());
	}

}
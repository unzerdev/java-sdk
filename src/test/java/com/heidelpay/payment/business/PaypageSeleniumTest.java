package com.heidelpay.payment.business;

/*-
 * #%L
 * Heidelpay Java SDK
 * %%
 * Copyright (C) 2018 - 2019 Heidelpay GmbH
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.heidelpay.payment.PaymentException;
import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.communication.HttpCommunicationException;

@Ignore("Selenium Tests do not work in Bamboo. Execute them manually")
public class PaypageSeleniumTest extends AbstractSeleniumTest {

	@After
	public void tearDown() throws Exception {
		close();
	}

	@Test
	public void testSddPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-sepa-direct-debit");

		assertFalse(isAltTagPresent(driver, "logo"));
		//assertFalse(isH1TagPresent(driver, getMaximumPaypage().getDescriptionMain())); 		// DescriptionMain

		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getContactUrl()));
		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getHelpUrl()));
		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getImpressumUrl()));
		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getPrivacyPolicyUrl()));
		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getTermsAndConditionUrl()));

		sendDataByXpath(driver, "//*[contains(@name, 'iban')]", "DE89370400440532013000");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	@Ignore ("Description is not shown?")
	public void testMaximumPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMaximumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-sepa-direct-debit");
		
		assertTrue(isAltTagPresent(driver, "logo")); 				// Logo Image
		assertTrue(isStyleTagPresent(driver, "background-image"));  // Full page image
//		assertTrue(isAltTagPresent(driver, "basketImage")); 		// Basket Image Basket image is not implemented yet

//		assertTrue(isH1TagPresent(driver, getMaximumPaypage().getShopName())); 				// ShopName Bug https://heidelpay.atlassian.net/browse/AHC-1620
//		assertTrue(isH1TagPresent(driver, getMaximumPaypage().getDescriptionSmall())); 		// DescriptionSmall Bug https://heidelpay.atlassian.net/browse/AHC-1636

		assertTrue(isHrefTagPresent(driver, getMaximumPaypage().getContactUrl()));
		assertTrue(isHrefTagPresent(driver, getMaximumPaypage().getHelpUrl()));
		assertTrue(isHrefTagPresent(driver, getMaximumPaypage().getImpressumUrl()));
		assertTrue(isHrefTagPresent(driver, getMaximumPaypage().getPrivacyPolicyUrl()));
		assertTrue(isHrefTagPresent(driver, getMaximumPaypage().getTermsAndConditionUrl()));

		sendDataByName(driver, "iban", "DE89370400440532013000");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	public void testCardPaypageWithout3DS() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-card");

		sendDataFrameById(driver, "heidelpay-number-iframe", "card-number", "4444333322221111");
		sendDataFrameById(driver, "heidelpay-expiry-iframe", "card-expiry-date", "12/30");
		sendDataFrameById(driver, "heidelpay-cvc-iframe", "card-ccv", "123");

		pay(driver, "https://payment.heidelpay.com/v1/redirect/3ds/");

		close();

	}

	// Not possible to specify card3ds=true
	@Test
	public void testCardPaypageWith3DS() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-card");

		sendDataFrameById(driver, "heidelpay-number-iframe", "card-number", "4444333322221111");
		sendDataFrameById(driver, "heidelpay-expiry-iframe", "card-expiry-date", "12/30");
		sendDataFrameById(driver, "heidelpay-cvc-iframe", "card-ccv", "123");

		pay(driver, "https://payment.heidelpay.com/v1/redirect/3ds/");

		close();
	}

	@Test
	public void testSofortPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-sofort");
		pay(driver, "https://www.sofort.com/payment/start");

		close();
	}

	@Test
	public void testGiropayPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-giropay");
		pay(driver, "https://giropay.starfinanz.de/ftgbank/bankselection");

		close();
	}

	@Test
	@Ignore("Works in Debug mode but not in run mode?")
	public void testSDDGuaranteedWithCustomerReferencePaypage() throws MalformedURLException, HttpCommunicationException, PaymentException, ParseException {
		Paypage paypage = getHeidelpay().paypage(getMinimumWithReferencesPaypage("866.49"));
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-sepa-direct-debit-guaranteed");

		sendDataByXpath(driver, "//div[@id='sepa-direct-debit-guaranteed']/div/div/div/input", "DE89370400440532013000");

		pay(driver, getReturnUrl(), "Pay € 866.49");

		close();
	}

	@Test
	@Ignore ("currently B2B is confgiured")
	public void testSDDGuaranteedWithoutCustomerReferencePaypage() throws MalformedURLException, HttpCommunicationException, PaymentException, ParseException, InterruptedException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage(BigDecimal.TEN));
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-sepa-direct-debit-guaranteed");

		assertTrue(isDivTextPresent(driver, "Personal data"));
		assertTrue(isLabelPresent(driver, "Salutation"));
		assertTrue(isLabelPresent(driver, "First Name"));
		assertTrue(isLabelPresent(driver, "Last Name"));
		assertTrue(isLabelPresent(driver, "Birthday"));
		assertTrue(isLabelPresent(driver, "Street"));
		assertTrue(isLabelPresent(driver, "Postal Code"));
		assertTrue(isLabelPresent(driver, "State"));
		assertTrue(isLabelPresent(driver, "City"));
		assertTrue(isLabelPresent(driver, "Country"));

		Thread.sleep(1000);
		getWebElementByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='salutation' and @value='mr']").click();
		sendDataByXpath(driver, "//div[@id='sepa-direct-debit-guaranteed']/div/div/div/input", "DE89370400440532013000");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='firstname']", "Peter");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='lastname']", "Universum");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='birthDate']", "1974-12-24");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='street']", "Hugo-Junkers-Str. 5");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='zip']", "60386");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='state']", "DE-BO");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='zip']", "60386");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='city']", "Frankfurt am Main");
		sendDataByXpath(driver, "//div[@id='customer-sepa-direct-debit-guaranteed']//input[@name='zip']", "60386");


		pay(driver, getReturnUrl(), "Pay € 10.00");

		close();
	}

	@Test
	@Ignore("Works in Debug mode but not in run mode?")
	public void testInvoiceFactoringWithCustomerReferencePaypage() throws MalformedURLException, HttpCommunicationException, PaymentException, ParseException, InterruptedException {
		Paypage paypage = getHeidelpay().paypage(getMinimumWithReferencesPaypage("866.49"));
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-invoice-factoring");
        
		pay(driver, getReturnUrl(), "Pay € 866.49");

		close();
	}

	@Test
	public void testPaypalPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-paypal");
		pay(driver, "https://www.sandbox.paypal.com/");

		close();
	}

	@Test
	public void testPrepaymentPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-prepayment");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	@Ignore("Problem with PIS URL for Tipico")
	public void testPisPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-pis");
		pay(driver, "https://www.flexipay-direct.com/checkout");

		close();
	}

	@Test
	public void testIdealPaypage() throws MalformedURLException, HttpCommunicationException, InterruptedException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());
		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		

		choosePaymentMethod(driver, "payment-type-name-ideal");
		
		selectDropDown(driver, "ideal");

		WebElement item = driver.findElement(By.xpath("//div[contains(text(),'Test Issuer Simulation V3 - ING')]"));
		item.click();
		
		pay(driver, "https://idealtest.rabobank.nl/ideal/issuerSim.do");

		close();
	}

	@Test
	public void testEPSPaypage() throws MalformedURLException, HttpCommunicationException, InterruptedException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-eps");

		selectDropDown(driver, "eps");
        
		WebElement item = driver.findElement(By.xpath("//div[@data-value='GIBAATWGXXX']"));
		item.click();
		pay(driver, "https://login.fat.sparkasse.at/sts/oauth/authorize");

		close();
	}

}

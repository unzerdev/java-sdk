package com.heidelpay.payment.business;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.communication.HttpCommunicationException;

public class PaypageTest extends AbstractSeleniumTest {

	@Test
	public void testSddPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getMinimumPaypage());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-sepa-direct-debit");

		assertFalse(isAltTagPresent(driver, "logo"));
		assertFalse(isH1TagPresent(driver, getMaximumPaypage().getDescriptionMain())); 		// DescriptionMain

		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getContactUrl()));
		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getHelpUrl()));
		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getImpressumUrl()));
		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getPrivacyPolicyUrl()));
		assertFalse(isHrefTagPresent(driver, getMaximumPaypage().getTermsAndConditionUrl()));

		sendDataByName(driver, "iban", "DE89370400440532013000");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
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

		assertTrue(isH1TagPresent(driver, getMaximumPaypage().getDescriptionMain())); 		// DescriptionMain
		
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
	@Ignore
	public void testCardPaypageWith3DS() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
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
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
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
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-giropay");
		pay(driver, "https://giropay.starfinanz.de/ftgbank/bankselection");

		close();
	}

	@Test
	public void testSDDGuaranteedPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-sepa-direct-debit-guaranteed");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	public void testInvoiceFactoringPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-invoice-factoring");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	public void testPaypalPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-paypal");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	public void testPrepaymentPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-prepayment");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	public void testPisPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-pis");
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	public void testIdealPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-ideal");

		WebElement dropdown = driver.findElement(By.xpath("//div[contains(text(),'Test Issuer Simulation V3 - ING')]"));
		dropdown.click();
		
		pay(driver, getReturnUrl());

		close();
	}

	@Test
	@Ignore
	public void testEPSPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-eps");

		WebElement dropdown = driver.findElement(By.xpath("//div[@data-value='GIBAATWGXXX']"));
		dropdown.click();
		pay(driver, "https://giropay.starfinanz.de/ftgbank/bankselection");

		close();
	}
}

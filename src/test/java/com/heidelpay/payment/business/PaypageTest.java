package com.heidelpay.payment.business;

import static org.junit.Assert.assertNotNull;

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
	public void testEPSPaypage() throws MalformedURLException, HttpCommunicationException {
		Paypage paypage = getHeidelpay().paypage(getPaypage3DS());
		assertNotNull(paypage);
		assertNotNull(paypage.getId());
		assertNotNull(paypage.getRedirectUrl());

		RemoteWebDriver driver = openUrl(paypage.getRedirectUrl());
		choosePaymentMethod(driver, "payment-type-name-eps");

		driver.findElement(By.id("eps-element")).click();
		WebElement dropdown = driver.findElement(By.xpath("//*[contains(@id, 'heidelpay-eps-input')]"));
		dropdown.findElement(By.xpath("//option[. = 'Erste Bank und Sparkassen']")).click();

		pay(driver, "https://giropay.starfinanz.de/ftgbank/bankselection");

		close();
	}
}

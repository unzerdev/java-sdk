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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.heidelpay.payment.Linkpay;
import com.heidelpay.payment.Paypage;
import com.heidelpay.payment.communication.HttpCommunicationException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractSeleniumTest extends AbstractPaymentTest {

	public enum Browser {Chrome, Firefox}; 
	private Browser defaultBrowser = Browser.Firefox;
	private boolean close = true;

	public AbstractSeleniumTest() {
	}


	private RemoteWebDriver driver;

	protected void initializeDriver(Browser browser) {
		if(browser == Browser.Chrome) {
			System.setProperty("webdriver.chrome.driver", getChromedriverPath());
			driver = new ChromeDriver();
		} else {
			System.setProperty("webdriver.gecko.driver", getGeckodriverPath());
			driver = new FirefoxDriver();
		}
	}
	
	protected RemoteWebDriver getDriver(Browser browser) {
		initializeDriver(browser);
		return driver;
	}

	protected RemoteWebDriver getDriver() {
		if (driver == null) {
			initializeDriver(defaultBrowser);
		}
		return driver;
	}

	protected RemoteWebDriver openUrl(String url, Browser browser) {
		getDriver(browser).get(url);
		return getDriver();
	}

	protected RemoteWebDriver openUrl(String url) {
		getDriver().get(url);
		return getDriver();
	}
	
	private String getGeckodriverPath () {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return "selenium/geckodriver/geckodriver.exe";
		} else if (os.contains("mac")) {
			return "selenium/geckodriver/geckodriver";
		} else {
			return null;
		}
	}
	private String getChromedriverPath () {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return "selenium/chrome/chromedriver.exe";
		} else if (os.contains("mac")) {
			return "selenium/chrome/chromedriver";
		} else {
			return null;
		}
	}
		

	protected void pay(WebDriver driver, String expectedUrl) {
		pay(driver, expectedUrl, "Pay € 1.00");
	}
	protected void pay(WebDriver driver, String expectedUrl, String buttonText) {
		WebElement pay = driver.findElement(By.xpath("//*[contains(text(), '" + buttonText + "')]"));
		pay.click();
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.urlContains(expectedUrl));
		assertContains(expectedUrl, driver.getCurrentUrl());		
	}

	protected void sendDataFrameById(RemoteWebDriver driver, String frameName, String id, String value) {
		WebElement iFrame = driver.findElement(By.xpath("//*[contains(@id, '" + frameName + "')]"));
		TargetLocator iFrameList = driver.switchTo();
		WebDriver iFrameDriver = iFrameList.frame(iFrame);
		sendDataById(iFrameDriver, id, value);
		driver.switchTo().defaultContent();
	}
	

	protected boolean isLabelPresent(RemoteWebDriver driver, String label) {
		WebElement element = driver.findElement(By.xpath("//label[contains(text(), '" + label + "')]"));
		return element != null;
	}
	protected boolean isDivTextPresent(RemoteWebDriver driver, String text) {
		WebElement element = driver.findElement(By.xpath("//*[contains(text(), '" + text + "')]"));
		return element != null;
	}
	protected boolean isAltTagPresent(RemoteWebDriver driver, String altText) {
		try {
			return driver.findElement(By.xpath("//img[@alt='" + altText + "']")) != null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	protected boolean isStyleTagPresent(RemoteWebDriver driver, String altText) {
		try {
			return driver.findElement(By.xpath("//div[contains(@style, '" + altText + "')]")) != null;
		} catch (NoSuchElementException e) {
			System.out.println(e);
			return false;
		}
	}
	protected boolean isHrefTagPresent(RemoteWebDriver driver, URL href) {
		try {
			return driver.findElement(By.xpath("//a[@href='" + href.toString() + "']")) != null;
		} catch (NoSuchElementException e) {
			System.out.println(e);
			return false;
		}
	}

	protected boolean isH1TagPresent(RemoteWebDriver driver, String text) {
		try {
			return driver.findElement(By.xpath("//h1[contains(text(), '" + text + "')]")) != null;
		} catch (NoSuchElementException e) {
			System.out.println(e);
			return false;
		}
	}

	private void assertContains(String expectedUrl, String currentUrl) {
		assertTrue(currentUrl.contains(expectedUrl));
	}

	protected WebElement getWebElementByXpath(WebDriver driver, String xpath) {
		WebElement element = driver.findElement(By.xpath(xpath));
		return element;
	}

	protected void sendDataByXpath(WebDriver driver, String xpath, String data) {
		WebElement element = driver.findElement(By.xpath(xpath));
		element.sendKeys(data);

	}
	protected void sendDataByName(WebDriver driver, String name, String data) {
		WebElement element = driver.findElement(By.name(name));
		element.sendKeys(data);
	}

	protected void sendDataById(WebDriver driver, String id, String data) {
		WebElement element = driver.findElement(By.id(id));
		element.sendKeys(data);
	}

	protected void choosePaymentMethod(WebDriver driver, String id) {
		WebElement sdd = driver.findElement(By.id(id));
		WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(sdd));
        assertNotNull(sdd);
		sdd.click();
		
	}

	// TODO: QUESTION: Why is returnUrl mandatory. In case of embedded Paypage it may not be needed
	protected Paypage getMinimumPaypage() throws MalformedURLException {
		return getMinimumPaypage(BigDecimal.ONE);
	}
	protected Paypage getMinimumPaypage(BigDecimal amount) throws MalformedURLException {
		Paypage paypage = new Paypage();
		paypage.setAmount(amount);
		paypage.setCurrency(Currency.getInstance("EUR"));
		paypage.setReturnUrl(new URL(getReturnUrl()));
		return paypage;
	}

	protected Paypage getMinimumWithReferencesPaypage(String amount) throws MalformedURLException, HttpCommunicationException, ParseException {
		Paypage paypage = new Paypage();
		paypage.setAmount(new BigDecimal(amount));
		paypage.setCurrency(Currency.getInstance("EUR"));
		paypage.setReturnUrl(new URL(getReturnUrl()));
		paypage.setCustomerId(getHeidelpay().createCustomer(getMaximumCustomerSameAddress(getRandomId())).getId());
		paypage.setBasketId(getHeidelpay().createBasket(getMaxTestBasket()).getId());
		paypage.setMetadataId(getHeidelpay().createMetadata(getTestMetadata()).getId());
		return paypage;
	}

	protected Paypage getMaximumPaypage() throws MalformedURLException {
		Paypage paypage = new Paypage();
		String[] excludeTypes = {"paypal"};
		paypage.setExcludeTypes(excludeTypes);
		paypage.setAmount(BigDecimal.ONE);
		paypage.setCurrency(Currency.getInstance("EUR"));
		paypage.setReturnUrl(new URL(getReturnUrl()));
		paypage.setShopName("Heidelpay Demo Shop");
		paypage.setShopDescription("Heidelpay Demo Shop Description");
		paypage.setTagline("Heidelpay Tagline");
		paypage.setTermsAndConditionUrl(new URL("https://www.heidelpay.com/en/privacy-statement/"));
		paypage.setPrivacyPolicyUrl(new URL("https://www.heidelpay.com/en/privacy-statement/"));
		paypage.setCss(getCssMap());

		paypage.setLogoImage("https://www.heidelpay.com/typo3conf/ext/heidelpay_site/Resources/Public/Images/Heidelpay-Logo_mitUnterzeile-orange.svg");
		paypage.setFullPageImage("https://www.heidelpay.com/fileadmin/content/header-Imges-neu/Header_Phone_12.jpg");

		paypage.setContactUrl(new URL("mailto:rene.felder@heidelpay.com"));
		paypage.setHelpUrl(new URL("https://www.heidelpay.com/de/support/"));
		paypage.setImprintUrl(new URL("https://www.heidelpay.com/de/imprint/"));
		paypage.setPrivacyPolicyUrl(new URL("https://www.heidelpay.com/de/datenschutz/"));
		paypage.setTermsAndConditionUrl(new URL("https://www.heidelpay.com/de/datenschutz/"));

		paypage.setInvoiceId(getRandomId());
		paypage.setOrderId(getRandomId());
		return paypage;
	}

	protected Linkpay getMaximumLinkpay() throws MalformedURLException {
		Linkpay linkpay = new Linkpay();
		String[] excludeTypes = {"paypal"};
		linkpay.setExcludeTypes(excludeTypes);
		linkpay.setAmount(BigDecimal.ONE);
		linkpay.setCurrency(Currency.getInstance("EUR"));
		linkpay.setReturnUrl(new URL(getReturnUrl()));
		linkpay.setShopName("Heidelpay Demo Shop");
		linkpay.setShopDescription("Heidelpay Demo Shop Description");
		linkpay.setTagline("Heidelpay Tagline");
		linkpay.setTermsAndConditionUrl(new URL("https://www.heidelpay.com/en/privacy-statement/"));
		linkpay.setPrivacyPolicyUrl(new URL("https://www.heidelpay.com/en/privacy-statement/"));
		linkpay.setCss(getCssMap());

		linkpay.setLogoImage("https://www.heidelpay.com/typo3conf/ext/heidelpay_site/Resources/Public/Images/Heidelpay-Logo_mitUnterzeile-orange.svg");
		linkpay.setFullPageImage("https://www.heidelpay.com/fileadmin/content/header-Imges-neu/Header_Phone_12.jpg");

		linkpay.setContactUrl(new URL("mailto:rene.felder@heidelpay.com"));
		linkpay.setHelpUrl(new URL("https://www.heidelpay.com/de/support/"));
		linkpay.setImprintUrl(new URL("https://www.heidelpay.com/de/imprint/"));
		linkpay.setPrivacyPolicyUrl(new URL("https://www.heidelpay.com/de/datenschutz/"));
		linkpay.setTermsAndConditionUrl(new URL("https://www.heidelpay.com/de/datenschutz/"));

		linkpay.setInvoiceId(getRandomId());
		linkpay.setOrderId(getRandomId());
		return linkpay;
	}

	// TODO: Currently not possible as card3ds flag is missing
	protected Paypage getPaypage3DS() throws MalformedURLException {
		Paypage paypage = new Paypage();
		paypage.setAmount(BigDecimal.ONE);
		paypage.setCurrency(Currency.getInstance("EUR"));
		paypage.setReturnUrl(new URL(getReturnUrl()));
		return paypage;
	}

	protected String getReturnUrl() {
		return "https://www.google.at/";
	}
	
	
	
	protected void close() {
		if (driver != null && close) driver.quit();
	}

	protected void assertUIElement(String url, String id, String value) {
		RemoteWebDriver driver = openUrl(url);
		assertEquals(value, driver.findElementById(id).getText());
	}

	protected void assertNotExistent(RemoteWebDriver driver, By by) {
		try {
			driver.findElement(by);
			fail("Element with name '" + by + "' found");
		} catch (NoSuchElementException e) {
			; // That is what we expect
		}
	}

	protected Map<String, String> getFormParameterMap(String parReq, String termUrl, String md) {
		Map<String, String> formParameterMap = new LinkedHashMap<String, String>();
		formParameterMap.put("PaReq", parReq);
		formParameterMap.put("TermUrl", termUrl);
		formParameterMap.put("MD", md);
		return formParameterMap;
	}

	protected Map<String, String> getCssMap() {
		Map<String, String> cssMap = new HashMap<String, String>();
		cssMap.put("shopDescription", "color: blue; font-size: 30px");
		cssMap.put("tagline", "color: blue; font-size: 30px");
		cssMap.put("header", "background-color: white");
		cssMap.put("shopName", "color: blue; font-size: 30px");
		cssMap.put("contactUrl", "color: blue; font-size: 30px");
		cssMap.put("helpUrl", "color: blue; font-size: 30px");
		return cssMap;
	}
	
	protected void selectDropDown(RemoteWebDriver driver, String paymentMethod) throws InterruptedException {
		Thread.sleep(1000);
		WebElement dropdown = driver.findElement(By.xpath("//div[@class='field " + paymentMethod + " sixteen wide']//div[@class='heidelpayChoices__inner']"));
		WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOf(dropdown));
        dropdown.click();
	}

}

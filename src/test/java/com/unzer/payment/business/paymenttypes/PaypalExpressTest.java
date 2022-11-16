/*
 * Copyright 2020-today Unzer E-Com GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.AbstractTransaction.Status;
import com.unzer.payment.*;
import com.unzer.payment.business.AbstractPaymentTest;
import com.unzer.payment.business.BasketV2TestData;
import com.unzer.payment.communication.HttpCommunicationException;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.models.PaypalData;
import com.unzer.payment.paymenttypes.Paypal;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.Currency;

import static com.unzer.payment.util.Url.unsafeUrl;
import static org.junit.jupiter.api.Assertions.*;

public class PaypalExpressTest extends AbstractPaymentTest {
    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    @Test
    @Disabled
    public void testAuthorizeAndCharge() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        Paypal type = unzer.createPaymentType(new Paypal());
        Basket basket = unzer.createBasket(BasketV2TestData.getMaxTestBasketV2());

        // Create authorization
        Authorization authorization = (Authorization) new Authorization()
                .setAmount(BigDecimal.valueOf(500.5))
                .setTypeId(type.getId())
                .setBasketId(basket.getId())
                .setCurrency(Currency.getInstance("EUR"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setPrivacyPolicyUrl("https://en.wikipedia.org/wiki/Policy")
                                .setTermsAndConditionsUrl("https://en.wikipedia.org/wiki/Terms_of_service")
                                .setPaypal(new PaypalData().setCheckoutType(PaypalData.CheckoutType.EXPRESS))
                );

        Authorization createdAuthorization = unzer.authorize(authorization);
        assertEquals(Status.PENDING, createdAuthorization.getStatus());

        // Resume authorization
        performPaypalExpressUIActions(createdAuthorization.getRedirectUrl().toString());

        // PayPal-Express transaction must be in RESUMED status before update
        Authorization uiInteractedAuth = unzer.fetchAuthorization(createdAuthorization.getPaymentId());
        assertEquals(Status.RESUMED, uiInteractedAuth.getStatus());

        // Update authorization
        Authorization resumedAuthorization = unzer.updateAuthorization(createdAuthorization);
        assertEquals(Status.SUCCESS, resumedAuthorization.getStatus());

        // Charge
        Charge charge = unzer.chargeAuthorization(createdAuthorization.getPaymentId());
        assertEquals(Status.SUCCESS, charge.getStatus());
    }

    @Test
    @Disabled
    public void testCancelAuthorize() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        Paypal type = unzer.createPaymentType(new Paypal());
        Basket basket = unzer.createBasket(BasketV2TestData.getMaxTestBasketV2());

        // Create authorization
        Authorization authorization = (Authorization) new Authorization()
                .setAmount(BigDecimal.valueOf(500.5))
                .setTypeId(type.getId())
                .setBasketId(basket.getId())
                .setCurrency(Currency.getInstance("EUR"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setPrivacyPolicyUrl("https://en.wikipedia.org/wiki/Policy")
                                .setTermsAndConditionsUrl("https://en.wikipedia.org/wiki/Terms_of_service")
                                .setPaypal(new PaypalData().setCheckoutType(PaypalData.CheckoutType.EXPRESS))
                );

        Authorization createdAuthorization = unzer.authorize(authorization);
        assertEquals(Status.PENDING, createdAuthorization.getStatus());

        // Resume authorization
        performPaypalExpressUIActions(createdAuthorization.getRedirectUrl().toString());

        // PayPal-Express transaction must be in RESUMED status before update
        Authorization uiInteractedAuth = unzer.fetchAuthorization(createdAuthorization.getPaymentId());
        assertEquals(Status.RESUMED, uiInteractedAuth.getStatus());

        // Update authorization
        Authorization resumedAuthorization = unzer.updateAuthorization(createdAuthorization);
        assertEquals(Status.SUCCESS, resumedAuthorization.getStatus());

        // Cancellation is only possible when trx status is SUCCESS
        Cancel canceledAuth = unzer.cancelAuthorization(createdAuthorization.getPaymentId());
        assertEquals(Status.SUCCESS, canceledAuth.getStatus());
    }

    @Test
    @Disabled
    public void testCharge() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        Paypal type = unzer.createPaymentType(new Paypal());
        Basket basket = unzer.createBasket(BasketV2TestData.getMaxTestBasketV2());

        // Create charge
        Charge charge = (Charge) new Charge()
                .setAmount(BigDecimal.valueOf(500.5))
                .setTypeId(type.getId())
                .setBasketId(basket.getId())
                .setCurrency(Currency.getInstance("EUR"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setPrivacyPolicyUrl("https://en.wikipedia.org/wiki/Policy")
                                .setTermsAndConditionsUrl("https://en.wikipedia.org/wiki/Terms_of_service")
                                .setPaypal(new PaypalData().setCheckoutType(PaypalData.CheckoutType.EXPRESS))
                );

        Charge createdCharge = unzer.charge(charge);
        assertEquals(Status.PENDING, createdCharge.getStatus());

        // Resume charge
        performPaypalExpressUIActions(createdCharge.getRedirectUrl().toString());

        // PayPal-Express transaction must be in RESUMED status before update
        Charge uiInteractedChg = unzer.fetchCharge(createdCharge.getPaymentId(), createdCharge.getId());
        assertEquals(Status.RESUMED, uiInteractedChg.getStatus());

        // Update charge
        Charge resumedChg = unzer.updateCharge(createdCharge);
        assertEquals(Status.SUCCESS, resumedChg.getStatus());
    }

    @Test
    @Disabled
    public void testPartialCancelCharge() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        Paypal type = unzer.createPaymentType(new Paypal());
        Basket basket = unzer.createBasket(BasketV2TestData.getMaxTestBasketV2());

        // Create charge
        Charge charge = (Charge) new Charge()
                .setAmount(BigDecimal.valueOf(500.5))
                .setTypeId(type.getId())
                .setBasketId(basket.getId())
                .setCurrency(Currency.getInstance("EUR"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setPrivacyPolicyUrl("https://en.wikipedia.org/wiki/Policy")
                                .setTermsAndConditionsUrl("https://en.wikipedia.org/wiki/Terms_of_service")
                                .setPaypal(new PaypalData().setCheckoutType(PaypalData.CheckoutType.EXPRESS))
                );

        Charge createdCharge = unzer.charge(charge);
        assertEquals(Status.PENDING, createdCharge.getStatus());

        // Resume charge
        performPaypalExpressUIActions(createdCharge.getRedirectUrl().toString());

        // PayPal-Express transaction must be in RESUMED status before update
        Charge uiInteractedChg = unzer.fetchCharge(createdCharge.getPaymentId(), createdCharge.getId());
        assertEquals(Status.RESUMED, uiInteractedChg.getStatus());

        // Update charge
        Charge resumedChg = unzer.updateCharge(createdCharge);
        assertEquals(Status.SUCCESS, resumedChg.getStatus());

        // Cancel partial charge
        Cancel cancel = unzer.cancelCharge(createdCharge.getPaymentId(), BigDecimal.TEN);
        assertEquals(Status.SUCCESS, cancel.getStatus());
    }

    @Test
    public void testUpdateExistingBasketForChargePatch() throws HttpCommunicationException {
        Unzer unzer = getUnzer();

        Paypal type = unzer.createPaymentType(new Paypal());
        Basket basket = unzer.createBasket(BasketV2TestData.getMaxTestBasketV2());

        // Create charge
        Charge charge = (Charge) new Charge()
                .setAmount(BigDecimal.valueOf(380.48))
                .setTypeId(type.getId())
                .setBasketId(basket.getId())
                .setCurrency(Currency.getInstance("EUR"))
                .setReturnUrl(unsafeUrl("https://unzer.com"))
                .setAdditionalTransactionData(
                        new AdditionalTransactionData()
                                .setPrivacyPolicyUrl("https://en.wikipedia.org/wiki/Policy")
                                .setTermsAndConditionsUrl("https://en.wikipedia.org/wiki/Terms_of_service")
                                .setPaypal(new PaypalData().setCheckoutType(PaypalData.CheckoutType.EXPRESS))
                );

        // Basket is updated. Charge amount must be changed
        Charge createdCharge = unzer.charge((Charge) charge.setAmount(BigDecimal.valueOf(390.48)));
        assertEquals(Status.PENDING, createdCharge.getStatus());

        // Resume charge
        performPaypalExpressUIActions(createdCharge.getRedirectUrl().toString());

        // PayPal-Express transaction must be in RESUMED status before update
        Charge uiInteractedChg = unzer.fetchCharge(createdCharge.getPaymentId(), createdCharge.getId());
        assertEquals(Status.RESUMED, uiInteractedChg.getStatus());

        unzer.updateBasket(
                basket.setTotalValueGross(BigDecimal.valueOf(390.48)).addBasketItem(
                        new BasketItem().setBasketItemReferenceId("Shipping10")
                                .setAmountPerUnitGross(BigDecimal.TEN)
                                .setQuantity(1)
                                .setTitle("Shipping")
                                .setUnit("Pc.")
                                .setVat(BigDecimal.valueOf(19))
                                .setSubTitle("Express").setType(BasketItem.Type.SHIPMENT)
                ),
                basket.getId()
        );

        // Update charge
        Charge resumedChg = unzer.updateCharge(createdCharge);
        assertEquals(Status.SUCCESS, resumedChg.getStatus());

        // Check charge
        Charge completedChg = unzer.fetchCharge(resumedChg.getPaymentId(), resumedChg.getId());
        assertEquals(new BigDecimal("390.4800"), completedChg.getAmount());
    }

    private AbstractMap.SimpleEntry<WebDriver, WebDriverWait> driverSetup() {
        ChromeOptions chromeOptions = new ChromeOptions()
                .addArguments(
                        "--ignore-certificate-errors"
//                        "--headless" // PayPal is not loaded in headless mode
                );
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(1));

        return new AbstractMap.SimpleEntry<>(driver, wait);
    }

    // Simulates user interaction on paypal-express page.
    private void performPaypalExpressUIActions(String redirectUrl) {
        AbstractMap.SimpleEntry<WebDriver, WebDriverWait> pair = driverSetup();
        WebDriver driver = pair.getKey();
        WebDriverWait wait = pair.getValue();

        try {
            driver.get(redirectUrl);
            wait.until(ExpectedConditions.urlContains("https://www.sandbox.paypal.com/checkoutnow"));

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//div[@data-testid='basl-login-link-container']/div/button")
                    )
            ).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")))
                    .sendKeys("paypal-buyer@unzer.com");
            driver.findElement(By.id("btnNext")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")))
                    .sendKeys("unzer1234");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("btnLogin")))
                    .click();

            wait.until(ExpectedConditions.elementToBeClickable(By.id("payment-submit-btn")))
                    .click();
            wait.until(ExpectedConditions.urlContains("https://www.unzer.com"));
        } finally {
            driver.close();
        }
    }
}

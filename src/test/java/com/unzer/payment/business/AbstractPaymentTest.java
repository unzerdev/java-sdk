package com.unzer.payment.business;

import com.unzer.payment.*;
import com.unzer.payment.Customer.Salutation;
import com.unzer.payment.communication.impl.HttpClientBasedRestCommunication;
import com.unzer.payment.marketplace.MarketplaceAuthorization;
import com.unzer.payment.marketplace.MarketplaceCancelBasket;
import com.unzer.payment.marketplace.MarketplaceCancelBasketItem;
import com.unzer.payment.marketplace.MarketplaceCharge;
import com.unzer.payment.models.AdditionalTransactionData;
import com.unzer.payment.paymenttypes.Card;
import com.unzer.payment.paymenttypes.SepaDirectDebit;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.unzer.payment.util.Url.unsafeUrl;
import static com.unzer.payment.util.Uuid.generateUuid;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;


public abstract class AbstractPaymentTest {
    protected static final String PERSON_STRING = "Mr. Unzer Payment";
    protected static final String MAIL_STRING = "example@unzer.com";
    protected static final String INVALID_MAIL_STRING = "example@@@unzer.com";
    protected static final String NO_3DS_VISA_CARD_NUMBER = "4012888888881881";
    protected static final String VISA_3DS_ENABLED_CARD_NUMBER = "4711100000000000";
    protected static final String MARKETPLACE_PARTICIPANT_ID_2 = "31HA07BC814FC247577B309FF031D3F0";
    protected static final String MARKETPLACE_PARTICIPANT_ID_1 = "31HA07BC814FC247577B195E59A99FC6";


    protected static String maskString(String strText, int start, int end, char maskChar) {
        if (strText == null) return null;
        if (strText.equals("")) return "";
        if (start < 0) start = 0;
        if (end > strText.length()) end = strText.length();

        int maskLength = end - start;

        if (maskLength == 0) return strText;

        StringBuilder sbMaskString = new StringBuilder(maskLength);

        for (int i = 0; i < maskLength; i++) {
            sbMaskString.append(maskChar);
        }

        return strText.substring(0, start) + sbMaskString + strText.substring(start + maskLength);
    }

    protected String getRandomInvoiceId() {
        return generateUuid().substring(0, 5);
    }

    public Unzer getUnzer() {
        return new Unzer(Keys.DEFAULT);
    }

    public Unzer getUnzerDE() {
        return new Unzer(Keys.DEFAULT, Locale.GERMANY);
    }

    public Unzer getUnzer(String key) {
        return new Unzer(new HttpClientBasedRestCommunication(), key);
    }

    public Unzer getUnzerDE(String key) {
        return new Unzer(new HttpClientBasedRestCommunication(Locale.GERMANY), key);
    }

    protected Authorization getAuthorization(String typeId) {
        return getAuthorization(typeId, (String) null);
    }

    protected Authorization getAuthorization(String typeId, Boolean card3ds) {
        return getAuthorization(typeId, null, null, null, null, card3ds);
    }

    protected Authorization getAuthorization(String typeId, String customerId) {
        return getAuthorization(typeId, customerId, null, null, null, null);
    }

    protected Authorization getAuthorization(String typeId, String customerId, String metadataId) {
        return getAuthorization(typeId, customerId, null, metadataId, null, null);
    }

    protected Authorization getAuthorization(String typeId, String customerId, String orderId, String metadataId, String basketId) {
        return getAuthorization(typeId, customerId, orderId, metadataId, basketId, null);
    }

    protected Authorization getAuthorization(String typeId, String customerId, String orderId, String metadataId, String basketId, Boolean card3ds) {
        Authorization authorization = new Authorization();
        authorization
                .setAmount(new BigDecimal(10))
                .setCurrency(Currency.getInstance("EUR"))
                .setTypeId(typeId)
                .setReturnUrl(unsafeUrl("https://www.unzer.com"))
                .setOrderId(orderId)
                .setCustomerId(customerId)
                .setMetadataId(metadataId)
                .setBasketId(basketId)
                .setCard3ds(card3ds);
        return authorization;
    }

    protected MarketplaceAuthorization getMarketplaceAuthorization(String typeId, String customerId, String orderId, String metadataId, String basketId, Boolean card3ds) {
        MarketplaceAuthorization authorization = new MarketplaceAuthorization();
        authorization
                .setAmount(new BigDecimal(10))
                .setCurrency(Currency.getInstance("EUR"))
                .setTypeId(typeId)
                .setReturnUrl(unsafeUrl("https://www.unzer.com"))
                .setOrderId(orderId)
                .setCustomerId(customerId)
                .setMetadataId(metadataId)
                .setBasketId(basketId)
                .setCard3ds(card3ds);
        return authorization;
    }

    protected MarketplaceCharge getMarketplaceCharge(String typeId, String customerId, String orderId, String metadataId, String basketId, Boolean card3ds) {
        MarketplaceCharge authorization = new MarketplaceCharge();
        authorization
                .setAmount(new BigDecimal(10))
                .setCurrency(Currency.getInstance("EUR"))
                .setTypeId(typeId)
                .setReturnUrl(unsafeUrl("https://www.unzer.com"))
                .setOrderId(orderId)
                .setCustomerId(customerId)
                .setMetadataId(metadataId)
                .setBasketId(basketId)
                .setCard3ds(card3ds);
        return authorization;
    }

    protected Charge getCharge() {
        return getCharge(null);
    }

    protected Charge getCharge(String orderId) {
        Charge charge = getCharge(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), null, null);
        charge.setOrderId(orderId);
        return charge;
    }

    protected Charge getCharge(String orderId, Boolean card3ds, AdditionalTransactionData additionalTransactionData) {
        return getCharge(createPaymentTypeCard(getUnzer(), "4711100000000000").getId(), null, orderId, null, null, card3ds, additionalTransactionData);
    }

    protected Charge getCharge(String typeId, String customerId, String orderId, String metadataId, String basketId, AdditionalTransactionData additionalTransactionData) {
        return getCharge(typeId, customerId, orderId, metadataId, basketId, null, additionalTransactionData);
    }

    protected Charge getCharge(String typeId, String customerId, String orderId, String metadataId, String basketId, Boolean card3ds, AdditionalTransactionData additionalTransactionData) {
        Charge charge = new Charge();
        charge.setAmount(BigDecimal.ONE)
                .setCurrency(Currency.getInstance("EUR"))
                .setTypeId(typeId)
                .setReturnUrl(unsafeUrl("https://www.unzer.com"))
                .setOrderId(orderId)
                .setCustomerId(customerId)
                .setMetadataId(metadataId)
                .setBasketId(basketId)
                .setCard3ds(card3ds)
                .setAdditionalTransactionData(additionalTransactionData);
        return charge;
    }

    protected Card createPaymentTypeCard(Unzer unzer, String cardNumber) {
        Card card = getPaymentTypeCard(cardNumber);
        card = unzer.createPaymentType(card);
        return card;
    }

    protected Card getPaymentTypeCard() {
        return getPaymentTypeCard("4444333322221111");
    }

    protected Card getPaymentTypeCard(String cardnumber) {
        Card card = new Card(cardnumber, "03/99");
        card.setCvc("123");
        return card;
    }

    protected SepaDirectDebit getSepaDirectDebit() {
        SepaDirectDebit sdd = new SepaDirectDebit("DE89370400440532013000");
        sdd.setBic("COBADEFFXXX");
        sdd.setHolder("Max Mustermann");
        return sdd;
    }

    protected Customer getMinimumCustomer() {
        return new Customer("Max", "Mustermann");
    }

    protected Customer getMinimumRegisteredCustomer() {
        return new Customer("Unzer E-Com GmbH");
    }

    protected Customer getMaximumCustomerSameAddress(String customerId) {
        Customer customer = new Customer("Peter", "Universum");
        customer
                .setCustomerId(customerId)
                .setSalutation(Salutation.MR)
                .setEmail("support@unzer.com")
                .setMobile("+4315136633669")
                .setPhone("+4962214310100")
                .setBirthDate(getDate("03.10.1974"))
                .setBillingAddress(getAddress())
                .setShippingAddress(
                        ShippingAddress.of(getAddress(), ShippingAddress.Type.EQUALS_BILLING)
                );
        customer.setCompany("Unzer E-Com GmbH");
        return customer;
    }

    protected Customer getMaximumCustomer(String customerId) {
        Customer customer = new Customer("Max", "Mustermann");
        customer
                .setCustomerId(customerId)
                .setSalutation(Salutation.MR)
                .setEmail("support@unzer.com")
                .setMobile("+4315136633669")
                .setPhone("+4962216471100")
                .setBirthDate(getDate("03.10.1974"))
                .setBillingAddress(getAddress())
                .setLanguage(Locale.GERMAN)
                .setShippingAddress(
                        ShippingAddress.of(
                                getAddress("Mustermann", "Vangerowstraße 18", "Heidelberg", "BW", "69115", "DE"),
                                ShippingAddress.Type.DIFFERENT_ADDRESS
                        )
                );
        return customer;
    }

    protected Customer getMaximumMrsCustomer(String customerId) {
        Customer customer = new Customer("Anna", "Sadriu");
        customer
                .setCustomerId(customerId)
                .setSalutation(Salutation.MRS)
                .setEmail("support@unzer.com")
                .setMobile("+4315136633669")
                .setBirthDate(getDate("08.05.1986"))
                .setBillingAddress(getAddress())
                .setShippingAddress(
                        ShippingAddress.of(
                                getAddress("Schubert", "Vangerowstraße 18", "Heidelberg", "BW", "69115", "DE"),
                                ShippingAddress.Type.DIFFERENT_ADDRESS
                        )
                );
        return customer;
    }

    protected Customer getMaximumUnknownCustomer(String customerId) {
        Customer customer = new Customer("XXX", "YYY");
        customer
                .setCustomerId(customerId)
                .setSalutation(Salutation.UNKNOWN)
                .setEmail("support@unzer.com")
                .setMobile("+4315136633669")
                .setBirthDate(getDate("01.01.1999"))
                .setBillingAddress(getAddress())
                .setShippingAddress(
                        ShippingAddress.of(
                                getAddress("Schubert", "Vangerowstraße 18", "Heidelberg", "BW", "69115", "DE"),
                                ShippingAddress.Type.DIFFERENT_ADDRESS
                        )
                );
        return customer;
    }

    protected Customer getRegisterdMinimumBusinessCustomer() {
        Customer customer = getMinimumRegisteredCustomer();
        customer.setBillingAddress(getAddress());
        customer.setCompanyData(getRegisteredCompanyData());
        return customer;
    }

    protected Customer getRegisterdMaximumBusinessCustomer(String customerId) throws ParseException {
        Customer customer = getMaximumCustomer(customerId);
        customer.setCompany("Unzer E-Com GmbH");
        getUnregisteredCompanyData();
        customer.setCompanyData(getRegisteredCompanyData());
        return customer;
    }

    protected Customer getUnRegisterdMinimumBusinessCustomer() throws ParseException {
        Customer customer = getMinimumCustomer();
        customer.setBillingAddress(getAddress());

        customer.setCompanyData(getUnregisteredCompanyData());
        return customer;
    }

    protected Customer getUnRegisterdMaximumBusinessCustomer() throws ParseException {
        Customer customer = getMaximumCustomer(generateUuid());
        customer.setCompanyData(getUnregisteredCompanyData());
        return customer;
    }

    protected CompanyInfo getUnregisteredCompanyData() {
        CompanyInfo business = new CompanyInfo();
        business.setCommercialRegisterNumber("HRB337681 MANNHEIM");
        return business;
    }

    protected CompanyInfo getRegisteredCompanyData() {
        CompanyInfo customerBusinessData = new CompanyInfo();
        customerBusinessData.setCommercialRegisterNumber("HRB337681 MANNHEIM");
        return customerBusinessData;
    }

    protected Customer getFactoringOKCustomer(String customerId) throws ParseException {
        Customer customer = new Customer("Maximilian", "Mustermann");
        customer
                .setCustomerId(customerId)
                .setSalutation(Salutation.MR)
                .setBirthDate(getDate("22.11.1980"))
                .setBillingAddress(getFactoringOKAddress())
                .setShippingAddress(
                        ShippingAddress.of(getFactoringOKAddress(), ShippingAddress.Type.EQUALS_BILLING)
                );
        return customer;
    }

    protected Address getFactoringOKAddress() {
        return getAddress("Maximilian Mustermann", "Hugo-Junkers-Str. 3", "Frankfurt am Main", "Frankfurt am Main", "60386", "DE");
    }

    protected Address getAddress() {
        return getAddress("Peter Universum", "Hugo-Junkers-Str. 6", "Frankfurt am Main", "DE-BO", "60386", "DE");
    }

    protected Address getAddress(String name, String street, String city, String state, String zip, String country) {
        Address address = new Address();
        address
                .setName(name)
                .setStreet(street)
                .setCity(city)
                .setState(state)
                .setZip(zip)
                .setCountry(country);
        return address;
    }

    protected Metadata getTestMetadata() {
        return getTestMetadata(false);
    }

    protected Metadata getTestMetadata(boolean sorted) {
        return new Metadata(sorted)
                .addMetadata("invoice-nr", "Rg-2018-11-1")
                .addMetadata("shop-id", "4711")
                .addMetadata("delivery-date", "24.12.2018")
                .addMetadata("reason", "X-mas present");
    }

    protected Date getDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate parsedLocalDate = LocalDate.parse(date, formatter);
        return Date.from(parsedLocalDate.atStartOfDay().atZone(ZoneId.of("UTC")).toInstant());
    }

    protected void assertMapEquals(Map<String, String> testMetadataMap, Map<String, String> metadataMap) {
        for (Map.Entry<String, String> entry : metadataMap.entrySet()) {
            assertEquals(entry.getValue(), testMetadataMap.get(entry.getKey()));
        }
    }

    protected void assertChargeEquals(Charge initCharge, Charge charge) {
        assertEquals(initCharge.getAmount(), charge.getAmount());
        assertEquals(initCharge.getCurrency(), charge.getCurrency());
        assertEquals(initCharge.getCustomerId(), charge.getCustomerId());
        assertEquals(initCharge.getId(), charge.getId());
        assertEquals(initCharge.getPaymentId(), charge.getPaymentId());
        assertEquals(initCharge.getReturnUrl(), charge.getReturnUrl());
        assertEquals(initCharge.getRiskId(), charge.getRiskId());
        assertEquals(initCharge.getTypeId(), charge.getTypeId());
        assertEquals(initCharge.getUrl(), charge.getUrl());
        assertEquals(initCharge.getCancelList(), charge.getCancelList());
        assertProcessingEquals(initCharge.getProcessing(), charge.getProcessing());
    }

    private void assertProcessingEquals(Processing initProcessing, Processing processing) {
        assertEquals(initProcessing.getShortId(), processing.getShortId());
        assertEquals(initProcessing.getUniqueId(), processing.getUniqueId());
    }

    protected void assertBusinessCustomerEquals(CompanyInfo customerExpected, CompanyInfo customer) {
        if (customerExpected == null && customer == null) return;
        assertEquals(customerExpected.getCommercialRegisterNumber(), customer.getCommercialRegisterNumber());
        assertEquals(customerExpected.getCommercialSector(), customer.getCommercialSector());
    }

    protected void assertCustomerEquals(Customer customerExpected, Customer customer) {
        assertEquals(customerExpected.getFirstname(), customer.getFirstname());
        assertEquals(customerExpected.getLastname(), customer.getLastname());
        assertEquals(customerExpected.getCustomerId(), customer.getCustomerId());
        assertEquals(customerExpected.getBirthDate(), customer.getBirthDate());
        assertEquals(customerExpected.getEmail(), customer.getEmail());
        assertEquals(customerExpected.getMobile(), customer.getMobile());
        assertEquals(customerExpected.getPhone(), customer.getPhone());

        assertAddressEquals(customerExpected.getBillingAddress(), customer.getBillingAddress());
        assertAddressEquals(customerExpected.getShippingAddress(), customer.getShippingAddress());
        assertBusinessCustomerEquals(customerExpected.getCompanyData(), customer.getCompanyData());
    }

    protected void assertAddressEquals(Address addressExpected, Address address) {
        if (addressExpected == null) return;
        assertEquals(addressExpected.getCity(), address.getCity());
        assertEquals(addressExpected.getCountry(), address.getCountry());
        assertEquals(addressExpected.getName(), address.getName());
        assertEquals(addressExpected.getState(), address.getState());
        assertEquals(addressExpected.getStreet(), address.getStreet());
        assertEquals(addressExpected.getZip(), address.getZip());
    }

    protected void assertCancelEquals(Cancel cancelInit, Cancel cancel) {
        assertEquals(cancelInit.getAmount(), cancel.getAmount());
        assertEquals(cancelInit.getId(), cancel.getId());
        assertEquals(cancelInit.getUrl(), cancel.getUrl());
    }

    protected void assertNumberEquals(BigDecimal expected, BigDecimal actual) {
        if (expected == null && actual == null) return;
        if (expected == null && actual != null) throw new AssertionError("expected is null, but actual is not");
        if (expected != null && actual == null) throw new AssertionError("expected is not null, but actual is null");
        if (expected.compareTo(actual) != 0) throw new AssertionError("expected: " + expected + ", actual: " + actual);
    }

    protected BigDecimal getBigDecimal(String number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        bigDecimal.setScale(4);
        return bigDecimal;
    }

    protected int confirmMarketplacePendingTransaction(String redirectUrl) {
        try {
            HttpClient httpClient = HttpClients.custom().useSystemProperties().build();
            ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(new HttpGet(redirectUrl));

            Document html = Jsoup.parse(readHtml(response.getEntity().getContent()));
            String apiRediretUrl = html.getElementById("authForm").attr("action");

            response = (ClassicHttpResponse) httpClient.execute(new HttpPost(apiRediretUrl));
            await().atLeast(5, SECONDS).atMost(10, SECONDS);
            return response.getCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 500;
    }

    private String readHtml(InputStream is) {
        try {
            BufferedReader bis = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String s = null;
            while ((s = bis.readLine()) != null) {
                stringBuilder.append(s.concat("\n"));
            }
            bis.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    protected MarketplaceCancelBasket buildCancelBasketByParticipant(List<BasketItem> basketItems, String participantId) {
        MarketplaceCancelBasket cancelBasket = new MarketplaceCancelBasket();

        List<MarketplaceCancelBasketItem> cancelBasketItems = new ArrayList<MarketplaceCancelBasketItem>();
        for (BasketItem basketItem : basketItems) {
            if (participantId.equals(basketItem.getParticipantId())) {
                MarketplaceCancelBasketItem cancelBasketItem = new MarketplaceCancelBasketItem();
                cancelBasketItem.setParticipantId(basketItem.getParticipantId());
                cancelBasketItem.setQuantity(basketItem.getQuantity());
                cancelBasketItem.setBasketItemReferenceId(basketItem.getBasketItemReferenceId());
                cancelBasketItem.setAmountGross(basketItem.getAmountGross());
                cancelBasketItems.add(cancelBasketItem);
            }
        }
        cancelBasket.setItems(cancelBasketItems);
        return cancelBasket;
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
}

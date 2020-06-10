# heidelpayJava
Official heidelpay Java Payment SDK

## Requirements

Java 1.6 or later.

## Installation
```xml
<dependency>
  <groupId>com.heidelpay.payment</groupId>
  <artifactId>heidelpayJava</artifactId>
  <version>1.9.1.0</version>
</dependency>
```

## Documentation
Documentation is available at https://docs.heidelpay.com/docs/java-sdk.

## SDK Overview
### Heidelpay class
The Heidelpay class is instantiated using your private or public key:
```java
Heidelpay heidelpay = new Heidelpay("s-priv-xxxxxxxxxx");
```
You can inject a custom implementation of the http-network communication into the Heidelpay constructor. For implementing a custom communication stack, you have to subclass the AbstractHeidelpayRestCommunication class together with a HeidelpayHttpRequest. For an example please refer to the reference implementation com.heidelpay.payment.communication.impl.HttpClientBasedRestCommunication.

### Authorize or Charge a payment
The first step is to create a Payment Type and then do an authorize or charge for this Payment Type

Example for Authorize

```java
Heidelpay heidelpay = new Heidelpay("s-priv-xxxxxxxxxx");

// Already created the payment type using our Javascript or Mobile SDK's
Authorization authorize = heidelpay.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-fm7tifzkqewy", new URL("https://www.heidelpay.com"));


// Without a payment type created before
Card card = new Card("4444333322221111", "12/19");
Authorization authorize2 = heidelpay.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.heidelpay.com"));
```

Example for Charge

```java
Heidelpay heidelpay = new Heidelpay("s-priv-xxxxxxxxxx");

// Already created the payment type using our Javascript or Mobile SDK's
Charge charge = heidelpay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), "s-sft-fm7tifzkqewy", new URL("https://www.heidelpay.com"));


// Without a payment type created before
Charge charge2 = heidelpay.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new Sofort(), new URL("https://www.heidelpay.com"));
```

As a result for authorize or charge an Authorization or Charge object will be returned which contains a reference to a Payment object.   

### Payment status
To query the status of a payment you can fetch the Payment:

```java
Heidelpay heidelpay = new Heidelpay("s-priv-xxxxxxxxxx");

Payment payment = heidelpay.fetchPayment(authorize.getPayment().getId());
```

### Charge after Authorize
If you started the payment with an authorize (reservation) then you can do a Charge based on this Authorization. 
There are different options to Charge after an Authorization:

```java
Heidelpay heidelpay = new Heidelpay("s-priv-xxxxxxxxxx");

// Charge using Authorize object
Authorization authorization = heidelpay.fetchAuthorization("s-pay-1");
Charge charge = authorization.charge();

// Charge using Payment object
Payment payment = heidelpay.fetchPayment("s-pay-1");
Charge charge2 = payment.charge();

// Charge using Heidelpay object
Charge charge3 = heidelpay.chargeAuthorization("s-pay-1");
```

### Cancel an Authorize
Cancelling a payment after authorization. There are again three options how to do this:

```java
Heidelpay heidelpay = new Heidelpay("s-priv-xxxxxxxxxx");

// Cancel Authorization
Authorization authorization = heidelpay.fetchAuthorization("s-pay-1");
Cancel cancel = authorization.cancel();

// Cancel Payment
Payment payment = heidelpay.fetchPayment("s-pay-1");
Cancel cancel2 = payment.cancel(new BigDecimal(0.1));

// Cancel using Heidelpay object
Cancel cancel3 = heidelpay.cancelAuthorization("s-pay-1");
```

### Cancel on Charge (Refund) 
Cancelling a charge refunds money from the merchant to the customer. There are several options how to do a refund:

```java
Heidelpay heidelpay = new Heidelpay("s-priv-xxxxxxxxxx");

// cancel a charge using Charge object
Charge charge = heidelpay.fetchCharge("s-pay-1", "s-chg-1");
Cancel cancel = charge.cancel();

// cancel a charge using Heidelpay object
Cancel cancel = heidelpay.cancelCharge("s-pay-1", "s-chg-1", BigDecimal.ONE);
```


### Shipment 
To execute a Shipment you need to call the shipment method in Heidelpay object:

```java
Heidelpay heidelpay = new Heidelpay("s-priv-xxxxxxxxxx");

Shipment shipment = heidelpay.shipment(authorize.getPaymentId());
```

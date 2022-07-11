![Logo](unzer_logo.svg)

# Unzer Java Payment SDK
This SDK provides for an easy way to connect to the Unzer Rest API.

## Requirements

Java 1.8 or later.

## Installation
```xml
<dependency>
  <groupId>com.unzer.payment</groupId>
  <artifactId>java-sdk</artifactId>
  <version>1.1.2.7</version>
</dependency>
```

## Documentation
Documentation is available at https://docs.unzer.com/server-side-integration/java-sdk-integration/.

## SDK Overview
### Unzer class
The Unzer class is instantiated using your private or public key:
```java
Unzer unzer = new Unzer("s-priv-xxxxxxxxxx");
```
You can inject a custom implementation of the http-network communication into the Unzer constructor. For implementing a custom communication stack, you have to subclass the AbstractUnzerRestCommunication class together with a UnzerHttpRequest. For an example please refer to the reference implementation HttpClientBasedRestCommunication.

### Authorize or Charge a payment
The first step is to create a Payment Type and then do an authorize or charge for this Payment Type

Example for Authorize

```java
Unzer unzer = new Unzer("s-priv-xxxxxxxxxx");

// Already created the payment type using our Javascript or Mobile SDK's
Authorization authorize = unzer.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), "s-crd-fm7tifzkqewy", new URL("https://www.unzer.com"));


// Without a payment type created before
Card card = new Card("4444333322221111", "12/19");
Authorization authorize2 = unzer.authorize(BigDecimal.ONE, Currency.getInstance("EUR"), card, new URL("https://www.unzer.com"));
```

Example for Charge

```java
Unzer unzer = new Unzer("s-priv-xxxxxxxxxx");

// Already created the payment type using our Javascript or Mobile SDK's
Charge charge = unzer.charge(BigDecimal.ONE, Currency.getInstance("EUR"), "s-sft-fm7tifzkqewy", new URL("https://www.unzer.com"));


// Without a payment type created before
Charge charge2 = unzer.charge(BigDecimal.ONE, Currency.getInstance("EUR"), new Sofort(), new URL("https://www.unzer.com"));
```

As a result for authorize or charge an Authorization or Charge object will be returned which contains a reference to a Payment object.   

### Payment status
To query the status of a payment you can fetch the Payment:

```java
Unzer unzer = new Unzer("s-priv-xxxxxxxxxx");

Payment payment = unzer.fetchPayment(authorize.getPayment().getId());
```

### Charge after Authorize
If you started the payment with an authorize (reservation) then you can do a Charge based on this Authorization. 
There are different options to Charge after an Authorization:

```java
Unzer unzer = new Unzer("s-priv-xxxxxxxxxx");

// Charge using Authorize object
Authorization authorization = unzer.fetchAuthorization("s-pay-1");
Charge charge = authorization.charge();

// Charge using Payment object
Payment payment = unzer.fetchPayment("s-pay-1");
Charge charge2 = payment.charge();

// Charge using Unzer object
Charge charge3 = unzer.chargeAuthorization("s-pay-1");
```

### Cancel an Authorize
Cancelling a payment after authorization. There are again three options how to do this:

```java
Unzer unzer = new Unzer("s-priv-xxxxxxxxxx");

// Cancel Authorization
Authorization authorization = unzer.fetchAuthorization("s-pay-1");
Cancel cancel = authorization.cancel();

// Cancel Payment
Payment payment = unzer.fetchPayment("s-pay-1");
Cancel cancel2 = payment.cancel(new BigDecimal(0.1));

// Cancel using Unzer object
Cancel cancel3 = unzer.cancelAuthorization("s-pay-1");
```

### Cancel on Charge (Refund) 
Cancelling a charge refunds money from the merchant to the customer. There are several options how to do a refund:

```java
Unzer unzer = new Unzer("s-priv-xxxxxxxxxx");

// cancel a charge using Charge object
Charge charge = unzer.fetchCharge("s-pay-1", "s-chg-1");
Cancel cancel = charge.cancel();

// cancel a charge using Unzer object
Cancel cancel = unzer.cancelCharge("s-pay-1", "s-chg-1", BigDecimal.ONE);
```


### Shipment 
To execute a Shipment you need to call the shipment method in Unzer object:

```java
Unzer unzer = new Unzer("s-priv-xxxxxxxxxx");

Shipment shipment = unzer.shipment(authorize.getPaymentId());
```

## Support
For any issues or questions please get in touch with our support team.

### Web page
[https://docs.unzer.com/](https://docs.unzer.com/)

### Email
[support@unzer.com](mailto:support@unzer.com)

### Phone
* DE: [+49 6221 43101-00](tel:+4962214310100)
* AT: [+43 1 513 66 33 669](tel:+4315136633669)

### Twitter
[@UnzerTech](https://twitter.com/UnzerTech)
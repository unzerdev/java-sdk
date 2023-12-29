# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres
to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [UNRELEASED]

### Fixed

* Fixed `Unzer:updateCharge` and `Unzer:updateAuthorization` uses wrong endpoints
* SEPA Direct Debit is restored from deprecation. See: `com.unzer.payment.paymenttypes.SepaDirectDebit`

## [5.0.0](https://github.com/unzerdev/java-sdk/compare/4.4.0..5.0.0)

Consists of internal refactorings and improvements.

### Added

* Added `com.unzer.payment.Resource` interface.

### Changed

* Renamed `AbstractTransaction` -> `BaseTransaction`
* Refactored `UrlUtil`: moved Resource URL construction logic to `Resource`-classes.
* Every Unzer Payment Gateway resource implements `Resource` instead of `PaymentType`: `Customer`, `Basket`, `Payment`, etc.
* Renamed `com.unzer.payment.CustomerCompanyData` to `CompanyInfo`
* Renamed property and methods of `com.unzer.payment.Customer.Customer` to match class name:
    * property `companyData`-> `companyInfo`
    * method `getCompanyData()` -> `getCompanyInfo()`
    * method `setCompanyData()` -> `setCompanyInfo()`

### Removed

* Removed `PaymentType::getId()` and `PaymentType::getTypeUrl()` methods. Use `Resource::getId()` and `Resource::getUrl()` instead.

## [4.4.0](https://github.com/unzerdev/java-sdk/compare/4.3.0..4.4.0)

### Added

* Added Paylater Direct Debit payment method

### Deprecated

* Deprecated Sepa Direct Debit and Sepa Direct Debit Secured payment methods. Please, use Paylater Direct Debit instead.

## [4.3.0](https://github.com/unzerdev/java-sdk/compare/4.2.0..4.3.0)

This release adds support for PayU payment method and fixes some LinkPay issues

### Added

* Added PayU payment method support
* Added BasePaypage.Action set of constants for Authorize and Charge actions for Paypage/Linkpay
* Added Authorize support for Linkpay: `Linkpay.setAction(BasePaypage.Action.AUTHORIZE)`

### Deprecated

* Deprecated `Paypage.Action`. Use `BasePaypage.Action` instead

## [4.2.0](https://github.com/unzerdev/java-sdk/compare/4.1.0..4.2.0)

This Java SDK version delivers Unzer Paylater Installment to Java projects.

### Added

* Add Paylater Installment support:
    * Add `com.unzer.payment.service.PaymentService.fetchPaylaterInstallmentPlans()` to get available installment plans.
    * Add payment type `com.unzer.payment.paymenttypes.PaylaterInstallment`.
* `com.unzer.payment.fetchPayment` now fetches chargeback transactions of the payment. See `com.unzer.payment.Payment.chargebackList`
* Added field `paypageId` to payment and its transactions (authorize, charge, cancel, chargeback). See: `com.unzer.payment.AbstractPayment.paypageId`, `com.unzer.payment.AbstractTransaction.paypageId` 

### Changed

* Refactored internal class `com.unzer.payment.communication.mapper.ApiToSdkConverter`:
  * Unified parameter names to `src` and `output`
  * Changed parameters order. First parameter is `src`, second is `output`
* Renamed internal classes at `com.unzer.payment.communication.json`:
  * `JsonPayment` -> `ApiPayment`
  * `JsonTransaction` -> `ApiTransaction`

### Deprecated
*   Installment Secured: 
    * method `com.unzer.payment.Unzer.installmentSecuredRates()`
    * class `com.unzer.payment.business.paymenttypes.InstallmentSecuredRatePlan`
    * class `com.unzer.payment.business.paymenttypes.InstallmentSecuredRate`

## [4.1.0](https://github.com/unzerdev/java-sdk/compare/4.0.0..4.1.0)

This release adds functionality to fetch payment page

### Added

* Added `com.unzer.payment.Unzer.fetchPaypage`

### Changed

* Changed return value for `com.unzer.payment.Paypage.getTypeUrl`. Was: `paypage/charge` or `paypage/authorize`, now: `paypage`

## [4.0.0][4.0.0]

This release brings liability and exemption type support to Java SDK

### Added

* Added `Liability` and `ExemptionType` fields to `AdditionalTransactionData.CardTransactionData`

### Removed

* Removed resource `version.properties`. Version is set at generate-sources phase.
* Removed class `com.unzer.payment.exceptions.PropertiesException`
* Removed deprecated method `com.unzer.payment.util.SDKInfo::getVersion()`. Please, use `com.unzer.payment.util.SDKInfo.VERSION` instead

### Changed

* Enriched request and response logging

### Fixed

* Fixed `AdditionalTransactionData.RecurrenceType` field is empty for fetched transactions. 

## [3.1.1][3.1.1]

This release updates dependencies with security issues

### Changed

* Upgraded Apache HttpClient from version 4 to version 5.
* Made `HttpCommunicationException` unchecked exception.
* Updated `jackson-core` dependency.

## [3.1.0][3.1.0]

This release introduces Unzer PayPal Express in Java SDK.

### Added

* Added Paypal-Express support:
  * Extended `AdditionalTransactionData` with `paypal.checkoutType` field. See `com.unzer.payment.models.AdditionalTransactionData.setPaypal`.
  * Defined `RESUMED` value for `com.unzer.payment.AbstractTransaction.Status`
  * Added `com.unzer.payment.Unzer::updateCharge()`, `com.unzer.payment.Unzer::updateAuthorization()` which must be invoked after Paypal-Express transaction is resumed.
* Added fields `orderId` and `invoiceId` to `Authorize`, `Charge` and `Cancel` transactions.
* Added authorize support for Paypage. Use `Paypage::setAction(Paypage.Action.AUTHORIZE)`
* Added new capture (charge authorization) methods. See: `com.unzer.payment.Unzer.chargeAuthorization(charge)` and `com.unzer.payment.Unzer.chargeAuthorization(paymentId, charge)`

### Changed

* Reduced multiple `com.unzer.payment.service.PaymentService.chargeAuthorization` methods to one with `Charge` argument.

### Deprecated 

* Deprecated `com.unzer.payment.paymenttypes.Invoice`. Use `com.unzer.payment.paymenttypes.PaylaterInvoice` instead 
* Deprecated behavioral methods for data objects. Please, use Unzer facade instead. List of deprecations:
  * `AbstractTransaction` (base class of `Authorization`, `Cancel`, `Charge`, `Payout`, `Recurring`, `Shipment`, `MarketplaceAuthorization`, `MarketplaceCharge`, `MarketplacePayment`, `MarketplaceCancel`):
    * `getUnzer`/`setUnzer`
  * `Charge`:
    * `cancel`
  * `Authorization`:
    * `charge`
    * `cancel`
  * `Payment`:
    * `charge`
    * `authorize`
    * `cancel`
  * `MarketplaceCharge`:
    * `cancel`
  * `MarketplaceAuthorization`:
    * `charge`
    * `cancel`
  * `MarketplacePayment`:
    * `marketplaceFullChargesCancel`
    * `fullChargeAuthorizations`
    * `marketplaceFullAuthorizeCancel`
  * `AbstractPayment`:
    * `fetchBasket`, `fetchMetadata`, `fetchCustomer`, `fetchPaymentType`

## [3.0.0][3.0.0]

This release switches Java SDK version to a traditional 3-digit semantic versioning style.

### Changed
* Removed first digit at semver: ~~API_VERSION.~~ MAJOR.MINOR.PATCH

## [1.3.0.0][1.3.0.0]

New version of Java SDK is not configurable via `unzer.properties` file anymore.

### Breaking changes
* Removed class `com.unzer.payment.service.PropertiesUtil`, unzer.properties file and `privatekey1`, `privatekey2`, `privatekey3`, `publickey1`, `marketplacekey`
  and corresponding constants `PUBLIC_KEY1`, `PRIVATE_KEY1`, `PRIVATE_KEY2`, `PRIVATE_KEY3`, `MARKETPLACE_PRIVATE_KEY`. Since this properties were used only for internal testing purpose.
* Removed property `applepay.validValidationUrls` from `unzer.properties`. Use `com.unzer.payment.util.ApplePayAdapterUtil.replaceValidationUrls` instead
* Changed method `com.unzer.payment.util.ApplePayAdapterUtil.getPlainDomainName()` modifier to private.
* Removed `unzer.properties` file. Use `com.unzer.payment.util.ApplePayAdapterUtil.setCustomAppleValidationUrls` to configure ApplePay validation urls.

### Changed
* Upgraded `jackson-databind` dependency. See: [CVE-2022-42004](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2022-42004)

## [1.2.2.0][1.2.2.0]

This release adds Klarna payment type to Java SDK.

### Added

* Added payment type `Klarna`.
* Added `language` field to `Customer` resource.
* Added `termsAndConditionsUrl` and `privacyPolicyUrl` fields to `AdditionalTransactionData` resource

### Fixed

* Fixed typo `ShippingAddress.Type:DIFFERENT_ADDRESSES` -> `ShippingAddress.Type:DIFFERENT_ADDRESS`. This typo caused errors on authorize/charge in some payment cases
* Fixed (de-)serialization of `AbstractTransaction` cause infinite Webhook calls.

## [1.2.1.0][1.2.1.0]

This release brings Unzer Paylater Invoice payment type support to Java SDK.

### Added

* Added payment type Paylater Invoice. See more at [Unzer Docs](https://docs.unzer.com/payment-methods/unzer-invoice-upl/)
* Added ability to set client IP. Use `com.unzer.payment.communication.impl.HttpClientBasedRestCommunication.HttpClientBasedRestCommunication(java.util.Locale, java.lang.String)` and `com.unzer.payment.Unzer.Unzer(java.lang.String, java.util.Locale, java.lang.String, java.lang.String)` to set client ip.
* Added `com.unzer.payment.Unzer.cancelCharge(java.lang.String, java.math.BigDecimal)` to cancel Paylater charges.

### Fixed

* Fixed: customer salutation is null, because of marshalling/unmarshalling issue.
* Fixed: cancel InvoiceSecured charge failed with error `API.340.100.024: Reason code is mandatory for the payment type INVOICE_SECURED`. Please, use `com.unzer.payment.Cancel.setReasonCode` to set reason code and `com.unzer.payment.Unzer.cancelCharge(java.lang.String, java.lang.String, com.unzer.payment.Cancel)` to cancel InvoiceSecured charge. 

### Removed

* Removed unused `com.unzer.payment.UnsupportedPaymentTypeException`

### Deprecated

* Deprecated `com.unzer.payment.util.SDKInfo.getVersion()`. Use `com.unzer.payment.util.SDKInfo.VERSION` instead.
* Deprecated payment type `com.unzer.payment.paymenttypes.InvoiceSecured`. Use `com.unzer.payment.paymenttypes.PaylaterInvoice` instead.
* Deprecated `com.unzer.payment.Customer.setShippingAddress(com.unzer.payment.Address)`. Use `com.unzer.payment.Customer.setShippingAddress(com.unzer.payment.ShippingAddress)` instead

### Changed

* Changed type of `com.unzer.payment.Customer.shippingAddress` and according getter/setter: `com.unzer.payment.Address` -> `com.unzer.payment.ShippingAddress`. Use `ShippingAddress.of(Address, Type)` to adapt

## [1.2.0.0][1.2.0.0]

### Breaking changes 

*   Removed `log4j-core` from Maven dependencies. **Please, provide logger
    implementation on your own**

*   Renamed enum value `AbstractTransaction.Status.ERRROR` to `AbstractTransaction.Status.ERROR`

*   Renamed enum value `Paypage.Status.ERRROR` to `Paypage.Status.ERROR`

### Deprecated

*   `com.unzer.payment.service.UrlUtil#getUrl(String)` will not be part of
    java-sdk, because it has nothing to do with unzer/sdk specific logic. If you
    rely on this method, please, replace it with `java.net.URL#URL(String)`
    constructor call

*   Deprecated Basket getters/setters: `amountTotalVat`, `amountTotalGross`,
    `amountTotalDiscount`

*   Deprecated BasketItem getters/setters: `amountDiscount`, `amountGross`, `amountVat`,
    `amountPerUnit`, `amountNet`

### Added

*   Basket v2 support:

    *   Basket getters/setters: `totalValueGross`

    *   BasketItem getters/setters: `amountPerUnitGross`, `amountDiscountPerUnitGross`

### Removed

*   Remove `log.error` in catch clause 
    `com.unzer.payment.service.PropertiesUtil#loadProperties()` because the
    exception with exact same message is thrown after the `log.error` call.

### Fixed

*   Fix log message of `com.unzer.payment.service.UrlUtil#getUrl(String)`. It was
    not formatted and contained `%s` instead
    of values

### Changed

*   Type of `BasketItem` type field (was `String`, become `BasketItem.Type`)

*   Type of `BasketItem` `vat` field (was `Integer`, become `BigDecimal`)

## [1.1.2.7][1.1.2.7]

### Changed

*   Upgrade of the used gson Dependencies from 2.8.6 to 2.8.9
*   Upgrade of the used faster-xml dependencies from 2.11.3 to 2.12.7
*   Upgrade of the used log4j dependencies from 2.17.1 to 2.18.0

## [1.1.2.6][1.1.2.6]

### Added

*   Added Unzer-Logo and updated the reference in Readme-File.
*   Added Json-Validation to JsonParser-Class before real Json-Deserialization
    happens.

*   Added Twitter-Handle to Readme-File.

### Changed

*   Documentation-Link in Readme-File has been changed.
*   Several minor improvements.

## [1.1.2.5][1.1.2.5]

### Changed

*   Upgrade of the used Log4j Dependencies to fix the Log4j Zero-Day-Exploit from
    2.16.0 to 2.17.0.

*   Handling of PaymentException-Message has been changed to now return correct
    Message on occurrence.

*   Several minor improvements.

## [1.1.2.4][1.1.2.4]

### Changed

*   POM-File Updates.
*   Several minor improvements.

## [1.1.2.3][1.1.2.3]

### Changed

*   Upgrade of the used Log4j Dependencies to fix the Log4j Zero-Day-Exploit.
*   Bumped log4j-Dependency from 2.15.0 to 2.16.0
*   Bumped Maven-Dependency-Check-Dependency from 5.3.2 to 6.5.0
*   Bumped httpclient from 4.5.12 to 4.5.13
*   Bumped junit from 4.13 to 4.13.1
*   Bumped jsoup from 1.13.1 to 1.14.2
*   Increased Deployment Timeout for Sonatype to 20 Minutes.
*   Several minor improvements.

## [1.1.2.2][1.1.2.2]

### Changed

*   Upgrade of the used Log4j Dependencies to fix the Log4j Zero-Day-Exploit.
*   Several minor improvements.

## [1.1.2.1][1.1.2.1]

### Changed

*   Changed the DateTime-Handling from PAPI so that all timezones will be handled
    correctly in parsing and formatting.

*   Several minor improvements.

## [1.1.2.0][1.1.2.0]

### Added

*   Enabled to set RecurrenceType for Authorize-, Charge- and Recurring-Requests.
*   Added SDK-Info to HTTP-Request-Header like SDK-Type or SDK-Version.

### Changed

*   Several minor improvements.

## [1.1.1.1][1.1.1.1]

### Changed

*   Added JAXB as a separate library to support newer Java versions.

## [1.1.1.0][1.1.1.0]

### Changed

*   Changed `ApplePay`-Paymenttype accordingly to new Tech-Spec.
    *   Added new Parameters to `ApplePay`-Class.
    *   Added new Test-Functions for `ApplePay`-Class and `ApplePayHeader`-Class.
    *   Added Constructors for `ApplePay`-Class and `ApplePayHeader`-Class with mandatory Parameters.

*   Changed Documentation Link in Readme-File.

## [1.1.0.1][1.1.0.1]

### Fixed

*   Fixed the Way how the PAPI-Keypairs are loaded on Unzer Object Creation and
    eliminated the NullPointerException which
    was thrown.

### Changed

*   Some Tests were failing because of missing Merchant Configuration of the used
    Test-Merchant.

## [1.1.0.0][1.1.0.0]

### Added

*   Added mapping of old payment type ids to the new payment type resources.
    *   e.g. `InvoiceGuaranteed` and `InvoiceFactoring` replaced by `InvoiceSecured`.

*   Add email property to payment type `card` to meet 3Ds2.x regulations.

### Changed

*   Rebranded SDK to Unzer.
*   Removed payment type string from URL when fetching a payment type resource.
*   Replace payment methods guaranteed/factoring by secured payment methods, i.e.:
    *   `InvoiceGuaranteed` and `InvoiceFactoring` replaced by `InvoiceSecured`
    *   `SepaDirectDebitGuaranteed` replaced by `SepaDirectDebitSecured`
    *   `HirePurchaseDirectDebit` replaced by `InstallmentSecured`
    *   Basket is now mandatory for all those payment types above.
*   Several minor changes.

### Fixed

*   Changed handling of Keypairs for tests.

### Removed

*   Remove deprecated methods.
    *   getBasketImage
    *   setBasketImage
    *   getDescriptionMain
    *   setDescriptionMain
    *   getDescriptionSmall
    *   setDescriptionSmall
    *   cancelAuthorization
*   Remove deprecated classes
    *   RestCommunication

[4.0.0]: http://github.com/unzerdev/java-sdk/compare/3.1.1..4.0.0

[3.1.1]: http://github.com/unzerdev/java-sdk/compare/3.1.0..3.1.1

[3.1.0]: http://github.com/unzerdev/java-sdk/compare/3.0.0..3.1.0

[3.0.0]: http://github.com/unzerdev/java-sdk/compare/1.3.0.0..3.0.0

[1.3.0.0]: http://github.com/unzerdev/java-sdk/compare/1.2.2.0..1.3.0.0

[1.2.2.0]: http://github.com/unzerdev/java-sdk/compare/1.2.1.0..1.2.2.0

[1.2.1.0]: http://github.com/unzerdev/java-sdk/compare/1.2.0.0..1.2.1.0

[1.2.0.0]: http://github.com/unzerdev/java-sdk/compare/1.1.2.7..1.2.0.0

[1.1.2.7]: http://github.com/unzerdev/java-sdk/compare/1.1.2.6..1.1.2.7

[1.1.2.6]: http://github.com/unzerdev/java-sdk/compare/1.1.2.5..1.1.2.6

[1.1.2.5]: http://github.com/unzerdev/java-sdk/compare/1.1.2.4..1.1.2.5

[1.1.2.4]: http://github.com/unzerdev/java-sdk/compare/1.1.2.3..1.1.2.4

[1.1.2.3]: http://github.com/unzerdev/java-sdk/compare/1.1.2.2..1.1.2.3

[1.1.2.2]: http://github.com/unzerdev/java-sdk/compare/1.1.2.1..1.1.2.2

[1.1.2.1]: http://github.com/unzerdev/java-sdk/compare/1.1.2.0..1.1.2.1

[1.1.2.0]: http://github.com/unzerdev/java-sdk/compare/1.1.1.1..1.1.2.0

[1.1.1.1]: http://github.com/unzerdev/java-sdk/compare/1.1.1.0..1.1.1.1

[1.1.1.0]: http://github.com/unzerdev/java-sdk/compare/1.1.0.1..1.1.1.0

[1.1.0.1]: http://github.com/unzerdev/java-sdk/compare/1.1.0.0..1.1.0.1

[1.1.0.0]: http://github.com/unzerdev/java-sdk/compare/c45ad44972e4a96b30b0744f5b70734f2122f142..1.1.0.0

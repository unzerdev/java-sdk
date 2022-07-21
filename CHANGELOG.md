# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres
to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## Unreleased

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

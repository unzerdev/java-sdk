# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres
to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [1.1.1.0][1.1.1.0]

### Changed

*  Changed `ApplePay`-Paymenttype accordingly to new Tech-Spec
   * Added new Parameters to `ApplePay`-Class
   * Added new Test-Functions for `ApplePay`-Class and `ApplePayHeader`-Class
   * Added Constructors for `ApplePay`-Class and `ApplePayHeader`-Class with mandatory Parameters

## [1.1.0.1][1.1.0.1]

### Fixed

*   Fixed the Way how the PAPI-Keypairs are loaded on Unzer Object Creation and eliminated the NullPointerException which was thrown

### Changed

*   Some Tests were failing because of missing Merchant Configuration of the used Test-Merchant

## [1.1.0.0][1.1.0.0]

### Added

*  Added mapping of old payment type ids to the new payment type resources.
   * e.g. `InvoiceGuaranteed` and `InvoiceFactoring` replaced by `InvoiceSecured`
*  Add email property to payment type `card` to meet 3Ds2.x regulations.

### Changed

*  Rebranded SDK to Unzer.
*  Removed payment type string from URL when fetching a payment type resource.
*  Replace payment methods guaranteed/factoring by secured payment methods, i.e.:
   * `InvoiceGuaranteed` and `InvoiceFactoring` replaced by `InvoiceSecured`
   * `SepaDirectDebitGuaranteed` replaced by `SepaDirectDebitSecured`
   * `HirePurchaseDirectDebit` replaced by `InstallmentSecured`
   * Basket is now mandatory for all those payment types above.
*  Several minor changes.

### Fixed

*  Changed handling of Keypairs for tests

### Removed

*  Remove deprecated methods.
   * getBasketImage
   * setBasketImage
   * getDescriptionMain
   * setDescriptionMain
   * getDescriptionSmall
   * setDescriptionSmall
   * cancelAuthorization
*  Remove deprecated classes
   * RestCommunication

[1.1.1.0]: http://github.com/unzerdev/java-sdk/compare/1.1.1.0..1.1.0.1

[1.1.0.1]: http://github.com/unzerdev/java-sdk/compare/1.1.0.0..1.1.0.1

[1.1.0.0]: http://github.com/unzerdev/java-sdk/compare/c45ad44972e4a96b30b0744f5b70734f2122f142..1.1.0.0
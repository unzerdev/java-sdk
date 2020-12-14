# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [1.1.0.0][1.1.0.0]

### Added
* Added mapping of old payment type ids to the new payment type resources.
  * e.g. `InvoiceGuaranteed` and `InvoiceFactoring` replaced by `InvoiceSecured`

### Changed
* Rebranded SDK to Unzer.
* Removed payment type string from URL when fetching a payment type resource.
* Replace payment methods guaranteed/factoring by secured payment methods, i.e.:
  * `InvoiceGuaranteed` and `InvoiceFactoring` replaced by `InvoiceSecured`
  * `SepaDirectDebitGuaranteed` replaced by `SepaDirectDebitSecured`
  * `HirePurchaseDirectDebit` replaced by `InstallmentSecured`
  * Basket is now mandatory for all those payment types above.
* Several minor changes.

### Fixed
* Changed handling of Keypairs for tests

### Removed
* Remove deprecated methods.
    * getBasketImage
    * setBasketImage
    * getDescriptionMain
    * setDescriptionMain
    * getDescriptionSmall
    * setDescriptionSmall
    * cancelAuthorization
* Remove deprecated classes
    * RestCommunication

[1.1.0.0]: http://github.com/unzerdev/java-sdk/compare/c45ad44972e4a96b30b0744f5b70734f2122f142..1.1.0.0
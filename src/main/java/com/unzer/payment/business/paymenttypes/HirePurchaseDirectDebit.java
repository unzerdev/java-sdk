package com.unzer.payment.business.paymenttypes;

import com.unzer.payment.paymenttypes.PaylaterInstallment;

/**
 * @deprecated Will be replaced by {@link PaylaterInstallment} in the future.
 */
@Deprecated
public class HirePurchaseDirectDebit extends InstallmentSecuredRatePlan {
    @Override
    protected String getResourceUrl() {
        return "/v1/types/hire-purchase-direct-debit/<resourceId>";
    }

}

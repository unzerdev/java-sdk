package com.unzer.payment.paymenttypes;

import com.unzer.payment.Resource;
import com.unzer.payment.communication.json.ApiObject;

public interface PaymentType extends Resource {
    PaymentType map(PaymentType paymentType, ApiObject apiObject);
}

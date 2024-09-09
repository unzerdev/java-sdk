package com.unzer.payment.models.paypage;

import lombok.Data;

@Data
public class Style {
    private String fontFamily;
    private String buttonColor;
    private String primaryTextColor;
    private String linkColor;
    private String backgroundColor;
    private String cornerRadius;
    private Boolean shadows;
    private Boolean hideUnzerLogo;
}

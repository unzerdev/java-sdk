package com.heidelpay.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:paypal-configuration.properties")
public class TestKeyConfiguration {

	@Value("${heidelpay.test.publickkey1}")
	private String publicKey1;

	@Value("${heidelpay.test.privatekey1}")
	private String privateKey1;

	@Value("${heidelpay.test.privatekey2}")
	private String privateKey2;

	@Value("${heidelpay.test.privatekey3}")
	private String privateKey3;

	public String getPublicKey1() {
		return publicKey1;
	}

	public String getPrivateKey1() {
		return privateKey1;
	}

	public String getPrivateKey2() {
		return privateKey2;
	}

	public String getPrivateKey3() {
		return privateKey3;
	}
}

package com.heidelpay.payment.business;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.heidelpay.payment.Customer;
import com.heidelpay.payment.Customer.Salutation;

public class CustomerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Customer customer = new Customer("Rene", "Felder");
		customer.setCustomerId("123").setSalutation(Salutation.mr)
		.setEmail("rene.felder@felderit.at").setMobile("+43676123456");
		assertNotNull(customer);
		
	}

}

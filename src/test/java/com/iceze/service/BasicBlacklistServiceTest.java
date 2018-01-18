package com.iceze.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.iceze.model.CreditCard;

public class BasicBlacklistServiceTest {
	private BasicBlacklistService service;
	
	@Before
	public void setup() throws IOException {
		this.service = new BasicBlacklistService("src/test/resources/blacklist.json");
	}

	@Test
	public void isBlacklistedReturns() {
		CreditCard card = new CreditCard("5144 3854 3852 3845", "01/19");
		boolean result = this.service.isBlacklisted(card);
		assertThat(result).isEqualTo(true);
		
		card = new CreditCard("5144385438523845", "01/19");
		result = this.service.isBlacklisted(card);
		assertThat(result).isEqualTo(true);
		
		card = new CreditCard("5144 3854 3852 3846", "01/19");
		result = this.service.isBlacklisted(card);
		assertThat(result).isEqualTo(false);
	}
}

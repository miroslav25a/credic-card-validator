package com.iceze.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.iceze.exception.CreditCardException;
import com.iceze.model.CreditCard;

public class BasicCreditCardServiceTest {
	private BasicCreditCardService service;
	@Mock
	private BlacklistService blacklistService;
	
	@Before
	public void setup() {
		initMocks(this);
		this.service = new BasicCreditCardService(blacklistService);
	}
	
	@Test
	public void validateNumberReturnsFifteenDigitCardNumber() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateNumber("5418 5900 1234 567"))
	        .withMessage("Invalid number length for number: 5418 5900 1234 567")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateNumberReturnsSeventeenDigitCardNumber() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateNumber("5418 5900 1234 56793"))
	        .withMessage("Invalid number length for number: 5418 5900 1234 56793")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateNumberReturnsInvalidDigit() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateNumber("5418 5900 1234 567i"))
	        .withMessage("Invalid digits for number: 5418 5900 1234 567i")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateCardTypeReturnsCreditCardNumberStartsWith3() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateCardType("3418 5900 1234 5679"))
	        .withMessage("Invalid card type for number: 3418 5900 1234 5679")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateCardTypeReturnsCreditCardNumberStartsWith6() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateCardType("6418 5900 1234 5679"))
	        .withMessage("Invalid card type for number: 6418 5900 1234 5679")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateCardTypeReturnsCreditCardNumberStartsWith50() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateCardType("5018 5900 1234 5679"))
	        .withMessage("Invalid card type for number: 5018 5900 1234 5679")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateCardTypeReturnsCreditCardNumberStartsWith56() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateCardType("5618 5900 1234 5679"))
	        .withMessage("Invalid card type for number: 5618 5900 1234 5679")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateVerificationDigitReturnsInvalidDigit() {
		assertThatExceptionOfType(CreditCardException.class)
			.isThrownBy(() -> this.service.validateVerificationDigit("5418 5900 1234 5678"))
			.withMessage("Invalid verification digit for number: 5418 5900 1234 5678")
			.withStackTraceContaining("CreditCardException")
			.withNoCause();
	}
	
	@Test
	public void validateCardTypeReturnsCreditCardNumberStartsWith51() {
		assertThatCode(() -> this.service.validateCardType("5354 2000 0955 5498"))
			.doesNotThrowAnyException();
	}
	
	@Test
	public void validateReturnsValidCreditCard() {
		String date = LocalDate.now().plusYears(2).format(DateTimeFormatter.ofPattern("MM/yy"));
		CreditCard card = new CreditCard("4547 4220 0705 5103", date);
		
		assertThatCode(() -> this.service.validate(card))
			.doesNotThrowAnyException();
	}
	
	@Test
	public void validateNumberReturnsValidCreditCardNoSpaces() {
		assertThatCode(() -> this.service.validateNumber("5418590012345679"))
			.doesNotThrowAnyException();
	}
	
	@Test
	public void validateReturnsValidCreditCardSomeSpaces() {
		assertThatCode(() -> this.service.validateNumber("5418 5900 12345679"))
			.doesNotThrowAnyException();
	}
	
	@Test
	public void validateExpiryDateReturnsDateInThePast() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateExpiryDate("01/01"))
	        .withMessage("Invalid, date in the past : 01/01")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateExpiryDateReturnsDateInThePastBefore2000() {
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateExpiryDate("01/99"))
	        .withMessage("Invalid, date in the past : 01/99")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
	}
	
	@Test
	public void validateAgainstBlacklistReturns() {
		when(this.blacklistService.isBlacklisted(any(CreditCard.class))).thenReturn(true, false);
		
		assertThatExceptionOfType(CreditCardException.class)
	        .isThrownBy(() -> this.service.validateAgainstBlacklist(new CreditCard("5144 3854 3852 3845", "01/19")))
	        .withMessage("Invalid, blacklisted number: 5144 3854 3852 3845")
	        .withStackTraceContaining("CreditCardException")
	        .withNoCause();
		
		assertThatCode(() -> this.service.validateAgainstBlacklist(new CreditCard("5144 3854 3852 3846", "01/19")))
			.doesNotThrowAnyException();
		
		verify(this.blacklistService, times(2)).isBlacklisted(any(CreditCard.class));
	}
}

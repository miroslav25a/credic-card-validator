package com.iceze.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.iceze.exception.CreditCardException;
import com.iceze.model.CreditCard;

/**
 * This class contains basic implementation of the common credit card functionalities.
 * 
 * @author miroslav
 */
@Service("creditCardService")
public class BasicCreditCardService implements CreditCardService {
	private BlacklistService blacklistService;
	
	public BasicCreditCardService(final BlacklistService blacklistService) {
		this.blacklistService = blacklistService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void validate(final CreditCard card) throws CreditCardException {
		this.validateNumber(card.getNumber());
		this.validateCardType(card.getNumber());
		this.validateVerificationDigit(card.getNumber());
		this.validateExpiryDate(card.getExpiration());
		this.validateAgainstBlacklist(card);
	}
	
	/**
	 * Validate a credit card against a blacklist.
	 * 
	 * @param card
	 * 			CreditCard, card to validate
	 * 
	 * @throws CreditCardException
	 */
	protected void validateAgainstBlacklist(final CreditCard card) throws CreditCardException {
		if (this.blacklistService.isBlacklisted(card)) {
			throw new CreditCardException("Invalid, blacklisted number: " + card.getNumber());
		}
	}
	
	/**
	 * Validate the last digit of the given card number against the Lunh formula.
	 * 
	 * @param number,
	 * 			String represents a card number
	 * 
	 * @throws CreditCardException
	 */
	protected void validateVerificationDigit(final String number) throws CreditCardException {
		List<Integer> digits = Arrays.asList(number.replaceAll("\\s", "").split(""))
						.stream()
						.map(Integer::parseInt)
						.collect(Collectors.toList());

		Integer oddDigitsSum = IntStream.range(0, digits.size()- 1 )
						.filter(i -> i % 2 != 0).map(i -> {
							int doubledValue = 2 * digits.get(i);
							int x1 = doubledValue / 10;
							int x2 = doubledValue % 10;
							return x1 + x2;
						}).sum();
		
		Integer evenDigitsSum = IntStream.range(0, digits.size() - 1)
						.filter(i -> i % 2 == 0)
						.map(i -> digits.get(i))
						.sum();
		
		Integer totalSum = oddDigitsSum + evenDigitsSum;
		
		Integer checkDigit = new Integer(10 - (totalSum % 10));
		
		if (!digits.get(digits.size() - 1).equals(checkDigit)) {
			throw new CreditCardException("Invalid verification digit for number: " + number);
		}
	}
	
	/**
	 * Validate that the card number is only Visa or MasterCard.
	 * 
	 * @param number,
	 * 			String represents a card number
	 * 
	 * @throws CreditCardException
	 */
	protected void validateCardType(final String number) throws CreditCardException {
		String numberNoSpace = number.replaceAll("\\s", "");
		int firstDigit = Character.getNumericValue(numberNoSpace.charAt(0));

		if(firstDigit < 4 || firstDigit > 5) {
			throw new CreditCardException("Invalid card type for number: " + number);
		}
		
		int secondDigit = Character.getNumericValue(numberNoSpace.charAt(1));
		
		if(firstDigit == 5) {
			if(secondDigit < 1 || secondDigit > 5) {
				throw new CreditCardException("Invalid card type for number: " + number);
			}
		}
	}
	
	/**
	 * Validate the card number length and digits.
	 * 
	 * @param number,
	 * 			String represents a card number
	 * 
	 * @throws CreditCardException
	 */
	protected void validateNumber(final String number) throws CreditCardException {
		// remove spaces from a card number
		String numberNoSpace = number.replaceAll("\\s", "");
				
		// check that a card number is 16 chars long
		if(numberNoSpace.length() != 16) {
			throw new CreditCardException("Invalid number length for number: " + number);
		}
				
		// check that every card number char is a digit
		String[] numberArr = numberNoSpace.split("");
		try {
			IntStream.range(0, numberArr.length).forEach(i -> {
				Integer.parseInt(numberArr[i]);
			});
		} catch(NumberFormatException e) {
			throw new CreditCardException("Invalid digits for number: " + number);
		}
	}
	
	/**
	 * Validate the credit card expiry date.
	 * 
	 * @param date
	 * 			String, represents a credit card date.
	 * 
	 * @throws CreditCardException
	 * @throws  
	 */
	protected void validateExpiryDate(final String date) throws CreditCardException  {
		try {
			DateFormat informat= new SimpleDateFormat("MM/yy");
			DateFormat outformat= new SimpleDateFormat("MM/yyyy");

			String formattedDate = "1/" + outformat.format(informat.parse(date));
	
			DateTimeFormatter df = DateTimeFormatter.ofPattern("d/MM/yyyy");
			
			LocalDate cardDate = LocalDate.parse(formattedDate, df);
			LocalDate currentDate = LocalDate.now();
			
			if(cardDate.isBefore(currentDate)) {
				throw new CreditCardException("Invalid, date in the past : " + date);
			}
		} catch(DateTimeParseException | ParseException e) {
			throw new CreditCardException("Invalid date format: " + date);
		}
	}
}

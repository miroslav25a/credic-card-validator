package com.iceze.service;

import com.iceze.exception.CreditCardException;
import com.iceze.model.CreditCard;

/**
 * This interface contains the common functionalities for a credit card.
 * 
 * @author miroslav
 */
public interface CreditCardService {
	
	/**
	 * Validate credit card details.
	 * 
	 * @param card 
	 * 			CreditCard, represents a credit card.
	 * 
	 * @throws CreditCardException
	 */
	void validate(final CreditCard card) throws CreditCardException;
}

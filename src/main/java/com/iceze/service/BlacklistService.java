package com.iceze.service;

import com.iceze.model.CreditCard;

/**
 * This interface contains the common functionality of the blacklist service.
 * 
 * @author miroslav
 */
public interface BlacklistService {
	/**
	 * Check if the given credit card is blacklisted.
	 * 
	 * @param card
	 * 			CreditCard, card to check
	 * 
	 * @return boolean
	 * 			true if it's blacklisted false otherwise
	 */
	boolean isBlacklisted(final CreditCard card);
}

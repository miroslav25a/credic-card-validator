package com.iceze.exception;

/**
 * Invalid credit card exception.
 * 
 * @author miroslav
 */
public class CreditCardException extends RuntimeException {
	private static final long serialVersionUID = -9096652951128464901L;

	public CreditCardException(final String e) {
		super(e);
	}
}

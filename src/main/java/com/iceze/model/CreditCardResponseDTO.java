package com.iceze.model;

public class CreditCardResponseDTO {
	private String number;
	private boolean valid;
	private String message;

	public CreditCardResponseDTO() {
		super();
	}

	public CreditCardResponseDTO(final String number, final boolean valid, final String message) {
		super();
		this.number = number;
		this.valid = valid;
		this.message = message;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

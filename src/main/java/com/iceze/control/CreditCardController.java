package com.iceze.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iceze.exception.CreditCardException;
import com.iceze.model.CreditCard;
import com.iceze.model.CreditCardResponseDTO;
import com.iceze.service.CreditCardService;


@RestController
@RequestMapping("/api")
public class CreditCardController {
	public static final Logger LOG = LoggerFactory.getLogger(CreditCardController.class);
	
	@Autowired
	@Qualifier("creditCardService")
	private CreditCardService creditCardService;
	
	@RequestMapping(value ="/credit-card/{number}", method = RequestMethod.GET) 
	public ResponseEntity<?> validateCreditCard(@PathVariable("number") final String number,
												@RequestParam(value = "expiry_date", required = true) final String expiryDate) {
		LOG.info("Validating Credit Card, number: {}", number);
		
		CreditCard cd = new CreditCard(number, expiryDate);
		
		try {
			this.creditCardService.validate(cd);
		} catch(CreditCardException e) {
			return new ResponseEntity<CreditCardResponseDTO>(new CreditCardResponseDTO(cd.getNumber(), false, e.getMessage()),
													  		 HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<CreditCardResponseDTO>(new CreditCardResponseDTO(cd.getNumber(), true, "OK"), 
														 HttpStatus.OK);
	}
}

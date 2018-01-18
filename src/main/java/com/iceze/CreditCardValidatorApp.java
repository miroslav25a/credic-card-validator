package com.iceze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Class
 * 
 *@author Miroslav
 */
@SpringBootApplication(scanBasePackages={"com.iceze"})
public class CreditCardValidatorApp 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(CreditCardValidatorApp.class, args);
    }
}

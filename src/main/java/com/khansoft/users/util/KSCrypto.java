package com.khansoft.users.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

@Component
public class KSCrypto {

    private static final Logger logger = LoggerFactory.getLogger(KSCrypto.class);
    private static final SecureRandom secureRandom = new SecureRandom();

    private static final String MISSING_INTERNATIONAL_PREFIX = "International phone number prefix missing";
    private static final String INCORRECT_PHONE_NUMBER = "Missing/incorrect phone number";
    private static final String INCORRECT_CHARACTERS = "Mobile Number Contains Incorrect Characters (+ and digits only, no spaces, dashes or parentheses)";
    private static final String INCORRECT_PHONE_PREFIX = "Incorrect Phone Prefix (doesn't match selected country)";
   

    /**
     * Hashes the plaintext password using BCrypt.
     *
     * @param plainText the password to hash
     * @return the hashed password
     */
    public String passwordHash(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            logger.warn("Attempted to hash an empty or null password.");
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        // The gensalt's log_rounds parameter determines the complexity.
        // Here, 12 is chosen for better security balance.
        return BCrypt.hashpw(plainText, BCrypt.gensalt(12));
    }

    /**
     * Verifies the plaintext password against the given hash.
     *
     * @param plainText the password to verify
     * @param hash      the hash to verify against
     * @return true if the password matches the hash, false otherwise
     */
    public boolean passwordVerify(String plainText, String hash) {
        if (plainText == null || plainText.isEmpty() || hash == null || hash.isEmpty()) {
            logger.warn("Invalid inputs for password verification.");
            throw new IllegalArgumentException("Password and hash cannot be null or empty");
        }
        try {
            return BCrypt.checkpw(plainText, hash);
        } catch (Exception ex) {
            logger.error("Error verifying password: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Generates a random 6-digit token.
     *
     * @return a 6-digit token as a String
     */
    public String generate6DigitToken() {
        int token = secureRandom.nextInt(900000) + 100000; // Generates a number between 100000 and 999999
        return String.valueOf(token);
    }


    public String validatePhoneNumber(String phoneNumber, String countryCode) {
        
        String returnMessage = "valid";

        //System.err.println("phoneNumber:"+phoneNumber);

        // Check if phone number starts with '+'
        if (!phoneNumber.startsWith("+")) {
            returnMessage = MISSING_INTERNATIONAL_PREFIX;
        } 
        // Check if phone number is at least 8 characters long
        else if (phoneNumber.length() < 8) {
            returnMessage = INCORRECT_PHONE_NUMBER;
        } 
        // Check if phone number contains only digits after '+'
        else if (!phoneNumber.substring(1).matches("^[0-9]+$")) {
            returnMessage = INCORRECT_CHARACTERS;
        } 
        // Check the country prefix and remaining phone number
        else {
            if (!"0".equals(countryCode)) {

                // TODO: check country code with prefix
                //Country country = countryService.getCountryByCode(countryCode);
                String country = "231232";
                if (country != null) {
                    System.err.println("Call ok");
                    //String countryPrefix = country.getCountryPrefix();
                    String countryPrefix = "+880";
                    
                    String phonePrefix = phoneNumber.substring(0, countryPrefix.length());
                    //String phonePrefix = "+880";

                    if (!phonePrefix.equals(countryPrefix)) {
                        returnMessage = INCORRECT_PHONE_PREFIX;
                    } else {
                        String phoneStripped = phoneNumber.substring(countryPrefix.length());
                        if (phoneStripped.length() < 6) {
                            returnMessage = INCORRECT_PHONE_NUMBER;
                        }
                    }
                } else {
                    System.err.println("Call not ok");
                    returnMessage = INCORRECT_PHONE_PREFIX;
                }
            } else {
                returnMessage = INCORRECT_PHONE_PREFIX;
            }
        }

        return returnMessage.trim();
    }
}

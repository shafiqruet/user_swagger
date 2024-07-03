/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khansoft.users.util;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khansoft.users.repositories.UsersRepository;

@Service
public class UsersSystem {

    @Autowired
    private UsersRepository userRepository;

    /* public static String generateUniqueCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return code.toString();
    } 
 */

   /*  public  String generateUniqueReferralNumber(int length) {
        System.err.println("length"+length);
        String referenceNo = generate(length);
        while (userRepository.findByReferenceNo(referenceNo)) {
            referenceNo = generate(8);
        }
        return referenceNo.toString();
    } */


    public String generateUniqueReferralNumber(int length) {
        System.err.println("length: " + length);
        String referenceNo = generate(length);
        
        // Keep generating a new reference number until it is unique
        while (userRepository.findByReferenceNo(referenceNo).isPresent()) {
            referenceNo = generate(length);
        }

        return referenceNo; // No need to call toString() on a String
    }


    private String generate(int length) {
        // Generate a random alphanumeric string of the given length
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder refNo = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            refNo.append(characters.charAt(random.nextInt(characters.length())));
        }
        return refNo.toString();
    }
}

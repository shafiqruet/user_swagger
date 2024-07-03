package com.khansoft.users.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khansoft.users.entities.request.TestRequest;

@RequestMapping("/users")
@RestController
public class MyController {


    @PostMapping(value = "/validatePhoneNumber",consumes = "application/json")
    public ResponseEntity<String> validatePhoneNumber(@RequestBody TestRequest testRequest) {
        System.err.println("Shafiq");
        //System.err.println(request.toString());

        /* String phoneNumber = testRequest.getPhone();
        System.err.println(phoneNumber+"phone");
        if (phoneNumber == null) {
            return ResponseEntity.ok("Phone number null");
        } */

        //String countryCode = testRequest.getCountryCode(); // Optional: Check if country code is present
        //System.err.println(countryCode);

        // Implement your phone number validation logic using phoneNumber and countryCode (if needed)

        // ... (Your validation logic here)

        return ResponseEntity.ok("Phone number validated successfully"); // Or appropriate response based on validation
    }
}

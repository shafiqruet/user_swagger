package com.khansoft.users.controllers;
import com.khansoft.users.annotations.ApiController;
import com.khansoft.users.entities.request.TestRequest;
import com.khansoft.users.util.UrlConstraint;
import com.khansoft.users.util.KSCrypto;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RequestMapping(UrlConstraint.HealthCheck.ROOT)
@ApiController
public class HealthCheckController {

    
    private final KSCrypto ksCrypto = new KSCrypto();

    @Autowired
    private MessageSource messageSource;
    /**
     * Home page with welcome message!
     *
     * @return Welcome message
     */
    @GetMapping(UrlConstraint.HealthCheck.STATUS)
    public String index() {
        return "Welcome to Khan Soft user portal!";
    }

    @GetMapping(UrlConstraint.HealthCheck.TEST_CODE)
    public String test_code(@RequestBody TestRequest request) {
        String phone = request.getPhone();
        String countryCode = request.getCountryCode();
        String message =  ksCrypto.validatePhoneNumber(phone, countryCode);
        return message;
    } 


    @GetMapping("/check-lang")
    public String greet(@NonNull @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return messageSource.getMessage("greeting", null, locale);
    }

    @GetMapping("/change-language")
    public String changeLanguage(@RequestParam(name = "lang", required = false) String lang) {
        Locale locale = (lang != null) ? Locale.forLanguageTag(lang) : Locale.getDefault();
        return messageSource.getMessage("greeting", null, locale);
    }

}

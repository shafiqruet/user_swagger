package com.khansoft.users.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentAuthenticatedUserId() {
        UserPrinciple principal = getUserPrincipal();
        if (principal != null) {
            return principal.getId();
        }
        return null;
    }

    private static UserPrinciple getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrinciple) {
            return (UserPrinciple) principal;
        } else {
            System.out.println("Principal is of type: " + principal.getClass().getName());
            return null;
        }
    }
}
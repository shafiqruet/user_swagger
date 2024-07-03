package com.khansoft.users.util;

public final class UrlConstraint {
    private UrlConstraint() {
    }

    private static final String VERSION = "/v1";
    private static final String API = "/api";

    public static class HealthCheck {
        public static final String ROOT = VERSION + API + "/users";
        public static final String STATUS = "/";
        public static final String SWAGGER = "/swagger-ui.html";
        public static final String TEST_CODE = "/test_code";
    }

    public static class AuthManagement {
        public static final String ROOT = VERSION + API + "/users/auth";
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
    }

}
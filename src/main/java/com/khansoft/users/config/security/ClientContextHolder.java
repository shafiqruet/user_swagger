package com.khansoft.users.config.security;

public class ClientContextHolder {

    private static final ThreadLocal<Integer> clientIdHolder = new ThreadLocal<>();

    public static void setClientId(Integer clientId) {
        clientIdHolder.set(clientId);
    }

    public static Integer getClientId() {
        return clientIdHolder.get();
    }

    public static void clear() {
        clientIdHolder.remove();
    }
}
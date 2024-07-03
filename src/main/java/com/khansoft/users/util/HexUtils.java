package com.khansoft.users.util;

public class HexUtils {
    public static String ensureEvenLength(String hexString) {
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString; // Pad with leading zero if length is odd
        }
        return hexString;
    }

    public static boolean isHex(String str) {
        return str.matches("^[0-9a-fA-F]+$");
    }
}

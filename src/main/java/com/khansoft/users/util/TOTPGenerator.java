package com.khansoft.users.util;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.digest.HmacUtils;

import java.nio.ByteBuffer;
import java.time.Instant;

public class TOTPGenerator {
    private static final int TIME_STEP_SECONDS = 30;
    private static final int TOTP_LENGTH = 6;

    public static String generateTOTP(String secret) {
        long time = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;
        byte[] secretBytes = new Base32().decode(secret);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(time);
        byte[] timeBytes = buffer.array();
        byte[] hash = HmacUtils.hmacSha1(secretBytes, timeBytes);
        int offset = hash[hash.length - 1] & 0xF;
        int binary =
                ((hash[offset] & 0x7f) << 24) |
                        ((hash[offset + 1] & 0xff) << 16) |
                        ((hash[offset + 2] & 0xff) << 8) |
                        (hash[offset + 3] & 0xff);
        int otp = binary % (int) Math.pow(10, TOTP_LENGTH);
        return String.format("%0" + TOTP_LENGTH + "d", otp);
    }
}
package com.khansoft.users.config.security;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorException;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import net.glxn.qrgen.QRCode;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class TwoFactorAuthService {
    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public boolean verifyCode(String secretKey, int verificationCode) {
        try {
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            return gAuth.authorize(secretKey, verificationCode);
        } catch (GoogleAuthenticatorException e) {
            return false;
        }
    }

    public String getQRCode(String secretKey, String account) {
        String qrUrl = String.format("otpauth://totp/%s?secret=%s", account, secretKey);
        ByteArrayOutputStream stream = QRCode.from(qrUrl).withSize(250, 250).stream();
        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }

}
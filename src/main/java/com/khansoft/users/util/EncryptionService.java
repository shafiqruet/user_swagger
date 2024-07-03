package com.khansoft.users.util;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final TextEncryptor textEncryptor;

    public EncryptionService(TextEncryptor textEncryptor) {
        this.textEncryptor = textEncryptor;
    }

    public String encrypt(String data) {
        return textEncryptor.encrypt(data);
    }

    public String decrypt(String encryptedData) {
        return textEncryptor.decrypt(encryptedData);
    }
}

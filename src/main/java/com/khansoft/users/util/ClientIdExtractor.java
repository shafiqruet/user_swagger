package com.khansoft.users.util;

import com.khansoft.users.exception.ClientIdNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class ClientIdExtractor {

    private final Logger logger = LoggerFactory.getLogger(ClientIdExtractor.class);
    private final TextEncryptor textEncryptor;

    public ClientIdExtractor(TextEncryptor textEncryptor) {
        this.textEncryptor = textEncryptor;
    }

    public String extractAndDecryptClientId(HttpHeaders headers) {
        String headerValue = headers.getFirst("client-id");
        if (headerValue.isEmpty()) {
            throw new ClientIdNotFoundException("Client Id is missing");
        }

        if (HexUtils.isHex(headerValue)) {
            String hexString = HexUtils.ensureEvenLength(headerValue);
            String decryptedId = textEncryptor.decrypt(hexString);
            logger.info("Decrypted ID  = " + decryptedId);
            return decryptedId;
        } else {
            logger.error("Invalid hex string: " + headerValue);
            throw new IllegalArgumentException("Invalid hex string: " + headerValue);
        }
    }
}
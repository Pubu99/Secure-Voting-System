package com.securevote.secure_voting.security;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class RSAKeyInitializer {

    @PostConstruct
    public void init() {
        try {
            File publicKeyFile = new File(RSAUtil.PUBLIC_KEY_FILE);
            File privateKeyFile = new File(RSAUtil.PRIVATE_KEY_FILE);

            if (!publicKeyFile.exists() || !privateKeyFile.exists()) {
                log.warn("RSA key files not found. Generating new keys...");
                RSAUtil.generateKeyPair();
            } else {
                log.info("RSA key files already exist.");
            }
        } catch (Exception e) {
            log.error("Failed to initialize RSA key pair", e);
            throw new RuntimeException("RSA key initialization failed", e);
        }
    }
}
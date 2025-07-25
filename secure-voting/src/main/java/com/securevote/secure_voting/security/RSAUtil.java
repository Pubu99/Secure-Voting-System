package com.securevote.secure_voting.security;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Slf4j
public class RSAUtil {

    private static final String PUBLIC_KEY_FILE = "public.key";
    private static final String PRIVATE_KEY_FILE = "private.key";

    public static void generateKeyPair() {
        try {
            log.info("Generating RSA key pair...");
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();

            // Save keys to file
            saveKey(PUBLIC_KEY_FILE, pair.getPublic().getEncoded());
            saveKey(PRIVATE_KEY_FILE, pair.getPrivate().getEncoded());

            log.info("RSA key pair generated and saved.");
        } catch (Exception e) {
            log.error("Error generating RSA key pair", e);
        }
    }

    private static void saveKey(String filename, byte[] keyBytes) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(keyBytes);
        }
    }

    public static PublicKey getPublicKey() throws Exception {
        byte[] bytes = Files.readAllBytes(new File(PUBLIC_KEY_FILE).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        log.info("Public key loaded from file");
        return factory.generatePublic(spec);
    }

    public static PrivateKey getPrivateKey() throws Exception {
        byte[] bytes = Files.readAllBytes(new File(PRIVATE_KEY_FILE).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        log.info("Private key loaded from file");
        return factory.generatePrivate(spec);
    }

    public static String decrypt(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

}

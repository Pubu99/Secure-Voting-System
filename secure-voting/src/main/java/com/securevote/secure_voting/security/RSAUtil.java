package com.securevote.secure_voting.security;

import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    public static final String PUBLIC_KEY_FILE = "public.key";
    public static final String PRIVATE_KEY_FILE = "private.key";

    public static void generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        // Save public key
        Files.write(Paths.get(PUBLIC_KEY_FILE), pair.getPublic().getEncoded());

        // Save private key
        Files.write(Paths.get(PRIVATE_KEY_FILE), pair.getPrivate().getEncoded());
    }

    public static PublicKey getPublicKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static PrivateKey getPrivateKey() throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static String decrypt(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
}
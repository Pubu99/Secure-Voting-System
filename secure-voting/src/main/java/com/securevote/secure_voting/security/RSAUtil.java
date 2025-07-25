package com.securevote.secure_voting.security;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

public class RSAUtil {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }
}

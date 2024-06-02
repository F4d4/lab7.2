package com.f4d4.server.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class PasswordHashing {

    private static final SecureRandom random = new SecureRandom();

    public  static String generateSalt() {
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getUrlEncoder().encodeToString(saltBytes);
    }

    public  static String hashPasswordWithSalt(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            digest.update(salt.getBytes());
            digest.update(password.getBytes());
            byte[] hashBytes = digest.digest();
            return Base64.getUrlEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

}

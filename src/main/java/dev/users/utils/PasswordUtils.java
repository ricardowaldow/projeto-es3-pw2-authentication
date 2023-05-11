package dev.users.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 256;

    public static String encrypt(String password) {
        byte[] salt = generateSalt();
        byte[] hash = hash(password, salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean checkPassword(String password, String encryptedPassword) {
        String[] parts = encryptedPassword.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);
        byte[] testHash = hash(password, salt);
        return MessageDigest.isEqual(hash, testHash);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] hash(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);
            byte[] input = digest.digest(password.getBytes());
            for (int i = 0; i < ITERATIONS - 1; i++) {
                digest.reset();
                input = digest.digest(input);
            }
            byte[] hash = new byte[KEY_LENGTH / 8];
            System.arraycopy(input, 0, hash, 0, hash.length);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

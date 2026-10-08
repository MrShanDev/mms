package com.sxpcwlkj.framework.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Versioned password hashes; legacy MMS MD5 is accepted only for migration. */
public final class PasswordHashUtil {
    private static final int ITERATIONS = 600_000;
    private static final SecureRandom RANDOM = new SecureRandom();
    private PasswordHashUtil() { }

    public static String hash(String password) {
        validate(password);
        return encode(password);
    }

    /** Only after successful legacy authentication; preserve existing 6–7 character accounts. */
    public static String migrateVerifiedLegacy(String password) {
        if (password == null || password.length() < 6 || password.length() > 128)
            throw new IllegalArgumentException("Invalid legacy password length");
        return encode(password);
    }

    private static String encode(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return "pbkdf2-sha256$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(derive(password, salt, ITERATIONS));
    }

    public static boolean matches(String password, String stored, String legacySalt) {
        if (password == null || stored == null || password.length() > 128) return false;
        if (!stored.startsWith("pbkdf2-sha256$")) {
            if (legacySalt == null || !stored.matches("[0-9a-fA-F]{32}")) return false;
            return MessageDigest.isEqual(stored.toLowerCase().getBytes(StandardCharsets.UTF_8),
                    SignUtil.pressWord(password, legacySalt).getBytes(StandardCharsets.UTF_8));
        }
        try {
            String[] parts = stored.split("\\$", -1);
            if (parts.length != 4) return false;
            int iterations = Integer.parseInt(parts[1]);
            if (iterations < 100_000 || iterations > 2_000_000) return false;
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expected = Base64.getDecoder().decode(parts[3]);
            return salt.length == 16 && expected.length == 32
                    && MessageDigest.isEqual(expected, derive(password, salt, iterations));
        } catch (IllegalArgumentException e) { return false; }
    }

    public static boolean isLegacy(String stored) {
        return stored != null && stored.matches("[0-9a-fA-F]{32}");
    }

    private static void validate(String password) {
        if (password == null || password.length() < 8 || password.length() > 128)
            throw new com.sxpcwlkj.common.exception.MmsException("密码长度必须在8至128个字符之间");
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (java.security.GeneralSecurityException e) {
            throw new IllegalStateException("Password hash unavailable", e);
        } finally { spec.clearPassword(); }
    }
}

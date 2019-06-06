package app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {

    public static String hash(final CharSequence password) {
        return hash(String.valueOf(password));
    }

    public static String hash(final String password) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");

            digest.update(password.getBytes());
            final String encryptedString = new String(bytesToHex(digest.digest()));

            return encryptedString;
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalArgumentException(exception.getMessage(), exception);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

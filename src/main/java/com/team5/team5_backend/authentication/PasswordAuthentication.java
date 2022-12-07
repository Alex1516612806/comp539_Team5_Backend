package com.team5.team5_backend.authentication;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordAuthentication {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";
    private static final int SIZE = 128;
    private static final int SALT_SIZE = SIZE/8;
    private final SecureRandom random;
    public static final int DEFAULT_COST = 16;


    public PasswordAuthentication()
    {
        iterations(DEFAULT_COST); // Validate cost
        this.random = new SecureRandom();
    }


    // cost validation
    private static int iterations(int cost)
    {
        if ((cost < 0) || (cost > 30))
            throw new IllegalArgumentException("cost: " + cost);
        return 1 << cost;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations)
    {
        KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            return f.generateSecret(spec).getEncoded();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
        }
        catch (InvalidKeySpecException ex) {
            throw new IllegalStateException("Invalid SecretKeyFactory", ex);
        }
    }

    public List<String> hash(char[] password)
    {
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);
        byte[] hash = pbkdf2(password, salt, iterations(DEFAULT_COST));
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        List<String> result=new ArrayList<>();
        result.add(enc.encodeToString(salt));
        result.add(enc.encodeToString(hash));
        return result;
    }

    public static boolean validatePassword(String password) {
    /*
        Not give any restrictions for password!!
        But generally invalid passwords match:
        Anything with less than eight characters
        OR anything with no numbers
        OR anything with no uppercase
        OR anything with no lowercase
        OR anything with no special characters
    */
        return true;
    }

    public boolean authenticate(char[] inputPassword, String salt, String hash) {
        byte[] saltBytes=Base64.getUrlDecoder().decode(salt);
        byte[] hashBytes=Base64.getUrlDecoder().decode(hash);
        byte[] checkBytes = pbkdf2(inputPassword, saltBytes, iterations(DEFAULT_COST));
        int zero = 0;
        for (int idx = 0; idx < checkBytes.length; ++idx)
            zero |= hashBytes[idx] ^ checkBytes[idx];
        return zero == 0;
    }
}

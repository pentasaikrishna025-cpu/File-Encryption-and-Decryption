package crypto;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class CryptoService {

    private static final String AES = "AES";
    private static final String CHACHA = "ChaCha20";
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 65536;

    private static SecretKey generateKey(String password, byte[] salt, String algorithm) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), algorithm.equals("AES") ? AES : CHACHA);
    }

    public static byte[] encrypt(byte[] data, String password, String algorithm) throws Exception {
        byte[] salt = SecureRandom.getSeed(16);
        SecretKey key = generateKey(password, salt, algorithm);

        Cipher cipher;

        if (algorithm.equals("AES")) {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else {
            cipher = Cipher.getInstance("ChaCha20");
            byte[] nonce = SecureRandom.getSeed(12);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(nonce));
        }

        byte[] encrypted = cipher.doFinal(data);

        byte[] output = new byte[salt.length + encrypted.length];
        System.arraycopy(salt, 0, output, 0, salt.length);
        System.arraycopy(encrypted, 0, output, salt.length, encrypted.length);

        return output;
    }

    public static byte[] decrypt(byte[] data, String password, String algorithm) throws Exception {
        byte[] salt = Arrays.copyOfRange(data, 0, 16);
        byte[] encrypted = Arrays.copyOfRange(data, 16, data.length);

        SecretKey key = generateKey(password, salt, algorithm);

        Cipher cipher;

        if (algorithm.equals("AES")) {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
        } else {
            cipher = Cipher.getInstance("ChaCha20");
            cipher.init(Cipher.DECRYPT_MODE, key);
        }

        return cipher.doFinal(encrypted);
    }
}

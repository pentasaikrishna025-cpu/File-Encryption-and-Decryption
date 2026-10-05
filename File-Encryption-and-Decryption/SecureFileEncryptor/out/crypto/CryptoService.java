package crypto;

import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoService {
   private static final String AES = "AES";
   private static final String CHACHA = "ChaCha20";
   private static final int KEY_SIZE = 256;
   private static final int ITERATIONS = 65536;

   private static SecretKey generateKey(String var0, byte[] var1, String var2) throws Exception {
      SecretKeyFactory var3 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
      PBEKeySpec var4 = new PBEKeySpec(var0.toCharArray(), var1, 65536, 256);
      SecretKey var5 = var3.generateSecret(var4);
      return new SecretKeySpec(var5.getEncoded(), var2.equals("AES") ? "AES" : "ChaCha20");
   }

   public static byte[] encrypt(byte[] var0, String var1, String var2) throws Exception {
      byte[] var3 = SecureRandom.getSeed(16);
      SecretKey var4 = generateKey(var1, var3, var2);
      Cipher var5;
      byte[] var6;
      if (var2.equals("AES")) {
         var5 = Cipher.getInstance("AES");
         var5.init(1, var4);
      } else {
         var5 = Cipher.getInstance("ChaCha20");
         var6 = SecureRandom.getSeed(12);
         var5.init(1, var4, new IvParameterSpec(var6));
      }

      var6 = var5.doFinal(var0);
      byte[] var7 = new byte[var3.length + var6.length];
      System.arraycopy(var3, 0, var7, 0, var3.length);
      System.arraycopy(var6, 0, var7, var3.length, var6.length);
      return var7;
   }

   public static byte[] decrypt(byte[] var0, String var1, String var2) throws Exception {
      byte[] var3 = Arrays.copyOfRange(var0, 0, 16);
      byte[] var4 = Arrays.copyOfRange(var0, 16, var0.length);
      SecretKey var5 = generateKey(var1, var3, var2);
      Cipher var6;
      if (var2.equals("AES")) {
         var6 = Cipher.getInstance("AES");
         var6.init(2, var5);
      } else {
         var6 = Cipher.getInstance("ChaCha20");
         var6.init(2, var5);
      }

      return var6.doFinal(var4);
   }
}

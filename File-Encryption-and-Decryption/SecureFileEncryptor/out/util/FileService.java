package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {
   public static byte[] readFile(String var0) throws IOException {
      FileInputStream var1 = new FileInputStream(var0);
      byte[] var2 = var1.readAllBytes();
      var1.close();
      return var2;
   }

   public static void writeFile(String var0, byte[] var1) throws IOException {
      FileOutputStream var2 = new FileOutputStream(var0);
      var2.write(var1);
      var2.close();
   }
}

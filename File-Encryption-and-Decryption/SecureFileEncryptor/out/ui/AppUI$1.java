package ui;

import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;
import java.util.Objects;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

class AppUI$1 extends TransferHandler {
   // $FF: synthetic field
   final JTextField val$field;
   // $FF: synthetic field
   final AppUI this$0;

   AppUI$1(final AppUI param1, final JTextField param2) {
      super();
      Objects.requireNonNull(param1);
      this.this$0 = param1;
      this.val$field = param2;
   }

   public boolean canImport(TransferSupport var1) {
      return var1.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
   }

   public boolean importData(TransferSupport var1) {
      try {
         List var2 = (List)var1.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
         if (!var2.isEmpty()) {
            this.val$field.setText(((File)var2.get(0)).getAbsolutePath());
            return true;
         }
      } catch (Exception var3) {
      }

      return false;
   }
}

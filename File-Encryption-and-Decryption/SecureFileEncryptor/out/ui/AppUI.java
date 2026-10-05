package ui;

import crypto.CryptoService;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import util.FileService;

public class AppUI extends JFrame {
   private JTextField fileField;
   private JPasswordField passwordField;
   private JComboBox<String> algorithmBox;

   public AppUI() {
      this.setTitle("Secure File Encryptor");
      this.setSize(550, 300);
      this.setLocationRelativeTo((Component)null);
      this.setDefaultCloseOperation(3);
      UIManager.put("Button.font", new Font("Arial", 1, 14));
      UIManager.put("Label.font", new Font("Arial", 0, 14));
      UIManager.put("TextField.font", new Font("Arial", 0, 14));
      this.initUI();
      this.setVisible(true);
   }

   private void initUI() {
      JPanel var1 = new JPanel();
      var1.setLayout(new GridBagLayout());
      var1.setBackground(new Color(245, 247, 250));
      GridBagConstraints var2 = new GridBagConstraints();
      var2.insets = new Insets(10, 10, 10, 10);
      var2.gridx = 0;
      var2.gridy = 0;
      var1.add(new JLabel("File:"), var2);
      this.fileField = new JTextField(25);
      this.enableDragAndDrop(this.fileField);
      var2.gridx = 1;
      var1.add(this.fileField, var2);
      JButton var3 = new JButton("Browse");
      var2.gridx = 2;
      var1.add(var3, var2);
      var3.addActionListener((var1x) -> {
         this.chooseFile();
      });
      var2.gridx = 0;
      var2.gridy = 1;
      var1.add(new JLabel("Password:"), var2);
      this.passwordField = new JPasswordField(25);
      var2.gridx = 1;
      var1.add(this.passwordField, var2);
      var2.gridx = 0;
      var2.gridy = 2;
      var1.add(new JLabel("Algorithm:"), var2);
      this.algorithmBox = new JComboBox(new String[]{"AES", "ChaCha20"});
      var2.gridx = 1;
      var1.add(this.algorithmBox, var2);
      JButton var4 = new JButton("Encrypt");
      var4.setBackground(new Color(0, 123, 255));
      var4.setForeground(Color.WHITE);
      JButton var5 = new JButton("Decrypt");
      var5.setBackground(new Color(40, 167, 69));
      var5.setForeground(Color.WHITE);
      var2.gridx = 0;
      var2.gridy = 3;
      var1.add(var4, var2);
      var2.gridx = 1;
      var1.add(var5, var2);
      var4.addActionListener((var1x) -> {
         this.processFile(true);
      });
      var5.addActionListener((var1x) -> {
         this.processFile(false);
      });
      this.add(var1);
   }

   private void chooseFile() {
      JFileChooser var1 = new JFileChooser();
      if (var1.showOpenDialog(this) == 0) {
         this.fileField.setText(var1.getSelectedFile().getAbsolutePath());
      }

   }

   void processFile(boolean var1) {
      try {
         String var2 = this.fileField.getText();
         String var3 = new String(this.passwordField.getPassword());
         String var4 = (String)this.algorithmBox.getSelectedItem();
         if (var2.isEmpty() || var3.isEmpty()) {
            JOptionPane.showMessageDialog(this, "File and Password required!");
            return;
         }

         byte[] var5 = FileService.readFile(var2);
         byte[] var6;
         if (var1) {
            var6 = CryptoService.encrypt(var5, var3, var4);
            FileService.writeFile(var2 + ".enc", var6);
            JOptionPane.showMessageDialog(this, "File Encrypted!");
         } else {
            var6 = CryptoService.decrypt(var5, var3, var4);
            FileService.writeFile(var2 + ".dec", var6);
            JOptionPane.showMessageDialog(this, "File Decrypted!");
         }
      } catch (Exception var7) {
         JOptionPane.showMessageDialog(this, "Wrong Password or Error!");
      }

   }

   private void enableDragAndDrop(JTextField var1) {
      var1.setTransferHandler(new AppUI$1(this, var1));
   }
}

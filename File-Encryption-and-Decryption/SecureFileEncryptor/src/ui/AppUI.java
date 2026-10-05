package ui;

import crypto.CryptoService;
import util.FileService;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

public class AppUI extends JFrame {

    private JTextField fileField;
    private JPasswordField passwordField;
    private JComboBox<String> algorithmBox;

    public AppUI() {
        setTitle("Secure File Encryptor");
        setSize(550, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 14));

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // File field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("File:"), gbc);

        fileField = new JTextField(25);
        enableDragAndDrop(fileField);

        gbc.gridx = 1;
        panel.add(fileField, gbc);

        JButton browseBtn = new JButton("Browse");
        gbc.gridx = 2;
        panel.add(browseBtn, gbc);

        browseBtn.addActionListener(e -> chooseFile());

        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(25);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Algorithm selection
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Algorithm:"), gbc);

        algorithmBox = new JComboBox<>(new String[]{"AES", "ChaCha20"});
        gbc.gridx = 1;
        panel.add(algorithmBox, gbc);

        // Buttons
        JButton encryptBtn = new JButton("Encrypt");
        encryptBtn.setBackground(new Color(0, 123, 255));
        encryptBtn.setForeground(Color.WHITE);

        JButton decryptBtn = new JButton("Decrypt");
        decryptBtn.setBackground(new Color(40, 167, 69));
        decryptBtn.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(encryptBtn, gbc);

        gbc.gridx = 1;
        panel.add(decryptBtn, gbc);

        encryptBtn.addActionListener(e -> processFile(true));
        decryptBtn.addActionListener(e -> processFile(false));

        add(panel);
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void processFile(boolean encrypt) {
        try {
            String filePath = fileField.getText();
            String password = new String(passwordField.getPassword());
            String algorithm = (String) algorithmBox.getSelectedItem();

            if (filePath.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "File and Password required!");
                return;
            }

            byte[] fileData = FileService.readFile(filePath);
            byte[] result;

            if (encrypt) {
                result = CryptoService.encrypt(fileData, password, algorithm);
                FileService.writeFile(filePath + ".enc", result);
                JOptionPane.showMessageDialog(this, "File Encrypted!");
            } else {
                result = CryptoService.decrypt(fileData, password, algorithm);
                FileService.writeFile(filePath + ".dec", result);
                JOptionPane.showMessageDialog(this, "File Decrypted!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Wrong Password or Error!");
        }
    }

    private void enableDragAndDrop(JTextField field) {
        field.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    List<File> files = (List<File>) support.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        field.setText(files.get(0).getAbsolutePath());
                        return true;
                    }
                } catch (Exception ignored) {}
                return false;
            }
        });
    }
}
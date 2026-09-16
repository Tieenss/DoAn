package TienIch;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ComboBoxUtil {

    public static void makeSearchableAndEditable(JComboBox comboBox) {
        comboBox.setEditable(true);
        Component editor = comboBox.getEditor().getEditorComponent();

        if (editor instanceof JTextComponent) {
            JTextComponent textField = (JTextComponent) editor;

            textField.addKeyListener(new KeyAdapter() {
                private List<Object> originalItems = null;

                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    
                    // Bỏ qua các phím điều hướng và Enter/Escape
                    if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN ||
                        keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT ||
                        keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE) {
                        return;
                    }

                    // Lưu trữ danh sách gốc (chỉ chạy 1 lần hoặc khi dữ liệu bị ghi đè/load lại)
                    if (originalItems == null || comboBox.getItemCount() > originalItems.size()) {
                        originalItems = new ArrayList<>();
                        for (int i = 0; i < comboBox.getItemCount(); i++) {
                            originalItems.add(comboBox.getItemAt(i));
                        }
                    }

                    String text = textField.getText();
                    
                    SwingUtilities.invokeLater(() -> {
                        comboBox.hidePopup();
                        comboBox.removeAllItems();
                        
                        if (text.isEmpty()) {
                            for (Object item : originalItems) {
                                comboBox.addItem(item);
                            }
                        } else {
                            for (Object item : originalItems) {
                                if (item != null && item.toString().toLowerCase().contains(text.toLowerCase())) {
                                    comboBox.addItem(item);
                                }
                            }
                        }
                        
                        textField.setText(text);
                        if (comboBox.getItemCount() > 0) {
                            comboBox.showPopup();
                        }
                    });
                }
            });
        }
    }
}

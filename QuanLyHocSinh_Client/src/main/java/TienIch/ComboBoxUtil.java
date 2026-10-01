package TienIch;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ComboBoxUtil {

    // Key dùng để lưu danh sách gốc của ComboBox
    private static final String ORIGINAL_ITEMS_KEY = "ComboBoxUtil.originalItems";
    private static final String IS_FILTERING_KEY = "ComboBoxUtil.isFiltering";

    public static void makeSearchableAndEditable(JComboBox comboBox) {
        makeSearchableAndEditable(comboBox, false);
    }

    public static void makeSearchableAndEditable(JComboBox comboBox, boolean allowCustomInput) {
        comboBox.setEditable(true);

        // Lưu danh sách hiện tại làm danh sách gốc
        refreshOriginalItems(comboBox);

        // Đăng ký listener tự động cập nhật danh sách gốc khi Model của ComboBox thay đổi dữ liệu
        comboBox.getModel().addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                onModelChanged();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                onModelChanged();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                onModelChanged();
            }

            private void onModelChanged() {
                Boolean isFiltering = (Boolean) comboBox.getClientProperty(IS_FILTERING_KEY);
                if (isFiltering == null || !isFiltering) {
                    refreshOriginalItems(comboBox);
                }
            }
        });

        Component editor = comboBox.getEditor().getEditorComponent();

        if (editor instanceof JTextComponent) {
            JTextComponent textField = (JTextComponent) editor;
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    // Bỏ qua các phím điều hướng
                    if (keyCode == KeyEvent.VK_UP
                            || keyCode == KeyEvent.VK_DOWN
                            || keyCode == KeyEvent.VK_LEFT
                            || keyCode == KeyEvent.VK_RIGHT
                            || keyCode == KeyEvent.VK_ENTER
                            || keyCode == KeyEvent.VK_ESCAPE) {
                        return;
                    }

                    String text = textField.getText();

                    SwingUtilities.invokeLater(() -> {
                        @SuppressWarnings("unchecked")
                        List<Object> originalItems = (List<Object>)
                                comboBox.getClientProperty(
                                        ORIGINAL_ITEMS_KEY
                                );
                        if (originalItems == null) {
                            return;
                        }

                        // Đánh dấu đang lọc để ListDataListener không đè danh sách gốc
                        comboBox.putClientProperty(IS_FILTERING_KEY, Boolean.TRUE);
                        try {
                            comboBox.hidePopup();
                            comboBox.removeAllItems();

                            /*
                             * Nếu xóa hết nội dung tìm kiếm
                             * thì khôi phục toàn bộ danh sách gốc.
                             */
                            if (text.trim().isEmpty()) {
                                for (Object item : originalItems) {
                                    comboBox.addItem(item);
                                }
                                textField.setText("");
                            } else {
                                /*
                                 * Lọc từ danh sách gốc,
                                 * KHÔNG lọc từ danh sách hiện tại.
                                 */
                                String keyword = text.toLowerCase().trim();
                                for (Object item : originalItems) {
                                    if (item != null
                                            && item.toString()
                                            .toLowerCase()
                                            .contains(keyword)) {
                                        comboBox.addItem(item);
                                    }
                                }
                                textField.setText(text);
                                if (comboBox.getItemCount() > 0) {
                                    comboBox.showPopup();
                                }
                            }
                        } finally {
                            comboBox.putClientProperty(IS_FILTERING_KEY, Boolean.FALSE);
                        }
                    });
                }
            });

            /*
             * Khi mất focus:
             * Nếu cho phép nhập mới (allowCustomInput = true): giữ lại giá trị vừa gõ.
             * Nếu không cho phép: xóa về null.
             */
            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    String text = textField.getText().trim();
                    if (text.isEmpty()) {
                        return;
                    }
                    @SuppressWarnings("unchecked")
                    List<Object> originalItems = (List<Object>)
                            comboBox.getClientProperty(
                                    ORIGINAL_ITEMS_KEY
                            );
                    if (originalItems == null) {
                        return;
                    }

                    boolean exists = false;

                    for (Object item : originalItems) {
                        if (item != null && item.toString().equalsIgnoreCase(text)) {
                            exists = true;
                            comboBox.setSelectedItem(item);
                            break;
                        }
                    }

                    if (!exists) {
                        if (allowCustomInput) {
                            // Giữ lại năm học/giá trị mới vừa gõ
                            originalItems.add(text);
                            comboBox.addItem(text);
                            comboBox.setSelectedItem(text);
                        } else {
                            comboBox.setSelectedItem(null);
                            textField.setText("");
                        }
                    }
                }
            });
        }
    }

    /**
     * Cập nhật danh sách gốc của ComboBox.
     */
    public static void refreshOriginalItems(JComboBox comboBox) {

        List<Object> originalItems = new ArrayList<>();

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            originalItems.add(comboBox.getItemAt(i));
        }

        comboBox.putClientProperty(
                ORIGINAL_ITEMS_KEY,
                originalItems
        );
    }
}

package TienIch;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Hộp thoại cảnh báo mức độ nguy hiểm 2 bước khi Sửa hoặc Xóa dữ liệu quan trọng.
 * - Bước 1: Xác nhận ý định thao tác.
 * - Bước 2: Hiển thị giao diện cảnh báo nguy cơ cao, phân tích các chức năng/kết quả
 *           bị ảnh hưởng dây chuyền, so sánh dữ liệu trước/sau và yêu cầu cam kết.
 */
public class DangerConfirmDialog extends JDialog {

    private boolean confirmed = false;

    private DangerConfirmDialog(Window owner, String title, String entityInfo,
                                List<String[]> changes, List<String> impacts, boolean isDelete) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        initComponents(entityInfo, changes, impacts, isDelete);
    }

    private void initComponents(String entityInfo, List<String[]> changes, List<String> impacts, boolean isDelete) {
        setSize(650, 520);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // 1. Header Banner Cảnh báo
        JPanel pnlHeader = new JPanel(new BorderLayout());
        Color headerBg = isDelete ? new Color(211, 47, 47) : new Color(230, 81, 0);
        pnlHeader.setBackground(headerBg);
        pnlHeader.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel lblWarningTitle = new JLabel(isDelete ? "⚠️ CẢNH BÁO MỨC ĐỘ NGUY HIỂM CAO: THAO TÁC XÓA DỮ LIỆU"
                : "⚠️ CẢNH BÁO MỨC ĐỘ NGUY HIỂM CAO: THAO TÁC SỬA ĐỔI DỮ LIỆU");
        lblWarningTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblWarningTitle.setForeground(Color.WHITE);
        lblWarningTitle.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        pnlHeader.add(lblWarningTitle, BorderLayout.NORTH);

        JLabel lblSub = new JLabel("Thao tác này có thể ảnh hưởng trực tiếp đến các tính toán và chức năng khác trong hệ thống!");
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSub.setForeground(new Color(255, 235, 238));
        pnlHeader.add(lblSub, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Nội dung chính (Center)
        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Thông tin bản ghi
        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblInfoTitle = new JLabel("Đối tượng thao tác: ");
        lblInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel lblInfoVal = new JLabel(entityInfo);
        lblInfoVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfoVal.setForeground(new Color(0, 102, 204));
        pnlInfo.add(lblInfoTitle);
        pnlInfo.add(lblInfoVal);
        pnlCenter.add(pnlInfo);

        // Bảng so sánh hoặc chi tiết xóa
        if (!isDelete && changes != null && !changes.isEmpty()) {
            JPanel pnlChanges = new JPanel(new BorderLayout());
            pnlChanges.setBorder(new TitledBorder("Chi tiết thay đổi dữ liệu (Cũ vs Mới):"));
            String[] cols = {"Thông tin", "Giá trị hiện tại (Cũ)", "Giá trị cập nhật (Mới)"};
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (String[] row : changes) {
                model.addRow(row);
            }
            JTable tblChanges = new JTable(model);
            tblChanges.setRowHeight(24);
            tblChanges.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tblChanges.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

            // Màu cho cột mới
            tblChanges.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setForeground(new Color(198, 40, 40));
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    return c;
                }
            });

            JScrollPane spTable = new JScrollPane(tblChanges);
            spTable.setPreferredSize(new Dimension(600, 110));
            pnlChanges.add(spTable, BorderLayout.CENTER);
            pnlCenter.add(pnlChanges);
        } else if (isDelete) {
            JPanel pnlDeleteDetail = new JPanel(new BorderLayout());
            pnlDeleteDetail.setBorder(new TitledBorder("Cảnh báo dữ liệu bị xóa:"));
            JTextArea txtDetail = new JTextArea("Toàn bộ thông tin của bản ghi này sẽ bị XÓA VĨNH VIỄN khỏi cơ sở dữ liệu và KHÔNG THỂ KHÔI PHỤC tự động!\n" + entityInfo);
            txtDetail.setWrapStyleWord(true);
            txtDetail.setLineWrap(true);
            txtDetail.setEditable(false);
            txtDetail.setBackground(new Color(255, 243, 224));
            txtDetail.setForeground(new Color(191, 54, 12));
            txtDetail.setFont(new Font("Segoe UI", Font.BOLD, 13));
            txtDetail.setBorder(new EmptyBorder(8, 8, 8, 8));
            pnlDeleteDetail.add(txtDetail, BorderLayout.CENTER);
            pnlCenter.add(pnlDeleteDetail);
        }

        pnlCenter.add(Box.createVerticalStrut(8));

        // Khung danh sách các tác động / chức năng bị ảnh hưởng
        JPanel pnlImpacts = new JPanel(new BorderLayout());
        pnlImpacts.setBorder(new TitledBorder("Các chức năng và kết quả bị ảnh hưởng dây chuyền:"));
        pnlImpacts.setBackground(new Color(255, 253, 231));

        StringBuilder sb = new StringBuilder();
        if (impacts != null) {
            for (int i = 0; i < impacts.size(); i++) {
                sb.append(String.format("  [%d] %s\n", i + 1, impacts.get(i)));
            }
        }
        JTextArea txtImpacts = new JTextArea(sb.toString());
        txtImpacts.setEditable(false);
        txtImpacts.setBackground(new Color(255, 253, 231));
        txtImpacts.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtImpacts.setForeground(new Color(66, 66, 66));
        txtImpacts.setBorder(new EmptyBorder(6, 8, 6, 8));
        pnlImpacts.add(new JScrollPane(txtImpacts), BorderLayout.CENTER);
        pnlImpacts.setPreferredSize(new Dimension(600, 110));
        pnlCenter.add(pnlImpacts);

        add(pnlCenter, BorderLayout.CENTER);

        // 3. Footer: Checkbox xác nhận & Nút bấm
        JPanel pnlSouth = new JPanel(new BorderLayout(5, 5));
        pnlSouth.setBorder(new EmptyBorder(5, 15, 12, 15));

        JCheckBox chkUnderstand = new JCheckBox("Tôi đã đọc kỹ các cảnh báo nguy hiểm trên và đồng ý chịu trách nhiệm khi thực hiện.");
        chkUnderstand.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkUnderstand.setForeground(new Color(183, 28, 28));
        pnlSouth.add(chkUnderstand, BorderLayout.NORTH);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));

        JButton btnCancel = new JButton("Hủy bỏ thao tác");
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setPreferredSize(new Dimension(140, 36));
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnConfirm = new JButton(isDelete ? "XÁC NHẬN XÓA (BƯỚC 2)" : "XÁC NHẬN LƯU (BƯỚC 2)");
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnConfirm.setBackground(isDelete ? new Color(211, 47, 47) : new Color(230, 81, 0));
        btnConfirm.setForeground(Color.RED);
        btnConfirm.setPreferredSize(new Dimension(200, 36));
        btnConfirm.setEnabled(false); // Chỉ bật khi tích checkbox

        chkUnderstand.addActionListener(e -> btnConfirm.setEnabled(chkUnderstand.isSelected()));

        btnConfirm.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        pnlButtons.add(btnCancel);
        pnlButtons.add(btnConfirm);
        pnlSouth.add(pnlButtons, BorderLayout.SOUTH);

        add(pnlSouth, BorderLayout.SOUTH);
    }

    /**
     * Quy trình xác nhận 2 bước khi SỬA dữ liệu.
     * @return true nếu người dùng đã đồng ý qua cả 2 bước.
     */
    public static boolean showUpdateConfirmation(Component parent, String title, String entityInfo,
                                                 List<String[]> changes, List<String> impacts) {
        // BƯỚC 1: Xác nhận ban đầu
        int step1 = JOptionPane.showConfirmDialog(
                parent,
                "Bạn đang thực hiện thao tác SỬA DỮ LIỆU QUAN TRỌNG cho:\n" + entityInfo +
                        "\n\nBạn có muốn tiếp tục tới bước kiểm tra rủi ro (Bước 1/2)?",
                "Xác nhận cập nhật dữ liệu (Bước 1/2)",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (step1 != JOptionPane.YES_OPTION) {
            return false;
        }

        // BƯỚC 2: Cảnh báo mức độ nguy hiểm & phân tích tác động
        Window owner = SwingUtilities.getWindowAncestor(parent);
        DangerConfirmDialog dialog = new DangerConfirmDialog(owner, title, entityInfo, changes, impacts, false);
        dialog.setVisible(true);
        return dialog.confirmed;
    }

    /**
     * Quy trình xác nhận 2 bước khi XÓA dữ liệu.
     * @return true nếu người dùng đã đồng ý qua cả 2 bước.
     */
    public static boolean showDeleteConfirmation(Component parent, String title, String entityInfo,
                                                 List<String> impacts) {
        // BƯỚC 1: Xác nhận ban đầu
        int step1 = JOptionPane.showConfirmDialog(
                parent,
                "Bạn đang chuẩn bị XÓA DỮ LIỆU sau:\n" + entityInfo +
                        "\n\nBạn có chắc chắn muốn tiếp tục tới bước xác nhận nguy hiểm (Bước 1/2)?",
                "Xác nhận xóa dữ liệu (Bước 1/2)",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (step1 != JOptionPane.YES_OPTION) {
            return false;
        }

        // BƯỚC 2: Cảnh báo mức độ nguy hiểm & cam kết xóa
        Window owner = SwingUtilities.getWindowAncestor(parent);
        DangerConfirmDialog dialog = new DangerConfirmDialog(owner, title, entityInfo, null, impacts, true);
        dialog.setVisible(true);
        return dialog.confirmed;
    }
}

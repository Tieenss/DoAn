package View.Dai;

import Controller.Dai.DoiTuongUuTienController;
import Model.DoiTuongUuTien;
import TienIch.ButtonStyleHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import TienIch.TableSortHelper;

public class QuanLyDoiTuongUuTienPanel extends JPanel {

    private DoiTuongUuTienController controller = new DoiTuongUuTienController();

    private JTable tableDT;
    private DefaultTableModel tableModel;

    private JTextField txtMaDT, txtTenDT, txtTiLeGiam;

    private JTextField txtTimKiem;
    private JButton btnTim, btnHienThiTatCa;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy;
    
    private boolean isThem = false;

    public QuanLyDoiTuongUuTienPanel() {
        initComponents();
        controller.loadTable(tableModel);
        setFormEnabled(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new GridLayout(2, 1, 5, 5));

        JLabel lblTitle = new JLabel("QUẢN LÝ ĐỐI TƯỢNG ƯU TIÊN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm"));

        pnlSearch.add(new JLabel("Từ khóa:"));
        txtTimKiem = new JTextField(20);
        pnlSearch.add(txtTimKiem);

        btnTim = new JButton("Tìm");
        btnHienThiTatCa = new JButton("Hiển thị tất cả");

        ButtonStyleHelper.styleButtonSearch(btnTim);
        ButtonStyleHelper.styleButtonView(btnHienThiTatCa);

        pnlSearch.add(btnTim);
        pnlSearch.add(btnHienThiTatCa);

        pnlNorth.add(pnlSearch);
        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"Mã Đối Tượng", "Tên Đối Tượng", "Tỉ Lệ Giảm (%)"};
        tableModel = new DefaultTableModel(cols, 0);
        tableDT = new JTable(tableModel);
        TableSortHelper.enableTableSorting(tableDT);
        tableDT.setRowHeight(25);
        tableDT.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDT.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        tableDT.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doDuLieuVaoForm();
            }
        });

        add(new JScrollPane(tableDT), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thông tin đối tượng ưu tiên"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlInput.add(new JLabel("Mã Đối Tượng:"), gbc);
        gbc.gridx = 1;
        txtMaDT = new JTextField(15);
        pnlInput.add(txtMaDT, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlInput.add(new JLabel("Tên Đối Tượng:"), gbc);
        gbc.gridx = 1;
        txtTenDT = new JTextField(20);
        pnlInput.add(txtTenDT, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlInput.add(new JLabel("Tỉ Lệ Giảm (%):"), gbc);
        gbc.gridx = 1;
        txtTiLeGiam = new JTextField(10);
        pnlInput.add(txtTiLeGiam, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");

        ButtonStyleHelper.styleButtonAdd(btnThem);
        ButtonStyleHelper.styleButtonEdit(btnSua);
        ButtonStyleHelper.styleButtonDelete(btnXoa);
        ButtonStyleHelper.styleButtonSave(btnLuu);
        ButtonStyleHelper.styleButtonCancel(btnHuy);

        pnlButton.add(btnThem);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);

        pnlSouth.add(pnlButton, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnLuu.addActionListener(e -> luu());
        btnHuy.addActionListener(e -> huy());

        btnTim.addActionListener(e -> timKiem());
        btnHienThiTatCa.addActionListener(e -> hienThiTatCa());
    }

    private void them() {
        clearForm();
        isThem = true;
        setFormEnabled(true);
        txtMaDT.requestFocus();
    }

    private void sua() {
        if (txtMaDT.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn đối tượng cần sửa");
            return;
        }
        isThem = false;
        setFormEnabled(true);
        txtMaDT.setEnabled(false);
    }
    
    private void luu() {
        if (txtMaDT.getText().trim().isEmpty()
                || txtTenDT.getText().trim().isEmpty()
                || txtTiLeGiam.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        double tiLe;
        try {

            tiLe = Double.parseDouble(txtTiLeGiam.getText());

            if (tiLe < 0 || tiLe > 100) {
                JOptionPane.showMessageDialog(this, "Tỉ lệ giảm phải từ 0 đến 100");
                return;
            }

            tiLe = tiLe / 100.0;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this, "Tỉ lệ giảm phải là số");
            return;

        }

        DoiTuongUuTien dt = new DoiTuongUuTien(
                txtMaDT.getText(),
                txtTenDT.getText(),
                tiLe
        );

        boolean ok = isThem ? controller.them(dt) : controller.sua(dt);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Lưu thành công");
            hienThiTatCa();
            setFormEnabled(false);
        }
    }

    private void huy() {
        clearForm();
        setFormEnabled(false);
    }
    
    private void setFormEnabled(boolean enabled) {
        txtMaDT.setEnabled(enabled);
        txtTenDT.setEnabled(enabled);
        txtTiLeGiam.setEnabled(enabled);

        btnLuu.setEnabled(enabled);
        btnHuy.setEnabled(enabled);

        btnThem.setEnabled(!enabled);
        btnSua.setEnabled(!enabled);
        btnXoa.setEnabled(!enabled);
    }

    private void xoa() {
    if (txtMaDT.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Chọn đối tượng cần xóa");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xóa đối tượng này?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        if (controller.xoa(txtMaDT.getText())) {
            JOptionPane.showMessageDialog(this, "Xóa thành công");
            hienThiTatCa();
        }
    }
}

    private void timKiem() {

        String keyword = txtTimKiem.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập mã hoặc tên đối tượng cần tìm!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );

            txtTimKiem.requestFocus();
            return;
        }

        try {

            boolean found = controller.timKiem(keyword, tableModel);

            if (!found) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy đối tượng ưu tiên!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (RuntimeException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );

        }
    }

    private void hienThiTatCa() {

        txtTimKiem.setText("");

        tableDT.clearSelection();

        clearForm();

        controller.loadTable(tableModel);
    }

    private void doDuLieuVaoForm() {
        int row = tableDT.getSelectedRow();
        if (row >= 0) {
            txtMaDT.setText(tableModel.getValueAt(row, 0).toString());
            txtTenDT.setText(tableModel.getValueAt(row, 1).toString());
            txtTiLeGiam.setText(tableModel.getValueAt(row, 2).toString());
        }
    }

    private void clearForm() {
        txtMaDT.setText("");
        txtTenDT.setText("");
        txtTiLeGiam.setText("");
    }
}

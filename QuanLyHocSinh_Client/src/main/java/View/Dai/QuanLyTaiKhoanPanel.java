package View.Dai;

import Model.TaiKhoan;
import Controller.Dai.TaiKhoanController;
import TienIch.ButtonStyleHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import TienIch.TableSortHelper;

public class QuanLyTaiKhoanPanel extends JPanel {

    private TaiKhoanController controller = new TaiKhoanController();

    private JTable tableTK;
    private DefaultTableModel tableModel;

    private JTextField txtTenDangNhap, txtMaNguoiDung;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboQuyen;

    private JTextField txtTimKiem;
    private JButton btnTim, btnHienThiTatCa;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy;

    private boolean isThem = false;

    public QuanLyTaiKhoanPanel() {
        initComponents();
        controller.loadTable(tableModel);
        setFormEnabled(false);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new GridLayout(2, 1, 5, 5));

        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm"));

        txtTimKiem = new JTextField(20);
        btnTim = new JButton("Tìm");
        btnHienThiTatCa = new JButton("Hiển thị tất cả");

        ButtonStyleHelper.styleButtonSearch(btnTim);
        ButtonStyleHelper.styleButtonView(btnHienThiTatCa);

        pnlSearch.add(new JLabel("Từ khóa:"));
        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTim);
        pnlSearch.add(btnHienThiTatCa);

        pnlNorth.add(pnlSearch);
        add(pnlNorth, BorderLayout.NORTH);

        String[] columns = {"Tên đăng nhập", "Mật khẩu", "Quyền", "Mã người dùng"};
        tableModel = new DefaultTableModel(columns, 0);
        tableTK = new JTable(tableModel);
        TableSortHelper.enableTableSorting(tableTK);
        tableTK.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableTK.setRowHeight(25);
        tableTK.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        tableTK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doDuLieuVaoForm();
                setFormEnabled(false); 
            }
        });

        add(new JScrollPane(tableTK), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thông tin tài khoản"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlInput.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        txtTenDangNhap = new JTextField(15);
        pnlInput.add(txtTenDangNhap, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlInput.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        txtMatKhau = new JPasswordField(15);
        pnlInput.add(txtMatKhau, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlInput.add(new JLabel("Quyền:"), gbc);
        gbc.gridx = 1;
        cboQuyen = new JComboBox<>(new String[]{"", "Admin", "GiaoVien", "HocSinh"});
        cboQuyen.setSelectedIndex(0);
        pnlInput.add(cboQuyen, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlInput.add(new JLabel("Mã người dùng:"), gbc);
        gbc.gridx = 1;
        txtMaNguoiDung = new JTextField(15);
        pnlInput.add(txtMaNguoiDung, gbc);

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
        btnHienThiTatCa.addActionListener(e -> controller.loadTable(tableModel));
    }

    private void them() {
        clearForm();
        isThem = true;
        setFormEnabled(true);
        txtTenDangNhap.requestFocus();
    }

    private void sua() {
        if (txtTenDangNhap.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn tài khoản cần sửa");
            return;
        }
        isThem = false;
        setFormEnabled(true);
        txtTenDangNhap.setEnabled(false);
    }

    private void luu() {
        if (txtTenDangNhap.getText().trim().isEmpty()
                || txtMaNguoiDung.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        if (isThem && txtMatKhau.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống");
            return;
        }

        TaiKhoan tk = getTaiKhoanFromForm();
        boolean ok = isThem ? controller.them(tk) : controller.sua(tk);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Lưu thành công");
            controller.loadTable(tableModel);
            clearForm();
            setFormEnabled(false);
        }
    }

    private void xoa() {
        if (txtTenDangNhap.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn tài khoản cần xóa");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa tài khoản này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.xoa(txtTenDangNhap.getText())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                controller.loadTable(tableModel);
                clearForm();
            }
        }
    }

    private void huy() {
        clearForm();
        setFormEnabled(false);
    }

    private void setFormEnabled(boolean enabled) {
        txtTenDangNhap.setEnabled(enabled);
        txtMatKhau.setEnabled(enabled);
        cboQuyen.setEnabled(enabled);
        txtMaNguoiDung.setEnabled(enabled);

        btnLuu.setEnabled(enabled);
        btnHuy.setEnabled(enabled);

        btnThem.setEnabled(!enabled);
        btnSua.setEnabled(!enabled);
        btnXoa.setEnabled(!enabled);
    }

    private void doDuLieuVaoForm() {
        int r = tableTK.getSelectedRow();
        if (r >= 0) {
            txtTenDangNhap.setText(tableModel.getValueAt(r, 0).toString());
     
            txtMatKhau.setText(tableModel.getValueAt(r, 1).toString()); 
            cboQuyen.setSelectedItem(tableModel.getValueAt(r, 2));
            txtMaNguoiDung.setText(tableModel.getValueAt(r, 3).toString());
        }
    }

    private TaiKhoan getTaiKhoanFromForm() {
        return new TaiKhoan(
                txtTenDangNhap.getText(),
                new String(txtMatKhau.getPassword()),
                cboQuyen.getSelectedItem().toString(),
                txtMaNguoiDung.getText()
        );
    }

    private void clearForm() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        txtMaNguoiDung.setText("");
        cboQuyen.setSelectedIndex(0);
        txtTenDangNhap.setEnabled(true);
    }

    private void timKiem() {

        String keyword = txtTimKiem.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập tên đăng nhập, quyền hoặc mã người dùng cần tìm!",
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
                        "Không tìm thấy tài khoản!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không thể kết nối tới Server!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );

        }
    }
}
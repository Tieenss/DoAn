package View.Dai;

import Model.TaiKhoan;
import Controller.Dai.TaiKhoanController;
import TienIch.ButtonStyleHelper;

import java.util.List;
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

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboQuyen, cboMaNguoiDung;

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
        cboQuyen.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                capNhatMaNguoiDung();
            }
        });

        gbc.gridx = 0; gbc.gridy = 3;
        pnlInput.add(new JLabel("Mã người dùng:"), gbc);
        gbc.gridx = 1;
        cboMaNguoiDung = new JComboBox<>();
//        cboMaNguoiDung.setPreferredSize(new Dimension(180, 15));
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboMaNguoiDung);
        cboMaNguoiDung.setEnabled(false);
        pnlInput.add(cboMaNguoiDung, gbc);

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
        txtMaNguoiDungHienTai = "";
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
//        txtTenDangNhap.setEnabled(false);
//
//        // Không cho thay đổi quyền
//        cboQuyen.setEnabled(false);
//
//        // Chọn lại mã người dùng hiện tại
//        if (!txtMaNguoiDungHienTai.isEmpty()) {
//            cboMaNguoiDung.setSelectedItem(
//                    txtMaNguoiDungHienTai
//            );
//        }
    }

    private void luu() {

        if (!validateTaiKhoan()) {
            return;
        }

        TaiKhoan tk = getTaiKhoanFromForm();

        boolean ok;

        if (isThem) {
            ok = controller.them(tk);
        } else {
            ok = controller.sua(tk);
        }

        if (ok) {
            JOptionPane.showMessageDialog(
                    this,
                    "Lưu thành công"
            );
            controller.loadTable(tableModel);
            clearForm();
            setFormEnabled(false);

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Lưu thất bại! Vui lòng kiểm tra lại thông tin.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void xoa() {
        String tenDangNhap = txtTenDangNhap.getText().trim();

        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn tài khoản để xóa!"
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa tài khoản \""
                        + tenDangNhap + "\"?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = controller.xoa(tenDangNhap);

        if (ok) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa thành công"
            );

            controller.loadTable(tableModel);
            clearForm();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa thất bại!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void huy() {
        clearForm();
        setFormEnabled(false);
    }

    private boolean validateTaiKhoan() {

        String tenDangNhap = txtTenDangNhap.getText().trim();
        String quyen = (String) cboQuyen.getSelectedItem();

        // 1. Kiểm tra tên đăng nhập
        if (tenDangNhap.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tên đăng nhập không được để trống"
            );
            txtTenDangNhap.requestFocus();
            return false;
        }

//        if (tenDangNhap.length() < 3 || tenDangNhap.length() > 20) {
//            JOptionPane.showMessageDialog(
//                    this,
//                    "Tên đăng nhập phải từ 3 đến 20 ký tự"
//            );
//            txtTenDangNhap.requestFocus();
//            return false;
//        }

        if (tenDangNhap.contains(" ")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tên đăng nhập không được chứa khoảng trắng"
            );
            txtTenDangNhap.requestFocus();
            return false;
        }

        // 2. Kiểm tra mật khẩu khi thêm
        String matKhau = new String(txtMatKhau.getPassword());

        if (isThem) {

            if (matKhau.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Mật khẩu không được để trống"
                );
                txtMatKhau.requestFocus();
                return false;
            }

//            if (matKhau.length() < 6) {
//                JOptionPane.showMessageDialog(
//                        this,
//                        "Mật khẩu phải có ít nhất 6 ký tự"
//                );
//                txtMatKhau.requestFocus();
//                return false;
//            }
        }

        // 3. Kiểm tra quyền
        if (quyen == null || quyen.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn quyền"
            );
            cboQuyen.requestFocus();
            return false;
        }

        // 4. Kiểm tra mã người dùng
        Object selected = cboMaNguoiDung.getSelectedItem();

        if (!"Admin".equals(quyen)
                && (selected == null
                || selected.toString().trim().isEmpty()
                || !isValidComboValue(cboMaNguoiDung))) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mã người dùng đã có tài khoản hoặc không hợp lệ.\n"
                            + "Vui lòng chọn mã người dùng khác."
            );

            // Khôi phục lại danh sách mã người dùng
            capNhatMaNguoiDung();

            cboMaNguoiDung.requestFocus();

            return false;
        }

        return true;
    }

    private void setFormEnabled(boolean enabled) {
        txtTenDangNhap.setEnabled(enabled);
        txtMatKhau.setEnabled(enabled);

        // Khi thêm mới mới được chọn quyền
        cboQuyen.setEnabled(enabled && isThem);

        btnLuu.setEnabled(enabled);
        btnHuy.setEnabled(enabled);

        btnThem.setEnabled(!enabled);
        btnSua.setEnabled(!enabled);
        btnXoa.setEnabled(!enabled);

        if (enabled) {
            capNhatMaNguoiDung();
            // Khi sửa: không cho thay đổi mã người dùng
            if (!isThem) {
                cboMaNguoiDung.setEnabled(false);
            }
        } else {
            cboMaNguoiDung.setEnabled(false);
        }
    }

    private void doDuLieuVaoForm() {
        int viewRow = tableTK.getSelectedRow();

        if (viewRow >= 0) {
            int r = tableTK.convertRowIndexToModel(viewRow);
            txtTenDangNhap.setText(tableModel.getValueAt(r, 0).toString());
            txtMatKhau.setText(tableModel.getValueAt(r, 1).toString());

            String quyen = tableModel.getValueAt(r, 2).toString();
            Object value = tableModel.getValueAt(r, 3);

            String maNguoiDung = value == null ? "" : value.toString().trim();

            // Lưu mã người dùng hiện tại trước
            // khi load lại ComboBox
            txtMaNguoiDungHienTai = maNguoiDung;

            // Chọn quyền
            cboQuyen.setSelectedItem(quyen);

            // Load lại danh sách theo quyền
            capNhatMaNguoiDung();

            // Chọn lại mã người dùng của tài khoản
            if (!"Admin".equals(quyen) && !maNguoiDung.isEmpty()) {
                cboMaNguoiDung.setSelectedItem(maNguoiDung);
            }
        }
    }

    private TaiKhoan getTaiKhoanFromForm() {
        String quyen = cboQuyen.getSelectedItem().toString();

        String maNguoiDung = "";

        if (!"Admin".equals(quyen) && cboMaNguoiDung.getSelectedItem() != null) {
            maNguoiDung = cboMaNguoiDung.getSelectedItem().toString();
        }

        return new TaiKhoan(
                txtTenDangNhap.getText().trim(),
                new String(txtMatKhau.getPassword()),
                quyen,
                maNguoiDung
        );
    }

    private void clearForm() {
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");

        cboQuyen.setSelectedIndex(0);

        cboMaNguoiDung.removeAllItems();
        cboMaNguoiDung.setEnabled(false);

        txtTenDangNhap.setEnabled(true);
        txtMaNguoiDungHienTai = "";
    }

    private String txtMaNguoiDungHienTai = "";

    private List<String> getMaNguoiDungDaCoTaiKhoan() {
        List<String> danhSachDaCoTaiKhoan = new java.util.ArrayList<>();
        List<TaiKhoan> danhSachTaiKhoan = controller.getAll();

        for (TaiKhoan tk : danhSachTaiKhoan) {
            String maNguoiDung = tk.getMaNguoiDung();

            if (maNguoiDung != null && !maNguoiDung.trim().isEmpty()) {
                danhSachDaCoTaiKhoan.add(
                        maNguoiDung.trim()
                );
            }
        }
        return danhSachDaCoTaiKhoan;
    }

    private boolean daCoTaiKhoan(List<String> danhSachDaCoTaiKhoan, String maNguoiDung) {
        for (String ma : danhSachDaCoTaiKhoan) {
            if (ma != null && maNguoiDung != null && ma.trim().equalsIgnoreCase(maNguoiDung.trim())) {
                return true;
            }
        }
        return false;
    }

    private void loadMaGiaoVien() {

        List<String> maGV = controller.getMaGiaoVien();
        List<String> daCoTaiKhoan = getMaNguoiDungDaCoTaiKhoan();

        for (String ma : maGV) {
            boolean laMaHienTai = !isThem && ma.equalsIgnoreCase(txtMaNguoiDungHienTai);
            if (!daCoTaiKhoan(daCoTaiKhoan, ma) || laMaHienTai) {
                cboMaNguoiDung.addItem(ma);
            }
        }

        TienIch.ComboBoxUtil.refreshOriginalItems(cboMaNguoiDung);
    }

    private void loadMaHocSinh() {
        List<String> maHS = controller.getMaHocSinh();

        List<String> daCoTaiKhoan = getMaNguoiDungDaCoTaiKhoan();

        for (String ma : maHS) {
            boolean laMaHienTai = !isThem && ma.equalsIgnoreCase(txtMaNguoiDungHienTai);
            if (!daCoTaiKhoan(daCoTaiKhoan, ma) || laMaHienTai) {
                cboMaNguoiDung.addItem(ma);
            }
        }

        TienIch.ComboBoxUtil.refreshOriginalItems(cboMaNguoiDung);
    }

    private void capNhatMaNguoiDung() {
        String quyen = (String) cboQuyen.getSelectedItem();

        cboMaNguoiDung.removeAllItems();

        if (quyen == null || quyen.isEmpty()) {
            cboMaNguoiDung.setEnabled(false);
            return;
        }

        if ("Admin".equals(quyen)) {
            cboMaNguoiDung.setEnabled(false);
            return;
        }

        cboMaNguoiDung.setEnabled(true);

        if ("GiaoVien".equals(quyen)) {
            loadMaGiaoVien();
        } else if ("HocSinh".equals(quyen)) {
            loadMaHocSinh();
        }
    }

    private boolean isValidComboValue(JComboBox<String> comboBox) {
        Object selected = comboBox.getSelectedItem();

        if (selected == null) {
            return false;
        }

        String value = selected.toString().trim();

        if (value.isEmpty()) {
            return false;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String item = comboBox.getItemAt(i);

            if (item != null
                    && item.trim().equalsIgnoreCase(value)) {
                return true;
            }
        }

        return false;
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
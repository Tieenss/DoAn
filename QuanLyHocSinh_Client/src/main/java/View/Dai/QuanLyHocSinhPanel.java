package View.Dai;

import Controller.Dai.HocSinhController;
import Model.HocSinh;
import TienIch.ButtonStyleHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class QuanLyHocSinhPanel extends JPanel {

    private HocSinhController controller = new HocSinhController();

    private JTable tableHS;
    private DefaultTableModel tableModel;

    private JTextField txtMaHS, txtHoTen, txtDiaChi;
 
    private JSpinner spNgaySinh, spNamBatDau, spNamKetThuc;
    
    private JComboBox<String> cboGioiTinh,cboMaLop, cboMaDT;

    private JTextField txtTimKiem;
    private JComboBox<String> cboLocNienKhoa;
    private JButton btnTimKiem, btnHienThiTatCa;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy;
    
    private boolean isThem = false;

    public QuanLyHocSinhPanel() {
        initComponents();
        loadComboBox();
        controller.loadTable(tableModel);
        setFormEnabled(false);

//        if (Model.Auth.isHocSinh()) {
//            loadThongTinCaNhan();
//
//            setFormEnabled(false);
//
//            btnThem.setVisible(false);
//            btnSua.setVisible(false);
//            btnXoa.setVisible(false);
//            btnLuu.setVisible(false);
//            btnHuy.setVisible(false);
//        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new GridLayout(2, 1, 5, 5));

        String titleText = "QUẢN LÝ HỌC SINH";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm"));

        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tìm");
        btnHienThiTatCa = new JButton("Hiển thị tất cả");

        ButtonStyleHelper.styleButtonSearch(btnTimKiem);
        ButtonStyleHelper.styleButtonView(btnHienThiTatCa);

        pnlSearch.add(new JLabel("Từ khóa:"));
        pnlSearch.add(txtTimKiem);

        pnlSearch.add(new JLabel("  Niên khóa:"));
        cboLocNienKhoa = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboLocNienKhoa);
        pnlSearch.add(cboLocNienKhoa);

        pnlSearch.add(btnTimKiem);
        pnlSearch.add(btnHienThiTatCa);

        pnlNorth.add(pnlSearch);
        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {
            "Mã HS", "Họ tên", "Ngày sinh", "Giới tính",
            "Địa chỉ", "Mã lớp", "Mã đối tượng", "Niên khóa"
        };

        tableModel = new DefaultTableModel(cols, 0);
        tableHS = new JTable(tableModel);
        tableHS.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHS.setRowHeight(25);
        tableHS.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        tableHS.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                doDuLieuVaoForm();
            }
        });

        controller.loadComboLocNienKhoa(cboLocNienKhoa);
        TienIch.ComboBoxUtil.refreshOriginalItems(cboLocNienKhoa);

        add(new JScrollPane(tableHS), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thông tin học sinh"));

        JPanel pnlInput = new JPanel(new GridLayout(1, 2, 20, 0));
        JPanel pnlLeft = new JPanel(new GridBagLayout());
        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlInput.add(pnlLeft);
        pnlInput.add(pnlRight);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        pnlLeft.add(new JLabel("Mã HS:"), gbc);
        gbc.gridx = 1;
        txtMaHS = new JTextField(15);
        pnlLeft.add(txtMaHS, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        pnlLeft.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1;
        txtHoTen = new JTextField(20);
        pnlLeft.add(txtHoTen, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        pnlLeft.add(new JLabel("Ngày sinh:"), gbc);
        gbc.gridx = 1;
  
        spNgaySinh = new JSpinner(new SpinnerDateModel());
        spNgaySinh.setEditor(new JSpinner.DateEditor(spNgaySinh, "dd/MM/yyyy"));
        pnlLeft.add(spNgaySinh, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        pnlLeft.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboGioiTinh);
        pnlLeft.add(cboGioiTinh, gbc);

        y = 0;
        gbc.gridx = 0; gbc.gridy = y;
        pnlRight.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1;
        txtDiaChi = new JTextField(20);
        pnlRight.add(txtDiaChi, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        pnlRight.add(new JLabel("Mã lớp:"), gbc);
        gbc.gridx = 1;
        cboMaLop = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboMaLop);
        pnlRight.add(cboMaLop, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        pnlRight.add(new JLabel("Mã đối tượng:"), gbc);
        gbc.gridx = 1;
        cboMaDT = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboMaDT);
        pnlRight.add(cboMaDT, gbc);

        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        pnlRight.add(new JLabel("Niên khóa:"), gbc);

        gbc.gridx = 1;

        JPanel pnlNienKhoa = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 5, 0)
        );

        int namHienTai = Calendar.getInstance().get(Calendar.YEAR);

        spNamBatDau = new JSpinner(
                new SpinnerNumberModel(namHienTai, 2000, 2100, 1)
        );

        spNamKetThuc = new JSpinner(
                new SpinnerNumberModel(namHienTai + 3, 2000, 2100, 1)
        );

        spNamBatDau.setEditor(
                new JSpinner.NumberEditor(spNamBatDau, "####")
        );

        spNamKetThuc.setEditor(
                new JSpinner.NumberEditor(spNamKetThuc, "####")
        );

        pnlNienKhoa.add(spNamBatDau);
        pnlNienKhoa.add(new JLabel("-"));
        pnlNienKhoa.add(spNamKetThuc);

        pnlRight.add(pnlNienKhoa, gbc);

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

        btnTimKiem.addActionListener(e -> timKiem());
        btnHienThiTatCa.addActionListener(e -> hienThiTatCa());
        
//        if (Model.Auth.isHocSinh()) {
//            pnlSearch.setVisible(false);
//            btnThem.setVisible(false);
//            btnXoa.setVisible(false);
//        } else if (Model.Auth.isGiaoVien()) {
//            btnThem.setVisible(false);
//            btnXoa.setVisible(false);
//        }
        if (Model.Auth.isGiaoVien()) {
            btnThem.setVisible(false);
            btnXoa.setVisible(false);
        }

    }

    private void them() {
        clearForm();

        isThem = true;

        setFormEnabled(true);

        int namHienTai = Calendar.getInstance().get(Calendar.YEAR);

        spNamBatDau.setValue(namHienTai);
        spNamKetThuc.setValue(namHienTai + 3);

        txtMaHS.requestFocus();
    }

    private void sua() {
        if (txtMaHS.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn học sinh cần sửa");
            return;
        }
//        // Học sinh không được phép sửa thông tin
//        if (Model.Auth.isHocSinh()) {
//            JOptionPane.showMessageDialog(
//                    this,
//                    "Học sinh không có quyền sửa thông tin học sinh."
//            );
//            return;
//        }
        isThem = false;
        setFormEnabled(true);
        txtMaHS.setEnabled(false);
        
//        if (Model.Auth.isHocSinh()) {
//            cboMaLop.setEnabled(false);
//            cboMaDT.setEnabled(false);
//        }
    }

    private void luu() {

        if (!validateThongTinHocSinh()) {
            return;
        }

        if (!validateNienKhoa()) {
            return;
        }

        HocSinh hs = getHocSinhFromForm();

        if (hs == null) {
            return;
        }

        boolean ok = isThem
                ? controller.them(hs)
                : controller.sua(hs);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Lưu thành công");

            controller.loadTable(tableModel);

            setFormEnabled(false);
            clearForm();

            isThem = false;

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Lưu thất bại! (Kiểm tra lại Mã HS, Mã Lớp, Mã ĐT...)",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void xoa() {
        if (txtMaHS.getText().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Vui lòng chọn học sinh để xóa!");
             return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (controller.xoa(txtMaHS.getText())) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thành công",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                controller.loadTable(tableModel);
                clearForm();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thất bại! Không thể xóa học sinh.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void huy() {
        clearForm();
        setFormEnabled(false);
    }

    private void setFormEnabled(boolean enabled) {
        txtMaHS.setEnabled(enabled);
        txtHoTen.setEnabled(enabled);
        spNgaySinh.setEnabled(enabled); 
        cboGioiTinh.setEnabled(enabled);
        txtDiaChi.setEnabled(enabled);
        cboMaLop.setEnabled(enabled);
        cboMaDT.setEnabled(enabled);
        spNamBatDau.setEnabled(enabled);
        spNamKetThuc.setEnabled(enabled);

        btnLuu.setEnabled(enabled);
        btnHuy.setEnabled(enabled);

        btnThem.setEnabled(!enabled);
        btnSua.setEnabled(!enabled);
        btnXoa.setEnabled(!enabled);
    }

    private void timKiem() {

        String keyword = txtTimKiem.getText().trim();

//        if (keyword.isEmpty()) {
//            JOptionPane.showMessageDialog(
//                    this,
//                    "Vui lòng nhập mã hoặc tên học sinh cần tìm!",
//                    "Thông báo",
//                    JOptionPane.WARNING_MESSAGE
//            );
//
//            txtTimKiem.requestFocus();
//            return;
//        }

        String nienKhoa = cboLocNienKhoa.getSelectedItem() != null ? cboLocNienKhoa.getSelectedItem().toString() : "";

        try {
            boolean found = controller.timKiem(keyword, nienKhoa, tableModel);

            if (!found) {
                JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy học sinh!",
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

    private boolean validateThongTinHocSinh() {

        // Validate Mã HS
        String maHS = txtMaHS.getText().trim();

        if (maHS.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập Mã HS!",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            txtMaHS.requestFocus();
            return false;
        }

        if (maHS.contains(" ")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mã HS không được chứa khoảng trắng!",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            txtMaHS.requestFocus();
            return false;
        }

        if (maHS.length() > 10) {

            JOptionPane.showMessageDialog(
                    this,
                    "Mã HS không được vượt quá 10 ký tự!",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            txtMaHS.requestFocus();
            return false;
        }

        // Validate Họ tên
        String hoTen = txtHoTen.getText().trim();

        if (hoTen.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập Họ tên!",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            txtHoTen.requestFocus();
            return false;
        }

        if (hoTen.length() > 50) {

            JOptionPane.showMessageDialog(
                    this,
                    "Họ tên không được vượt quá 50 ký tự!",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            txtHoTen.requestFocus();
            return false;
        }

        // Validate ngày sinh
        if (!validateNgaySinh()) {
            return false;
        }

        // Validate giới tính
        Object gioiTinh = cboGioiTinh.getSelectedItem();

        if (gioiTinh == null
                || gioiTinh.toString().trim().isEmpty()
                || (!gioiTinh.toString().equals("Nam")
                && !gioiTinh.toString().equals("Nữ"))) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn Giới tính hợp lệ!\n"
                            + "Chỉ được chọn Nam hoặc Nữ.",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            cboGioiTinh.requestFocus();
            return false;
        }

        // Validate địa chỉ
        String diaChi = txtDiaChi.getText().trim();

        if (diaChi.length() > 200) {

            JOptionPane.showMessageDialog(
                    this,
                    "Địa chỉ không được vượt quá 200 ký tự!",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            txtDiaChi.requestFocus();
            return false;
        }

        // Validate mã lớp
        if (!isValidComboValue(cboMaLop)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn Mã lớp hợp lệ!\n"
                            + "Mã lớp phải được chọn từ danh sách.",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            cboMaLop.requestFocus();
            return false;
        }

        // Validate mã đối tượng
        if (!isValidComboValue(cboMaDT)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn Mã đối tượng hợp lệ!\n"
                            + "Mã đối tượng phải được chọn từ danh sách.",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            cboMaDT.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidComboValue(JComboBox<String> comboBox) {

        Object selected = comboBox.getSelectedItem();

        if (selected == null || selected.toString().trim().isEmpty()) {
            return false;
        }

        String value = selected.toString().trim();

        for (int i = 0; i < comboBox.getItemCount(); i++) {

            String item = comboBox.getItemAt(i);

            if (item != null && item.trim().equalsIgnoreCase(value)) {
                return true;
            }
        }

        return false;
    }

    private boolean validateNgaySinh() {

        Date ngaySinh = (Date) spNgaySinh.getValue();
        Date homNay = new Date();

        if (ngaySinh.after(homNay)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ngày sinh không được lớn hơn ngày hiện tại!",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            spNgaySinh.requestFocus();
            return false;
        }

//        LocalDate ngaySinhLocal = ngaySinh.toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
//
//        LocalDate homNayLocal = LocalDate.now();
//
//        int tuoi = Period.between(ngaySinhLocal, homNayLocal).getYears();
//
//        // Kiểm tra độ tuổi học sinh THPT
//        if (tuoi < 15 || tuoi > 20) {
//            JOptionPane.showMessageDialog(
//                    this,
//                    "Tuổi học sinh không hợp lệ!\n"
//                            + "Học sinh THPT phải từ 15 đến 20 tuổi.\n"
//                            + "Tuổi hiện tại: " + tuoi,
//                    "Lỗi dữ liệu",
//                    JOptionPane.ERROR_MESSAGE
//            );
//
//            spNgaySinh.requestFocus();
//            return false;
//        }

        return true;
    }

    private boolean validateNienKhoa() {

        int namBatDau = (Integer) spNamBatDau.getValue();
        int namKetThuc = (Integer) spNamKetThuc.getValue();

        if (namKetThuc != namBatDau + 3) {

            JOptionPane.showMessageDialog(
                    this,
                    "Niên khóa không hợp lệ!\n"
                            + "Niên khóa THPT phải kéo dài 3 năm.\n\n"
                            + "Ví dụ: 2025 - 2028",
                    "Lỗi dữ liệu",
                    JOptionPane.ERROR_MESSAGE
            );

            spNamKetThuc.requestFocus();

            return false;
        }

        return true;
    }

    private void hienThiTatCa() {

        txtTimKiem.setText("");
        if (cboLocNienKhoa.getItemCount() > 0) {
            cboLocNienKhoa.setSelectedIndex(0);
        }

        tableHS.clearSelection();

        clearForm();

        setFormEnabled(false);

        controller.loadTable(tableModel);
    }

    private void doDuLieuVaoForm() {
        int r = tableHS.getSelectedRow();
        if (r >= 0) {
            txtMaHS.setText(tableModel.getValueAt(r, 0).toString());
            txtHoTen.setText(tableModel.getValueAt(r, 1).toString());

            try {
                String strDate = tableModel.getValueAt(r, 2).toString();
         
                Date d = new SimpleDateFormat("dd/MM/yyyy").parse(strDate);
                spNgaySinh.setValue(d);
            } catch (Exception e) {
           
                spNgaySinh.setValue(new Date());
            }

            cboGioiTinh.setSelectedItem(tableModel.getValueAt(r, 3));
            txtDiaChi.setText(tableModel.getValueAt(r, 4).toString());
            cboMaLop.setSelectedItem(tableModel.getValueAt(r, 5).toString());
            cboMaDT.setSelectedItem(tableModel.getValueAt(r, 6).toString());
            Object value = tableModel.getValueAt(r, 7);

            if (value != null) {

                String nienKhoa = value.toString().trim();

                try {
                    String[] parts = nienKhoa.split("-");

                    if (parts.length == 2) {

                        int namBatDau = Integer.parseInt(parts[0]);
                        int namKetThuc = Integer.parseInt(parts[1]);

                        spNamBatDau.setValue(namBatDau);
                        spNamKetThuc.setValue(namKetThuc);
                    }

                } catch (NumberFormatException e) {

                    int namHienTai = Calendar.getInstance().get(Calendar.YEAR);

                    spNamBatDau.setValue(namHienTai);
                    spNamKetThuc.setValue(namHienTai + 3);
                }
            }
        }
    }

    private HocSinh getHocSinhFromForm() {
        try {

            Date d = (Date) spNgaySinh.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strNgaySinh = sdf.format(d);

            HocSinh hs = new HocSinh(
                    txtMaHS.getText().trim(),
                    txtHoTen.getText().trim(),
                    strNgaySinh,
                    cboGioiTinh.getSelectedItem().toString().trim(),
                    txtDiaChi.getText().trim(),
                    cboMaLop.getSelectedItem().toString().trim(),
                    cboMaDT.getSelectedItem().toString().trim()
            );
            int namBatDau = (Integer) spNamBatDau.getValue();
            int namKetThuc = (Integer) spNamKetThuc.getValue();

            String nienKhoa = namBatDau + "-" + namKetThuc;

            hs.setNienKhoa(nienKhoa);
            return hs;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng!");
            return null;
        }
    }

    private void clearForm() {
        txtMaHS.setText("");
        txtHoTen.setText("");
        spNgaySinh.setValue(new Date());
        cboGioiTinh.setSelectedIndex(0);
        txtDiaChi.setText("");
        cboMaLop.setSelectedIndex(-1);
        cboMaDT.setSelectedIndex(-1);
        int namHienTai = Calendar.getInstance().get(Calendar.YEAR);

        spNamBatDau.setValue(namHienTai);
        spNamKetThuc.setValue(namHienTai + 3);

    }

    private void loadComboBox() {
        controller.loadComboMaLop(cboMaLop);
        TienIch.ComboBoxUtil.refreshOriginalItems(cboMaLop);

        controller.loadComboMaDT(cboMaDT);
        TienIch.ComboBoxUtil.refreshOriginalItems(cboMaDT);
    }

//    private void loadThongTinCaNhan() {
//        HocSinh hs = controller.getThongTinCaNhan();
//
//        if (hs != null) {
//            txtMaHS.setText(hs.getMaHS());
//            txtHoTen.setText(hs.getHoTen());
//
//            try {
//                Date d = new SimpleDateFormat("yyyy-MM-dd").parse(hs.getNgaySinh());
//                spNgaySinh.setValue(d);
//            } catch (Exception e) {
//                spNgaySinh.setValue(new Date());
//            }
//
//            cboGioiTinh.setSelectedItem(hs.getGioiTinh());
//            txtDiaChi.setText(hs.getDiaChi());
//            cboMaLop.setSelectedItem(hs.getMaLop());
//            cboMaDT.setSelectedItem(hs.getMaDT());
//            String nienKhoa = hs.getNienKhoa();
//
//            if (nienKhoa != null && !nienKhoa.trim().isEmpty()) {
//
//                try {
//                    String[] parts = nienKhoa.trim().split("-");
//
//                    if (parts.length == 2) {
//
//                        int namBatDau = Integer.parseInt(parts[0]);
//                        int namKetThuc = Integer.parseInt(parts[1]);
//
//                        spNamBatDau.setValue(namBatDau);
//                        spNamKetThuc.setValue(namKetThuc);
//                    }
//
//                } catch (NumberFormatException e) {
//
//                    int namHienTai = Calendar.getInstance().get(Calendar.YEAR);
//
//                    spNamBatDau.setValue(namHienTai);
//                    spNamKetThuc.setValue(namHienTai + 3);
//                }
//            }
//        }
//    }
}
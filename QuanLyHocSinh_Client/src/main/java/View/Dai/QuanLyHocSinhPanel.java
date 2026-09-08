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
import java.util.Date;

public class QuanLyHocSinhPanel extends JPanel {

    private HocSinhController controller = new HocSinhController();

    private JTable tableHS;
    private DefaultTableModel tableModel;

    private JTextField txtMaHS, txtHoTen, txtDiaChi;
 
    private JSpinner spNgaySinh; 
    
    private JComboBox<String> cboGioiTinh,cboMaLop, cboMaDT;

    private JTextField txtTimKiem;
    private JButton btnTim, btnHienThiTatCa;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy;
    
    private boolean isThem = false;

    public QuanLyHocSinhPanel() {
        initComponents();
        loadComboBox();
        controller.loadTable(tableModel);
        setFormEnabled(false);

        if (Model.Auth.isHocSinh()) {
            loadThongTinCaNhan();
            
            setFormEnabled(false);

            btnThem.setVisible(false);
            btnXoa.setVisible(false);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new GridLayout(2, 1, 5, 5));

        String titleText = Model.Auth.isHocSinh() ? "HỒ SƠ HỌC SINH" : "QUẢN LÝ HỌC SINH";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
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

        String[] cols = {
            "Mã HS", "Họ tên", "Ngày sinh", "Giới tính",
            "Địa chỉ", "Mã lớp", "Mã đối tượng"
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
        pnlRight.add(cboMaLop, gbc);

        y++;
        gbc.gridx = 0; gbc.gridy = y;
        pnlRight.add(new JLabel("Mã đối tượng:"), gbc);
        gbc.gridx = 1;
        cboMaDT = new JComboBox<>();
        pnlRight.add(cboMaDT, gbc);

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
        
        if (Model.Auth.isHocSinh()) {
            pnlSearch.setVisible(false);
            btnThem.setVisible(false);
            btnXoa.setVisible(false);
        } else if (Model.Auth.isGiaoVien()) {
            btnThem.setVisible(false);
            btnXoa.setVisible(false);
        }

    }

    private void them() {
        clearForm();
        isThem = true;
        setFormEnabled(true);
        txtMaHS.requestFocus();
    }

    private void sua() {
        if (txtMaHS.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn học sinh cần sửa");
            return;
        }
        isThem = false;
        setFormEnabled(true);
        txtMaHS.setEnabled(false);
        
        if (Model.Auth.isHocSinh()) {
            cboMaLop.setEnabled(false);
            cboMaDT.setEnabled(false);
        }
    }
    
    private void luu() {
  
        if(txtMaHS.getText().isEmpty() || txtHoTen.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã HS và Họ Tên!");
            return;
        }

        HocSinh hs = getHocSinhFromForm();
        if(hs == null) return; 

        boolean ok = isThem ? controller.them(hs) : controller.sua(hs);

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
        txtMaHS.setEnabled(enabled);
        txtHoTen.setEnabled(enabled);
        spNgaySinh.setEnabled(enabled); 
        cboGioiTinh.setEnabled(enabled);
        txtDiaChi.setEnabled(enabled);
        cboMaLop.setEnabled(enabled);
        cboMaDT.setEnabled(enabled);

        btnLuu.setEnabled(enabled);
        btnHuy.setEnabled(enabled);

        btnThem.setEnabled(!enabled);
        btnSua.setEnabled(!enabled);
        btnXoa.setEnabled(!enabled);
    }

    private void timKiem() {

        String keyword = txtTimKiem.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập mã hoặc tên học sinh cần tìm!",
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

    private void hienThiTatCa() {

        txtTimKiem.setText("");

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

        }
    }

    private HocSinh getHocSinhFromForm() {
        try {

            Date d = (Date) spNgaySinh.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strNgaySinh = sdf.format(d);

            return new HocSinh(
                txtMaHS.getText(),
                txtHoTen.getText(),
                strNgaySinh,
                cboGioiTinh.getSelectedItem().toString(),
                txtDiaChi.getText(),
                cboMaLop.getSelectedItem().toString(),
                cboMaDT.getSelectedItem().toString()
            );
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

    }
    
    private void loadComboBox() {
        controller.loadComboMaLop(cboMaLop);
        controller.loadComboMaDT(cboMaDT);
    }

    private void loadThongTinCaNhan() {
        HocSinh hs = controller.getThongTinCaNhan();

        if (hs != null) {
            txtMaHS.setText(hs.getMaHS());
            txtHoTen.setText(hs.getHoTen());

            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd").parse(hs.getNgaySinh());
                spNgaySinh.setValue(d);
            } catch (Exception e) {
                spNgaySinh.setValue(new Date());
            }

            cboGioiTinh.setSelectedItem(hs.getGioiTinh());
            txtDiaChi.setText(hs.getDiaChi());
            cboMaLop.setSelectedItem(hs.getMaLop());
            cboMaDT.setSelectedItem(hs.getMaDT());
        }
    }
}
package View.Tien;

import Model.Diem;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import TienIch.ButtonStyleHelper;
import javax.swing.table.DefaultTableCellRenderer;
public class QuanLyDiemPanel extends JPanel {

    private JComboBox<String> cboLocMaLop, cboLocMon, cboLocHocKy, cboLocNamHoc;
    private JButton btnLocDuLieu;

    private JTable tableDiem;
    private DefaultTableModel tableModel;

    private JTextField txtMaHS, txtTenHS, txtDiem15p, txtDiem1Tiet, txtDiemGiuaKy, txtDiemCuoiKy;
    private JComboBox<String> cboHocKyInput, cboNamHocInput;
    private JComboBox<String> cboMonHocInput;
    private JButton btnCapNhat, btnThem, btnSua, btnXoa, btnLuu, btnHuy;

    private JTextField txtTimKiem;
    private JButton btnTimKiem;
    private JButton btnXuatExcel;
    private List<Model.MonHoc> monHocList;

    public QuanLyDiemPanel() {
        initComponents();
    }

    private void initComponents() {
        
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));

        String titleText = Model.Auth.isHocSinh() ? "XEM ĐIỂM HỌC SINH" : "QUẢN LÝ ĐIỂM HỌC SINH";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlToolBar = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlFilter.setBorder(new TitledBorder("Lọc theo lớp (Mặc định)"));
        
        pnlFilter.add(new JLabel("Mã Lớp:"));
        cboLocMaLop = new JComboBox<>(); 
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboLocMaLop);
        pnlFilter.add(cboLocMaLop);

        pnlFilter.add(new JLabel("Môn:"));
        cboLocMon = new JComboBox<>(); 
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboLocMon);
        pnlFilter.add(cboLocMon);

        pnlFilter.add(new JLabel("Năm Học:"));
        cboLocNamHoc = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboLocNamHoc);
        pnlFilter.add(cboLocNamHoc);

        pnlFilter.add(new JLabel("Học Kỳ:"));
        cboLocHocKy = new JComboBox<>(); 
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboLocHocKy);
        pnlFilter.add(cboLocHocKy);

        btnLocDuLieu = new JButton("Lọc");
        ButtonStyleHelper.styleButtonFilter(btnLocDuLieu);
        pnlFilter.add(btnLocDuLieu);
        pnlToolBar.add(pnlFilter);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm nhanh"));
        
        pnlSearch.add(new JLabel("Nhập Tên hoặc Mã HS:"));
        txtTimKiem = new JTextField(20);
        pnlSearch.add(txtTimKiem);
        
        btnTimKiem = new JButton("Tìm Kiếm");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);

        pnlSearch.add(btnTimKiem);
        
        pnlToolBar.add(pnlSearch);
        
        pnlNorth.add(pnlToolBar, BorderLayout.CENTER);
        this.add(pnlNorth, BorderLayout.NORTH);

        String[] columnNames = {"Mã HS", "Họ Tên", "Mã Lớp", "Môn", "Năm Học", "HK", "Điểm 15p", "1 Tiết", "Giữa Kỳ", "Cuối Kỳ", "Tổng Kết"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableDiem = new JTable(tableModel);
        tableDiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDiem.setRowHeight(25);
        tableDiem.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                Object tongKetObj = table.getValueAt(row, 10);
                boolean chuaNhapDiem = tongKetObj == null || "Chưa nhập".equals(tongKetObj.toString()) || "".equals(tongKetObj.toString().trim());

                if (!isSelected) {
                    if (chuaNhapDiem) {
                        c.setBackground(new Color(255, 235, 238)); // Nền đỏ hồng pastel cảnh báo chưa nhập điểm
                        c.setForeground(new Color(198, 40, 40));   // Chữ đỏ sẫm
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setBackground(new Color(187, 222, 251)); // Màu khi được chọn
                    c.setForeground(Color.BLACK);
                }

                // Cột Tổng Kết (cột 10)
                if (column == 10) {
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    if (chuaNhapDiem) {
                        c.setForeground(new Color(198, 40, 40));
                    } else {
                        c.setForeground(new Color(0, 102, 204));
                    }
                }
                return c;
            }
        };
        tableDiem.setDefaultRenderer(Object.class, customRenderer);
        
        this.add(new JScrollPane(tableDiem), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Cập nhật điểm"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; pnlInput.add(new JLabel("Mã HS:"), gbc);
        gbc.gridx=1; gbc.gridy=0; txtMaHS=new JTextField(12); txtMaHS.setEditable(false); pnlInput.add(txtMaHS, gbc);
        gbc.gridx=2; gbc.gridy=0; pnlInput.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx=3; gbc.gridy=0; txtTenHS=new JTextField(12); txtTenHS.setEditable(false); pnlInput.add(txtTenHS, gbc);

        gbc.gridx=0; gbc.gridy=1; pnlInput.add(new JLabel("Điểm 15p:"), gbc);
        gbc.gridx=1; gbc.gridy=1; txtDiem15p=new JTextField(); pnlInput.add(txtDiem15p, gbc);
        
        gbc.gridx=2; gbc.gridy=1; pnlInput.add(new JLabel("Điểm 1 Tiết:"), gbc);
        gbc.gridx=3; gbc.gridy=1; txtDiem1Tiet=new JTextField(); pnlInput.add(txtDiem1Tiet, gbc);

        gbc.gridx=0; gbc.gridy=2; pnlInput.add(new JLabel("Điểm Giữa Kỳ:"), gbc);
        gbc.gridx=1; gbc.gridy=2; txtDiemGiuaKy=new JTextField(); pnlInput.add(txtDiemGiuaKy, gbc);
        
        gbc.gridx=2; gbc.gridy=2; pnlInput.add(new JLabel("Điểm Cuối Kỳ:"), gbc);
        gbc.gridx=3; gbc.gridy=2; txtDiemCuoiKy=new JTextField(); pnlInput.add(txtDiemCuoiKy, gbc);

        gbc.gridx=0; gbc.gridy=3; pnlInput.add(new JLabel("Năm Học:"), gbc);
        cboNamHocInput = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboNamHocInput);
        gbc.gridx=1; gbc.gridy=3; pnlInput.add(cboNamHocInput, gbc);

        gbc.gridx=2; gbc.gridy=3; pnlInput.add(new JLabel("Học Kỳ:"), gbc);
        cboHocKyInput = new JComboBox<>(new String[]{"1", "2"});
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboHocKyInput);
        gbc.gridx=3; gbc.gridy=3; pnlInput.add(cboHocKyInput, gbc);

        gbc.gridx=0; gbc.gridy=4; pnlInput.add(new JLabel("Môn Học:"), gbc);
        cboMonHocInput = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboMonHocInput);
        gbc.gridx=1; gbc.gridy=4; gbc.gridwidth=3; pnlInput.add(cboMonHocInput, gbc);
        gbc.gridwidth=1;

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        Dimension sz = new Dimension(90, 36);

        btnThem = new JButton("Thêm");
        ButtonStyleHelper.styleButtonAdd(btnThem);
        btnThem.setPreferredSize(sz);
        pnlButton.add(btnThem);

        btnSua = new JButton("Sửa");
        ButtonStyleHelper.styleButtonEdit(btnSua);
        btnSua.setPreferredSize(sz);
        pnlButton.add(btnSua);

        btnXoa = new JButton("Xóa");
        ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnXoa.setPreferredSize(sz);
        pnlButton.add(btnXoa);

        btnLuu = new JButton("Lưu");
        ButtonStyleHelper.styleButtonSave(btnLuu);
        btnLuu.setPreferredSize(sz);
        pnlButton.add(btnLuu);
        btnCapNhat = btnLuu; // Tương thích ngược

        btnHuy = new JButton("Hủy");
        ButtonStyleHelper.styleButtonCancel(btnHuy);
        btnHuy.setPreferredSize(sz);
        pnlButton.add(btnHuy);

        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);
        btnXuatExcel.setPreferredSize(new Dimension(120, 36));
        pnlButton.add(btnXuatExcel);
        
        pnlSouth.add(pnlButton, BorderLayout.SOUTH);
        this.add(pnlSouth, BorderLayout.SOUTH);
        
        if (Model.Auth.isHocSinh()) {
            pnlSearch.setBorder(new TitledBorder("Tìm kiếm môn học"));
            if (pnlSearch.getComponentCount() > 0 && pnlSearch.getComponent(0) instanceof JLabel) {
                ((JLabel) pnlSearch.getComponent(0)).setText("Nhập tên môn học:");
            }
            pnlSouth.setVisible(false);
        }

        setCrudButtonState(true, false, false, false, false);
    }

    public String getMaLopFilter() { 
        if (cboLocMaLop.getSelectedItem() == null) return "";
        String val = cboLocMaLop.getSelectedItem().toString();
        return val.equals("Tất cả") ? "" : val;
    }
    public String getMaMonFilter() { 
        String tenMonSelected = cboLocMon.getSelectedItem() != null ? cboLocMon.getSelectedItem().toString() : "";
        if (monHocList != null) {
            for (Model.MonHoc m : monHocList) {
                if (m.getTenMH().equals(tenMonSelected)) {
                    return m.getMaMH();
                }
            }
        }
        return ""; 
    }
    public int getHocKyFilter() { 
        try {
            return cboLocHocKy.getSelectedItem() != null && !cboLocHocKy.getSelectedItem().toString().isEmpty() ? Integer.parseInt(cboLocHocKy.getSelectedItem().toString()) : 0;
        } catch (Exception e) {
            return 0;
        }
    }
    public String getNamHocFilter() {
        if (cboLocNamHoc.getSelectedItem() == null) return "";
        String val = cboLocNamHoc.getSelectedItem().toString();
        return val.equals("Tất cả") ? "" : val;
    }
    public String getTuKhoaTimKiem() { return txtTimKiem.getText().trim(); }

    public void setMaLopData(List<String> lops) {
        cboLocMaLop.removeAllItems();
        cboLocMaLop.addItem("Tất cả"); 
        for (String lop : lops) {
            cboLocMaLop.addItem(lop);
        }
        
        if (cboLocMaLop.getItemCount() > 1) {
            cboLocMaLop.setSelectedIndex(1);
        }
    }

    public void setMonHocData(List<Model.MonHoc> mons) {
        this.monHocList = mons;
        cboLocMon.removeAllItems();
        if(cboMonHocInput != null) cboMonHocInput.removeAllItems();
        cboLocMon.addItem("");
        for (Model.MonHoc mon : mons) {
            cboLocMon.addItem(mon.getTenMH());
            if(cboMonHocInput != null) cboMonHocInput.addItem(mon.getTenMH());
        }
    }

    public void setHocKyData(List<Integer> hks) {
        cboLocHocKy.removeAllItems();
        cboLocHocKy.addItem("");
        for (Integer hk : hks) {
            cboLocHocKy.addItem(hk.toString());
        }
    }

    public void setNamHocData(List<String> nhs) {
        cboLocNamHoc.removeAllItems();
        cboLocNamHoc.addItem("Tất cả");
        if(cboNamHocInput != null) cboNamHocInput.removeAllItems();
        for (String nh : nhs) {
            cboLocNamHoc.addItem(nh);
            if(cboNamHocInput != null) cboNamHocInput.addItem(nh);
        }
    }

    public Diem getDiemInput() {
        Diem d = new Diem();
        d.setMaHS(txtMaHS.getText());

        String tenMonSelected = cboMonHocInput.getSelectedItem() != null ? cboMonHocInput.getSelectedItem().toString() : "";
        String maMH = "";
        if (monHocList != null) {
            for (Model.MonHoc m : monHocList) {
                if (m.getTenMH().equals(tenMonSelected)) {
                    maMH = m.getMaMH();
                    break;
                }
            }
        }
        d.setMaMH(maMH); 

        int hocKy = 1;
        try {
            if (cboHocKyInput.getSelectedItem() != null) {
                hocKy = Integer.parseInt(cboHocKyInput.getSelectedItem().toString());
            }
        } catch (Exception e) {}
        d.setHocKy(hocKy); 
        d.setNamHoc(cboNamHocInput.getSelectedItem() != null ? cboNamHocInput.getSelectedItem().toString() : "");
        try {
            if (!txtDiem15p.getText().trim().isEmpty()) {
                d.setDiem15p(Double.parseDouble(txtDiem15p.getText().trim()));
            }
            if (!txtDiem1Tiet.getText().trim().isEmpty()) {
                d.setDiem1Tiet(Double.parseDouble(txtDiem1Tiet.getText().trim()));
            }
            if (!txtDiemGiuaKy.getText().trim().isEmpty()) {
                d.setDiemGiuaKy(Double.parseDouble(txtDiemGiuaKy.getText().trim()));
            }
            if (!txtDiemCuoiKy.getText().trim().isEmpty()) {
                d.setDiemCuoiKy(Double.parseDouble(txtDiemCuoiKy.getText().trim()));
            }
        } catch (Exception e) { return null; }
        return d;
    }

    public void setTableData(List<Diem> list) {
        tableModel.setRowCount(0);
        for (Diem d : list) {
            String str15p = d.getDiem15p() != null ? String.valueOf(d.getDiem15p()) : "";
            String str1Tiet = d.getDiem1Tiet() != null ? String.valueOf(d.getDiem1Tiet()) : "";
            String strGiuaKy = d.getDiemGiuaKy() != null ? String.valueOf(d.getDiemGiuaKy()) : "";
            String strCuoiKy = d.getDiemCuoiKy() != null ? String.valueOf(d.getDiemCuoiKy()) : "";

            Double dtb = d.getDiemTongKet();
            String strTongKet = dtb != null ? String.valueOf(Math.round(dtb * 100.0) / 100.0) : "Chưa nhập";

            tableModel.addRow(new Object[]{
                d.getMaHS(), 
                d.getTenHS(), 
                d.getMaLop(), 
                d.getTenMH() != null ? d.getTenMH() : d.getMaMH(), 
                d.getNamHoc(),
                d.getHocKy(),
                str15p, 
                str1Tiet,   
                strGiuaKy, 
                strCuoiKy,   
                strTongKet               
            });
        }
    }

    public void fillFormInput(int row) {
        if (row >= 0) {
            txtMaHS.setText(tableModel.getValueAt(row, 0) != null ? tableModel.getValueAt(row, 0).toString() : "");
            txtTenHS.setText(tableModel.getValueAt(row, 1) != null ? tableModel.getValueAt(row, 1).toString() : "");
            
            Object tenMonObj = tableModel.getValueAt(row, 3);
            if(tenMonObj != null) cboMonHocInput.setSelectedItem(tenMonObj.toString());
            
            Object nhObj = tableModel.getValueAt(row, 4);
            if(nhObj != null) cboNamHocInput.setSelectedItem(nhObj.toString());

            Object hkObj = tableModel.getValueAt(row, 5);
            if(hkObj != null) cboHocKyInput.setSelectedItem(hkObj.toString());

            Object d15 = tableModel.getValueAt(row, 6);
            txtDiem15p.setText(d15 != null ? d15.toString() : "");

            Object d1t = tableModel.getValueAt(row, 7);
            txtDiem1Tiet.setText(d1t != null ? d1t.toString() : "");

            Object dgk = tableModel.getValueAt(row, 8);
            txtDiemGiuaKy.setText(dgk != null ? dgk.toString() : "");

            Object dck = tableModel.getValueAt(row, 9);
            txtDiemCuoiKy.setText(dck != null ? dck.toString() : "");
        }
    }

    public void clearForm() {
        txtMaHS.setText("");
        txtTenHS.setText("");
        txtDiem15p.setText("");
        txtDiem1Tiet.setText("");
        txtDiemGiuaKy.setText("");
        txtDiemCuoiKy.setText("");
        if (tableDiem != null) {
            tableDiem.clearSelection();
        }
    }

    public void setFormEnabled(boolean enabled) {
        txtDiem15p.setEditable(enabled);
        txtDiem1Tiet.setEditable(enabled);
        txtDiemGiuaKy.setEditable(enabled);
        txtDiemCuoiKy.setEditable(enabled);
        cboNamHocInput.setEnabled(enabled);
        cboHocKyInput.setEnabled(enabled);
        cboMonHocInput.setEnabled(enabled);
    }

    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them);
        btnSua.setEnabled(sua);
        btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu);
        btnHuy.setEnabled(huy);

        if (luu && !sua && !xoa) {
            // Đang ở chế độ Thêm mới
            txtMaHS.setEditable(true);
            setFormEnabled(true);
        } else if (luu && sua) {
            // Đang ở chế độ Sửa
            txtMaHS.setEditable(false);
            setFormEnabled(true);
            cboNamHocInput.setEnabled(false);
            cboHocKyInput.setEnabled(false);
            cboMonHocInput.setEnabled(false);
        } else {
            // Trạng thái chờ hoặc chỉ chọn dòng
            txtMaHS.setEditable(false);
            setFormEnabled(false);
        }
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public JTable getTable() { return tableDiem; }

    public void addBtnXemListener(ActionListener action) { btnLocDuLieu.addActionListener(action); }
    public void addBtnTimKiemListener(ActionListener action) { btnTimKiem.addActionListener(action); } 
    public void addBtnCapNhatListener(ActionListener action) { btnLuu.addActionListener(action); }
    public void addBtnThemListener(ActionListener action) { btnThem.addActionListener(action); }
    public void addBtnSuaListener(ActionListener action) { btnSua.addActionListener(action); }
    public void addBtnXoaListener(ActionListener action) { btnXoa.addActionListener(action); }
    public void addBtnLuuListener(ActionListener action) { btnLuu.addActionListener(action); }
    public void addBtnHuyListener(ActionListener action) { btnHuy.addActionListener(action); }
    public void addTableMouseListener(MouseAdapter adapter) { tableDiem.addMouseListener(adapter); }
    public void addBtnXuatExcelListener(ActionListener ac) { btnXuatExcel.addActionListener(ac); }
    public void addTxtMaHSFocusListener(java.awt.event.FocusAdapter adapter) { txtMaHS.addFocusListener(adapter); }

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) tableDiem.getModel();
    }
    public JButton getBtnCapNhat() {
        return btnLuu;
    }
    public String getMaHSInput() { return txtMaHS.getText().trim(); }
    public void setTenHS(String ten) { txtTenHS.setText(ten); }
}
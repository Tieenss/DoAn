package View.Tien;

import Model.HanhKiem;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class HanhKiemPanel extends JPanel {

    private JButton btnXuatExcel;
    
    private JTextField txtTimKiem; 
    private JComboBox<String> cboLocMaLop, cboLocNamHoc, cboLocHocKy;

    private JTable table;
    private DefaultTableModel model;

    private JButton btnXem, btnTimKiem, btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnMoi;
    
    private JTextField txtMaHS, txtTenHS;
    private JTextArea txtNhanXet;
    private JComboBox<String> cboMaLopInput, cboNamHocInput, cboHocKyInput, cboXepLoai;

    public HanhKiemPanel() {
        initComponents();
    }

    private void initComponents() {
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));

        String titleText = Model.Auth.isHocSinh() ? "XEM HẠNH KIỂM" : "QUẢN LÝ HẠNH KIỂM";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlTools = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlFilter.setBorder(new TitledBorder("Lọc theo lớp (Mặc định)"));
        
        pnlFilter.add(new JLabel("Mã Lớp:"));
        cboLocMaLop = new JComboBox<>();
        cboLocMaLop.addItem("Tất cả");
        
        pnlFilter.add(cboLocMaLop); 
        
        pnlFilter.add(new JLabel("Năm Học:"));
        cboLocNamHoc = new JComboBox<>();
        cboLocNamHoc.addItem("");
        cboLocNamHoc.setSelectedIndex(0);
        pnlFilter.add(cboLocNamHoc);
        
        pnlFilter.add(new JLabel("Học Kỳ:"));
        cboLocHocKy = new JComboBox<>(new String[]{"", "1", "2"}); 
        cboLocHocKy.setSelectedIndex(0);
        pnlFilter.add(cboLocHocKy);
        
        btnXem = new JButton("Lọc Danh Sách");
        ButtonStyleHelper.styleButtonFilter(btnXem);
        pnlFilter.add(btnXem);
        pnlTools.add(pnlFilter);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm nhanh"));
        
        pnlSearch.add(new JLabel("Nhập Tên hoặc Mã HS:"));
        txtTimKiem = new JTextField(20); pnlSearch.add(txtTimKiem);
        
        btnTimKiem = new JButton("Tìm Kiếm");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);
        pnlSearch.add(btnTimKiem);
        pnlTools.add(pnlSearch);

        pnlNorth.add(pnlTools, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);
        btnXuatExcel.setPreferredSize(new Dimension(120, 35));

        String[] cols = {"Mã HS", "Tên HS", "Lớp", "Năm Học", "Học Kỳ", "Xếp Loại", "Nhận Xét"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Cập nhật Hạnh Kiểm"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; pnlInput.add(new JLabel("Mã HS:"), gbc);
        gbc.gridx=1; gbc.gridy=0; txtMaHS = new JTextField(15); pnlInput.add(txtMaHS, gbc);
        
        gbc.gridx=2; gbc.gridy=0; pnlInput.add(new JLabel("Tên HS:"), gbc);
        gbc.gridx=3; gbc.gridy=0; txtTenHS = new JTextField(15); txtTenHS.setEditable(false); pnlInput.add(txtTenHS, gbc);

        gbc.gridx=0; gbc.gridy=1; pnlInput.add(new JLabel("Mã Lớp:"), gbc);
        gbc.gridx=1; gbc.gridy=1; 
        cboMaLopInput = new JComboBox<>(); pnlInput.add(cboMaLopInput, gbc);
        
        gbc.gridx=2; gbc.gridy=1; pnlInput.add(new JLabel("Năm Học:"), gbc);
        gbc.gridx=3; gbc.gridy=1; 
        cboNamHocInput = new JComboBox<>(); cboNamHocInput.setEditable(true); pnlInput.add(cboNamHocInput, gbc);

        gbc.gridx=0; gbc.gridy=2; pnlInput.add(new JLabel("Học Kỳ:"), gbc);
        gbc.gridx=1; gbc.gridy=2; 
        cboHocKyInput = new JComboBox<>(new String[]{"1", "2"}); pnlInput.add(cboHocKyInput, gbc);

        gbc.gridx=2; gbc.gridy=2; pnlInput.add(new JLabel("Xếp Loại:"), gbc);
        gbc.gridx=3; gbc.gridy=2; 
        cboXepLoai = new JComboBox<>(new String[]{"", "Tốt", "Khá", "Trung bình", "Yếu"}); 
        pnlInput.add(cboXepLoai, gbc);

        gbc.gridx=0; gbc.gridy=3; pnlInput.add(new JLabel("Nhận Xét:"), gbc);
        gbc.gridx=1; gbc.gridy=3; gbc.gridwidth=3;
        txtNhanXet = new JTextArea(3, 40);
        txtNhanXet.setLineWrap(true);
        txtNhanXet.setWrapStyleWord(true);
        pnlInput.add(new JScrollPane(txtNhanXet), gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnThem = new JButton("Thêm");
        ButtonStyleHelper.styleButtonAdd(btnThem);
        btnSua = new JButton("Sửa");
        ButtonStyleHelper.styleButtonEdit(btnSua);
        btnXoa = new JButton("Xóa");
        ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnLuu = new JButton("Lưu");
        ButtonStyleHelper.styleButtonSave(btnLuu);
        btnHuy = new JButton("Hủy");
        ButtonStyleHelper.styleButtonCancel(btnHuy);

        Dimension sz = new Dimension(90, 35);
        btnThem.setPreferredSize(sz);
        btnSua.setPreferredSize(sz);
        btnXoa.setPreferredSize(sz);
        btnLuu.setPreferredSize(sz);
        btnHuy.setPreferredSize(sz);

        pnlBtn.add(btnThem);
        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);
        pnlBtn.add(btnLuu);
        pnlBtn.add(btnHuy);

        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);
        btnXuatExcel.setPreferredSize(new Dimension(120, 35));
        pnlBtn.add(btnXuatExcel);
        
        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        
        add(pnlSouth, BorderLayout.SOUTH);
        
        if (Model.Auth.isHocSinh()) {
            pnlSearch.setVisible(false);
            pnlSouth.setVisible(false);
        }

        setCrudButtonState(true, false, false, false, false);
    }

    public String getMaLopFilter() {
        if (cboLocMaLop.getSelectedItem() == null) return "";
        String val = cboLocMaLop.getSelectedItem().toString();
        return val.equals("Tất cả") ? "" : val;
    }
    public String getNamHocFilter() { 
        return cboLocNamHoc.getSelectedItem() != null ? cboLocNamHoc.getSelectedItem().toString() : ""; 
    }
    public int getHocKyFilter() { 
        try {
            return cboLocHocKy.getSelectedItem() != null && !cboLocHocKy.getSelectedItem().toString().isEmpty() ? Integer.parseInt(cboLocHocKy.getSelectedItem().toString()) : 0;
        } catch(Exception e) { return 0; }
    }
    public String getTuKhoaTimKiem() { return txtTimKiem.getText().trim(); } 

    public void setMaLopData(List<String> lops) {
        cboLocMaLop.removeAllItems();
        cboMaLopInput.removeAllItems();
        cboLocMaLop.addItem("Tất cả");
        for (String lop : lops) {
            cboLocMaLop.addItem(lop);
            cboMaLopInput.addItem(lop);
        }
        if (cboLocMaLop.getItemCount() > 1) {
            cboLocMaLop.setSelectedIndex(1);
        }
    }

    public void setNamHocData(List<String> namHocs) {
        cboLocNamHoc.removeAllItems();
        cboNamHocInput.removeAllItems();
        cboLocNamHoc.addItem("");
        for (String n : namHocs) {
            cboLocNamHoc.addItem(n);
            cboNamHocInput.addItem(n);
        }
    }

    public HanhKiem getHanhKiemInput() {
        HanhKiem hk = new HanhKiem();
        hk.setMaHS(txtMaHS.getText());
        hk.setMaLop(cboMaLopInput.getSelectedItem() != null ? cboMaLopInput.getSelectedItem().toString() : "");
        hk.setNamHoc(cboNamHocInput.getSelectedItem() != null ? cboNamHocInput.getSelectedItem().toString() : "");
        hk.setHocKy(Integer.parseInt(cboHocKyInput.getSelectedItem().toString()));
        hk.setXepLoai(cboXepLoai.getSelectedItem().toString());
        hk.setNhanXet(txtNhanXet.getText());
        return hk;
    }

    public void setTableData(List<HanhKiem> list) {
        model.setRowCount(0);
        for (HanhKiem hk : list) {
            model.addRow(new Object[]{
                hk.getMaHS(), hk.getTenHS(), hk.getMaLop(), hk.getNamHoc(), hk.getHocKy(), hk.getXepLoai(), hk.getNhanXet()
            });
        }
    }
    
    public void fillFormInput(int row) {
        if(row >= 0) {
            txtMaHS.setText(model.getValueAt(row, 0).toString());
            txtTenHS.setText(model.getValueAt(row, 1).toString());
            
            String maLop = model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "";
            cboMaLopInput.setSelectedItem(maLop);
            
            String namHoc = model.getValueAt(row, 3).toString();
            String hocKy = model.getValueAt(row, 4).toString();
            cboNamHocInput.setSelectedItem(namHoc);
            cboHocKyInput.setSelectedItem(hocKy);
            
            String xepLoai = model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "";
            if(!xepLoai.isEmpty()) {
                cboXepLoai.setSelectedItem(xepLoai);
            } else {
                cboXepLoai.setSelectedIndex(0);
            }
            Object nx = model.getValueAt(row, 6);
            txtNhanXet.setText(nx != null ? nx.toString() : "");
        }
    }
    
    public void clearForm() {
        txtMaHS.setText("");
        txtTenHS.setText("");
        txtNhanXet.setText("");

        if (table != null) {
            table.clearSelection();
        }
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public JTable getTable() { return table; }

    public void addBtnXemListener(ActionListener log) { btnXem.addActionListener(log); }
    public void addBtnTimKiemListener(ActionListener log) { btnTimKiem.addActionListener(log); }
    public void addBtnThemListener(ActionListener log) { btnThem.addActionListener(log); }
    public void addBtnSuaListener(ActionListener log) { btnSua.addActionListener(log); }
    public void addBtnXoaListener(ActionListener log) { btnXoa.addActionListener(log); }
    public void addBtnLuuListener(ActionListener log) { btnLuu.addActionListener(log); }
    public void addBtnHuyListener(ActionListener log) { btnHuy.addActionListener(log); }
    public void addBtnMoiListener(ActionListener log) { btnMoi.addActionListener(log); }
    public void addTableMouseListener(MouseAdapter log) { table.addMouseListener(log); }
    public void addBtnXuatExcelListener(ActionListener log) { btnXuatExcel.addActionListener(log); }
    
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }

    public String getMaHSInput() { return txtMaHS.getText().trim(); }
    public void setTenHS(String ten) { txtTenHS.setText(ten); }
    public void addMaHS(java.awt.event.FocusAdapter adapter) { txtMaHS.addFocusListener(adapter); }
    
    public void setFormEnabled(boolean enabled) {
        cboMaLopInput.setEnabled(enabled);
        cboNamHocInput.setEnabled(enabled);
        cboHocKyInput.setEnabled(enabled);
        cboXepLoai.setEnabled(enabled);
        txtNhanXet.setEditable(enabled);
    }
    
    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them);
        btnSua.setEnabled(sua);
        btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu);
        btnHuy.setEnabled(huy);

        if (luu && !sua && !xoa) {
            txtMaHS.setEditable(true);
        } else {
            txtMaHS.setEditable(false);
        }
        
        setFormEnabled(luu); 
        
        // Nếu đang ở trạng thái Sửa (btnLuu bật, btnSua bật) thì khóa các trường khóa chính lại
        if (luu && sua) {
            cboNamHocInput.setEnabled(false);
            cboHocKyInput.setEnabled(false);
            cboMaLopInput.setEnabled(false);
        }
    }

    public void hideButtonForStudent() {
        btnLuu.setVisible(false);
        btnXoa.setVisible(false);
    }
}

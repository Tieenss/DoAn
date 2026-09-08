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

    private JComboBox<String> cboLocMaLop, cboLocMon, cboLocHocKy;
    private JButton btnLocDuLieu;

    private JTable tableDiem;
    private DefaultTableModel tableModel;

    private JTextField txtMaHS, txtTenHS, txtDiem15p, txtDiem1Tiet, txtDiemGiuaKy, txtDiemCuoiKy;
    private JComboBox<String> cboHocKyInput;
    private JComboBox<String> cboMonHocInput;
    private JButton btnCapNhat;

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
        pnlFilter.add(cboLocMaLop);

        pnlFilter.add(new JLabel("Môn:"));
        cboLocMon = new JComboBox<>(); 
        pnlFilter.add(cboLocMon);

        pnlFilter.add(new JLabel("Học Kỳ:"));
        cboLocHocKy = new JComboBox<>(); 
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

        String[] columnNames = {"Mã HS", "Họ Tên", "Mã Lớp", "Môn", "HK", "Điểm 15p", "1 Tiết", "Giữa Kỳ", "Cuối Kỳ", "Tổng Kết"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableDiem = new JTable(tableModel);
        tableDiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDiem.setRowHeight(25);
        tableDiem.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        DefaultTableCellRenderer tongKetRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                c.setForeground(Color.RED);
                return c;
            }
        };
        tableDiem.getColumnModel().getColumn(9).setCellRenderer(tongKetRenderer);
        
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

        gbc.gridx=0; gbc.gridy=3; pnlInput.add(new JLabel("Học Kỳ:"), gbc);
        cboHocKyInput = new JComboBox<>(new String[]{"1", "2"});
        gbc.gridx=1; gbc.gridy=3; pnlInput.add(cboHocKyInput, gbc);

        gbc.gridx=2; gbc.gridy=3; pnlInput.add(new JLabel("Môn Học:"), gbc);
        cboMonHocInput = new JComboBox<>();
        gbc.gridx=3; gbc.gridy=3; pnlInput.add(cboMonHocInput, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCapNhat = new JButton("Lưu / Cập Nhật Điểm");
        ButtonStyleHelper.styleButtonSave(btnCapNhat);
        btnCapNhat.setPreferredSize(new Dimension(200, 40));
        pnlButton.add(btnCapNhat);

        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);

        btnXuatExcel.setPreferredSize(new Dimension(130, 40));
        pnlButton.add(btnXuatExcel);
        
        pnlSouth.add(pnlButton, BorderLayout.SOUTH);
        this.add(pnlSouth, BorderLayout.SOUTH);
        
        if (Model.Auth.isHocSinh()) {
            pnlSearch.setVisible(false);
            pnlSouth.setVisible(false);
        }
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
        try {
            
            d.setDiem15p(Double.parseDouble(txtDiem15p.getText()));
            d.setDiem1Tiet(Double.parseDouble(txtDiem1Tiet.getText()));
            d.setDiemGiuaKy(Double.parseDouble(txtDiemGiuaKy.getText()));
            d.setDiemCuoiKy(Double.parseDouble(txtDiemCuoiKy.getText()));
        } catch (Exception e) { return null; }
        return d;
    }

    public void setTableData(List<Diem> list) {
        tableModel.setRowCount(0);
        for (Diem d : list) {
            
            double dtb = Math.round(d.getDiemTongKet() * 100.0) / 100.0;
            tableModel.addRow(new Object[]{
                d.getMaHS(), 
                d.getTenHS(), 
                d.getMaLop(), 
                d.getTenMH() != null ? d.getTenMH() : d.getMaMH(), 
                d.getHocKy(),
                d.getDiem15p(), 
                d.getDiem1Tiet(),   
                d.getDiemGiuaKy(), 
                d.getDiemCuoiKy(),   
                dtb               
            });
        }
    }

    public void fillFormInput(int row) {
        if (row >= 0) {
            txtMaHS.setText(tableModel.getValueAt(row, 0).toString());
            txtTenHS.setText(tableModel.getValueAt(row, 1).toString());
            
            Object tenMonObj = tableModel.getValueAt(row, 3);
            if(tenMonObj != null) cboMonHocInput.setSelectedItem(tenMonObj.toString());
            
            Object hkObj = tableModel.getValueAt(row, 4);
            if(hkObj != null) cboHocKyInput.setSelectedItem(hkObj.toString());

            txtDiem15p.setText(tableModel.getValueAt(row, 5).toString());
            txtDiem1Tiet.setText(tableModel.getValueAt(row, 6).toString());
            txtDiemGiuaKy.setText(tableModel.getValueAt(row, 7).toString());
            txtDiemCuoiKy.setText(tableModel.getValueAt(row, 8).toString());
        }
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public JTable getTable() { return tableDiem; }

    public void addBtnXemListener(ActionListener action) { btnLocDuLieu.addActionListener(action); }
    public void addBtnTimKiemListener(ActionListener action) { btnTimKiem.addActionListener(action); } 
    public void addBtnCapNhatListener(ActionListener action) { btnCapNhat.addActionListener(action); }
    public void addTableMouseListener(MouseAdapter adapter) { tableDiem.addMouseListener(adapter); }
    public void addBtnXuatExcelListener(ActionListener ac) { btnXuatExcel.addActionListener(ac); }

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) tableDiem.getModel();
    }
    public JButton getBtnCapNhat() {
        return btnCapNhat;
    }
}
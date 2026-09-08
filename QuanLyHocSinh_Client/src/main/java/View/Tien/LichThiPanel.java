package View.Tien;

import Model.LichThi;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;
import com.toedter.calendar.JDateChooser;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class LichThiPanel extends JPanel {

    private JTextField txtTimKiem;
    private JButton btnTimKiem, btnXemTatCa, btnLocDanhSach;
    private JComboBox<String> cboLocKyThi, cboLocMon, cboLocPhong, cboLocLop;

    private JTable table;
    private DefaultTableModel model;
    private List<LichThi> cachedList;

    private JButton btnXuatExcel;

    private JComboBox<String> cboMaLT, cboTenMH, cboTenPhong, cboTenKyThi, cboLop;
    private JDateChooser dateNgayThi;
    private JSpinner spinGioBatDau, spinGioKetThuc;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnMoi;

    public LichThiPanel() {
        initComponents();
    }

    private void initComponents() {
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout(5, 5));

        String titleText = (Model.Auth.isHocSinh() || Model.Auth.isGiaoVien()) ? "XEM LỊCH THI" : "QUẢN LÝ LỊCH THI";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlFilter.setBorder(new TitledBorder("Bộ Lọc Lịch Thi"));
        
        pnlFilter.add(new JLabel("Kỳ Thi:"));
        cboLocKyThi = new JComboBox<>(); pnlFilter.add(cboLocKyThi);
        
        pnlFilter.add(new JLabel("Môn:"));
        cboLocMon = new JComboBox<>(); pnlFilter.add(cboLocMon);
        
        pnlFilter.add(new JLabel("Phòng:"));
        cboLocPhong = new JComboBox<>(); pnlFilter.add(cboLocPhong);

        pnlFilter.add(new JLabel("Lớp:"));
        cboLocLop = new JComboBox<>(); pnlFilter.add(cboLocLop);
        
        btnLocDanhSach = new JButton("Lọc Danh Sách");
        ButtonStyleHelper.styleButtonFilter(btnLocDanhSach);
        pnlFilter.add(btnLocDanhSach);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm nhanh"));
        pnlSearch.add(new JLabel("Nhập Tên Môn hoặc Kỳ thi:"));
        txtTimKiem = new JTextField(20); pnlSearch.add(txtTimKiem);
        
        btnTimKiem = new JButton("Tìm Kiếm");
        btnXemTatCa = new JButton("Xem Tất Cả");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);
        ButtonStyleHelper.styleButtonView(btnXemTatCa);
        pnlSearch.add(btnTimKiem); pnlSearch.add(btnXemTatCa);
        
        JPanel pnlTools = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlTools.add(pnlFilter);
        pnlTools.add(pnlSearch);

        pnlNorth.add(pnlTools, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"Mã LT", "Kỳ Thi", "Tên Môn", "Lớp", "Ngày Thi", "Bắt Đầu", "Kết Thúc", "Phòng"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thông tin chi tiết"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        gbc.gridx=0; gbc.gridy=0; gbc.weightx = 0; 
        pnlInput.add(new JLabel("Mã LT:"), gbc);
        
        gbc.gridx=1; gbc.gridy=0; gbc.weightx = 1.0; 
        cboMaLT=new JComboBox<>(); cboMaLT.setEditable(true); pnlInput.add(cboMaLT, gbc);
        
        gbc.gridx=2; gbc.gridy=0; gbc.weightx = 0;
        pnlInput.add(new JLabel("Kỳ Thi:"), gbc);
        
        gbc.gridx=3; gbc.gridy=0; gbc.weightx = 1.0; 
        cboTenKyThi = new JComboBox<>(new String[]{"Giữa Kỳ 1", "Cuối Kỳ 1", "Giữa Kỳ 2", "Cuối Kỳ 2"});
        cboTenKyThi.setEditable(true);
        cboTenKyThi.setSelectedIndex(0);
        pnlInput.add(cboTenKyThi, gbc);

        gbc.gridx=0; gbc.gridy=1; gbc.weightx = 0;
        pnlInput.add(new JLabel("Tên Môn:"), gbc);
        
        gbc.gridx=1; gbc.gridy=1; gbc.weightx = 1.0;
        cboTenMH=new JComboBox<>(); cboTenMH.setEditable(true); pnlInput.add(cboTenMH, gbc);

        gbc.gridx=2; gbc.gridy=1; gbc.weightx = 0;
        pnlInput.add(new JLabel("Ngày Thi (yyyy-mm-dd):"), gbc);
        
        gbc.gridx=3; gbc.gridy=1; gbc.weightx = 1.0;
        dateNgayThi=new JDateChooser(); 
        dateNgayThi.setDateFormatString("dd/MM/yyyy");
        pnlInput.add(dateNgayThi, gbc);

        SpinnerDateModel modelBatDau = new SpinnerDateModel();
        spinGioBatDau = new JSpinner(modelBatDau);
        JSpinner.DateEditor editorBatDau = new JSpinner.DateEditor(spinGioBatDau, "HH:mm");
        spinGioBatDau.setEditor(editorBatDau);

        SpinnerDateModel modelKetThuc = new SpinnerDateModel();
        spinGioKetThuc = new JSpinner(modelKetThuc);
        JSpinner.DateEditor editorKetThuc = new JSpinner.DateEditor(spinGioKetThuc, "HH:mm");
        spinGioKetThuc.setEditor(editorKetThuc);

        gbc.gridx=0; gbc.gridy=2; gbc.weightx = 0;
        pnlInput.add(new JLabel("Giờ Bắt Đầu:"), gbc);
        
        gbc.gridx=1; gbc.gridy=2; gbc.weightx = 1.0;
        pnlInput.add(spinGioBatDau, gbc);

        gbc.gridx=2; gbc.gridy=2; gbc.weightx = 0;
        pnlInput.add(new JLabel("Giờ Kết Thúc:"), gbc);
        
        gbc.gridx=3; gbc.gridy=2; gbc.weightx = 1.0;
        pnlInput.add(spinGioKetThuc, gbc);

        gbc.gridx=0; gbc.gridy=3; gbc.weightx = 0;
        pnlInput.add(new JLabel("Tên Phòng:"), gbc);
        
        gbc.gridx=1; gbc.gridy=3; gbc.weightx = 1.0;
        cboTenPhong=new JComboBox<>(); cboTenPhong.setEditable(true); pnlInput.add(cboTenPhong, gbc);

        gbc.gridx=2; gbc.gridy=3; gbc.weightx = 0;
        pnlInput.add(new JLabel("Lớp:"), gbc);

        gbc.gridx=3; gbc.gridy=3; gbc.weightx = 1.0;
        cboLop = new JComboBox<>(); cboLop.setEditable(true); pnlInput.add(cboLop, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
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
        btnMoi = new JButton("Làm Mới");
        ButtonStyleHelper.styleButtonView(btnMoi);
        
        Dimension sz = new Dimension(90, 35);
        btnThem.setPreferredSize(sz);
        btnSua.setPreferredSize(sz);
        btnXoa.setPreferredSize(sz);
        btnLuu.setPreferredSize(sz);
        btnHuy.setPreferredSize(sz);
        btnMoi.setPreferredSize(sz);
        
        pnlBtn.add(btnThem);
        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);
        pnlBtn.add(btnLuu);
        pnlBtn.add(btnHuy);
        pnlBtn.add(btnMoi);

        btnXuatExcel = new JButton("Xuất Excel");
        ButtonStyleHelper.styleButtonExport(btnXuatExcel);
        btnXuatExcel.setPreferredSize(new Dimension(120, 35));
        pnlBtn.add(btnXuatExcel);
        
        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        
        add(pnlSouth, BorderLayout.SOUTH);
        
        if (Model.Auth.isHocSinh() || Model.Auth.isGiaoVien()) {
            pnlSouth.setVisible(false);
        }

        setCrudButtonState(true, false, false, false, false);
    }

    public String getKyThiFilter() { return cboLocKyThi.getSelectedItem() != null ? cboLocKyThi.getSelectedItem().toString() : ""; }
    public String getMonFilter() { return cboLocMon.getSelectedItem() != null ? cboLocMon.getSelectedItem().toString() : ""; }
    public String getPhongFilter() { return cboLocPhong.getSelectedItem() != null ? cboLocPhong.getSelectedItem().toString() : ""; }
    public String getLopFilter() {
        if (cboLocLop.getSelectedItem() == null) return "";
        String val = cboLocLop.getSelectedItem().toString();
        if (val.equals("Tất cả")) return "";
        return val;
    }

    public String getKeyword() { return txtTimKiem.getText().trim(); }

    public void setKyThiData(List<String> kyThis) {
        cboLocKyThi.removeAllItems();
        cboLocKyThi.addItem("");

        for (String k : kyThis) {
            cboLocKyThi.addItem(k);
            boolean exists = false;
            for(int i = 0; i < cboTenKyThi.getItemCount(); i++) {
                if(k.equals(cboTenKyThi.getItemAt(i))) { exists = true; break; }
            }
            if(!exists) cboTenKyThi.addItem(k);
        }
    }

    public void setMonHocData(List<String> monHocs) {
        cboLocMon.removeAllItems();
        cboTenMH.removeAllItems();
        cboLocMon.addItem("");
        for (String m : monHocs) {
            cboLocMon.addItem(m);
            cboTenMH.addItem(m);
        }
    }

    public void setPhongHocData(List<String> phongs) {
        cboLocPhong.removeAllItems();
        cboTenPhong.removeAllItems();
        cboLocPhong.addItem("");
        for (String p : phongs) {
            cboLocPhong.addItem(p);
            cboTenPhong.addItem(p);
        }
    }

    public void setLopData(List<String> lops) {
        cboLocLop.removeAllItems();
        cboLop.removeAllItems();
        cboLocLop.addItem("Tất cả");
        for (String l : lops) {
            cboLocLop.addItem(l);
            cboLop.addItem(l);
        }
    }
    
    public void setMaLTData(List<Integer> listLT) {
        cboMaLT.removeAllItems();
        for(Integer id : listLT) {
            cboMaLT.addItem(String.valueOf(id));
        }
        cboMaLT.setSelectedItem("");
    }
    
    public LichThi getLichThiInput() {
        LichThi lt = new LichThi();
        
        Object selectedMaLT = cboMaLT.getSelectedItem();
        String maLTStr = selectedMaLT != null ? selectedMaLT.toString() : "";
        if(cboMaLT.getEditor().getItem() != null) {
            maLTStr = cboMaLT.getEditor().getItem().toString();
        }
        
        if(!maLTStr.isEmpty()) {
            try { lt.setMaLT(Integer.parseInt(maLTStr)); } catch (Exception e) {}
        }
        
        Object kyThiObj = cboTenKyThi.getEditor().getItem();
        lt.setTenKyThi(kyThiObj != null ? kyThiObj.toString() : "");
        
        Object monHocObj = cboTenMH.getEditor().getItem();
        lt.setMaMH(monHocObj != null ? monHocObj.toString() : "");
        
        java.util.Date d = dateNgayThi.getDate();
        if (d != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            lt.setNgayThi(sdf.format(d));
        } else {
            lt.setNgayThi(((JTextField)dateNgayThi.getDateEditor().getUiComponent()).getText());
        }
        
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm");
        lt.setGioBatDau(timeFormat.format(spinGioBatDau.getValue()));
        lt.setGioKetThuc(timeFormat.format(spinGioKetThuc.getValue()));
        
        lt.setMaPhong(cboTenPhong.getSelectedItem() != null ? cboTenPhong.getSelectedItem().toString() : "");

        Object lopObj = cboLop.getSelectedItem();
        String lopStr = lopObj != null ? lopObj.toString() : "";
        if (cboLop.getEditor().getItem() != null) {
            lopStr = cboLop.getEditor().getItem().toString();
        }
        lt.setMaLop(lopStr);

        return lt;
    }

    private String formatTime(String rawTime) {
        if (rawTime == null) return "";
        if (rawTime.length() >= 5) return rawTime.substring(0, 5); 
        return rawTime;
    }

    private String formatToDDMMYYYY(String yyyyMMdd) {
        if(yyyyMMdd == null || yyyyMMdd.isEmpty()) return "";
        try {
            java.util.Date d = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(yyyyMMdd);
            return new java.text.SimpleDateFormat("dd/MM/yyyy").format(d);
        } catch (Exception e) {
            return yyyyMMdd; 
        }
    }

    public void setTableData(List<LichThi> list) {
        this.cachedList = list;
        model.setRowCount(0);
        for(LichThi lt : list) {
            String tenMon = lt.getTenMH() != null && !lt.getTenMH().isEmpty() ? lt.getTenMH() : lt.getMaMH();
            String lop = lt.getTenLop() != null && !lt.getTenLop().isEmpty() ? lt.getTenLop() : (lt.getMaLop() != null ? lt.getMaLop() : "");
            model.addRow(new Object[]{
                lt.getMaLT(), lt.getTenKyThi(), tenMon, lop, formatToDDMMYYYY(lt.getNgayThi()), 
                formatTime(lt.getGioBatDau()), formatTime(lt.getGioKetThuc()), lt.getMaPhong()
            });
        }
    }

    public void fillForm(int row) {
        if(row >= 0 && cachedList != null && row < cachedList.size()) {
            LichThi lt = cachedList.get(row);
            cboMaLT.setSelectedItem(String.valueOf(lt.getMaLT()));
            cboMaLT.getEditor().setItem(String.valueOf(lt.getMaLT()));
            
            cboTenKyThi.setSelectedItem(lt.getTenKyThi());
            cboTenKyThi.getEditor().setItem(lt.getTenKyThi());
            
            cboTenMH.setSelectedItem(lt.getTenMH() != null ? lt.getTenMH() : lt.getMaMH());
            cboTenMH.getEditor().setItem(lt.getTenMH() != null ? lt.getTenMH() : lt.getMaMH());
            
            try {
                if(lt.getNgayThi() != null && !lt.getNgayThi().isEmpty()) {
                    java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(lt.getNgayThi());
                    dateNgayThi.setDate(date);
                } else {
                    dateNgayThi.setDate(null);
                }
            } catch (Exception e) {}
            
            try {
                if(lt.getGioBatDau() != null && !lt.getGioBatDau().isEmpty()) {
                    java.util.Date time = new java.text.SimpleDateFormat("HH:mm").parse(formatTime(lt.getGioBatDau()));
                    spinGioBatDau.setValue(time);
                }
                if(lt.getGioKetThuc() != null && !lt.getGioKetThuc().isEmpty()) {
                    java.util.Date time = new java.text.SimpleDateFormat("HH:mm").parse(formatTime(lt.getGioKetThuc()));
                    spinGioKetThuc.setValue(time);
                }
            } catch (Exception e) {}
            
            cboTenPhong.setSelectedItem(lt.getMaPhong());
            cboTenPhong.getEditor().setItem(lt.getMaPhong());

            cboLop.setSelectedItem(lt.getMaLop());
            cboLop.getEditor().setItem(lt.getMaLop());
        }
    }

    public void clearForm() {
        cboMaLT.setSelectedItem(""); cboMaLT.getEditor().setItem("");
        cboTenMH.setSelectedItem(""); cboTenMH.getEditor().setItem("");
        dateNgayThi.setDate(null);
        spinGioBatDau.setValue(new java.util.Date()); 
        spinGioKetThuc.setValue(new java.util.Date()); 
        cboTenPhong.setSelectedItem(""); cboTenPhong.getEditor().setItem("");
        cboLop.setSelectedItem(""); cboLop.getEditor().setItem("");
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public JTable getTable() { return table; }

    public void addBtnLocDanhSachListener(ActionListener ac) { btnLocDanhSach.addActionListener(ac); }
    public void addBtnTimKiemListener(ActionListener ac) { btnTimKiem.addActionListener(ac); }
    public void addBtnXemTatCaListener(ActionListener ac) { btnXemTatCa.addActionListener(ac); }
    public void addBtnThemListener(ActionListener ac) { btnThem.addActionListener(ac); }
    public void addBtnSuaListener(ActionListener ac) { btnSua.addActionListener(ac); }
    public void addBtnXoaListener(ActionListener ac) { btnXoa.addActionListener(ac); }
    public void addBtnLuuListener(ActionListener ac) { btnLuu.addActionListener(ac); }
    public void addBtnHuyListener(ActionListener ac) { btnHuy.addActionListener(ac); }
    public void addBtnMoiListener(ActionListener ac) { btnMoi.addActionListener(ac); }
    public void addTableMouseListener(MouseAdapter ad) { table.addMouseListener(ad); }
    public void addBtnXuatExcelListener(ActionListener ac) { btnXuatExcel.addActionListener(ac); }
    
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    
    public JComboBox<String> getCboMaLT() { return cboMaLT; }
    
    public void setFormEnabled(boolean enabled) {
        cboMaLT.setEnabled(enabled);
        cboTenMH.setEnabled(enabled);
        dateNgayThi.setEnabled(enabled);
        spinGioBatDau.setEnabled(enabled);
        spinGioKetThuc.setEnabled(enabled);
        cboTenPhong.setEnabled(enabled);
        cboTenKyThi.setEnabled(enabled);
        cboLop.setEnabled(enabled);
    }
    
    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them);
        btnSua.setEnabled(sua);
        btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu);
        btnHuy.setEnabled(huy);
        
        if (luu && !sua && !xoa) {
            cboMaLT.setEditable(true);
            cboMaLT.setEnabled(true);
        } else {
            cboMaLT.setEditable(false);
            cboMaLT.setEnabled(false);
        }
        
        setFormEnabled(luu);
    }
}

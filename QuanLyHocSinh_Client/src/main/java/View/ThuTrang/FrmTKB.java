package View.ThuTrang;

import Model.TKB;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.Map;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class FrmTKB extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> cboLocMaLop, cboLocThu, cboLocNamHoc, cboLocHocKy;
    private JTextField txtLocMon;
    private JButton btnXemDanhSach, btnLocTimKiem;

    private JComboBox<String> cboMaLop, cboMaMH, cboMaGV, cboMaPhong;
    private JComboBox<Integer> cboThuThem, cboTietBD, cboTietKT, cboHocKyThem;
    private JComboBox<String> cboNamBatDau, cboNamKetThuc;

    private List<Map<String, String>> danhSachLop;
    private List<Map<String, String>> danhSachMon;
    private List<Map<String, String>> danhSachGV;
    private List<Map<String, String>> danhSachPhong;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnMoi, btnXuatExcel;
    private JPanel pnlView;
    private boolean isUpdatingForm = false;

    public FrmTKB() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        String titleText = Model.Auth.isHocSinh() ? "THỜI KHÓA BIỂU" : "THỜI KHÓA BIỂU / LỊCH DẠY";
        JLabel title = new JLabel(titleText, JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnlNorth.add(title, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        pnlSearch.setBorder(new TitledBorder("Bộ lọc & Tìm kiếm"));

        JLabel lblLop = new JLabel("Lớp:");
        cboLocMaLop = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboLocMaLop);
        cboLocMaLop.addItem("Tất cả");
        pnlSearch.add(lblLop);
        pnlSearch.add(cboLocMaLop);

        pnlSearch.add(new JLabel("Môn học:"));
        txtLocMon = new JTextField(10);
        pnlSearch.add(txtLocMon);

        pnlSearch.add(new JLabel("Thứ:"));
        cboLocThu = new JComboBox<>(new String[]{"Tất cả", "2", "3", "4", "5", "6", "7"});
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboLocThu);
        pnlSearch.add(cboLocThu);

        int curYear = java.time.Year.now().getValue();
        int totalYears = curYear >= 2000 ? (curYear - 2000 + 1) : 1;
        String[] yearList = new String[totalYears];
        for (int y = 2000, i = 0; y <= curYear; y++, i++) {
            yearList[i] = String.valueOf(y);
        }

        pnlSearch.add(new JLabel("Năm học:"));
        cboLocNamHoc = new JComboBox<>();
        cboLocNamHoc.addItem("Tất cả");
        pnlSearch.add(cboLocNamHoc);

        pnlSearch.add(new JLabel("Học kỳ:"));
        cboLocHocKy = new JComboBox<>(new String[]{"Tất cả", "1", "2"});
        pnlSearch.add(cboLocHocKy);

        btnLocTimKiem = new JButton("Lọc kết quả");
        ButtonStyleHelper.styleButtonFilter(btnLocTimKiem);
        pnlSearch.add(btnLocTimKiem);

        pnlNorth.add(pnlSearch, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Lớp", "Mã MH", "Tên MH", "Tên GV", "Phòng", "Thứ", "Tiết BD", "Tiết KT", "Năm Học", "Học Kỳ"}, 0
        );
        table = new JTable(model);
        table.removeColumn(table.getColumnModel().getColumn(2));
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(140);
        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(5).setPreferredWidth(50);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
        table.getColumnModel().getColumn(7).setPreferredWidth(60);
        table.getColumnModel().getColumn(8).setPreferredWidth(90);
        table.getColumnModel().getColumn(9).setPreferredWidth(65);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Lớp
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Phòng
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Thứ
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Tiết BD
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Tiết KT
        table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer); // Năm Học
        table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer); // Học Kỳ

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thêm / Cập nhật TKB"));

        JPanel pnlInput = new JPanel(new GridLayout(5, 4, 10, 8));

        cboMaLop = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboMaLop);
        cboMaMH = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboMaMH);
        cboMaGV = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboMaGV);
        cboMaPhong = new JComboBox<>();
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboMaPhong);
        cboThuThem = new JComboBox<>(new Integer[]{2, 3, 4, 5, 6, 7});
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboThuThem);

        Integer[] tiet = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        cboTietBD = new JComboBox<>(tiet);
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboTietBD);
        cboTietKT = new JComboBox<>(tiet);
        TienIch.ComboBoxUtil.makeSearchableAndEditable(cboTietKT);

        cboHocKyThem = new JComboBox<>(new Integer[]{1, 2});

        cboNamBatDau = new JComboBox<>(yearList);
        cboNamKetThuc = new JComboBox<>(yearList);
        cboNamBatDau.setEditable(true);
        cboNamKetThuc.setEditable(true);
        cboNamBatDau.setSelectedItem(String.valueOf(curYear));
        cboNamKetThuc.setSelectedItem(String.valueOf(curYear)); // Gợi ý năm kết thúc bằng năm bắt đầu

        // Báo lỗi ngay nếu năm kết thúc nhỏ hơn năm bắt đầu
        cboNamBatDau.addActionListener(e -> kiemTraNamHoc());
        cboNamKetThuc.addActionListener(e -> kiemTraNamHoc());

        pnlInput.add(new JLabel("Lớp"));          pnlInput.add(cboMaLop);
        pnlInput.add(new JLabel("Môn học"));      pnlInput.add(cboMaMH);
        pnlInput.add(new JLabel("Giáo viên"));    pnlInput.add(cboMaGV);
        pnlInput.add(new JLabel("Phòng"));        pnlInput.add(cboMaPhong);
        pnlInput.add(new JLabel("Thứ"));          pnlInput.add(cboThuThem);
        pnlInput.add(new JLabel("Học kỳ"));       pnlInput.add(cboHocKyThem);
        pnlInput.add(new JLabel("Tiết BD"));      pnlInput.add(cboTietBD);
        pnlInput.add(new JLabel("Tiết KT"));      pnlInput.add(cboTietKT);
        pnlInput.add(new JLabel("Năm bắt đầu"));  pnlInput.add(cboNamBatDau);
        pnlInput.add(new JLabel("Năm kết thúc")); pnlInput.add(cboNamKetThuc);
        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnThem = new JButton("Thêm"); ButtonStyleHelper.styleButtonAdd(btnThem);
        btnSua = new JButton("Sửa"); ButtonStyleHelper.styleButtonEdit(btnSua);
        btnXoa = new JButton("Xóa"); ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnLuu = new JButton("Lưu"); ButtonStyleHelper.styleButtonSave(btnLuu);
        btnHuy = new JButton("Hủy"); ButtonStyleHelper.styleButtonCancel(btnHuy);
        btnMoi = new JButton("Làm Mới"); ButtonStyleHelper.styleButtonView(btnMoi);
        btnXuatExcel = new JButton("Xuất Excel"); ButtonStyleHelper.styleButtonExport(btnXuatExcel);

        Dimension sz = new Dimension(90, 35);
        btnThem.setPreferredSize(sz); btnSua.setPreferredSize(sz); btnXoa.setPreferredSize(sz);
        btnLuu.setPreferredSize(sz); btnHuy.setPreferredSize(sz); btnMoi.setPreferredSize(sz);
        btnXuatExcel.setPreferredSize(new Dimension(120, 35));

        pnlBtn.add(btnThem); pnlBtn.add(btnSua); pnlBtn.add(btnXoa); pnlBtn.add(btnLuu);
        pnlBtn.add(btnHuy); pnlBtn.add(btnMoi); pnlBtn.add(btnXuatExcel);
        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        if (Model.Auth.isHocSinh()) {
            pnlSouth.setVisible(false);
            lblLop.setVisible(false);
            cboLocMaLop.setVisible(false);
        } else if (Model.Auth.isGiaoVien()) {
            pnlSouth.setVisible(false);
        }
        setCrudButtonState(true, false, false, false, false);
    }

    public void setDanhSachLop(List<Map<String, String>> list) {
        this.danhSachLop = list;
        cboMaLop.removeAllItems();
        cboLocMaLop.removeAllItems();
        cboLocMaLop.addItem("Tất cả");
        for (Map<String, String> m : list) {
            cboMaLop.addItem(m.get("ten"));
            cboLocMaLop.addItem(m.get("ten"));
        }
    }

    public void setDanhSachMon(List<Map<String, String>> list) {
        this.danhSachMon = list;
        cboMaMH.removeAllItems();
        for (Map<String, String> m : list) cboMaMH.addItem(m.get("ten"));
    }

    public void setDanhSachGV(List<Map<String, String>> list) {
        this.danhSachGV = list;
        cboMaGV.removeAllItems();
        for (Map<String, String> m : list) cboMaGV.addItem(m.get("ten"));
    }

    public void setDanhSachPhong(List<Map<String, String>> list) {
        this.danhSachPhong = list;
        cboMaPhong.removeAllItems();
        for (Map<String, String> m : list) cboMaPhong.addItem(m.get("ten"));
    }

    private String getMaFromTen(List<Map<String, String>> list, String ten) {
        return list.stream()
                .filter(m -> m.get("ten").equals(ten))
                .map(m -> m.get("ma"))
                .findFirst().orElse("");
    }

    public TKB getTKBInput() {
        TKB t = new TKB();
        t.setMaLop(getMaFromTen(danhSachLop, (String) cboMaLop.getSelectedItem()));
        t.setMaMH(getMaFromTen(danhSachMon, (String) cboMaMH.getSelectedItem()));
        t.setMaGV(getMaFromTen(danhSachGV, (String) cboMaGV.getSelectedItem()));
        t.setMaPhong(getMaFromTen(danhSachPhong, (String) cboMaPhong.getSelectedItem()));
        t.setThu((Integer) cboThuThem.getSelectedItem());
        t.setTietBatDau((Integer) cboTietBD.getSelectedItem());
        t.setTietKetThuc((Integer) cboTietKT.getSelectedItem());
        String namBD = cboNamBatDau.getSelectedItem() != null ? cboNamBatDau.getSelectedItem().toString().trim() : "2023";
        String namKT = cboNamKetThuc.getSelectedItem() != null ? cboNamKetThuc.getSelectedItem().toString().trim() : "2024";
        t.setNamHoc(namBD + "-" + namKT);
        t.setHocKy((Integer) cboHocKyThem.getSelectedItem());
        return t;
    }

    public void setTableData(List<TKB> list) {
        model.setRowCount(0);
        java.util.Set<String> namHocSet = new java.util.TreeSet<>(java.util.Collections.reverseOrder());
        for (TKB t : list) {
            String tenGV = (danhSachGV != null) ? danhSachGV.stream()
                    .filter(m -> m.get("ma").equals(t.getMaGV()))
                    .map(m -> m.get("ten")).findFirst().orElse(t.getMaGV()) : t.getMaGV();

            model.addRow(new Object[]{
                    t.getMaTKB(), t.getMaLop(), t.getMaMH(), t.getTenMH(),
                    tenGV, t.getMaPhong(), t.getThu(), t.getTietBatDau(), t.getTietKetThuc(),
                    t.getNamHoc(), t.getHocKy()
            });

            if (t.getNamHoc() != null && !t.getNamHoc().trim().isEmpty()) {
                namHocSet.add(t.getNamHoc().trim());
            }
        }

        // Tự động bổ sung các năm học có trong dữ liệu cột Năm Học vào bộ lọc
        for (String nh : namHocSet) {
            boolean exists = false;
            for (int i = 0; i < cboLocNamHoc.getItemCount(); i++) {
                if (nh.equalsIgnoreCase(cboLocNamHoc.getItemAt(i))) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                cboLocNamHoc.addItem(nh);
            }
        }
    }

    public void fillForm(int viewRow) {
        if (viewRow < 0) return;
        isUpdatingForm = true;
        try {
            int row = table.convertRowIndexToModel(viewRow);

            String tenLop = model.getValueAt(row, 1).toString();
            String tenMH = model.getValueAt(row, 3).toString();
            String tenGV = model.getValueAt(row, 4).toString();
            String tenPhong = model.getValueAt(row, 5).toString();

            cboMaLop.setSelectedItem(tenLop);
            cboMaMH.setSelectedItem(tenMH);
            cboMaGV.setSelectedItem(tenGV);
            cboMaPhong.setSelectedItem(tenPhong);
            cboThuThem.setSelectedItem(Integer.parseInt(model.getValueAt(row, 6).toString()));
            cboTietBD.setSelectedItem(Integer.parseInt(model.getValueAt(row, 7).toString()));
            cboTietKT.setSelectedItem(Integer.parseInt(model.getValueAt(row, 8).toString()));

            Object valNamHoc = model.getValueAt(row, 9);
            int cur = java.time.Year.now().getValue();
            if (valNamHoc != null && !valNamHoc.toString().isEmpty()) {
                String nh = valNamHoc.toString().trim();
                String[] parts = nh.split("-");
                if (parts.length == 2) {
                    cboNamBatDau.setSelectedItem(parts[0].trim());
                    cboNamKetThuc.setSelectedItem(parts[1].trim());
                } else {
                    cboNamBatDau.setSelectedItem(nh);
                    cboNamKetThuc.setSelectedItem(nh);
                }
            } else {
                cboNamBatDau.setSelectedItem(String.valueOf(cur));
                cboNamKetThuc.setSelectedItem(String.valueOf(cur));
            }

            Object valHocKy = model.getValueAt(row, 10);
            if (valHocKy != null) {
                try {
                    int hk = Integer.parseInt(valHocKy.toString());
                    cboHocKyThem.setSelectedItem(hk > 0 ? hk : 1);
                } catch (Exception ex) {
                    cboHocKyThem.setSelectedIndex(0);
                }
            }
        } finally {
            isUpdatingForm = false;
        }
    }

    public void clearForm() {
        isUpdatingForm = true;
        try {
            if (cboMaLop.getItemCount() > 0) cboMaLop.setSelectedIndex(0);
            if (cboMaMH.getItemCount() > 0) cboMaMH.setSelectedIndex(0);
            if (cboMaGV.getItemCount() > 0) cboMaGV.setSelectedIndex(0);
            if (cboMaPhong.getItemCount() > 0) cboMaPhong.setSelectedIndex(0);
            cboThuThem.setSelectedIndex(0);
            cboTietBD.setSelectedIndex(0);
            cboTietKT.setSelectedIndex(0);
            int cur = java.time.Year.now().getValue();
            cboNamBatDau.setSelectedItem(String.valueOf(cur));
            cboNamKetThuc.setSelectedItem(String.valueOf(cur)); // Gợi ý năm kết thúc bằng năm bắt đầu
            cboHocKyThem.setSelectedIndex(0);
        } finally {
            isUpdatingForm = false;
        }
    }

    public boolean kiemTraNamHoc() {
        if (isUpdatingForm) return true;
        try {
            if (cboNamBatDau.getSelectedItem() == null || cboNamKetThuc.getSelectedItem() == null) return true;
            String strBD = cboNamBatDau.getSelectedItem().toString().trim();
            String strKT = cboNamKetThuc.getSelectedItem().toString().trim();
            if (strBD.isEmpty() || strKT.isEmpty()) return true;

            int bd = Integer.parseInt(strBD);
            int kt = Integer.parseInt(strKT);
            if (kt < bd) {
                showMessage("Lỗi: Năm kết thúc (" + kt + ") nhỏ hơn năm bắt đầu (" + bd + ")!\nNăm kết thúc phải lớn hơn hoặc bằng năm bắt đầu.");
                return false;
            }
        } catch (NumberFormatException ignored) {}
        return true;
    }

    public JTable getTable() { return table; }
    public String getLocMaLop() { return cboLocMaLop.getSelectedItem().toString(); }
    public String getLocMon() { return txtLocMon.getText().trim(); }
    public int getLocThu() {
        if (cboLocThu.getSelectedIndex() == 0) return 0;
        return Integer.parseInt(cboLocThu.getSelectedItem().toString());
    }
    public String getLocNamHoc() {
        if (cboLocNamHoc.getSelectedIndex() <= 0) return "";
        return cboLocNamHoc.getSelectedItem().toString();
    }
    public int getLocHocKy() {
        if (cboLocHocKy.getSelectedIndex() <= 0) return 0;
        return Integer.parseInt(cboLocHocKy.getSelectedItem().toString());
    }

    public String getNamBatDau() {
        return cboNamBatDau.getSelectedItem() != null ? cboNamBatDau.getSelectedItem().toString().trim() : "";
    }

    public String getNamKetThuc() {
        return cboNamKetThuc.getSelectedItem() != null ? cboNamKetThuc.getSelectedItem().toString().trim() : "";
    }

    public void setCboLocMaLop(List<String> listLop) {
        cboLocMaLop.removeAllItems();
        cboLocMaLop.addItem("Tất cả");
        for (String lop : listLop) cboLocMaLop.addItem(lop);
    }

    public void setCboLocNamHoc(List<String> listNamHoc) {
        String selected = cboLocNamHoc.getSelectedItem() != null ? cboLocNamHoc.getSelectedItem().toString() : "Tất cả";
        cboLocNamHoc.removeAllItems();
        cboLocNamHoc.addItem("Tất cả");
        if (listNamHoc != null) {
            for (String nh : listNamHoc) {
                if (nh != null && !nh.trim().isEmpty()) {
                    cboLocNamHoc.addItem(nh.trim());
                }
            }
        }
        cboLocNamHoc.setSelectedItem(selected);
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }

    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them); btnSua.setEnabled(sua); btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu); btnHuy.setEnabled(huy);
    }

    public void addBtnXemDanhSachListener(ActionListener l) { btnXemDanhSach.addActionListener(l); }
    public void addBtnLocTimKiemListener(ActionListener l) { btnLocTimKiem.addActionListener(l); }
    public void addLocMonLiveListener(javax.swing.event.DocumentListener l) {
        txtLocMon.getDocument().addDocumentListener(l);
    }
    public void addCboLocMaLopListener(ActionListener l) { cboLocMaLop.addActionListener(l); }
    public void addCboLocThuListener(ActionListener l) { cboLocThu.addActionListener(l); }
    public void addCboLocNamHocListener(ActionListener l) { cboLocNamHoc.addActionListener(l); }
    public void addCboLocHocKyListener(ActionListener l) { cboLocHocKy.addActionListener(l); }
    public void addBtnThemListener(ActionListener l) { btnThem.addActionListener(l); }
    public void addBtnSuaListener(ActionListener l) { btnSua.addActionListener(l); }
    public void addBtnXoaListener(ActionListener l) { btnXoa.addActionListener(l); }
    public void addBtnLuuListener(ActionListener l) { btnLuu.addActionListener(l); }
    public void addBtnHuyListener(ActionListener l) { btnHuy.addActionListener(l); }
    public void addBtnMoiListener(ActionListener l) { btnMoi.addActionListener(l); }
    public void addBtnXuatExcelListener(ActionListener l) { btnXuatExcel.addActionListener(l); }
    public void addTableMouseListener(MouseAdapter l) { table.addMouseListener(l); }
}
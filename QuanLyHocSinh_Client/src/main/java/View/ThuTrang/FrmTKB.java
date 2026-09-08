package View.ThuTrang;

import Model.TKB;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
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

    private JComboBox<String> cboLocMaLop, cboLocThu;
    private JTextField txtLocMon;
    private JButton btnXemDanhSach, btnLocTimKiem;

    private JComboBox<String> cboMaLop, cboMaMH, cboMaGV, cboMaPhong;
    private JComboBox<Integer> cboThuThem, cboTietBD, cboTietKT;

    private List<Map<String, String>> danhSachLop;
    private List<Map<String, String>> danhSachMon;
    private List<Map<String, String>> danhSachGV;
    private List<Map<String, String>> danhSachPhong;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnMoi, btnXuatExcel;
    private JPanel pnlView;

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

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlSearch.setBorder(new TitledBorder("Bộ lọc & Tìm kiếm"));

        JLabel lblLop = new JLabel("Lớp:");
        cboLocMaLop = new JComboBox<>();
        cboLocMaLop.addItem("Tất cả");
        pnlSearch.add(lblLop);
        pnlSearch.add(cboLocMaLop);

        pnlSearch.add(new JLabel("Môn học:"));
        txtLocMon = new JTextField(12);
        pnlSearch.add(txtLocMon);

        pnlSearch.add(new JLabel("Thứ:"));
        cboLocThu = new JComboBox<>(new String[]{"Tất cả", "2", "3", "4", "5", "6", "7"});
        pnlSearch.add(cboLocThu);

        btnLocTimKiem = new JButton("Lọc kết quả");
        ButtonStyleHelper.styleButtonFilter(btnLocTimKiem);
        pnlSearch.add(btnLocTimKiem);

        pnlNorth.add(pnlSearch, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID", "Lớp", "Mã MH", "Tên MH", "Tên GV", "Phòng", "Thứ", "Tiết BD", "Tiết KT"}, 0
        );
        table = new JTable(model);
        table.removeColumn(table.getColumnModel().getColumn(2));
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thêm / Cập nhật TKB"));

        JPanel pnlInput = new JPanel(new GridLayout(4, 4, 10, 8));

        cboMaLop = new JComboBox<>();
        cboMaMH = new JComboBox<>();
        cboMaGV = new JComboBox<>();
        cboMaPhong = new JComboBox<>();
        cboThuThem = new JComboBox<>(new Integer[]{2, 3, 4, 5, 6, 7});

        Integer[] tiet = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        cboTietBD = new JComboBox<>(tiet);
        cboTietKT = new JComboBox<>(tiet);

        pnlInput.add(new JLabel("Lớp"));       pnlInput.add(cboMaLop);
        pnlInput.add(new JLabel("Môn học"));   pnlInput.add(cboMaMH);
        pnlInput.add(new JLabel("Giáo viên")); pnlInput.add(cboMaGV);
        pnlInput.add(new JLabel("Phòng"));     pnlInput.add(cboMaPhong);
        pnlInput.add(new JLabel("Thứ"));       pnlInput.add(cboThuThem);
        pnlInput.add(new JLabel("Tiết BD"));   pnlInput.add(cboTietBD);
        pnlInput.add(new JLabel("Tiết KT"));   pnlInput.add(cboTietKT);
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
        return t;
    }

    public void setTableData(List<TKB> list) {
        model.setRowCount(0);
        for (TKB t : list) {
            String tenGV = (danhSachGV != null) ? danhSachGV.stream()
                    .filter(m -> m.get("ma").equals(t.getMaGV()))
                    .map(m -> m.get("ten")).findFirst().orElse(t.getMaGV()) : t.getMaGV();

            model.addRow(new Object[]{
                    t.getMaTKB(), t.getMaLop(), t.getMaMH(), t.getTenMH(),
                    tenGV, t.getMaPhong(), t.getThu(), t.getTietBatDau(), t.getTietKetThuc()
            });
        }
    }

    public void fillForm(int viewRow) {
        if (viewRow < 0) return;
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
    }

    public void clearForm() {
        if (cboMaLop.getItemCount() > 0) cboMaLop.setSelectedIndex(0);
        if (cboMaMH.getItemCount() > 0) cboMaMH.setSelectedIndex(0);
        if (cboMaGV.getItemCount() > 0) cboMaGV.setSelectedIndex(0);
        if (cboMaPhong.getItemCount() > 0) cboMaPhong.setSelectedIndex(0);
        cboThuThem.setSelectedIndex(0);
        cboTietBD.setSelectedIndex(0);
        cboTietKT.setSelectedIndex(0);
    }

    public JTable getTable() { return table; }
    public String getLocMaLop() { return cboLocMaLop.getSelectedItem().toString(); }
    public String getLocMon() { return txtLocMon.getText().trim(); }
    public int getLocThu() {
        if (cboLocThu.getSelectedIndex() == 0) return 0;
        return Integer.parseInt(cboLocThu.getSelectedItem().toString());
    }

    public void setCboLocMaLop(List<String> listLop) {
        cboLocMaLop.removeAllItems();
        cboLocMaLop.addItem("Tất cả");
        for (String lop : listLop) cboLocMaLop.addItem(lop);
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
    public void addBtnThemListener(ActionListener l) { btnThem.addActionListener(l); }
    public void addBtnSuaListener(ActionListener l) { btnSua.addActionListener(l); }
    public void addBtnXoaListener(ActionListener l) { btnXoa.addActionListener(l); }
    public void addBtnLuuListener(ActionListener l) { btnLuu.addActionListener(l); }
    public void addBtnHuyListener(ActionListener l) { btnHuy.addActionListener(l); }
    public void addBtnMoiListener(ActionListener l) { btnMoi.addActionListener(l); }
    public void addBtnXuatExcelListener(ActionListener l) { btnXuatExcel.addActionListener(l); }
    public void addTableMouseListener(MouseAdapter l) { table.addMouseListener(l); }
}
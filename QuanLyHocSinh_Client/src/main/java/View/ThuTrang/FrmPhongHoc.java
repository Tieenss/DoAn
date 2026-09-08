package View.ThuTrang;

import Model.PhongHoc;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class FrmPhongHoc extends JPanel {

    private JTextField txtMaPhongTim;
    private JComboBox<String> cboLoaiPhongTim, cboTinhTrangTim;
    private JButton btnTim;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtMaPhong, txtTenPhong, txtSucChua;
    private JComboBox<String> cboLoaiPhong, cboTinhTrang;
    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy;

    public FrmPhongHoc() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ PHÒNG HỌC", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm"));

        pnlSearch.add(new JLabel("Mã phòng:"));
        txtMaPhongTim = new JTextField(10);
        pnlSearch.add(txtMaPhongTim);

        pnlSearch.add(new JLabel("Loại phòng:"));
        cboLoaiPhongTim = new JComboBox<>(new String[]{"Tất cả", "Lý thuyết", "Thực hành"});
        pnlSearch.add(cboLoaiPhongTim);

        pnlSearch.add(new JLabel("Tình trạng:"));
        cboTinhTrangTim = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đang học", "Bảo trì"});
        pnlSearch.add(cboTinhTrangTim);

        btnTim = new JButton("Tìm");
        ButtonStyleHelper.styleButtonSearch(btnTim);
        pnlSearch.add(btnTim);

        pnlNorth.add(pnlSearch, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"Mã phòng", "Tên phòng", "Sức chứa", "Loại", "Tình trạng"}, 0);
        table = new JTable(model);
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Cập nhật phòng học"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaPhong  = new JTextField(15);
        txtTenPhong = new JTextField(35);
        txtSucChua  = new JTextField(15);
        cboLoaiPhong = new JComboBox<>(new String[]{"Lý thuyết", "Thực hành"});
        cboTinhTrang = new JComboBox<>(new String[]{"Trống", "Đang học", "Bảo trì"});

        gbc.gridx = 0; gbc.gridy = 0; pnlInput.add(new JLabel("Mã phòng"), gbc);
        gbc.gridx = 1; pnlInput.add(txtMaPhong, gbc);
        gbc.gridx = 2; pnlInput.add(new JLabel("Tên phòng"), gbc);
        gbc.gridx = 3; pnlInput.add(txtTenPhong, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlInput.add(new JLabel("Sức chứa"), gbc);
        gbc.gridx = 1; pnlInput.add(txtSucChua, gbc);
        gbc.gridx = 2; pnlInput.add(new JLabel("Loại phòng"), gbc);
        gbc.gridx = 3; pnlInput.add(cboLoaiPhong, gbc);

        gbc.gridx = 0; gbc.gridy = 2; pnlInput.add(new JLabel("Tình trạng"), gbc);
        gbc.gridx = 1; pnlInput.add(cboTinhTrang, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout());
        btnThem = new JButton("Thêm"); ButtonStyleHelper.styleButtonAdd(btnThem);
        btnSua  = new JButton("Sửa");  ButtonStyleHelper.styleButtonEdit(btnSua);
        btnXoa  = new JButton("Xóa");  ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnLuu  = new JButton("Lưu");  ButtonStyleHelper.styleButtonSave(btnLuu);
        btnHuy  = new JButton("Hủy");  ButtonStyleHelper.styleButtonCancel(btnHuy);

        Dimension sz = new Dimension(90, 35);
        btnThem.setPreferredSize(sz); btnSua.setPreferredSize(sz);
        btnXoa.setPreferredSize(sz);  btnLuu.setPreferredSize(sz);
        btnHuy.setPreferredSize(sz);

        pnlBtn.add(btnThem); pnlBtn.add(btnSua); pnlBtn.add(btnXoa);
        pnlBtn.add(btnLuu);  pnlBtn.add(btnHuy);
        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        setCrudButtonState(true, false, false, false, false);
    }

    public String getMaPhongTim()  { return txtMaPhongTim.getText().trim(); }
    public String getLoaiPhongTim() { return cboLoaiPhongTim.getSelectedItem().toString(); }
    public String getTinhTrangTim() { return cboTinhTrangTim.getSelectedItem().toString(); }

    public PhongHoc getPhongHocInput() {
        PhongHoc p = new PhongHoc();
        p.setMaPhong(txtMaPhong.getText().trim());
        p.setTenPhong(txtTenPhong.getText().trim());
        p.setSucChua(Integer.parseInt(txtSucChua.getText()));
        p.setLoaiPhong(cboLoaiPhong.getSelectedItem().toString());
        p.setTinhTrang(cboTinhTrang.getSelectedItem().toString());
        return p;
    }

    public void setTableData(List<PhongHoc> list) {
        model.setRowCount(0);
        for (PhongHoc p : list)
            model.addRow(new Object[]{p.getMaPhong(), p.getTenPhong(),
                    p.getSucChua(), p.getLoaiPhong(), p.getTinhTrang()});
    }

    public JTable getTable() { return table; }

    public void fillForm(int row) {
        if (row < 0) return;
        txtMaPhong.setText(model.getValueAt(row, 0).toString());
        txtTenPhong.setText(model.getValueAt(row, 1).toString());
        txtSucChua.setText(model.getValueAt(row, 2).toString());
        cboLoaiPhong.setSelectedItem(model.getValueAt(row, 3).toString());
        cboTinhTrang.setSelectedItem(model.getValueAt(row, 4).toString());
    }

    public void clearForm() {
        txtMaPhong.setText(""); txtTenPhong.setText(""); txtSucChua.setText("");
        cboLoaiPhong.setSelectedIndex(0); cboTinhTrang.setSelectedIndex(0);
    }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }

    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua()  { return btnSua; }
    public JButton getBtnXoa()  { return btnXoa; }
    public JButton getBtnLuu()  { return btnLuu; }
    public JButton getBtnHuy()  { return btnHuy; }

    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them); btnSua.setEnabled(sua); btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu);   btnHuy.setEnabled(huy);
    }

    public void addBtnTimListener(ActionListener l)     { btnTim.addActionListener(l); }
    public void addBtnThemListener(ActionListener l)    { btnThem.addActionListener(l); }
    public void addBtnSuaListener(ActionListener l)     { btnSua.addActionListener(l); }
    public void addBtnXoaListener(ActionListener l)     { btnXoa.addActionListener(l); }
    public void addBtnLuuListener(ActionListener l)     { btnLuu.addActionListener(l); }
    public void addBtnHuyListener(ActionListener l)     { btnHuy.addActionListener(l); }
    public void addTableMouseListener(MouseAdapter l)   { table.addMouseListener(l); }
    public void addCboLoaiPhongTimListener(ActionListener l) { cboLoaiPhongTim.addActionListener(l); }
    public void addCboTinhTrangTimListener(ActionListener l) { cboTinhTrangTim.addActionListener(l); }
}
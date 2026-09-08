
package View.HaTrang;

import Controller.HaTrang.Thongbaocontroller;
import Model.Thongbao;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class QuanlyThongbaoPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTieuDe, txtNguoiGui, txtLocKeyword;
    private JTextArea txtNoiDung;
    private JButton btnLoc, btnThem, btnSua, btnXoa, btnLuu, btnHuy;

    public QuanlyThongbaoPanel() {
        initComponents();
        Thongbaocontroller controller = new Thongbaocontroller(this);
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);

        String titleText = (Model.Auth.isHocSinh() || Model.Auth.isGiaoVien()) ? "THÔNG BÁO" : "QUẢN LÝ THÔNG BÁO";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.add(new JLabel("Tìm kiếm (Tiêu đề/ Người gửi/ Nội dung):"));
        txtLocKeyword = new JTextField(20);
        btnLoc = new JButton("Tìm Kiếm");
        ButtonStyleHelper.styleButtonSearch(btnLoc);
        pnlFilter.add(txtLocKeyword); pnlFilter.add(btnLoc);

        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(pnlFilter, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"STT", "Tiêu đề", "Người gửi", "Ngày tạo", "Nội dung"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        TableSortHelper.enableTableSorting(table);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout(10, 10));
        pnlSouth.setOpaque(false);

        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(new TitledBorder("Thông tin chi tiết"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlInput.add(new JLabel("Tiêu đề:"), gbc);
        gbc.gridx = 1; txtTieuDe = new JTextField(30); pnlInput.add(txtTieuDe, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlInput.add(new JLabel("Người gửi:"), gbc);
        gbc.gridx = 1; txtNguoiGui = new JTextField(30); pnlInput.add(txtNguoiGui, gbc);
        gbc.gridx = 0; gbc.gridy = 2; pnlInput.add(new JLabel("Nội dung:"), gbc);
        gbc.gridx = 1; txtNoiDung = new JTextArea(4, 30);
        txtNoiDung.setLineWrap(true);
        pnlInput.add(new JScrollPane(txtNoiDung), gbc);

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
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

        pnlBtns.add(btnThem);
        pnlBtns.add(btnSua);
        pnlBtns.add(btnXoa);
        pnlBtns.add(btnLuu);
        pnlBtns.add(btnHuy);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);
        pnlSouth.add(pnlBtns, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        if (Model.Auth.isHocSinh() || Model.Auth.isGiaoVien()) {
            pnlSouth.setVisible(false);
        }

        setCrudButtonState(true, false, false, false, false);
        setInputEditable(false); 
    }

    public void setInputEditable(boolean editable) {
        txtTieuDe.setEditable(editable);
        txtNguoiGui.setEditable(editable);
        txtNoiDung.setEditable(editable);

        Color bgColor = editable ? Color.WHITE : new Color(240, 240, 240);
        txtTieuDe.setBackground(bgColor);
        txtNguoiGui.setBackground(bgColor);
        txtNoiDung.setBackground(bgColor);
    }

    private JButton createBtn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setPreferredSize(new Dimension(120, 35));
        return b;
    }

    public void loadTable(List<Thongbao> list) {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int stt = 1;
        for (Thongbao tb : list) {
            model.addRow(new Object[]{stt++, tb.getTieuDe(), tb.getNguoiGui(),
                    tb.getNgayTao() != null ? sdf.format(tb.getNgayTao()) : "", tb.getNoiDung()});
        }
    }

    public void fillForm(int row) {
        txtTieuDe.setText(table.getValueAt(row, 1).toString());
        txtNguoiGui.setText(table.getValueAt(row, 2).toString());
        txtNoiDung.setText(table.getValueAt(row, 4).toString());
    }

    public void refresh() {
        txtTieuDe.setText(""); txtNguoiGui.setText("");
        txtNoiDung.setText(""); txtLocKeyword.setText("");
        table.clearSelection();
    }

    public JTable getTable() { return table; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public JButton getBtnLoc() { return btnLoc; }
    public String getTieuDe() { return txtTieuDe.getText(); }
    public String getNguoiGui() { return txtNguoiGui.getText(); }
    public String getNoiDung() { return txtNoiDung.getText(); }
    public String getLocKeyword() { return txtLocKeyword.getText(); }

    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them);
        btnSua.setEnabled(sua);
        btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu);
        btnHuy.setEnabled(huy);
    }

    public void addBtnThemListener(java.awt.event.ActionListener ac) { btnThem.addActionListener(ac); }
    public void addBtnSuaListener(java.awt.event.ActionListener ac) { btnSua.addActionListener(ac); }
    public void addBtnXoaListener(java.awt.event.ActionListener ac) { btnXoa.addActionListener(ac); }
    public void addBtnLuuListener(java.awt.event.ActionListener ac) { btnLuu.addActionListener(ac); }
    public void addBtnHuyListener(java.awt.event.ActionListener ac) { btnHuy.addActionListener(ac); }
    public void addBtnLocListener(java.awt.event.ActionListener ac) { btnLoc.addActionListener(ac); }
    public void addTableMouseListener(java.awt.event.MouseAdapter ad) { table.addMouseListener(ad); }
}
package View.HaTrang;

import Model.Phuckhao;
import Controller.HaTrang.Phuckhaocontroller;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class QuanLyPhucKhaoPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaHS, txtMaMH, txtLoc;
    private JComboBox<String> cboTrangThai;
    private JTextArea txtLyDo;
    private JButton btnLoc, btnThem, btnSua, btnXoa, btnLuu, btnHuy;
    private JLabel lblTrangThai;
    private JScrollPane scrollLyDo;

    public QuanLyPhucKhaoPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);

        String titleText = Model.Auth.isHocSinh() ? "PHÚC KHẢO" : "QUẢN LÝ PHÚC KHẢO";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.add(new JLabel("Tìm kiếm (MaHS/MaMH/TT/Lý do): "));
        txtLoc = new JTextField(20);
        btnLoc = new JButton("Tìm Kiếm");
        ButtonStyleHelper.styleButtonSearch(btnLoc);
        pnlFilter.add(txtLoc);
        pnlFilter.add(btnLoc);
        pnlNorth.add(lblTitle, BorderLayout.NORTH);
        pnlNorth.add(pnlFilter, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã HS", "Mã MH", "Ngày gửi", "Trạng thái", "Lý do"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
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
        pnlInput.setBorder(new TitledBorder("Chi tiết yêu cầu"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlInput.add(new JLabel("Mã Học Sinh:"), gbc);
        gbc.gridx = 1; txtMaHS = new JTextField(25); pnlInput.add(txtMaHS, gbc);
        gbc.gridx = 0; gbc.gridy = 1; pnlInput.add(new JLabel("Mã Môn Học:"), gbc);
        gbc.gridx = 1; txtMaMH = new JTextField(25); pnlInput.add(txtMaMH, gbc);

        lblTrangThai = new JLabel("Trạng Thái:");
        gbc.gridx = 0; gbc.gridy = 2; pnlInput.add(lblTrangThai, gbc);
        gbc.gridx = 1;
        cboTrangThai = new JComboBox<>(new String[]{"Chờ xử lý", "Đang xử lý", "Đã xử lý", "Từ chối"});
        cboTrangThai.setPreferredSize(new Dimension(250, 28));
        pnlInput.add(cboTrangThai, gbc);

        gbc.gridx = 0; gbc.gridy = 3; pnlInput.add(new JLabel("Lý Do:"), gbc);
        gbc.gridx = 1; txtLyDo = new JTextArea(8, 25);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        scrollLyDo = new JScrollPane(txtLyDo);
        scrollLyDo.setPreferredSize(new Dimension(400, 150));
        pnlInput.add(scrollLyDo, gbc);

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnThem = new JButton("Thêm");   ButtonStyleHelper.styleButtonAdd(btnThem);
        btnSua = new JButton("Sửa");     ButtonStyleHelper.styleButtonEdit(btnSua);
        btnXoa = new JButton("Xóa");     ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnLuu = new JButton("Lưu");     ButtonStyleHelper.styleButtonSave(btnLuu);
        btnHuy = new JButton("Hủy");     ButtonStyleHelper.styleButtonCancel(btnHuy);

        Dimension sz = new Dimension(90, 35);
        btnThem.setPreferredSize(sz); btnSua.setPreferredSize(sz); btnXoa.setPreferredSize(sz);
        btnLuu.setPreferredSize(sz);  btnHuy.setPreferredSize(sz);

        pnlBtns.add(btnThem); pnlBtns.add(btnSua); pnlBtns.add(btnXoa); pnlBtns.add(btnLuu); pnlBtns.add(btnHuy);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);
        pnlSouth.add(pnlBtns, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        if (Model.Auth.isHocSinh()) {
            lblTrangThai.setVisible(false);
            cboTrangThai.setVisible(false);
            cboTrangThai.setSelectedItem("Chờ xử lý");

            setCrudButtonState(true, false, false, false, false);
            setInputEditable(false);
        } else {
            btnThem.setVisible(false); 
            setCrudButtonState(false, false, false, false, false);
            setInputEditable(false);
        }
    }

    public void setInputEditable(boolean editable) {
        if (Model.Auth.isHocSinh()) {
            txtMaHS.setEditable(false);
            txtMaHS.setBackground(new Color(245, 245, 245));
            txtMaMH.setEditable(editable);
            txtLyDo.setEditable(editable);

            txtMaMH.setBackground(editable ? Color.WHITE : new Color(245, 245, 245));
            txtLyDo.setBackground(editable ? Color.WHITE : new Color(245, 245, 245));
        } else {
            txtMaHS.setEditable(false);
            txtMaMH.setEditable(false);
            txtLyDo.setEditable(false);
            cboTrangThai.setEnabled(editable);

            txtMaHS.setBackground(new Color(245, 245, 245));
            txtMaMH.setBackground(new Color(245, 245, 245));
            txtLyDo.setBackground(new Color(245, 245, 245));
        }
        txtLoc.setEditable(true);
        txtLoc.setBackground(Color.WHITE);
    }

    public void loadTable(List<Phuckhao> list) {
        model.setRowCount(0);
        if (list == null) return;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int stt = 1;
        for (Phuckhao pk : list) {
            model.addRow(new Object[]{
                    stt++,
                    pk.getMaHS(),
                    pk.getMaMH(),
                    pk.getNgayGui() != null ? sdf.format(pk.getNgayGui()) : "",
                    pk.getTrangThai(),
                    pk.getLyDo()
            });
        }
    }

    public void fillForm(int row) {
        int modelRow = table.convertRowIndexToModel(row);
        txtMaHS.setText(model.getValueAt(modelRow, 1).toString());
        txtMaMH.setText(model.getValueAt(modelRow, 2).toString());

        String trangThai = model.getValueAt(modelRow, 4).toString().trim();

        if (trangThai.equalsIgnoreCase("Chờ xử lý")) trangThai = "Chờ xử lý";
        else if (trangThai.equalsIgnoreCase("Đang xử lý")) trangThai = "Đang xử lý";
        else if (trangThai.equalsIgnoreCase("Đã xử lý")) trangThai = "Đã xử lý";
        else if (trangThai.equalsIgnoreCase("Từ chối")) trangThai = "Từ chối";

        if (!containsTrangThai(trangThai)) {
            cboTrangThai.addItem(trangThai);
        }

        cboTrangThai.setSelectedItem(trangThai);
        txtLyDo.setText(model.getValueAt(modelRow, 5).toString());
    }

    public void refresh() {
        if (!Model.Auth.isHocSinh()) {
            txtMaHS.setText("");
        } else {
            txtMaHS.setText(Model.Auth.maNguoiDung != null ? Model.Auth.maNguoiDung.toUpperCase() : "");
        }
        txtMaMH.setText("");
        if (cboTrangThai.getItemCount() > 0) {
            cboTrangThai.setSelectedIndex(0);
        }
        if (Model.Auth.isHocSinh()) {
            cboTrangThai.setSelectedItem("Chờ xử lý");
        }
        txtLyDo.setText("");
        txtLoc.setText("");
        table.clearSelection();
    }

    private boolean containsTrangThai(String value) {
        if (value == null) return false;
        for (int i = 0; i < cboTrangThai.getItemCount(); i++) {
            if (value.equalsIgnoreCase(cboTrangThai.getItemAt(i))) return true;
        }
        return false;
    }

    public JTable getTable() { return table; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public JButton getBtnLoc() { return btnLoc; }
    public String getMaHS() { return txtMaHS.getText(); }
    public String getMaMH() { return txtMaMH.getText(); }
    public String getTrangThai() {
        Object value = cboTrangThai.getSelectedItem();
        return value == null ? "" : value.toString();
    }
    public String getLyDo() { return txtLyDo.getText(); }
    public String getLocKeyword() { return txtLoc.getText(); }
    public JTextField getTxtMaHS() { return txtMaHS; }

    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        btnThem.setEnabled(them);
        btnSua.setEnabled(sua);
        btnXoa.setEnabled(xoa);
        btnLuu.setEnabled(luu);
        btnHuy.setEnabled(huy);
    }
}
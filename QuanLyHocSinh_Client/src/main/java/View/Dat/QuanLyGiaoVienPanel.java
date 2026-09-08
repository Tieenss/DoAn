package View.Dat;

import Model.ToBoMon;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class QuanLyGiaoVienPanel extends JPanel {

    private JTable tableGV;
    private DefaultTableModel tableModel;

    private JTextField txtMaGV, txtHoTen, txtSDT, txtTimKiem;
    private JSpinner spNgaySinh;
    private JComboBox<ToBoMon> cboMaToHop;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnXem, btnTimKiem;

    public QuanLyGiaoVienPanel() {
        initComponents();

        setFormEnabled(false);

        if (Model.Auth.isGiaoVien()) {
            loadThongTinCaNhan();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        String titleText = Model.Auth.isGiaoVien() ? "HỒ SƠ GIÁO VIÊN" : "QUẢN LÝ GIÁO VIÊN";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm"));

        pnlSearch.add(new JLabel("Từ khóa:"));
        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tìm");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);

        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);
        pnlNorth.add(pnlSearch, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"Mã GV", "Họ Tên", "Ngày Sinh", "SĐT", "Tổ Bộ Môn"};
        tableModel = new DefaultTableModel(cols, 0);
        tableGV = new JTable(tableModel);
        TableSortHelper.enableTableSorting(tableGV);
        tableGV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableGV.setRowHeight(25);
        tableGV.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                                    boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    if (value instanceof java.util.Date) {
                        value = sdf.format((java.util.Date) value);
                    } else if (value instanceof String) {
                        try {
                            java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
                            value = sdf.format(date);
                        } catch (Exception e) {}
                    }
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        dateRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableGV.getColumnModel().getColumn(2).setCellRenderer(dateRenderer);
        add(new JScrollPane(tableGV), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thông tin giáo viên"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlInput.add(new JLabel("Mã GV:"), gbc);
        gbc.gridx = 1;
        txtMaGV = new JTextField(15);
        pnlInput.add(txtMaGV, gbc);

        gbc.gridx = 2;
        pnlInput.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 3;
        txtHoTen = new JTextField(15);
        pnlInput.add(txtHoTen, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlInput.add(new JLabel("Ngày Sinh:"), gbc);
        gbc.gridx = 1;
        spNgaySinh = new JSpinner(new SpinnerDateModel());
        spNgaySinh.setEditor(new JSpinner.DateEditor(spNgaySinh, "dd/MM/yyyy"));
        pnlInput.add(spNgaySinh, gbc);

        gbc.gridx = 2;
        pnlInput.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 3;
        txtSDT = new JTextField(15);
        pnlInput.add(txtSDT, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlInput.add(new JLabel("Tổ Bộ Môn:"), gbc);
        gbc.gridx = 1;
        cboMaToHop = new JComboBox<>();
        pnlInput.add(cboMaToHop, gbc);

        gbc.gridx = 3;
        btnXem = new JButton("Tải lại DS");
        ButtonStyleHelper.styleButtonView(btnXem);
        pnlInput.add(btnXem, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnThem = new JButton("Thêm");
        ButtonStyleHelper.styleButtonAdd(btnThem);
        btnSua  = new JButton("Sửa");
        ButtonStyleHelper.styleButtonEdit(btnSua);
        btnXoa  = new JButton("Xóa");
        ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnLuu  = new JButton("Lưu");
        ButtonStyleHelper.styleButtonSave(btnLuu);
        btnHuy  = new JButton("Hủy");
        ButtonStyleHelper.styleButtonCancel(btnHuy);

        pnlBtn.add(btnThem);
        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);
        pnlBtn.add(btnLuu);
        pnlBtn.add(btnHuy);

        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);

        if (Model.Auth.isGiaoVien()) {
            pnlSearch.setVisible(false);
        }
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtMaGV.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(QuanLyGiaoVienPanel.this, "Vui lòng chọn giáo viên trên bảng trước!");
                    return;
                }
                setFormEnabled(true);
            }
        });
    }

    public void setFormEnabled(boolean enabled) {
        
        txtHoTen.setEnabled(enabled);
        spNgaySinh.setEnabled(enabled);
        txtSDT.setEnabled(enabled);

        txtMaGV.setEnabled(false);

        if (Model.Auth.isGiaoVien()) {
            cboMaToHop.setEnabled(false);
        } else {
            cboMaToHop.setEnabled(enabled);
        }

        btnLuu.setEnabled(enabled);
        btnHuy.setEnabled(enabled);

        btnSua.setEnabled(!enabled);

        if (Model.Auth.isGiaoVien()) {
            btnThem.setVisible(false);
            btnXoa.setVisible(false);
        } else {
            btnThem.setEnabled(!enabled);
            btnXoa.setEnabled(!enabled);
        }
    }

    public void loadThongTinCaNhan() {
    }

    public JTable getTableGV() { return tableGV; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public JTextField getTxtMaGV() { return txtMaGV; }
    public JTextField getTxtHoTen() { return txtHoTen; }
    public JTextField getTxtSDT() { return txtSDT; }
    public JSpinner getSpNgaySinh() { return spNgaySinh; }
    public JComboBox<ToBoMon> getCboMaToHop() { return cboMaToHop; }

    public JTextField getTxtTimKiem() { return txtTimKiem; }
    public JButton getBtnTimKiem() { return btnTimKiem; }
    public String getTuKhoaTuKiem(){ return txtTimKiem.getText();}

    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public JButton getBtnXem() { return btnXem; }

}
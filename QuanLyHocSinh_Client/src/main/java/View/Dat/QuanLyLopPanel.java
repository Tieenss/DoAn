package View.Dat;

import Model.Giaovien;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class QuanLyLopPanel extends JPanel {

    private JTable tableLop;
    private DefaultTableModel tableModel;

    private JTextField txtMaLop, txtTenLop;
    private JComboBox<String> cboNienKhoa;
    private JComboBox<Giaovien> cboGVCN;

    private JTextField txtTimKiem;
    private JButton btnTimKiem;

    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnXem;

    public QuanLyLopPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ LỚP HỌC", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); 

        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBorder(new TitledBorder("Tìm kiếm"));

        pnlSearch.add(new JLabel("Từ khóa:"));
        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tìm");
        ButtonStyleHelper.styleButtonSearch(btnTimKiem);

        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimKiem);

        pnlNorth.add(pnlSearch, BorderLayout.CENTER);

        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"Mã Lớp", "Tên Lớp", "Niên Khóa", "GVCN"};
        tableModel = new DefaultTableModel(cols, 0);
        tableLop = new JTable(tableModel);
        TableSortHelper.enableTableSorting(tableLop);
        tableLop.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableLop.setRowHeight(25);
        tableLop.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());
        add(new JScrollPane(tableLop), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thông tin lớp"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlInput.add(new JLabel("Mã Lớp:"), gbc);
        gbc.gridx = 1;
        txtMaLop = new JTextField(15);
        pnlInput.add(txtMaLop, gbc);

        gbc.gridx = 2;
        pnlInput.add(new JLabel("Tên Lớp:"), gbc);
        gbc.gridx = 3;
        txtTenLop = new JTextField(15);
        pnlInput.add(txtTenLop, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlInput.add(new JLabel("Niên Khóa:"), gbc);
        gbc.gridx = 1;
        cboNienKhoa = new JComboBox<>();
        cboNienKhoa.setPrototypeDisplayValue("2024-2025");
        pnlInput.add(cboNienKhoa, gbc);

        gbc.gridx = 2;
        pnlInput.add(new JLabel("GV Chủ Nhiệm:"), gbc);
        gbc.gridx = 3;
        cboGVCN = new JComboBox<>();
        pnlInput.add(cboGVCN, gbc);

        gbc.gridx = 4;
        btnXem = new JButton("Tải lại");
        ButtonStyleHelper.styleButtonView(btnXem);
        pnlInput.add(btnXem, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout());
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

        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    public JTable getTableLop() { return tableLop; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public JTextField getTxtMaLop() { return txtMaLop; }
    public JTextField getTxtTenLop() { return txtTenLop; }
    public JComboBox<String> getCboNienKhoa() { return cboNienKhoa; }

    public JComboBox<Giaovien> getCboGVCN() { return cboGVCN; }

    public JTextField getTxtTimKiem() { return txtTimKiem; }
    public JButton getBtnTimKiem() { return btnTimKiem; }
    public String getTuKhoaTimKiem() { return txtTimKiem.getText(); }

    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public JButton getBtnXem() { return btnXem; }
}

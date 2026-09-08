package View.Dat;

/**
 *
 * @author ADMIN
 */

import Controller.Dat.ToBoMonController; 
import Model.ToBoMon;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class QuanLyToBoMonPanel extends JPanel {

    private JTable tableTBM;
    private DefaultTableModel tableModel;

    private JTextField txtMaToHop, txtTenToHop;
    private JTextField txtTimKiem;
    private JButton btnThem, btnSua, btnXoa, btnLuu, btnHuy, btnTim;

    public QuanLyToBoMonPanel() {
        initComponents();
        new ToBoMonController(this);
        setButtonState(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout(0, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ TỔ BỘ MÔN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnlNorth.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlSearch.add(new JLabel("Tìm kiếm (Mã/Tên):"));
        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlSearch.add(txtTimKiem);

        btnTim = new JButton("Tìm");
        ButtonStyleHelper.styleButtonAdd(btnTim);
        btnTim.setPreferredSize(new Dimension(90, 30));
        pnlSearch.add(btnTim);

        pnlNorth.add(pnlSearch, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"Mã Tổ Hợp", "Tên Tổ Hợp"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableTBM = new JTable(tableModel);
        TableSortHelper.enableTableSorting(tableTBM);
        tableTBM.setRowHeight(25);
        tableTBM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableTBM.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        add(new JScrollPane(tableTBM), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new TitledBorder("Thông tin tổ bộ môn"));

        JPanel pnlInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        pnlInput.add(new JLabel("Mã Tổ Hợp:"), gbc);
        gbc.gridx = 1;
        txtMaToHop = new JTextField(20);
        pnlInput.add(txtMaToHop, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnlInput.add(new JLabel("Tên Tổ Hợp:"), gbc);
        gbc.gridx = 1;
        txtTenToHop = new JTextField(20);
        pnlInput.add(txtTenToHop, gbc);

        pnlSouth.add(pnlInput, BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnThem = new JButton("Thêm");
        ButtonStyleHelper.styleButtonAdd(btnThem);
        btnSua = new JButton("Sửa");
        ButtonStyleHelper.styleButtonEdit(btnSua);
        btnXoa = new JButton("Xóa");
        ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnLuu = new JButton("Lưu");
        ButtonStyleHelper.styleButtonSave(btnLuu);
        btnHuy = new JButton("Huỷ");
        ButtonStyleHelper.styleButtonCancel(btnHuy);

        Dimension btnSize = new Dimension(100, 40);
        btnThem.setPreferredSize(btnSize);
        btnSua.setPreferredSize(btnSize);
        btnXoa.setPreferredSize(btnSize);
        btnLuu.setPreferredSize(btnSize);
        btnHuy.setPreferredSize(btnSize);

        pnlBtn.add(btnThem);
        pnlBtn.add(btnSua);
        pnlBtn.add(btnXoa);
        pnlBtn.add(btnLuu);
        pnlBtn.add(btnHuy);

        pnlSouth.add(pnlBtn, BorderLayout.SOUTH);
        add(pnlSouth, BorderLayout.SOUTH);
    }


    public void setTableData(List<ToBoMon> list) {
        tableModel.setRowCount(0);
        for (ToBoMon tbm : list) {
            tableModel.addRow(new Object[]{
                    tbm.getMaToHop(),
                    tbm.getTenToHop()
            });
        }
    }

    public void clearForm() {
        txtMaToHop.setText("");
        txtTenToHop.setText("");
        txtTimKiem.setText("");
        txtMaToHop.setEnabled(true);
        txtTenToHop.setEnabled(true);
    }

    public void setButtonState(boolean isNormalState) {
        btnThem.setEnabled(isNormalState);
        btnSua.setEnabled(isNormalState);
        btnXoa.setEnabled(isNormalState);
        btnTim.setEnabled(isNormalState);

        btnLuu.setEnabled(!isNormalState);
        btnHuy.setEnabled(!isNormalState);

        txtTimKiem.setEnabled(isNormalState);
        tableTBM.setEnabled(isNormalState);
    }

    public JTable getTableTBM() { return tableTBM; }
    public JTextField getTxtMaToHop() { return txtMaToHop; }
    public JTextField getTxtTenToHop() { return txtTenToHop; }
    public JTextField getTxtTimKiem() { return txtTimKiem; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public JButton getBtnTim() { return btnTim; }
}
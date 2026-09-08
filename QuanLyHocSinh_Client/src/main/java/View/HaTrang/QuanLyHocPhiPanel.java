package View.HaTrang;

import Controller.HaTrang.Hocphicontroller;
import Model.Hocphi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.border.TitledBorder;
import TienIch.ButtonStyleHelper;
import TienIch.TableSortHelper;

public class QuanLyHocPhiPanel extends JPanel {
    private JTextField txtMaLop, txtNamHoc;
    private JComboBox<String> cboHocKy;
    private JTextField txtMaLopCT;
    private JComboBox<String> cboHocKyCT;
    private JTextField txtNamHocCT;
    private JButton btnLoc, btnThem, btnSua, btnXoa, btnLuu, btnHuy;
    private JTable tableHocPhi;
    private DefaultTableModel tableModel;
    private JTextField txtMaHS, txtTongTien, txtMienGiam, txtPhaiDong;
    private JTextField txtTenHSCT;
    private JComboBox<String> cboTrangThai;

    private JLabel lblTongTienVal, lblMienGiamVal, lblPhaiDongVal, lblTrangThaiBadge;
    private JButton btnThanhToanHocSinh;

    public QuanLyHocPhiPanel() {
        initComponents();
        Hocphicontroller controller = new Hocphicontroller(this);
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtMaHS = new JTextField();
        txtTenHSCT = new JTextField();
        txtTenHSCT.setEditable(false);
        txtTenHSCT.setBackground(new Color(245, 245, 245));
        txtMaLopCT = new JTextField();
        txtMaLopCT.setEditable(false);
        cboHocKyCT = new JComboBox<>(new String[]{"1","2"});
        txtNamHocCT = new JTextField();
        txtTongTien = new JTextField();
        txtMienGiam = new JTextField();
        txtPhaiDong = new JTextField();
        txtPhaiDong.setEditable(false);
        txtPhaiDong.setBackground(new Color(245, 245, 245));
        cboTrangThai = new JComboBox<>(new String[]{"Chưa đóng", "Đã đóng", "Bảo lưu"});

        btnThem = new JButton("Thêm");       ButtonStyleHelper.styleButtonAdd(btnThem);
        btnSua = new JButton("Sửa");         ButtonStyleHelper.styleButtonEdit(btnSua);
        btnXoa = new JButton("Xóa");         ButtonStyleHelper.styleButtonDelete(btnXoa);
        btnLuu = new JButton("Lưu");         ButtonStyleHelper.styleButtonSave(btnLuu);
        btnHuy = new JButton("Hủy");         ButtonStyleHelper.styleButtonCancel(btnHuy);

        JPanel pnlNorth = new JPanel(new BorderLayout(10, 10));
        pnlNorth.setOpaque(false);

        String titleText = Model.Auth.isHocSinh() ? "HỌC PHÍ HỌC SINH" : "QUẢN LÝ HỌC PHÍ";
        JLabel lblTitle = new JLabel(titleText, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(41, 128, 185));
        pnlNorth.add(lblTitle, BorderLayout.NORTH); 

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        pnlFilter.add(new JLabel("Mã Lớp:"));
        txtMaLop = new JTextField(10);
        pnlFilter.add(txtMaLop);

        pnlFilter.add(new JLabel("Học Kỳ:"));
        cboHocKy = new JComboBox<>(new String[]{"", "1", "2"});
        cboHocKy.setSelectedIndex(0);
        pnlFilter.add(cboHocKy);

        pnlFilter.add(new JLabel("Năm Học:"));
        txtNamHoc = new JTextField(10);
        pnlFilter.add(txtNamHoc);

        btnLoc = new JButton("Tìm Kiếm"); 
        ButtonStyleHelper.styleButtonSearch(btnLoc); 
        pnlFilter.add(btnLoc);

        pnlNorth.add(pnlFilter, BorderLayout.SOUTH);
        add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"ID", "Mã HS", "Tên Học Sinh", "Mã Lớp", "Kỳ", "Năm học", "Tổng tiền", "Miễn giảm", "Phải đóng", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableHocPhi = new JTable(tableModel);

        tableHocPhi.getColumnModel().removeColumn(tableHocPhi.getColumnModel().getColumn(1));
        
        TableSortHelper.enableTableSorting(tableHocPhi);
        tableHocPhi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHocPhi.setRowHeight(30);

        tableHocPhi.getTableHeader().setDefaultRenderer(new TienIch.CustomTableHeaderRenderer());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i=0; i<tableHocPhi.getColumnCount(); i++) {
            tableHocPhi.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableHocPhi);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách học phí"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout(10, 10));
        pnlSouth.setOpaque(false);

        if (!Model.Auth.isHocSinh()) {
            
            JPanel pnlInput = new JPanel(new GridLayout(5, 4, 15, 10));
            pnlInput.setBackground(Color.WHITE);
            pnlInput.setBorder(BorderFactory.createCompoundBorder(
                    new TitledBorder("Thông tin chi tiết"),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            pnlInput.add(new JLabel("Mã Học Sinh:"));   pnlInput.add(txtMaHS);
            pnlInput.add(new JLabel("Tên Học Sinh:"));  pnlInput.add(txtTenHSCT);
            pnlInput.add(new JLabel("Mã Lớp:"));      pnlInput.add(txtMaLopCT);
            pnlInput.add(new JLabel("Học Kỳ:"));      pnlInput.add(cboHocKyCT);
            pnlInput.add(new JLabel("Năm Học:"));     pnlInput.add(txtNamHocCT);
            pnlInput.add(new JLabel("Tổng Tiền:"));    pnlInput.add(txtTongTien);
            pnlInput.add(new JLabel("Miễn Giảm:"));    pnlInput.add(txtMienGiam);
            pnlInput.add(new JLabel("Phải Đóng:"));    pnlInput.add(txtPhaiDong);
            pnlInput.add(new JLabel("Trạng Thái:"));   pnlInput.add(cboTrangThai);
            pnlInput.add(new JLabel(""));              pnlInput.add(new JLabel(""));

            pnlSouth.add(pnlInput, BorderLayout.CENTER);

            JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            pnlButtons.setOpaque(false);

            Dimension sz = new Dimension(90, 35);
            btnThem.setPreferredSize(sz); btnSua.setPreferredSize(sz); btnXoa.setPreferredSize(sz);
            btnLuu.setPreferredSize(sz);  btnHuy.setPreferredSize(sz);

            pnlButtons.add(btnThem); pnlButtons.add(btnSua); pnlButtons.add(btnXoa);
            pnlButtons.add(btnLuu);  pnlButtons.add(btnHuy);
            pnlSouth.add(pnlButtons, BorderLayout.SOUTH);

        } else {
            
            JPanel pnlCard = new JPanel(new GridLayout(1, 3, 20, 0));
            pnlCard.setOpaque(false);
            pnlCard.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            lblTongTienVal = new JLabel("0 đ", JLabel.CENTER);
            lblTongTienVal.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblTongTienVal.setForeground(new Color(41, 128, 185));
            pnlCard.add(createInfoCard("TỔNG HỌC PHÍ", lblTongTienVal, new Color(236, 240, 241)));

            lblMienGiamVal = new JLabel("0 đ", JLabel.CENTER);
            lblMienGiamVal.setFont(new Font("Segoe UI", Font.BOLD, 22));
            lblMienGiamVal.setForeground(new Color(39, 174, 96));
            pnlCard.add(createInfoCard("ĐƯỢC MIỄN GIẢM", lblMienGiamVal, new Color(233, 247, 239)));

            lblPhaiDongVal = new JLabel("0 đ", JLabel.CENTER);
            lblPhaiDongVal.setFont(new Font("Segoe UI", Font.BOLD, 26));
            lblPhaiDongVal.setForeground(new Color(192, 57, 43));
            pnlCard.add(createInfoCard("THỰC TẾ PHẢI ĐÓNG", lblPhaiDongVal, new Color(250, 229, 211)));

            pnlSouth.add(pnlCard, BorderLayout.CENTER);

            JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
            pnlAction.setOpaque(false);

            lblTrangThaiBadge = new JLabel("CHƯA CHỌN KỲ", JLabel.CENTER);
            lblTrangThaiBadge.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTrangThaiBadge.setOpaque(true);
            lblTrangThaiBadge.setBackground(Color.LIGHT_GRAY);
            lblTrangThaiBadge.setForeground(Color.WHITE);
            lblTrangThaiBadge.setPreferredSize(new Dimension(200, 45));
            pnlAction.add(lblTrangThaiBadge);

            btnThanhToanHocSinh = new JButton("Thanh Toán Học Phí");
            btnThanhToanHocSinh.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnThanhToanHocSinh.setBackground(new Color(46, 204, 113));
            btnThanhToanHocSinh.setForeground(Color.BLACK);
            btnThanhToanHocSinh.setOpaque(true);
            btnThanhToanHocSinh.setFocusPainted(false);
            btnThanhToanHocSinh.setPreferredSize(new Dimension(220, 45));
            btnThanhToanHocSinh.setVisible(false);
            pnlAction.add(btnThanhToanHocSinh);

            pnlSouth.add(pnlAction, BorderLayout.SOUTH);

            btnThanhToanHocSinh.addActionListener(e -> {
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Mã QR Thanh Toán", true);
                dialog.setSize(400, 450);
                dialog.setLocationRelativeTo(this);
                dialog.setLayout(new BorderLayout());
                dialog.getContentPane().setBackground(Color.WHITE);

                JLabel lblTitleQR = new JLabel("Quét Mã QR Để Thanh Toán", JLabel.CENTER);
                lblTitleQR.setFont(new Font("Segoe UI", Font.BOLD, 18));
                lblTitleQR.setForeground(new Color(41, 128, 185));
                lblTitleQR.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

                JLabel lblQR = new JLabel();
                lblQR.setHorizontalAlignment(JLabel.CENTER);
                try {
                    java.net.URL imgUrl = QuanLyHocPhiPanel.class.getResource("/TienIch/thanhToanHocPhi.png");
                    if (imgUrl != null) {
                        ImageIcon icon = new ImageIcon(imgUrl);
                        Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                        lblQR.setIcon(new ImageIcon(img));
                    } else {
                        lblQR.setText("Không tìm thấy ảnh QR");
                    }
                } catch (Exception ex) {
                    lblQR.setText("Lỗi tải ảnh QR");
                }

                String maHsHienTai = txtMaHS.getText().isEmpty() ? Model.Auth.maNguoiDung : txtMaHS.getText();
                JLabel lblGhiChu = new JLabel("Nội dung chuyển khoản: " + maHsHienTai + " - Nop hoc phi", JLabel.CENTER);
                lblGhiChu.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                lblGhiChu.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

                dialog.add(lblTitleQR, BorderLayout.NORTH);
                dialog.add(lblQR, BorderLayout.CENTER);
                dialog.add(lblGhiChu, BorderLayout.SOUTH);

                dialog.setVisible(true);
            });
        }
        add(pnlSouth, BorderLayout.SOUTH);

        tableHocPhi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tableHocPhi.getSelectedRow();
                if (r >= 0) {
                    txtMaHS.setText(tableModel.getValueAt(r, 1).toString());
                    Object tenHS = tableModel.getValueAt(r, 2);
                    txtTenHSCT.setText(tenHS != null ? tenHS.toString() : "");
                    txtMaHS.setEditable(false);
                    txtMaLopCT.setText(tableModel.getValueAt(r,3).toString());
                    cboHocKyCT.setSelectedItem(tableModel.getValueAt(r,4).toString());
                    txtNamHocCT.setText(tableModel.getValueAt(r,5).toString());
                    txtTongTien.setText(tableModel.getValueAt(r, 6).toString());
                    txtMienGiam.setText(tableModel.getValueAt(r, 7).toString());
                    txtPhaiDong.setText(tableModel.getValueAt(r, 8).toString());

                    Object trangThaiValue = tableModel.getValueAt(r, 9);
                    String trangThai = (trangThaiValue == null || trangThaiValue.toString().trim().isEmpty())
                            ? "Chưa đóng"
                            : trangThaiValue.toString();
                    cboTrangThai.setSelectedItem(trangThai);

                    if (!Model.Auth.isHocSinh()) {
                        setInputEditable(false);
                    }

                    if (Model.Auth.isHocSinh()) {
                        if (lblTongTienVal != null) {
                            try {
                                long tt = Long.parseLong(txtTongTien.getText().isEmpty() ? "0" : txtTongTien.getText());
                                lblTongTienVal.setText(String.format("%,d đ", tt));
                            } catch (Exception ex) { lblTongTienVal.setText(txtTongTien.getText() + " đ"); }
                        }
                        if (lblMienGiamVal != null) {
                            try {
                                long mg = Long.parseLong(txtMienGiam.getText().isEmpty() ? "0" : txtMienGiam.getText());
                                lblMienGiamVal.setText(String.format("%,d đ", mg));
                            } catch (Exception ex) { lblMienGiamVal.setText(txtMienGiam.getText() + " đ"); }
                        }
                        if (lblPhaiDongVal != null) {
                            try {
                                long pd = Long.parseLong(txtPhaiDong.getText().isEmpty() ? "0" : txtPhaiDong.getText());
                                lblPhaiDongVal.setText(String.format("%,d đ", pd));
                            } catch (Exception ex) { lblPhaiDongVal.setText(txtPhaiDong.getText() + " đ"); }
                        }
                        if (lblTrangThaiBadge != null) {
                            lblTrangThaiBadge.setText(trangThai.toUpperCase());
                            if (trangThai.equalsIgnoreCase("Đã đóng") || trangThai.equalsIgnoreCase("Đã thanh toán") || trangThai.equalsIgnoreCase("Bảo lưu")) {
                                if (trangThai.equalsIgnoreCase("Bảo lưu")) {
                                    lblTrangThaiBadge.setBackground(new Color(243, 156, 18));
                                } else {
                                    lblTrangThaiBadge.setBackground(new Color(39, 174, 96));
                                }
                                if (btnThanhToanHocSinh != null) btnThanhToanHocSinh.setVisible(false);
                            } else {
                                lblTrangThaiBadge.setBackground(new Color(231, 76, 60));
                                if (btnThanhToanHocSinh != null) btnThanhToanHocSinh.setVisible(true);
                            }
                        }
                    }
                }
            }
        });

        if (!Model.Auth.isHocSinh()) {
            setCrudButtonState(true, false, false, false, false);
            setInputEditable(false);
        }
        
        txtMaHS.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String maHS = txtMaHS.getText().trim();
                if (!maHS.isEmpty()) {
                    Model.HocSinh hs = new Api.Đai.HocSinhApi().getHocSinh(maHS);
                    if (hs != null) {
                        txtTenHSCT.setText(hs.getHoTen());
                    } else {
                        txtTenHSCT.setText("Không tìm thấy học sinh");
                    }
                } else {
                    txtTenHSCT.setText("");
                }
            }
        });
    }

    private JPanel createInfoCard(String title, JLabel lblValue, Color bgColor) {
        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBackground(bgColor);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(100, 100, 100));

        pnl.add(lblTitle, BorderLayout.NORTH);
        pnl.add(lblValue, BorderLayout.CENTER);
        return pnl;
    }

    public void loadTable(List<Hocphi> list) {
        tableModel.setRowCount(0);
        for (Hocphi hp : list) {
            String trangThai = (hp.getTrangThai() == null || hp.getTrangThai().trim().isEmpty())
                    ? "Chưa đóng"
                    : hp.getTrangThai();
            tableModel.addRow(new Object[]{
                    hp.getMaHP(), hp.getMaHS(), hp.getTenHocSinh(), hp.getMaLop(), hp.getHocKy(),
                    hp.getNamHoc(), hp.getTongTien(), hp.getMienGiam(), hp.getPhaiDong(),
                    trangThai
            });
        }
    }

    public void refreshForm() {
        txtMaHS.setText("");
        txtMaHS.setEditable(true);
        txtTenHSCT.setText("");
        txtMaLopCT.setText("");
        txtNamHocCT.setText("");
        cboHocKyCT.setSelectedIndex(0);
        txtTongTien.setText("");
        txtMienGiam.setText("");
        txtPhaiDong.setText("");
        cboTrangThai.setSelectedIndex(0);
        tableHocPhi.clearSelection();

        txtMaLop.setText("");
        txtNamHoc.setText("");
        cboHocKy.setSelectedIndex(0);
    }

    public void setInputEditable(boolean editable) {
        txtMaHS.setEditable(editable);
        txtNamHocCT.setEditable(editable);
        cboHocKyCT.setEnabled(editable);
        txtTongTien.setEditable(editable);
        txtMienGiam.setEditable(editable);
        cboTrangThai.setEnabled(editable);

        txtMaLopCT.setEditable(false);
        txtPhaiDong.setEditable(false);
    }

    public JTextField getTxtMaLop() {return txtMaLop;}
    public JComboBox<String> getCboHocKy() { return cboHocKy; }
    public JTextField getTxtNamHoc() {return txtNamHoc;}
    public JTextField getTxtMaLopCT() {return txtMaLopCT;}
    public JComboBox<String> getCboHocKyCT() {return cboHocKyCT;}
    public JTextField getTxtNamHocCT() {return txtNamHocCT;}
    public JButton getBtnLoc() { return btnLoc; }
    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public void setCrudButtonState(boolean them, boolean sua, boolean xoa, boolean luu, boolean huy) {
        if(btnThem != null) btnThem.setEnabled(them);
        if(btnSua != null) btnSua.setEnabled(sua);
        if(btnXoa != null) btnXoa.setEnabled(xoa);
        if(btnLuu != null) btnLuu.setEnabled(luu);
        if(btnHuy != null) btnHuy.setEnabled(huy);
    }
    public JTable getTableHocPhi() { return tableHocPhi; }
    public JTextField getTxtMaHS() { return txtMaHS; }
    public JTextField getTxtTongTien() { return txtTongTien; }
    public JTextField getTxtMienGiam() { return txtMienGiam; }
    public JTextField getTxtPhaiDong() { return txtPhaiDong; }
    public JComboBox<String> getCboTrangThai() { return cboTrangThai; }
}
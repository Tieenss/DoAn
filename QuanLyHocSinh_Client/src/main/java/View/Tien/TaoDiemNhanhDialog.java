package View.Tien;

import Api.Tien.DiemApi;
import Api.Đai.HocSinhApi;
import Api.ThuTrang.MonHocApiClient;
import Model.Diem;
import Model.HocSinh;
import Model.MonHoc;
import TienIch.ButtonStyleHelper;
import TienIch.ComboBoxUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TaoDiemNhanhDialog extends JDialog {

    private JComboBox<String> cboHocSinh;
    private JComboBox<String> cboNamHoc;
    private JComboBox<String> cboHocKy;
    private JPanel pnlMonHoc;
    private List<JCheckBox> chkMonHocs;
    private JButton btnLuu, btnHuy;

    private List<HocSinh> listHocSinh;
    private List<MonHoc> listMonHoc;
    
    private boolean isSuccess = false;

    public TaoDiemNhanhDialog(Window parent) {
        super(parent, "Tạo Nhanh Dữ Liệu Điểm", ModalityType.APPLICATION_MODAL);
        initComponents();
        loadData();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Tiêu đề
        JLabel lblTitle = new JLabel("TẠO NHANH BẢNG ĐIỂM", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 204));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        
        // Input Form
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(new TitledBorder("Thông tin chung"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        pnlInput.add(new JLabel("Học sinh:"), gbc);
        cboHocSinh = new JComboBox<>();
        ComboBoxUtil.makeSearchableAndEditable(cboHocSinh);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.gridwidth = 3;
        pnlInput.add(cboHocSinh, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        pnlInput.add(new JLabel("Năm học:"), gbc);
        cboNamHoc = new JComboBox<>(new String[]{"2023-2024", "2024-2025", "2025-2026", "2026-2027"});
        ComboBoxUtil.makeSearchableAndEditable(cboNamHoc, true);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
        pnlInput.add(cboNamHoc, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        pnlInput.add(new JLabel("Học kỳ:"), gbc);
        cboHocKy = new JComboBox<>(new String[]{"1", "2"});
        ComboBoxUtil.makeSearchableAndEditable(cboHocKy);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.5;
        pnlInput.add(cboHocKy, gbc);
        
        pnlCenter.add(pnlInput, BorderLayout.NORTH);

        // Môn học Checkboxes
        pnlMonHoc = new JPanel(new GridLayout(0, 3, 10, 5));
        pnlMonHoc.setBorder(new TitledBorder("Chọn các môn học cần tạo"));
        JScrollPane scrollPane = new JScrollPane(pnlMonHoc);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        contentPane.add(pnlCenter, BorderLayout.CENTER);

        // Buttons
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnLuu = new JButton("Tạo Dữ Liệu");
        ButtonStyleHelper.styleButtonSave(btnLuu);
        btnLuu.setPreferredSize(new Dimension(130, 35));
        
        btnHuy = new JButton("Hủy");
        ButtonStyleHelper.styleButtonCancel(btnHuy);
        btnHuy.setPreferredSize(new Dimension(100, 35));

        pnlBottom.add(btnLuu);
        pnlBottom.add(btnHuy);
        
        contentPane.add(pnlBottom, BorderLayout.SOUTH);
        setContentPane(contentPane);

        // Events
        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> handleTaoDuLieu());
    }

    private void loadData() {
        try {
            // Load học sinh
            HocSinhApi hsApi = new HocSinhApi();
            listHocSinh = hsApi.getAllHocSinh();
            cboHocSinh.removeAllItems();
            cboHocSinh.addItem("");
            if (listHocSinh != null) {
                for (HocSinh hs : listHocSinh) {
                    cboHocSinh.addItem(hs.getMaHS() + " - " + hs.getHoTen());
                }
            }

            // Load môn học
            MonHocApiClient mhApi = new MonHocApiClient();
            listMonHoc = mhApi.getAll();
            chkMonHocs = new ArrayList<>();
            pnlMonHoc.removeAll();
            
            // Add a "Chọn tất cả" checkbox
            JCheckBox chkAll = new JCheckBox("Chọn tất cả");
            chkAll.setFont(new Font("Segoe UI", Font.BOLD, 14));
            chkAll.setForeground(new Color(198, 40, 40));
            chkAll.addActionListener(e -> {
                boolean selected = chkAll.isSelected();
                for (JCheckBox chk : chkMonHocs) {
                    chk.setSelected(selected);
                }
            });
            pnlMonHoc.add(chkAll);
            pnlMonHoc.add(new JLabel()); // padding
            pnlMonHoc.add(new JLabel()); // padding

            if (listMonHoc != null) {
                for (MonHoc mh : listMonHoc) {
                    JCheckBox chk = new JCheckBox(mh.getTenMH());
                    chk.putClientProperty("maMH", mh.getMaMH());
                    chk.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    chkMonHocs.add(chk);
                    pnlMonHoc.add(chk);
                }
            }
            pnlMonHoc.revalidate();
            pnlMonHoc.repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }

    private void handleTaoDuLieu() {
        String hsSelection = cboHocSinh.getSelectedItem() != null ? cboHocSinh.getSelectedItem().toString() : "";
        if (hsSelection.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn học sinh!");
            return;
        }

        String namHoc = cboNamHoc.getEditor().getItem() != null ? cboNamHoc.getEditor().getItem().toString() : "";
        if (namHoc.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập năm học!");
            return;
        }

        int hocKy = 1;
        try {
            hocKy = Integer.parseInt(cboHocKy.getSelectedItem().toString());
        } catch (Exception e) {}

        String maHS = hsSelection.split(" - ")[0].trim();

        List<String> selectedMaMHs = new ArrayList<>();
        for (JCheckBox chk : chkMonHocs) {
            if (chk.isSelected()) {
                selectedMaMHs.add(chk.getClientProperty("maMH").toString());
            }
        }

        if (selectedMaMHs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 môn học để tạo điểm!");
            return;
        }

        DiemApi diemApi = new DiemApi();
        int successCount = 0;
        int conflictCount = 0;
        int errorCount = 0;

        btnLuu.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            for (String maMH : selectedMaMHs) {
                Diem d = new Diem();
                d.setMaHS(maHS);
                d.setMaMH(maMH);
                d.setHocKy(hocKy);
                d.setNamHoc(namHoc);
                // Các điểm để trống (hoặc null) mặc định sẽ là chưa nhập

                String result = diemApi.addDiem(d);
                if (result == null) {
                    successCount++;
                } else if (result.toLowerCase().contains("tồn tại") || result.toLowerCase().contains("conflict")) {
                    conflictCount++;
                } else {
                    errorCount++;
                }
            }
            
            String msg = String.format("Hoàn tất tạo dữ liệu:\n- Thành công: %d môn\n- Đã tồn tại (bỏ qua): %d môn\n- Lỗi: %d môn", 
                    successCount, conflictCount, errorCount);
            JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
            if (successCount > 0) {
                this.isSuccess = true;
                dispose();
            }
        } finally {
            btnLuu.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}

package View.Dai;

import Controller.Dai.HocSinhController;
import Model.*;

import javax.swing.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HoSoHocSinhPanel extends JPanel {

    private final HocSinhController controller = new HocSinhController();

    private JLabel lblTenHocSinh;

    // =========================
    // THÔNG TIN CÁ NHÂN
    // =========================
    private JLabel lblMaHS, lblHoTen, lblNgaySinh, lblGioiTinh,lblDiaChi, lblMaLop, lblMaDT, lblNienKhoa;

    // =========================
    // THÔNG TIN GIÁO VIÊN
    // =========================
    private JLabel lblTenLop, lblGiaoVienCN, lblSiSo, lblPhongHoc;
    private JPanel lichThiPanel;

    // =========================
    // THỐNG KÊ HỌC SINH
    // =========================
    private JLabel lblGpaValue, lblGpaMoTa, lblHanhKiemValue, lblHanhKiemMoTa, lblSoMonValue, lblSoMonMoTa, lblHocPhiValue, lblHocPhiMoTa;

    private JLabel lblAnh;
    private JComboBox<String> cboNamHoc;
    private String maHSCurrent;
    private boolean boDangTaiDuLieu = false;

    public HoSoHocSinhPanel() {
        initComponents();
        loadThongTinCaNhan();
    }

    // =========================================================
    // GIAO DIỆN CHÍNH
    // =========================================================
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // =====================================================
        // CONTAINER CHÍNH
        // =====================================================
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        // =====================================================
        // HEADER
        // =====================================================
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(18));

        // =====================================================
        // 4 CARD THỐNG KÊ
        // =====================================================
        JPanel statPanel = createStatCards();
        cboNamHoc.addActionListener(e -> {
            if (boDangTaiDuLieu) return;
            taiThongKe();
        });
        mainPanel.add(statPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // =====================================================
        // THÔNG TIN CHÍNH
        // =====================================================
        JPanel profilePanel = createProfileSection();
        mainPanel.add(profilePanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // =====================================================
        // PHẦN DƯỚI
        // =====================================================
        JPanel bottomPanel = createBottomSection();
        mainPanel.add(bottomPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    // =========================================================
    // HEADER
    // =========================================================
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        JLabel lblNhom = new JLabel("HỆ THỐNG & CHÍNH SÁCH");
        lblNhom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNhom.setForeground(new Color(100, 100, 100));
        JLabel lblTitle = new JLabel("Hồ sơ của tôi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        left.add(lblNhom);
        left.add(Box.createVerticalStrut(3));
        left.add(lblTitle);

        // Năm học bên phải: combo box
        cboNamHoc = new JComboBox<>();
        cboNamHoc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboNamHoc.setFocusable(false);
        cboNamHoc.setPrototypeDisplayValue("2025-2026");

        JLabel lblCaption = new JLabel("Năm học:");
        lblCaption.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCaption.setForeground(new Color(70, 90, 120));

        JPanel pnlNamHoc = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pnlNamHoc.setOpaque(false);
        pnlNamHoc.add(lblCaption);
        pnlNamHoc.add(cboNamHoc);

        panel.add(left, BorderLayout.WEST);
        panel.add(pnlNamHoc, BorderLayout.EAST);

        return panel;
    }

    private void taiThongKe() {
        String namHoc = (String) cboNamHoc.getSelectedItem();
        ThongKeHocSinh tk = controller.getThongKeHocSinh(maHSCurrent, namHoc);

        lblGpaValue.setText(giaTri(tk.getGpa()));
        lblGpaMoTa.setText(giaTri(tk.getGpaMoTa()));

        lblHanhKiemValue.setText(giaTri(tk.getXepLoai()));
        lblHanhKiemMoTa.setText(giaTri(tk.getXepLoaiMoTa()));

        lblSoMonValue.setText(giaTri(tk.getSoMon()));
        lblSoMonMoTa.setText(giaTri(tk.getSoMonMoTa()));

        lblHocPhiValue.setText(giaTri(tk.getHocPhi()));
        lblHocPhiMoTa.setText(giaTri(tk.getHocPhiMoTa()));
    }

    // =========================================================
    // 4 CARD THỐNG KÊ
    // =========================================================
    private JPanel createStatCards() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 12, 0));
        panel.setOpaque(false);

        // =========================
        // GPA
        // =========================
        lblGpaValue = new JLabel("-");
        lblGpaMoTa = new JLabel("-");
        panel.add(createStatCard("Điểm TB học kỳ", lblGpaValue, lblGpaMoTa));

        // =========================
        // HẠNH KIỂM
        // =========================
        lblHanhKiemValue = new JLabel("-");
        lblHanhKiemMoTa = new JLabel("-");
        panel.add(createStatCard("Hạnh kiểm", lblHanhKiemValue, lblHanhKiemMoTa));

        // =========================
        // SỐ MÔN
        // =========================
        lblSoMonValue = new JLabel("-");
        lblSoMonMoTa = new JLabel("-");
        panel.add(createStatCard("Số môn", lblSoMonValue, lblSoMonMoTa));

        // =========================
        // HỌC PHÍ
        // =========================
        lblHocPhiValue = new JLabel("-");
        lblHocPhiMoTa = new JLabel("-");
        panel.add(createStatCard("Học phí", lblHocPhiValue, lblHocPhiMoTa));

        return panel;
    }

    private JPanel createStatCard(String title, JLabel lblValue, JLabel lblDescription) {
        JPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        // =========================
        // TITLE
        // =========================
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(100, 100, 100));

        // =========================
        // VALUE
        // =========================
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 21));
        lblValue.setForeground(new Color(30, 70, 120));

        // =========================
        // DESCRIPTION
        // =========================
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDescription.setForeground(new Color(120, 120, 120));

        // =========================
        // CONTENT
        // =========================
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(4));
        content.add(lblValue);
        content.add(Box.createVerticalStrut(2));
        content.add(lblDescription);
        card.add(content, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // PROFILE
    // =========================================================
    private JPanel createProfileSection() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);

        // =====================================================
        // CARD BÊN TRÁI
        // =====================================================
        JPanel leftCard = createStudentCard();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.30;
        gbc.weighty = 1;

        panel.add(leftCard, gbc);

        // =====================================================
        // CARD THÔNG TIN CÁ NHÂN
        // =====================================================
        JPanel rightCard = createPersonalInfoCard();
        gbc.gridx = 1;
        gbc.weightx = 0.70;
        gbc.insets = new Insets(0, 10, 0, 0);

        panel.add(rightCard, gbc);

        return panel;
    }

    // =========================================================
    // CARD HỌC SINH
    // =========================================================
    private JPanel createStudentCard() {
        JPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        // -------------------------
        // ẢNH
        // -------------------------
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(130, 125));
        imagePanel.setBackground(new Color(245, 247, 250));
        imagePanel.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 230), 1));

        lblAnh = new JLabel("ẢNH HỌC SINH", JLabel.CENTER);
        lblAnh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAnh.setForeground(Color.GRAY);
        imagePanel.add(lblAnh, BorderLayout.CENTER);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(imagePanel);
        center.add(Box.createVerticalStrut(12));

        // -------------------------
        // TÊN HỌC SINH
        // -------------------------
        lblTenHocSinh = new JLabel("-", JLabel.CENTER);
        lblTenHocSinh.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTenHocSinh.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(lblTenHocSinh);

        JLabel lblLoai = new JLabel("Học sinh", JLabel.CENTER);
        lblLoai.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLoai.setForeground(Color.GRAY);
        lblLoai.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(lblLoai);
        center.add(Box.createVerticalStrut(12));

        // -------------------------
        // THÔNG TIN NGẮN
        // -------------------------

//        center.add(createSimpleInfo("Mã HS", lblMaHS = new JLabel("-")));
//
//        center.add(createSimpleInfo("Niên khóa", lblNienKhoa = new JLabel("-")));
//
//        center.add(createSimpleInfo("Đối tượng", lblMaDT = new JLabel("-")));
//
        center.add(Box.createVerticalGlue());

        // -------------------------
        // BUTTON
        // -------------------------
//        JButton btnCapNhat = new JButton("Cập nhật ảnh");
//        btnCapNhat.setFocusPainted(false);
//        btnCapNhat.setAlignmentX(Component.CENTER_ALIGNMENT);
//        center.add(btnCapNhat);
        card.add(center, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // THÔNG TIN CÁ NHÂN
    // =========================================================
    private JPanel createPersonalInfoCard() {
        JPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        JLabel title = new JLabel("Thông tin cá nhân");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);

//        JButton btnSua = new JButton("Chỉnh sửa");
//
//        btnSua.setBorderPainted(false);
//        btnSua.setContentAreaFilled(false);
//        btnSua.setFocusPainted(false);
//
//        top.add(btnSua, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);

        // =====================================================
        // GRID THÔNG TIN
        // =====================================================
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(12, 0, 12, 25);
        // Hàng 1
        addInfo(infoPanel, gbc, 0, 0, "MÃ HỌC SINH", lblMaHS = new JLabel("-"));
        addInfo(infoPanel, gbc, 1, 0, "HỌ VÀ TÊN", lblHoTen = new JLabel("-"));

        // Hàng 2
        addInfo(infoPanel, gbc, 0, 1, "NGÀY SINH", lblNgaySinh = new JLabel("-"));
        addInfo(infoPanel, gbc, 1, 1, "GIỚI TÍNH", lblGioiTinh = new JLabel("-"));

        // Hàng 3
        addInfo(infoPanel, gbc, 0, 2, "ĐỊA CHỈ", lblDiaChi = new JLabel("-"));
        addInfo(infoPanel, gbc, 1, 2, "MÃ LỚP", lblMaLop = new JLabel("-"));

        // Hàng 4
        addInfo(infoPanel, gbc, 0, 3, "ĐỐI TƯỢNG ƯU TIÊN", lblMaDT = new JLabel("-"));
        addInfo(infoPanel, gbc, 1, 3, "NIÊN KHÓA", lblNienKhoa = new JLabel("-"));
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // THÊM 1 Ô THÔNG TIN
    // =========================================================

    private void addInfo(JPanel panel, GridBagConstraints gbc, int x, int y, String title, JLabel value) {
        JPanel item = new JPanel();

        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));

        item.setOpaque(false);

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        lblTitle.setForeground(new Color(120, 130, 145));

        value.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        value.setForeground(new Color(30, 40, 50));

        item.add(lblTitle);

        item.add(Box.createVerticalStrut(5));

        item.add(value);
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(item, gbc);
    }

    // =========================================================
    // THÔNG TIN DƯỚI
    // =========================================================
    private JPanel createBottomSection() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setOpaque(false);
        panel.add(createClassInfoCard());
//        panel.add(createActivityCard());
        panel.add(createLichThiCard());

        return panel;
    }

    // =========================================================
    // THÔNG TIN LỚP HỌC
    // =========================================================
    private JPanel createClassInfoCard() {
        JPanel card = new RoundedPanel(15, Color.WHITE);

        card.setLayout(new BorderLayout());

        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Thông tin lớp học");

        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        card.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(4, 1, 0, 12));

        content.setOpaque(false);

        lblGiaoVienCN = new JLabel("-");

        lblTenLop = new JLabel("-");

        lblSiSo = new JLabel("-");

        lblPhongHoc = new JLabel("-");

        content.add(createClassRow("Giáo viên chủ nhiệm", lblGiaoVienCN));

        content.add(createClassRow("Lớp", lblTenLop));

        content.add(createClassRow("Sĩ số", lblSiSo));

        content.add(createClassRow("Phòng học", lblPhongHoc));

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // ROW THÔNG TIN LỚP
    // =========================================================
    private JPanel createClassRow(String title, JLabel value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(100, 110, 125));
        value.setHorizontalAlignment(SwingConstants.RIGHT);
        value.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        row.add(lblTitle, BorderLayout.WEST);
        row.add(value, BorderLayout.EAST);

        return row;
    }

    // =========================================================
    // HOẠT ĐỘNG GẦN ĐÂY
    // =========================================================
    private JPanel createActivityCard() {
        JPanel card = new RoundedPanel(15, Color.WHITE);

        card.setLayout(new BorderLayout());

        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Hoạt động gần đây");

        title.setFont(new Font("Segoe UI", Font.BOLD, 15));

        card.add(title, BorderLayout.NORTH);

        JPanel activities = new JPanel();

        activities.setLayout(new BoxLayout(activities, BoxLayout.Y_AXIS));

        activities.setOpaque(false);

        activities.add(createActivity("Nộp học phí HK1", "2 ngày trước"));

        activities.add(createActivity("Xem điểm kiểm tra Toán", "5 ngày trước"));

        activities.add(createActivity("Đăng ký phúc khảo môn Văn", "1 tuần trước"));

        activities.add(createActivity("Cập nhật thông tin địa chỉ", "2 tuần trước"));
        card.add(activities, BorderLayout.CENTER);

        return card;
    }

    private JPanel createLichThiCard() {
        JPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel title = new JLabel("Lịch thi sắp tới");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        card.add(title, BorderLayout.NORTH);
        lichThiPanel = new JPanel();
        lichThiPanel.setLayout(new BoxLayout(lichThiPanel, BoxLayout.Y_AXIS));
        lichThiPanel.setOpaque(false);
        lichThiPanel.add(createLichThiRow("Đang tải...", "", ""));
        card.add(lichThiPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createLichThiRow(String monHoc, String ngayThi, String gioThi) {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        panel.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel dot = new JLabel("●");

        dot.setForeground(new Color(50, 130, 200));

        dot.setBorder(new EmptyBorder(0, 0, 0, 8));

        JPanel text = new JPanel();

        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        text.setOpaque(false);

        JLabel lblMonHoc = new JLabel(monHoc);

        lblMonHoc.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblThoiGian = new JLabel(ngayThi + " | " + gioThi);

        lblThoiGian.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        lblThoiGian.setForeground(Color.GRAY);

        text.add(lblMonHoc);

        text.add(lblThoiGian);

        panel.add(dot, BorderLayout.WEST);

        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // 1 HOẠT ĐỘNG
    // =========================================================
    private JPanel createActivity(String title, String time) {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        panel.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel dot = new JLabel("●");

        dot.setForeground(new Color(50, 130, 200));

        dot.setBorder(new EmptyBorder(0, 0, 0, 8));

        JPanel text = new JPanel();

        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        text.setOpaque(false);

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lblTime = new JLabel(time);

        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        lblTime.setForeground(Color.GRAY);

        text.add(lblTitle);
        text.add(lblTime);

        panel.add(dot, BorderLayout.WEST);

        panel.add(text, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================
    // THÔNG TIN NGẮN TRONG CARD TRÁI
    // =========================================================
    private JPanel createSimpleInfo(String title, JLabel value) {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setOpaque(false);

        panel.setBorder(new EmptyBorder(4, 0, 4, 0));

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        lblTitle.setForeground(Color.GRAY);

        value.setFont(new Font("Segoe UI", Font.BOLD, 11));

        panel.add(lblTitle, BorderLayout.WEST);

        panel.add(value, BorderLayout.EAST);
        return panel;
    }

    // =========================================================
    // LOAD DỮ LIỆU
    // =========================================================
    private void loadThongTinCaNhan() {
        HocSinh hs = controller.getThongTinCaNhan();
        if (hs == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy thông tin học sinh.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // -------------------------
        // THÔNG TIN CÁ NHÂN
        // -------------------------
        lblMaHS.setText(giaTri(hs.getMaHS()));

        lblHoTen.setText(giaTri(hs.getHoTen()));

        lblTenHocSinh.setText(giaTri(hs.getHoTen()));

        lblNgaySinh.setText(formatNgaySinh(hs.getNgaySinh()));

        lblGioiTinh.setText(giaTri(hs.getGioiTinh()));

        lblDiaChi.setText(giaTri(hs.getDiaChi()));

        lblMaLop.setText(giaTri(hs.getMaLop()));

        lblMaDT.setText(giaTri(controller.hienThiDoiTuongUuTien(hs.getMaDT())));

        lblNienKhoa.setText(giaTri(hs.getNienKhoa()));

        // -------------------------
        // CẬP NHẬT TÊN HIỂN THỊ
        // -------------------------

        // Phần card trái hiện đang có
        // tên mẫu "Trần Thị Lan".
        // Có thể lấy trực tiếp từ model
        // nếu muốn.

        // -------------------------
        // THÔNG TIN LỚP
        // -------------------------
        Lop lop = controller.getLopCuaHocSinh(hs.getMaLop());

        List<LichThi> dsLichThi = controller.getLichThiCuaLop(hs.getMaLop());

        if (lop != null) {
            lblTenLop.setText(giaTri(lop.getTenLop()));
        } else {
            lblTenLop.setText("-");
        }

        String maGVCN = (lop == null) ? null : lop.getMaGVCN();

        Giaovien gv = controller.getGiaoVienChuNhiem(maGVCN);

        lblGiaoVienCN.setText((gv != null) ? giaTri(gv.getHoTen()) : "-");

        int siSo = controller.getSiSoLop(hs.getMaLop());

        lblSiSo.setText(siSo >= 0 ? siSo + " học sinh" : "-");

        lblPhongHoc.setText(controller.getPhongHocLop(hs.getMaLop()));

        // -------------------------
        // THỐNG KÊ HỌC SINH
        // -------------------------
        maHSCurrent = hs.getMaHS();

        boDangTaiDuLieu = true;
        cboNamHoc.removeAllItems();
        for (String nh : controller.getDanhSachNamHoc(maHSCurrent)) {
            cboNamHoc.addItem(nh);
        }
        String macDinh = controller.getNamHocMacDinh(maHSCurrent);
        if (macDinh != null) {
            cboNamHoc.setSelectedItem(macDinh);
        }
        boDangTaiDuLieu = false;

        taiThongKe();

        renderLichThi(dsLichThi);
    }




    // =========================================================
    // GIÁ TRỊ NULL
    // =========================================================
    private void renderLichThi(List<LichThi> dsLichThi) {
        lichThiPanel.removeAll();

        if (dsLichThi == null || dsLichThi.isEmpty()) {
            lichThiPanel.add(createLichThiRow("Không có lịch thi", "", ""));
        } else {
            int count = 0;
            for (LichThi lt : dsLichThi) {
                if (count++ >= 4) {
                    break;
                }
                lichThiPanel.add(createLichThiRow(giaTri(lt.getTenMH()),
                                formatNgayThi(lt.getNgayThi()),
                                giaTri(lt.getGioBatDau())
                                        + " · "
                                        + giaTri(lt.getTenPhong())
                        )
                );
            }
        }
        lichThiPanel.revalidate();
        lichThiPanel.repaint();
    }

    private String formatNgayThi(String ngay) {
        if (ngay == null || ngay.trim().isEmpty()) {
            return "-";
        }
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(ngay);
            return new SimpleDateFormat("dd/MM/yyyy").format(d);
        } catch (Exception e) {
            return ngay;
        }
    }

    private String giaTri(String value) {
        return value == null || value.trim().isEmpty() ? "-" : value.trim();
    }

    // =========================================================
    // FORMAT NGÀY
    // =========================================================
    private String formatNgaySinh(String ngaySinh) {
        if (ngaySinh == null || ngaySinh.trim().isEmpty()) {
            return "-";
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(ngaySinh);
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (Exception e) {
            return ngaySinh;
        }
    }

    // =========================================================
    // PANEL BO GÓC
    // =========================================================
    private static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color backgroundColor) {
            this.radius = radius;
            this.backgroundColor = backgroundColor;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(backgroundColor);

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(new Color(220, 225, 230));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();

            super.paintComponent(g);
        }
    }
}
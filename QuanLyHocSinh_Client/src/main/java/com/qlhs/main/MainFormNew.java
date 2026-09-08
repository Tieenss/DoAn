package com.qlhs.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.FontMetrics;
import Model.Auth;
import Model.HocSinh;
import Model.Giaovien;
import Api.Đai.HocSinhApi;
import Api.Đat.GiaoVienApi;
import View.Tien.HanhKiemPanel;
import View.LoginView;
import Controller.Dai.LoginController;
import View.Tien.QuanLyDiemPanel;
import Controller.Tien.DiemController;
import Controller.Tien.HanhKiemController;
import View.Tien.LichThiPanel;
import Controller.Tien.LichThiController;
import View.ThuTrang.FrmMonHoc;
import View.ThuTrang.FrmPhongHoc;
import View.ThuTrang.FrmTKB;
import View.Dai.QuanLyDoiTuongUuTienPanel;
import View.Dai.QuanLyHocSinhPanel;
import View.Dai.QuanLyTaiKhoanPanel;
import View.Dat.QuanLyGiaoVienPanel;
import View.Dat.QuanLyLopPanel;
import View.Dat.QuanLyToBoMonPanel;
import Controller.Dat.GiaoVienController;
import Controller.Dat.LopController;
import Controller.ThuTrang.MonHocController;
import Controller.ThuTrang.PhongHocController;
import Controller.ThuTrang.TKBController;
import View.HaTrang.QuanLyHocPhiPanel;
import View.HaTrang.QuanLyPhucKhaoPanel;
import View.HaTrang.QuanlyThongbaoPanel;

public class MainFormNew extends JFrame {

    private final Color SIDEBAR_BG = new Color(34, 45, 50);
    private final Color SIDEBAR_HOVER = new Color(44, 59, 65);
    private final Color SIDEBAR_ACTIVE = new Color(52, 152, 219);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color HEADER_COLOR = new Color(100, 150, 200);

    private JPanel mainPanel;
    private JPanel sidebarContainer;
    private JButton btnToggle;
    private JLabel lblUserGreeting;
    private boolean isCollapsed = false;
    private JButton lastActiveButton = null;
    private JPanel sidebarPanel;

    public MainFormNew() {
        System.out.println("QUYỀN ĐANG ĐĂNG NHẬP LÀ: [" + Auth.currentRole + "]");
        System.out.println("IS HOC SINH = " + Auth.isHocSinh());
        initUI();
    }

    private void initUI() {
        setTitle("HỆ THỐNG QUẢN LÝ TRƯỜNG HỌC THPT - DASHBOARD");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(createSidebarHeader(), BorderLayout.NORTH);

        sidebarContainer = new JPanel(new BorderLayout());
        sidebarPanel = createSidebar();
        JScrollPane scrollSidebar = new JScrollPane(sidebarPanel);
        scrollSidebar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollSidebar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollSidebar.setBorder(null);

        scrollSidebar.getVerticalScrollBar().setBackground(SIDEBAR_BG);
        scrollSidebar.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
        scrollSidebar.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 150, 200);
                this.trackColor = SIDEBAR_BG;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                JButton btn = super.createDecreaseButton(orientation);
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                JButton btn = super.createIncreaseButton(orientation);
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
        });

        sidebarContainer.add(scrollSidebar, BorderLayout.CENTER);
        westPanel.add(sidebarContainer, BorderLayout.CENTER);
        add(westPanel, BorderLayout.WEST);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        showWelcomeScreen();
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebarHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 35, 40));
        headerPanel.setBorder(new EmptyBorder(0, 0, 0, 8));

        btnToggle = new JButton("═");
        btnToggle.setFont(new Font("Arial", Font.BOLD, 20));
        btnToggle.setPreferredSize(new Dimension(50, 50));
        btnToggle.setBackground(new Color(25, 35, 40));
        btnToggle.setForeground(TEXT_COLOR);
        btnToggle.setBorder(null);
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> toggleSidebar());

        lblUserGreeting = new JLabel(getGreetingText());
        lblUserGreeting.setForeground(TEXT_COLOR);
        lblUserGreeting.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUserGreeting.setBorder(new EmptyBorder(0, 6, 0, 0));

        headerPanel.add(btnToggle, BorderLayout.WEST);
        headerPanel.add(lblUserGreeting, BorderLayout.CENTER);
        return headerPanel;
    }

    private String getGreetingText() {
        return "Chào mừng " + resolveDisplayName();
    }

    private String resolveDisplayName() {
        String maNguoiDung = Auth.maNguoiDung == null ? "" : Auth.maNguoiDung.trim();

        if (!maNguoiDung.isEmpty()) {
            if (Auth.isHocSinh()) {
                HocSinh hs = new HocSinhApi().getHocSinh(maNguoiDung);
                if (hs != null && hs.getHoTen() != null && !hs.getHoTen().trim().isEmpty()) {
                    return hs.getHoTen().trim();
                }
            } else if (Auth.isGiaoVien()) {
                try {
                    Giaovien gv = new GiaoVienApi().getByMaGV(maNguoiDung);

                    if (gv != null && gv.getHoTen() != null && !gv.getHoTen().trim().isEmpty()) {
                        return gv.getHoTen().trim();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (Auth.currentUser != null && !Auth.currentUser.trim().isEmpty()) {
            return Auth.currentUser.trim();
        }
        return "Khách";
    }

    private void toggleSidebar() {
        isCollapsed = !isCollapsed;
        if (isCollapsed) {
            btnToggle.setText(">");
            sidebarContainer.setPreferredSize(new Dimension(80, 0));
            lblUserGreeting.setText("");
        } else {
            btnToggle.setText("═");
            sidebarContainer.setPreferredSize(new Dimension(250, 0));
            lblUserGreeting.setText(getGreetingText());
        }
        updateAllButtonTexts();
        if (lblUserGreeting != null) {
            lblUserGreeting.revalidate();
            lblUserGreeting.repaint();
        }
        sidebarContainer.revalidate();
        sidebarContainer.repaint();
    }

    private void updateAllButtonTexts() {
        Component[] components = sidebarPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getClientProperty("formCode") != null) {
                    String title = (String) btn.getClientProperty("title");
                    if (isCollapsed) btn.setText("");
                    else btn.setText(" " + title);
                }
            }
        }
    }

    private ImageIcon loadIcon(String iconFileName, int size) {
        try {
            String resourcePath = "src/main/resources/icons/" + iconFileName;
            File iconFile = new File(resourcePath);
            if (!iconFile.exists()) return createDynamicIcon(iconFileName, size);
            BufferedImage img = ImageIO.read(iconFile);
            if (img == null) return createDynamicIcon(iconFileName, size);
            Image scaledImg = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception ex) {
            return createDynamicIcon(iconFileName, size);
        }
    }

    private ImageIcon createDynamicIcon(String iconFileName, int size) {
        java.util.Map<String, String> iconMap = new java.util.HashMap<>();
        iconMap.put("logout.png", "🚪"); iconMap.put("class.png", "📚");
        iconMap.put("teacher.png", "👨‍🏫"); iconMap.put("group.png", "👥");
        iconMap.put("subject.png", "📖"); iconMap.put("schedule.png", "📅");
        iconMap.put("room.png", "🏫"); iconMap.put("score.png", "📊");
        iconMap.put("conduct.png", "⭐"); iconMap.put("exam.png", "📝");
        iconMap.put("fee.png", "💰"); iconMap.put("notification.png", "📢");
        iconMap.put("review.png", "🔎"); iconMap.put("student.png", "👤");
        iconMap.put("user.png", "🔐"); iconMap.put("policy.png", "📋");

        String emoji = iconMap.getOrDefault(iconFileName, "•");
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, size, size);
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size - 8));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth(emoji)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        g2d.setColor(Color.WHITE);
        g2d.drawString(emoji, x, y);
        g2d.dispose();
        return new ImageIcon(img);
    }

    private void showWelcomeScreen() {
        mainPanel.removeAll();
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        JLabel lblWelcome = new JLabel("<html><center>CHÀO MỪNG ĐẾN VỚI<br>HỆ THỐNG QUẢN LÝ<br>TRƯỜNG THPT CHƯƠNG MỸ A</center></html>");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblWelcome.setForeground(new Color(0, 102, 204));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(lblWelcome, gbc);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setBorder(new EmptyBorder(10, 0, 10, 0));

        addSideHeader(sidebar, "Hệ thống");
        addSideButton(sidebar, "Đăng xuất", "DangXuat", "logout.png");
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        if (!Auth.isHocSinh()) {
            addSideHeader(sidebar, "Hồ sơ & cơ cấu");
            if (Auth.isAdmin()) {
                addSideButton(sidebar, "Quản lý lớp học", "FormLopHoc", "class.png");
                addSideButton(sidebar, "Quản lý giáo viên", "FormGiaoVien", "teacher.png");
                addSideButton(sidebar, "Quản lý tổ bộ môn", "FormToBoMon", "group.png");
            } else if (Auth.isGiaoVien()) {
                addSideButton(sidebar, "Hồ sơ giáo viên", "FormGiaoVien", "teacher.png");
            }
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        addSideHeader(sidebar, "Đào tạo");
        if (Auth.isAdmin()) {
            addSideButton(sidebar, "Quản lý môn học", "FormMonHoc", "subject.png");
            addSideButton(sidebar, "Thời khóa biểu / Lịch dạy", "FormTKB", "schedule.png");
            addSideButton(sidebar, "Phòng học & Thiết bị", "FormPhongHoc", "room.png");
        } else if (Auth.isGiaoVien()) {
            addSideButton(sidebar, "Lịch dạy", "FormTKB", "schedule.png");
        } else if (Auth.isHocSinh()) {
            addSideButton(sidebar, "Thời khóa biểu", "FormTKB", "schedule.png");
        }
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        addSideHeader(sidebar, "Khảo thí & kết quả");
        if (Auth.isHocSinh()) {
            addSideButton(sidebar, "Xem điểm số", "FormDiemSo", "score.png");
            addSideButton(sidebar, "Xem hạnh kiểm", "FormHanhKiem", "conduct.png");
            addSideButton(sidebar, "Lịch thi", "FormLichThi", "exam.png");
        } else {
            addSideButton(sidebar, "Quản lý điểm số", "FormDiemSo", "score.png");
            addSideButton(sidebar, "Quản lý hạnh kiểm", "FormHanhKiem", "conduct.png");
            addSideButton(sidebar, "Quản lý lịch thi", "FormLichThi", "exam.png");
        }
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        addSideHeader(sidebar, "Hành chính & tài vụ");
        if (Auth.isHocSinh()) {
            addSideButton(sidebar, "Phúc khảo", "FormPhucKhao", "review.png");
            addSideButton(sidebar, "Xem học phí", "FormHocPhi", "fee.png");
        } else if (Auth.isGiaoVien()) {
            addSideButton(sidebar, "Quản lý phúc khảo", "FormPhucKhao", "review.png");
        }
        if (Auth.isHocSinh() || Auth.isGiaoVien()) {
            addSideButton(sidebar, "Thông báo", "FormThongBao", "notification.png");
        } else {
            addSideButton(sidebar, "Quản lý Phúc khảo", "FormPhucKhao", "review.png");
            addSideButton(sidebar, "Quản lý học phí", "FormHocPhi", "fee.png");
            addSideButton(sidebar, "Quản lý thông báo", "FormThongBao", "notification.png");
        }
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        addSideHeader(sidebar, "Hệ thống & chính sách");
        addSideButton(sidebar, "Hồ sơ học sinh (Chi tiết)", "FormHocSinh", "student.png");
        if (Auth.isAdmin()) {
            addSideButton(sidebar, "Quản lý tài khoản user", "FormTaiKhoan", "user.png");
            addSideButton(sidebar, "Đối tượng chính sách", "FormChinhSach", "policy.png");
        }

        return sidebar;
    }

    private void addSideHeader(JPanel panel, String title) {
        JLabel lblHeader = new JLabel(title);
        lblHeader.setForeground(HEADER_COLOR);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHeader.setBorder(new EmptyBorder(5, 15, 5, 10));
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(lblHeader);
    }

    private void addSideButton(JPanel panel, String title, String formCode, String iconFileName) {
        JButton btn = new JButton();
        ImageIcon icon = loadIcon(iconFileName, 24);
        btn.setIcon(icon);
        btn.setText(" " + title);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
        btn.setOpaque(true); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != lastActiveButton) btn.setBackground(SIDEBAR_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != lastActiveButton) btn.setBackground(SIDEBAR_BG);
            }
        });

        btn.addActionListener(e -> {
            setActiveButton(btn);
            switchPanel(title, formCode);
        });

        btn.putClientProperty("formCode", formCode);
        btn.putClientProperty("title", title);
        btn.putClientProperty("iconName", iconFileName);
        panel.add(btn);
    }

    private void setActiveButton(JButton btn) {
        if (lastActiveButton != null) lastActiveButton.setBackground(SIDEBAR_BG);
        btn.setBackground(SIDEBAR_ACTIVE);
        lastActiveButton = btn;
    }

    private void switchPanel(String title, String formCode) {
        if (formCode.equals("DangXuat")) {
            int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                try {
                    LoginView view = new LoginView();
                    new LoginController(view);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi màn hình đăng nhập!");
                }
            }
            return;
        }

        Component viewToShow = null;
        if (formCode.equals("FormDiemSo")) {
            QuanLyDiemPanel view = new QuanLyDiemPanel();
            new DiemController(view); viewToShow = view;
        } else if (formCode.equals("FormHanhKiem")) {
            HanhKiemPanel view = new HanhKiemPanel();
            new HanhKiemController(view); viewToShow = view;
        } else if (formCode.equals("FormLichThi")) {
            LichThiPanel view = new LichThiPanel();
            new LichThiController(view); viewToShow = view;
        } else if (formCode.equals("FormHocPhi")) {
            QuanLyHocPhiPanel view = new QuanLyHocPhiPanel();
            new Controller.HaTrang.Hocphicontroller(view);
            viewToShow = view;
        } else if (formCode.equals("FormThongBao")) {
            QuanlyThongbaoPanel view = new QuanlyThongbaoPanel();
            new Controller.HaTrang.Thongbaocontroller(view);
            viewToShow = view;
        } else if (formCode.equals("FormPhucKhao")) {
            QuanLyPhucKhaoPanel view = new QuanLyPhucKhaoPanel();
            new Controller.HaTrang.Phuckhaocontroller(view);
            viewToShow = view;
        } else if (formCode.equals("FormMonHoc")) {
            FrmMonHoc panel = new FrmMonHoc();
            new MonHocController(panel); viewToShow = panel;
        } else if (formCode.equals("FormPhongHoc")) {
            FrmPhongHoc panel = new FrmPhongHoc();
            new PhongHocController(panel); viewToShow = panel;
        } else if (formCode.equals("FormTKB")) {
            FrmTKB panel = new FrmTKB();
            new TKBController(panel); viewToShow = panel;
        } else if (formCode.equals("FormGiaoVien")) {
            QuanLyGiaoVienPanel view = new QuanLyGiaoVienPanel();
            new GiaoVienController(view); viewToShow = view;
        } else if (formCode.equals("FormLopHoc")) {
            QuanLyLopPanel view = new QuanLyLopPanel();
            new LopController(view); viewToShow = view;
        } else if (formCode.equals("FormToBoMon")) {
            viewToShow = new QuanLyToBoMonPanel();
        } else if (formCode.equals("FormHocSinh")) {
            viewToShow = new QuanLyHocSinhPanel();
        } else if (formCode.equals("FormTaiKhoan")) {
            viewToShow = new QuanLyTaiKhoanPanel();
        } else if (formCode.equals("FormChinhSach")) {
            viewToShow = new QuanLyDoiTuongUuTienPanel();
        }

        mainPanel.removeAll();
        if (viewToShow != null) mainPanel.add(viewToShow, BorderLayout.CENTER);
        else {
            JLabel lblDemo = new JLabel("Chức năng đang phát triển: " + title, SwingConstants.CENTER);
            lblDemo.setFont(new Font("Arial", Font.BOLD, 18));
            mainPanel.add(lblDemo, BorderLayout.CENTER);
        }
        mainPanel.revalidate(); mainPanel.repaint();
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> { new MainFormNew().setVisible(true); });
    }
}

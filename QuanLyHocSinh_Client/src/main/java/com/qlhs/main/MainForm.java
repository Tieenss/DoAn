package com.qlhs.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import View.LoginView; 
import Controller.Dai.LoginController;

import View.Tien.QuanLyDiemPanel;
import Controller.Tien.DiemController;
import View.Tien.HanhKiemPanel;
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

public class MainForm extends JFrame {

    private final Color SIDEBAR_BG = new Color(34, 45, 50); 
    private final Color SIDEBAR_HOVER = new Color(44, 59, 65);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color HEADER_COLOR = new Color(100, 150, 200);

    private JPanel mainPanel;

    public MainForm() {
        initUI();
    }

    private void initUI() {
        setTitle("HỆ THỐNG QUẢN LÝ TRƯỜNG HỌC THPT - DASHBOARD");

        setSize(1400, 800); 

        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        JScrollPane scrollSidebar = new JScrollPane(sidebar);
        scrollSidebar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollSidebar.setBorder(null);

        scrollSidebar.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                JScrollBar verticalBar = scrollSidebar.getVerticalScrollBar();
                int scrollAmount = e.getWheelRotation() * 150; 
                int newValue = verticalBar.getValue() + scrollAmount;
                newValue = Math.max(0, Math.min(newValue, verticalBar.getMaximum() - verticalBar.getVisibleAmount()));
                verticalBar.setValue(newValue);
            }
        });
        
        add(scrollSidebar, BorderLayout.WEST);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        showWelcomeScreen(); 
        add(mainPanel, BorderLayout.CENTER);
    }

    private void showWelcomeScreen() {
        mainPanel.removeAll();
        JLabel lblWelcome = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblWelcome.setForeground(new Color(0, 102, 204));
        mainPanel.add(lblWelcome, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, 0)); 
        sidebar.setBorder(new EmptyBorder(10, 0, 10, 0));

        addSideHeader(sidebar, "HỆ THỐNG");
        addSideButton(sidebar, "Đăng Xuất", "DangXuat");
        addSideButton(sidebar, "Thoát", "Thoat");
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        addSideHeader(sidebar, "HỒ SƠ & CƠ CẤU");
        addSideButton(sidebar, "Quản lý Lớp học", "FormLopHoc");
        addSideButton(sidebar, "Quản lý Giáo viên", "FormGiaoVien");
        addSideButton(sidebar, "Quản lý Tổ bộ môn", "FormToBoMon");
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        addSideHeader(sidebar, "ĐÀO TẠO");
        addSideButton(sidebar, "Quản lý Môn học", "FormMonHoc");
        addSideButton(sidebar, "Thời khóa biểu / Lịch dạy", "FormTKB");
        addSideButton(sidebar, "Phòng học & Thiết bị", "FormPhongHoc");
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        addSideHeader(sidebar, "KHẢO THÍ & KẾT QUẢ");
        addSideButton(sidebar, "Quản lý Điểm số", "FormDiemSo");
        addSideButton(sidebar, "Hạnh kiểm / Rèn luyện", "FormHanhKiem");
        addSideButton(sidebar, "Lịch thi", "FormLichThi");
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        addSideHeader(sidebar, "HÀNH CHÍNH & TÀI VỤ");
        addSideButton(sidebar, "Quản lý Học phí", "FormHocPhi");
        addSideButton(sidebar, "Quản lý Thông báo", "FormThongBao");
        addSideButton(sidebar, "Quản lý Phúc khảo", "FormPhucKhao");
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        addSideHeader(sidebar, "HỆ THỐNG & CHÍNH SÁCH");
        addSideButton(sidebar, "Hồ sơ Học sinh (Chi tiết)", "FormHocSinh"); 
        addSideButton(sidebar, "Quản lý Tài khoản User", "FormTaiKhoan");
        addSideButton(sidebar, "Đối tượng chính sách", "FormChinhSach");

        return sidebar;
    }

    private void addSideHeader(JPanel panel, String title) {
        JLabel lblHeader = new JLabel(title);
        lblHeader.setForeground(HEADER_COLOR);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHeader.setBorder(new EmptyBorder(5, 15, 5, 10)); 
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblHeader);
    }

    private void addSideButton(JPanel panel, String title, String formCode) {
        JButton btn = new JButton("  " + title); 
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(TEXT_COLOR);
        btn.setBackground(SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(SIDEBAR_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(SIDEBAR_BG); }
        });

        btn.addActionListener(e -> switchPanel(title, formCode));
        panel.add(btn);
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
                    JOptionPane.showMessageDialog(null, "Lỗi không tìm thấy màn hình đăng nhập!");
                }
            }
            return;
        }

        if (formCode.equals("Thoat")) {
            System.exit(0);
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
        }

        else if (formCode.equals("FormHocPhi")) {
            QuanLyHocPhiPanel view = new QuanLyHocPhiPanel();
            new Controller.HaTrang.Hocphicontroller(view);
            viewToShow = view;
        }
        else if (formCode.equals("FormThongBao")) {
            QuanlyThongbaoPanel view = new QuanlyThongbaoPanel();
            new Controller.HaTrang.Thongbaocontroller(view);
            viewToShow = view;
        }
        else if (formCode.equals("FormPhucKhao")) {
            QuanLyPhucKhaoPanel view = new QuanLyPhucKhaoPanel();
            new Controller.HaTrang.Phuckhaocontroller(view);
            viewToShow = view;
        }

        else if (formCode.equals("FormMonHoc")) {
            FrmMonHoc panel = new FrmMonHoc();
            new MonHocController(panel);
            viewToShow = panel;
        } else if (formCode.equals("FormPhongHoc")) {
            FrmPhongHoc panel = new FrmPhongHoc();
            new PhongHocController(panel);
            viewToShow = panel;
        } else if (formCode.equals("FormTKB")) {
            FrmTKB panel = new FrmTKB();
            new TKBController(panel);
            viewToShow = panel;
        }

        else if (formCode.equals("FormGiaoVien")) {
            QuanLyGiaoVienPanel view = new QuanLyGiaoVienPanel();
            new GiaoVienController(view); viewToShow = view;
        } else if (formCode.equals("FormLopHoc")) {
            QuanLyLopPanel view = new QuanLyLopPanel();
            new LopController(view); viewToShow = view;
        } else if (formCode.equals("FormToBoMon")) { viewToShow = new QuanLyToBoMonPanel(); }

        else if (formCode.equals("FormHocSinh")) { viewToShow = new QuanLyHocSinhPanel(); }
        else if (formCode.equals("FormTaiKhoan")) { viewToShow = new QuanLyTaiKhoanPanel(); }
        else if (formCode.equals("FormChinhSach")) { viewToShow = new QuanLyDoiTuongUuTienPanel(); }

        mainPanel.removeAll(); 
        
        if (viewToShow != null) {
            mainPanel.add(viewToShow, BorderLayout.CENTER); 
        } else {
            
            JLabel lblDemo = new JLabel("Chức năng đang phát triển: " + title, SwingConstants.CENTER);
            lblDemo.setFont(new Font("Arial", Font.BOLD, 18));
            mainPanel.add(lblDemo, BorderLayout.CENTER);
        }
        
        mainPanel.revalidate(); 
        mainPanel.repaint();    
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}
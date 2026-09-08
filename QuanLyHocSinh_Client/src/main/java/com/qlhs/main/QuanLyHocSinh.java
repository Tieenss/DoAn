package com.qlhs.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import View.LoginView;
import Controller.Dai.LoginController;

public class QuanLyHocSinh {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> {
            LoginView view = new LoginView();
            new LoginController(view);
        });
    }
}
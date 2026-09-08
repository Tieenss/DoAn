package View; 

import javax.swing.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnExit;

    public LoginView() {
        setTitle("ĐĂNG NHẬP HỆ THỐNG");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lbUser = new JLabel("Tên đăng nhập:");
        lbUser.setBounds(30, 30, 100, 30);
        add(lbUser);
        
        txtUser = new JTextField();
        txtUser.setBounds(140, 30, 200, 30);
        add(txtUser);

        JLabel lbPass = new JLabel("Mật khẩu:");
        lbPass.setBounds(30, 80, 100, 30);
        add(lbPass);
        
        txtPass = new JPasswordField();
        txtPass.setBounds(140, 80, 200, 30);
        add(txtPass);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(80, 140, 100, 35);
        add(btnLogin);

        btnExit = new JButton("Thoát");
        btnExit.setBounds(200, 140, 100, 35);
        add(btnExit);
    }

    public String getUser() { return txtUser.getText(); }
    public String getPass() { return new String(txtPass.getPassword()); }
    public void addLoginListener(ActionListener l) { btnLogin.addActionListener(l); }
    public void addExitListener(ActionListener l) { btnExit.addActionListener(l); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}
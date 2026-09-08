package Controller.Dai;

import Model.Auth;
import Model.TaiKhoan;
import View.LoginView;
import Api.Đai.TaiKhoanApi;
import com.qlhs.main.MainFormNew;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginView view;
    private TaiKhoanApi dao;

    public LoginController(LoginView view) {
        this.view = view;
        this.dao = new TaiKhoanApi();

        view.addLoginListener(new LoginListener());
        view.addExitListener(e -> System.exit(0));
        view.setVisible(true);
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String user = view.getUser();
            String pass = view.getPass();

            if (user.isEmpty() || pass.isEmpty()) {
                view.showMessage("Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            String role = dao.checkLogin(user, pass);
            TaiKhoan tk = dao.checkLoginFull(user, pass);

            if (role != null) {
                Auth.currentUser = tk.getTenDangNhap();
                Auth.currentRole = tk.getQuyen();
                Auth.maNguoiDung = tk.getMaNguoiDung();
                System.out.println("ROLE = " + Auth.currentRole);
                view.showMessage("Đăng nhập thành công! Quyền: " + role);
                view.dispose(); 

                MainFormNew main = new MainFormNew();
                main.setVisible(true);
            } else {
                view.showMessage("Sai tên đăng nhập hoặc mật khẩu!");
            }
        }
    }
}
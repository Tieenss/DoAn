package Controller.Dai;

import Api.Đai.TaiKhoanApi;
import Model.TaiKhoan;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TaiKhoanController {

    private final TaiKhoanApi api = new TaiKhoanApi();

    public void loadTable(DefaultTableModel model) {

        model.setRowCount(0);

        List<TaiKhoan> list = api.getAll();

        for (TaiKhoan tk : list) {

            model.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getMatKhau(),
                    tk.getQuyen(),
                    tk.getMaNguoiDung()
            });

        }

    }

    public boolean them(TaiKhoan tk) {
        return api.insert(tk);
    }

    public boolean sua(TaiKhoan tk) {
        return api.update(tk);
    }

    public boolean xoa(String tenDangNhap) {
        return api.delete(tenDangNhap);
    }

    public boolean timKiem(String keyword, DefaultTableModel model) {

        model.setRowCount(0);

        List<TaiKhoan> list = api.search(keyword);

        for (TaiKhoan tk : list) {
            model.addRow(new Object[]{
                    tk.getTenDangNhap(),
                    tk.getMatKhau(),
                    tk.getQuyen(),
                    tk.getMaNguoiDung()
            });
        }

        return !list.isEmpty();
    }

}
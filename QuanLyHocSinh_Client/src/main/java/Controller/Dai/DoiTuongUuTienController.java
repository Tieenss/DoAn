package Controller.Dai;

import Api.Đai.DoiTuongUuTienApi;
import Model.DoiTuongUuTien;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DoiTuongUuTienController {

    private DoiTuongUuTienApi api = new DoiTuongUuTienApi();

    public void loadTable(DefaultTableModel model) {

        model.setRowCount(0);

        List<DoiTuongUuTien> list = api.getAll();

        if (list == null) {
            return;
        }

        for (DoiTuongUuTien dt : list) {

            model.addRow(new Object[]{
                    dt.getMaDT(),
                    dt.getTenDT(),
                    dt.getTiLeGiam() * 100
            });

        }
    }

    public boolean them(DoiTuongUuTien dt) {
        return api.insert(dt);
    }

    public boolean sua(DoiTuongUuTien dt) {
        return api.update(dt);
    }

    public boolean xoa(String maDT) {
        return api.delete(maDT);
    }

    public boolean timKiem(String keyword, DefaultTableModel model) {

        model.setRowCount(0);

        List<DoiTuongUuTien> list = api.search(keyword);

        if (list == null) {
            throw new RuntimeException("Không thể kết nối tới Server!");
        }

        if (list.isEmpty()) {
            return false;
        }

        for (DoiTuongUuTien dt : list) {

            model.addRow(new Object[]{
                    dt.getMaDT(),
                    dt.getTenDT(),
                    dt.getTiLeGiam() * 100
            });

        }

        return true;
    }
}
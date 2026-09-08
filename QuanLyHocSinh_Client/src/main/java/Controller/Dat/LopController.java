package Controller.Dat;

import Api.Đat.LopApi;
import Api.Đat.GiaoVienApi;
import Model.Giaovien;
import Model.Lop;
import Model.LopGVCN;
import View.Dat.QuanLyLopPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LopController {

    private QuanLyLopPanel view;
    private LopApi dao;
    private GiaoVienApi gvDao;
    private String currentMode = "";

    public LopController(QuanLyLopPanel view) {
        this.view = view;
        this.dao = new LopApi();
        this.gvDao = new GiaoVienApi();

        init();
    }

    private void init() {

        loadComboBox();
        loadTable();
        setButtonState(true);

        view.getTableLop().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillForm();
            }
        });

        view.getBtnThem().addActionListener(e -> them());
        view.getBtnSua().addActionListener(e -> sua());
        view.getBtnXoa().addActionListener(e -> xoa());
        view.getBtnLuu().addActionListener(e -> luu());
        view.getBtnHuy().addActionListener(e -> huy());
        view.getBtnXem().addActionListener(e -> {
            loadComboBox();
            loadTable();
        });
        view.getBtnTimKiem().addActionListener(e -> searchData());
    }

    private void loadComboBox() {
        view.getCboGVCN().removeAllItems();
        view.getCboNienKhoa().removeAllItems();

        List<String> dsNienKhoa = dao.getDistinctNienKhoa();
        if (dsNienKhoa.isEmpty()) {
            view.getCboNienKhoa().addItem("(Chưa có dữ liệu)");
        } else {
            for (String nienKhoa : dsNienKhoa) {
                view.getCboNienKhoa().addItem(nienKhoa);
            }
        }
        try {
            List<Giaovien> list = gvDao.getAll();
            for (Giaovien gv : list) {
                view.getCboGVCN().addItem(gv);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Không thể tải danh sách giáo viên!\n" + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadTable() {
        List<LopGVCN> list = dao.getAllLop();
        view.getTableModel().setRowCount(0);
        for (LopGVCN l : list) {
            view.getTableModel().addRow(new Object[] {
                    l.getMaLop(),
                    l.getTenLop(),
                    l.getNienKhoa(),
                    l.getTenGVCN()
            });
        }
    }

    private void fillForm() {
        int r = view.getTableLop().getSelectedRow();
        if (r < 0)
            return;
        view.getTxtMaLop().setText(view.getTableLop().getValueAt(r, 0).toString());
        view.getTxtTenLop().setText(view.getTableLop().getValueAt(r, 1).toString());
        String nienKhoa = view.getTableLop().getValueAt(r, 2).toString();
        boolean foundNienKhoa = false;
        for (int i = 0; i < view.getCboNienKhoa().getItemCount(); i++) {
            String item = view.getCboNienKhoa().getItemAt(i);
            if (nienKhoa.equals(item)) {
                foundNienKhoa = true;
                break;
            }
        }
        if (!foundNienKhoa && !nienKhoa.isEmpty()) {
            view.getCboNienKhoa().addItem(nienKhoa);
        }
        view.getCboNienKhoa().setSelectedItem(nienKhoa);

        String tenGV = view.getTableLop().getValueAt(r, 3).toString();

        for (int i = 0; i < view.getCboGVCN().getItemCount(); i++) {
            Giaovien gv = view.getCboGVCN().getItemAt(i);
            if (gv != null && gv.getHoTen().equals(tenGV)) {
                view.getCboGVCN().setSelectedIndex(i);
                break;
            }
        }
    }

    private void them() {
        clearForm();
        currentMode = "ADD";
        setButtonState(false);
        view.getTxtMaLop().setEnabled(true);
        view.getTxtMaLop().requestFocus();
    }

    private void sua() {
        if (view.getTableLop().getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(view, "Chọn lớp cần sửa!");
            return;
        }
        currentMode = "EDIT";
        setButtonState(false);
        view.getTxtMaLop().setEnabled(false);
    }

    private void xoa() {
        int r = view.getTableLop().getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn lớp cần xóa!");
            return;
        }

        if (JOptionPane.showConfirmDialog(view,
                "CẢNH BÁO: Xóa lớp này sẽ xóa toàn bộ Học sinh trong lớp!\nBạn có chắc chắn muốn tiếp tục?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            String ma = view.getTxtMaLop().getText();
            if (dao.delete(ma)) {
                JOptionPane.showMessageDialog(view, "Đã xóa!");
                loadComboBox();
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại!");
            }
        }
    }

    private void luu() {

        if (view.getTxtMaLop().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập Mã lớp!");
            return;
        }

        Lop l = new Lop();
        l.setMaLop(view.getTxtMaLop().getText());
        l.setTenLop(view.getTxtTenLop().getText());
        Object nienKhoaObj = view.getCboNienKhoa().getSelectedItem();
        String nienKhoa = nienKhoaObj == null ? "" : nienKhoaObj.toString().trim();
        if (nienKhoa.isEmpty() || "(Chưa có dữ liệu)".equalsIgnoreCase(nienKhoa)) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn niên khóa hợp lệ!");
            return;
        }
        l.setNienKhoa(nienKhoa);

        Giaovien gv = (Giaovien) view.getCboGVCN().getSelectedItem();
        if (gv != null)
            l.setMaGVCN(gv.getMaGV());

        boolean ok = false;
        if ("ADD".equals(currentMode))
            ok = dao.create(l);
        else if ("EDIT".equals(currentMode))
            ok = dao.update(l);

        if (ok) {
            JOptionPane.showMessageDialog(view, "Thành công!");
            loadComboBox();

            setButtonState(true);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(view, "Thất bại! Kiểm tra lại mã lớp.");
        }
        loadTable();
    }

    private void huy() {
        setButtonState(true);
        clearForm();
        view.getTableLop().clearSelection();
    }

    private void clearForm() {
        view.getTxtMaLop().setText("");
        view.getTxtTenLop().setText("");
        if (view.getCboNienKhoa().getItemCount() > 0) {
            view.getCboNienKhoa().setSelectedIndex(0);
        }
        view.getTxtMaLop().setEnabled(true);
    }

    private void setButtonState(boolean normal) {
        view.getBtnThem().setEnabled(normal);
        view.getBtnSua().setEnabled(normal);
        view.getBtnXoa().setEnabled(normal);
        view.getBtnLuu().setEnabled(!normal);
        view.getBtnHuy().setEnabled(!normal);
        view.getTableLop().setEnabled(normal);

    }

    private void searchData() {
        String keyword = view.getTxtTimKiem().getText().trim();
        System.out.println("KEYWORD = [" + keyword + "]");

        if (keyword.isEmpty()) {
            loadTable();
            return;
        }

        List<LopGVCN> list = dao.search(keyword);

        view.getTableModel().setRowCount(0);
        for (LopGVCN l : list) {
            view.getTableModel().addRow(new Object[] {
                    l.getMaLop(),
                    l.getTenLop(),
                    l.getNienKhoa(),
                    l.getTenGVCN()
            });
        }

        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy dữ liệu!");
        }

    }
}
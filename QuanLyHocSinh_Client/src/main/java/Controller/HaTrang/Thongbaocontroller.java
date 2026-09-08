package Controller.HaTrang;

import Api.HaTrang.ThongBaoApiClient;
import Model.Thongbao;
import View.HaTrang.QuanlyThongbaoPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class Thongbaocontroller {
    private final QuanlyThongbaoPanel view;
    private final ThongBaoApiClient dao;
    private List<Thongbao> currentList;

    public Thongbaocontroller(QuanlyThongbaoPanel view) {
        this.view = view;
        this.dao = new ThongBaoApiClient();
        initEvents();
        loadData();
    }

    private void initEvents() {
        boolean[] editMode = {false};

        Runnable updateUIState = () -> {
            int row = view.getTable().getSelectedRow();
            boolean hasSelected = row != -1;

            view.setCrudButtonState(!hasSelected && !editMode[0], hasSelected, hasSelected, editMode[0], editMode[0]);
            view.setInputEditable(editMode[0]);
        };

        updateUIState.run();

        view.getBtnLoc().addActionListener(e -> {
            String tuKhoa = view.getLocKeyword().trim();
            if (tuKhoa.isEmpty()) { loadData(); return; }

            currentList = dao.search(tuKhoa);
            view.loadTable(currentList);
            if (currentList.isEmpty()) JOptionPane.showMessageDialog(view, "Không tìm thấy thông báo nào!");
        });

        view.getBtnThem().addActionListener(e -> {
            editMode[0] = true; 
            view.refresh();
            view.getTable().clearSelection();
            updateUIState.run();
            editMode[0] = false; 
        });

        view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    view.fillForm(row);
                    updateUIState.run();
                }
            }
        });

        view.getBtnSua().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row != -1) {
                editMode[0] = true;
                view.fillForm(row);
                updateUIState.run();
                
                view.setCrudButtonState(false, true, true, true, true);
            } else {
                JOptionPane.showMessageDialog(view, "Chọn dòng cần sửa!");
            }
        });

        view.getBtnLuu().addActionListener(e -> {
            if (!validateForm()) return;

            int row = view.getTable().getSelectedRow();
            Thongbao tb = editMode[0] && row != -1 ? currentList.get(row) : new Thongbao();

            tb.setTieuDe(view.getTieuDe().trim());
            tb.setNoiDung(view.getNoiDung().trim());
            tb.setNguoiGui(view.getNguoiGui().trim());

            boolean success = editMode[0] ? dao.update(tb) : dao.insert(tb);
            if (success) {
                JOptionPane.showMessageDialog(view, editMode[0] ? "Cập nhật thành công!" : "Thêm thành công!");
                loadData();
                view.refresh();
                editMode[0] = false;
                view.getTable().clearSelection();
                updateUIState.run();
            }
        });

        view.getBtnXoa().addActionListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn dòng cần xóa!");
                return;
            }
            if (JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (dao.delete(currentList.get(row).getMaTB())) {
                    loadData();
                    view.refresh();
                    JOptionPane.showMessageDialog(view, "Đã xóa!");
                    editMode[0] = false;
                    view.getTable().clearSelection();
                    updateUIState.run();
                }
            }
        });

        view.getBtnHuy().addActionListener(e -> {
            view.refresh();
            loadData();
            editMode[0] = false;
            view.getTable().clearSelection();
            updateUIState.run();
        });
    }

    private void loadData() {
        currentList = dao.getAll();
        view.loadTable(currentList);
    }

    private boolean validateForm() {
        if (view.getTieuDe().trim().isEmpty() || view.getNguoiGui().trim().isEmpty() || view.getNoiDung().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng không để trống thông tin!");
            return false;
        }
        return true;
    }
}
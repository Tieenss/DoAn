package Controller.ThuTrang;

import Api.ThuTrang.MonHocApiClient;
import Model.MonHoc;
import View.ThuTrang.FrmMonHoc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MonHocController {
    private FrmMonHoc view;
    private MonHocApiClient apiClient;

    public MonHocController(FrmMonHoc view) {
        this.view = view;
        this.apiClient = new MonHocApiClient();
        initEvents();
        loadData();
    }

    private void initEvents() {
        boolean[] editMode = {false};
        Runnable setIdleState    = () -> view.setCrudButtonState(true, false, false, false, false);
        Runnable setAddState     = () -> view.setCrudButtonState(false, false, false, true, true);
        Runnable setSelectedState= () -> view.setCrudButtonState(false, true, true, false, true);
        Runnable setEditState    = () -> view.setCrudButtonState(false, true, true, true, true);
        setIdleState.run();

        Runnable doSearch = () -> {
            String key = view.getTuKhoa();
            try {
                List<MonHoc> list = key.isEmpty()
                        ? apiClient.getAll()
                        : apiClient.search(key);
                view.setTableData(list);
            } catch (Exception ignored) {
            }
        };

        view.addTimKiemLiveListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); }
        });

        view.addBtnTimKiemListener(e -> {
            doSearch.run();
            String key = view.getTuKhoa();
            if (!key.isEmpty() && view.getTable().getRowCount() == 0) {
                view.showMessage("Không tìm thấy môn học");
            }
        });

        view.addBtnThemListener(e -> {
            editMode[0] = false;
            view.clearForm();
            view.getTxtTimKiem().setText("");
            view.getTable().clearSelection();
            setAddState.run();
        });

        view.addBtnSuaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) { view.showMessage("Vui lòng chọn một bản ghi"); return; }
            editMode[0] = true;
            view.fillForm(row);
            setEditState.run();
        });

        view.addBtnXoaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) { view.showMessage("Vui lòng chọn một bản ghi"); return; }

            String ma = view.getTable().getValueAt(row, 0).toString();
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    view, "Bạn có chắc chắn muốn xóa?", "Xác nhận",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    apiClient.delete(ma);
                    view.showMessage("Đã xoá");
                    loadData();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } catch (Exception ex) {
                    view.showMessage("Lỗi xóa: " + ex.getMessage());
                }
            }
        });

        view.addBtnLuuListener(e -> {
            MonHoc m = view.getMonHocInput();
            if (m.getMaMH().isEmpty()) {
                view.showMessage("Mã môn không được để trống!");
                return;
            }
            if (m.getTenMH().isEmpty()) {
                view.showMessage("Tên môn không được để trống!");
                return;
            }
            try {
                if (editMode[0]) {
                    apiClient.update(m.getMaMH(), m);
                } else {
                    apiClient.create(m);
                }
                view.showMessage("Lưu thành công");
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg != null && (msg.contains("tồn tại") || msg.startsWith("Lỗi"))) {
                    view.showMessage(msg);
                } else {
                    view.showMessage("Lỗi: " + msg);
                }
            }
        });

        view.addBtnHuyListener(e -> {
            view.clearForm();
            loadData();
            editMode[0] = false;
            setIdleState.run();
        });

        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    editMode[0] = true;
                    view.fillForm(row);
                    setSelectedState.run();
                }
            }
        });
    }

    private void loadData() {
        try {
            view.setTableData(apiClient.getAll());
        } catch (Exception ex) {
            view.showMessage("Không thể kết nối server: " + ex.getMessage());
        }
    }
}
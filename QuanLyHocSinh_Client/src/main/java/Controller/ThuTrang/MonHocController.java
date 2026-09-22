package Controller.ThuTrang;

import Api.ThuTrang.MonHocApiClient;
import Model.MonHoc;
import TienIch.ValidationUtil;
import View.ThuTrang.FrmMonHoc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MonHocController {
    private FrmMonHoc view;
    private MonHocApiClient apiClient;
    private boolean isCustomOrder = false;

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
                list.sort((m1, m2) -> m1.getMaMH().compareToIgnoreCase(m2.getMaMH()));
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

        Runnable restoreOrderIfNeeded = () -> {
            if (isCustomOrder) {
                loadData();
            }
        };

        view.addBtnThemListener(e -> {
            restoreOrderIfNeeded.run();
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
            if (!editMode[0]) {
                String errMa = ValidationUtil.validateMa(m.getMaMH(), "Mã môn học");
                if (errMa != null) {
                    view.showMessage(errMa);
                    return;
                }
            }
            String errTen = ValidationUtil.validateTen(m.getTenMH(), "Tên môn học");
            if (errTen != null) {
                view.showMessage(errTen);
                return;
            }
            try {
                if (editMode[0]) {
                    apiClient.update(m.getMaMH(), m);
                    view.showMessage("Cập nhật môn học thành công!");
                } else {
                    apiClient.create(m);
                    view.showMessage("Thêm môn học thành công!");
                }
                loadDataWithHighlight(m.getMaMH());
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg != null && (msg.contains("tồn tại") || msg.startsWith("Lỗi") || msg.contains("ký tự") || msg.contains("kí tự"))) {
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

        view.addBtnMoiListener(e -> {
            view.clearForm();
            view.getTxtTimKiem().setText("");
            loadData();
            editMode[0] = false;
            view.getTable().clearSelection();
            setIdleState.run();
        });

        MouseAdapter formClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restoreOrderIfNeeded.run();
            }
        };
        view.getTxtMaMH().addMouseListener(formClickListener);
        view.getTxtTenMH().addMouseListener(formClickListener);
        view.getPnlInput().addMouseListener(formClickListener);

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

    private void loadDataWithHighlight(String maMH) {
        try {
            List<MonHoc> list = apiClient.getAll();
            list.sort((m1, m2) -> m1.getMaMH().compareToIgnoreCase(m2.getMaMH()));
            if (maMH != null && !maMH.trim().isEmpty()) {
                MonHoc target = null;
                for (MonHoc item : list) {
                    if (maMH.trim().equalsIgnoreCase(item.getMaMH())) {
                        target = item;
                        break;
                    }
                }
                if (target != null) {
                    list.remove(target);
                    list.add(0, target);
                }
            }
            view.setTableData(list);
            if (view.getTable().getRowCount() > 0) {
                view.getTable().setRowSelectionInterval(0, 0);
                view.getTable().scrollRectToVisible(view.getTable().getCellRect(0, 0, true));
            }
            isCustomOrder = true;
        } catch (Exception ex) {
            view.showMessage("Không thể kết nối server: " + ex.getMessage());
        }
    }

    public void loadData() {
        try {
            List<MonHoc> list = apiClient.getAll();
            list.sort((m1, m2) -> m1.getMaMH().compareToIgnoreCase(m2.getMaMH()));
            view.setTableData(list);
            isCustomOrder = false;
        } catch (Exception ex) {
            view.showMessage("Không thể kết nối server: " + ex.getMessage());
        }
    }
}
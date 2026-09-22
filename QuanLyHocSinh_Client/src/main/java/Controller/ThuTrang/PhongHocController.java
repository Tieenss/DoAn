package Controller.ThuTrang;

import Api.ThuTrang.PhongHocApiClient;
import Model.PhongHoc;
import TienIch.ValidationUtil;
import View.ThuTrang.FrmPhongHoc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PhongHocController {
    private FrmPhongHoc view;
    private PhongHocApiClient apiClient;
    private boolean isCustomOrder = false;

    public PhongHocController(FrmPhongHoc view) {
        this.view = view;
        this.apiClient = new PhongHocApiClient();
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
            try {
                String ma = view.getMaPhongTim();
                String loai = view.getLoaiPhongTim();
                String tinhTrang = view.getTinhTrangTim();
                if (ma.isEmpty() && (loai.isEmpty() || loai.equals("Tất cả")) && (tinhTrang.isEmpty() || tinhTrang.equals("Tất cả"))) {
                    loadData();
                } else {
                    List<PhongHoc> list = apiClient.search(ma, loai, tinhTrang);
                    list.sort((p1, p2) -> p1.getMaPhong().compareToIgnoreCase(p2.getMaPhong()));
                    view.setTableData(list);
                }
            } catch (Exception ignored) {
            }
        };

        view.addMaPhongTimLiveListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doSearch.run(); }
        });

        view.addBtnTimListener(e -> {
            doSearch.run();
            if (view.getTable().getRowCount() == 0) {
                view.showMessage("Không tìm thấy phòng học nào phù hợp!");
            }
        });
        view.addCboLoaiPhongTimListener(e -> doSearch.run());
        view.addCboTinhTrangTimListener(e -> doSearch.run());

        Runnable restoreOrderIfNeeded = () -> {
            if (isCustomOrder) {
                loadData();
            }
        };

        view.addBtnThemListener(e -> {
            restoreOrderIfNeeded.run();
            editMode[0] = false;
            view.clearForm();
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
            if (row == -1) { view.showMessage("Vui lòng chọn phòng cần xóa"); return; }

            String maPH = view.getTable().getValueAt(row, 0).toString();
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    view, "Bạn có chắc chắn muốn xóa?", "Xác nhận",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    apiClient.delete(maPH);
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
            try {
                PhongHoc p = view.getPhongHocInput();
                if (!editMode[0]) {
                    String errMa = ValidationUtil.validateMa(p.getMaPhong(), "Mã phòng học");
                    if (errMa != null) {
                        view.showMessage(errMa);
                        return;
                    }
                }
                String errTen = ValidationUtil.validateTen(p.getTenPhong(), "Tên phòng học");
                if (errTen != null) {
                    view.showMessage(errTen);
                    return;
                }
                String errSucChua = ValidationUtil.validateSucChua(view.getSucChuaText());
                if (errSucChua != null) {
                    view.showMessage(errSucChua);
                    return;
                }
                if (editMode[0]) {
                    apiClient.update(p.getMaPhong(), p);
                    view.showMessage("Cập nhật phòng học thành công!");
                } else {
                    apiClient.create(p);
                    view.showMessage("Thêm phòng học thành công!");
                }
                loadDataWithHighlight(p.getMaPhong());
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
            view.getTable().clearSelection();
            setIdleState.run();
        });

        view.addBtnMoiListener(e -> {
            view.clearForm();
            view.resetBoLoc();
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
        view.getTxtMaPhong().addMouseListener(formClickListener);
        view.getTxtTenPhong().addMouseListener(formClickListener);
        view.getTxtSucChua().addMouseListener(formClickListener);
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

    private void loadDataWithHighlight(String maPhong) {
        try {
            List<PhongHoc> list = apiClient.getAll();
            list.sort((p1, p2) -> p1.getMaPhong().compareToIgnoreCase(p2.getMaPhong()));
            if (maPhong != null && !maPhong.trim().isEmpty()) {
                PhongHoc target = null;
                for (PhongHoc item : list) {
                    if (maPhong.trim().equalsIgnoreCase(item.getMaPhong())) {
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
            List<PhongHoc> list = apiClient.getAll();
            list.sort((p1, p2) -> p1.getMaPhong().compareToIgnoreCase(p2.getMaPhong()));
            view.setTableData(list);
            isCustomOrder = false;
        } catch (Exception ex) {
            view.showMessage("Không thể kết nối server: " + ex.getMessage());
        }
    }
}
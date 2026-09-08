package Controller.ThuTrang;

import Api.ThuTrang.PhongHocApiClient;
import Model.PhongHoc;
import View.ThuTrang.FrmPhongHoc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PhongHocController {
    private FrmPhongHoc view;
    private PhongHocApiClient apiClient;

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

        view.addBtnTimListener(e -> {
            try {
                String ma = view.getMaPhongTim();
                String loai = view.getLoaiPhongTim();
                String tinhTrang = view.getTinhTrangTim();
                if (ma.isEmpty() && (loai.isEmpty() || loai.equals("Tất cả")) && (tinhTrang.isEmpty() || tinhTrang.equals("Tất cả"))) {
                    loadData();
                } else {
                    List<PhongHoc> list = apiClient.search(ma, loai, tinhTrang);
                    view.setTableData(list);
                }
            } catch (Exception ex) {
                view.showMessage("Lỗi tìm kiếm: " + ex.getMessage());
            }
        });

        view.addCboLoaiPhongTimListener(e -> {
            try {
                String ma = view.getMaPhongTim();
                String loai = view.getLoaiPhongTim();
                String tinhTrang = view.getTinhTrangTim();
                if (ma.isEmpty() && (loai.isEmpty() || loai.equals("Tất cả")) && (tinhTrang.isEmpty() || tinhTrang.equals("Tất cả"))) {
                    loadData();
                } else {
                    List<PhongHoc> list = apiClient.search(ma, loai, tinhTrang);
                    view.setTableData(list);
                }
            } catch (Exception ex) {
                view.showMessage("Lỗi tìm kiếm: " + ex.getMessage());
            }
        });

        view.addCboTinhTrangTimListener(e -> {
            try {
                String ma = view.getMaPhongTim();
                String loai = view.getLoaiPhongTim();
                String tinhTrang = view.getTinhTrangTim();
                if (ma.isEmpty() && (loai.isEmpty() || loai.equals("Tất cả")) && (tinhTrang.isEmpty() || tinhTrang.equals("Tất cả"))) {
                    loadData();
                } else {
                    List<PhongHoc> list = apiClient.search(ma, loai, tinhTrang);
                    view.setTableData(list);
                }
            } catch (Exception ex) {
                view.showMessage("Lỗi tìm kiếm: " + ex.getMessage());
            }
        });

        view.addBtnThemListener(e -> {
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
                if (p.getMaPhong().isEmpty()) {
                    view.showMessage("Mã phòng không được để trống");
                    return;
                }
                if (editMode[0]) {
                    apiClient.update(p.getMaPhong(), p);
                    view.showMessage("Cập nhật phòng học thành công");
                } else {
                    apiClient.create(p);
                    view.showMessage("Thêm phòng học thành công");
                }
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } catch (NumberFormatException ex) {
                view.showMessage("Sức chứa phải là số");
            } catch (Exception ex) {
                view.showMessage("Lỗi: " + ex.getMessage());
            }
        });

        view.addBtnHuyListener(e -> {
            view.clearForm();
            editMode[0] = false;
            view.getTable().clearSelection();
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
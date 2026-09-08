package Controller.ThuTrang;

import Api.ThuTrang.TKBApiClient;
import Api.Đai.HocSinhApi;
import Model.TKB;
import TienIch.XuatExcel;
import View.ThuTrang.FrmTKB;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TKBController {
    private FrmTKB view;
    private TKBApiClient apiClient;

    public TKBController(FrmTKB view) {
        this.view = view;
        this.apiClient = new TKBApiClient();

        try {
            view.setDanhSachLop(apiClient.getDanhSachLopTatCa());
            view.setDanhSachMon(apiClient.getDanhSachMon());
            view.setDanhSachGV(apiClient.getDanhSachGV());
            view.setDanhSachPhong(apiClient.getDanhSachPhong());
        } catch (Exception ex) {
            view.showMessage("Không thể tải danh sách: " + ex.getMessage());
        }

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

        view.addBtnLocTimKiemListener(e -> {
            try {
                String maLop = view.getLocMaLop();
                String maMH = view.getLocMon();
                int thu = view.getLocThu();
                if ((maLop.isEmpty() || maLop.equals("Tất cả")) && maMH.isEmpty() && thu == 0) {
                    loadData();
                } else {
                    List<TKB> list = apiClient.getByFilter(maLop, maMH, thu);
                    list = filterByRole(list);
                    view.setTableData(list);
                }
            } catch (Exception ex) {
                view.showMessage("Không thể kết nối server: " + ex.getMessage());
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
            if (row == -1) { view.showMessage("Vui lòng chọn dòng cần xóa"); return; }

            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String maTKB = view.getTable().getValueAt(row, 0).toString();
                    apiClient.delete(maTKB);
                    view.showMessage("Đã xóa");
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
                TKB t = view.getTKBInput();
                if (t.getMaLop().isEmpty() || t.getMaMH().isEmpty()
                        || t.getMaGV().isEmpty() || t.getMaPhong().isEmpty()) {
                    view.showMessage("Vui lòng nhập đầy đủ thông tin");
                    return;
                }
                if (t.getTietBatDau() > t.getTietKetThuc()) {
                    view.showMessage("Tiết bắt đầu phải nhỏ hơn hoặc bằng tiết kết thúc");
                    return;
                }
                if (editMode[0]) {
                    String maTKB = view.getTable().getValueAt(
                            view.getTable().getSelectedRow(), 0).toString();
                    apiClient.update(maTKB, t);
                    view.showMessage("Cập nhật thành công");
                } else {
                    apiClient.create(t);
                    view.showMessage("Thêm thành công");
                }
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } catch (NumberFormatException ex) {
                view.showMessage("Tiết bắt đầu / kết thúc phải là số");
            } catch (Exception ex) {
                view.showMessage("Lỗi: " + ex.getMessage());
            }
        });

        view.addBtnMoiListener(e -> {
            view.clearForm();
            editMode[0] = false;
            view.getTable().clearSelection();
            setIdleState.run();
        });

        view.addBtnHuyListener(e -> {
            view.clearForm();
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

        view.addBtnXuatExcelListener(e -> XuatExcel.xuatFileExcel(view.getTable(), view));
    }

    public void loadData() {
        try {
            List<TKB> list = apiClient.getAll();
            list = filterByRole(list);
            view.setTableData(list);
        } catch (Exception ex) {
            view.showMessage("Không thể kết nối server: " + ex.getMessage());
        }
    }

    private List<TKB> filterByRole(List<TKB> list) {
        if (Model.Auth.isHocSinh()) {
            Model.HocSinh hs = new HocSinhApi().getHocSinh(Model.Auth.maNguoiDung);
            if (hs != null && hs.getMaLop() != null) {
                return list.stream().filter(t -> hs.getMaLop().equals(t.getMaLop())).collect(java.util.stream.Collectors.toList());
            }
            return new java.util.ArrayList<>();
        } else if (Model.Auth.isGiaoVien()) {
            return list.stream().filter(t -> Model.Auth.maNguoiDung.equals(t.getMaGV())).collect(java.util.stream.Collectors.toList());
        }
        return list;
    }
}
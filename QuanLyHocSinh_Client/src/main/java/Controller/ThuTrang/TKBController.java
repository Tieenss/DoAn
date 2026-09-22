package Controller.ThuTrang;

import Api.ThuTrang.TKBApiClient;
import Api.Đai.HocSinhApi;
import Model.TKB;
import TienIch.ValidationUtil;
import TienIch.XuatExcel;
import View.ThuTrang.FrmTKB;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TKBController {
    private FrmTKB view;
    private TKBApiClient apiClient;
    private boolean isCustomOrder = false;

    public TKBController(FrmTKB view) {
        this.view = view;
        this.apiClient = new TKBApiClient();

        try {
            view.setDanhSachLop(apiClient.getDanhSachLopTatCa());
            view.setDanhSachMon(apiClient.getDanhSachMon());
            view.setDanhSachGV(apiClient.getDanhSachGV());
            view.setDanhSachPhong(apiClient.getDanhSachPhong());
            try {
                view.setCboLocNamHoc(apiClient.getDanhSachNamHoc());
            } catch (Exception ignored) {}
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

        Runnable doFilter = () -> {
            try {
                String maLop = view.getLocMaLop();
                String maMH = view.getLocMon();
                int thu = view.getLocThu();
                String namHoc = view.getLocNamHoc();
                int hocKy = view.getLocHocKy();
                
                if (Model.Auth.isHocSinh() && !maLop.isEmpty() && !maLop.equals("Tất cả")) {
                    Api.Đai.HocSinhApi hsApi = new Api.Đai.HocSinhApi();
                    Model.HocSinh hs = hsApi.getHocSinh(Model.Auth.maNguoiDung);
                    if (hs != null && !maLop.equals(hs.getMaLop())) {
                        view.showMessage("Bạn không có quyền tìm kiếm TKB của lớp khác!");
                        return;
                    }
                }

                if ((maLop.isEmpty() || maLop.equals("Tất cả")) && maMH.isEmpty() && thu == 0
                        && (namHoc.isEmpty() || namHoc.equals("Tất cả")) && hocKy == 0) {
                    loadData();
                } else {
                    List<TKB> list = apiClient.getByFilter(maLop, maMH, thu, namHoc, hocKy);
                    list = filterByRole(list);
                    sortTKB(list);
                    view.setTableData(list);
                }
            } catch (Exception ignored) {
            }
        };

        view.addLocMonLiveListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { doFilter.run(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { doFilter.run(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { doFilter.run(); }
        });

        view.addCboLocMaLopListener(e -> doFilter.run());
        view.addCboLocThuListener(e -> doFilter.run());
        view.addCboLocNamHocListener(e -> doFilter.run());
        view.addCboLocHocKyListener(e -> doFilter.run());
        view.addBtnLocTimKiemListener(e -> {
            doFilter.run();
            if (view.getTable().getRowCount() == 0) {
                view.showMessage("Không tìm thấy thời khóa biểu nào phù hợp!");
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
                    String maTKB = view.getSelectedMaTKB();
                    if (maTKB == null) return;
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
                if (t.getMaLop() == null || t.getMaLop().trim().isEmpty()) {
                    view.showMessage("Vui lòng chọn Lớp học!");
                    return;
                }
                if (t.getMaMH() == null || t.getMaMH().trim().isEmpty()) {
                    view.showMessage("Vui lòng chọn Môn học!");
                    return;
                }
                if (t.getMaGV() == null || t.getMaGV().trim().isEmpty()) {
                    view.showMessage("Vui lòng chọn Giáo viên giảng dạy!");
                    return;
                }
                if (t.getMaPhong() == null || t.getMaPhong().trim().isEmpty()) {
                    view.showMessage("Vui lòng chọn Phòng học!");
                    return;
                }
                if (t.getThu() <= 0) {
                    view.showMessage("Vui lòng chọn Thứ!");
                    return;
                }
                if (t.getHocKy() <= 0) {
                    view.showMessage("Vui lòng chọn Học kỳ!");
                    return;
                }
                if (t.getTietBatDau() <= 0 || t.getTietKetThuc() <= 0) {
                    view.showMessage("Vui lòng chọn Tiết bắt đầu và Tiết kết thúc!");
                    return;
                }
                if (t.getTietBatDau() > t.getTietKetThuc()) {
                    view.showMessage("Lỗi: Tiết bắt đầu (" + t.getTietBatDau() + ") phải nhỏ hơn hoặc bằng tiết kết thúc (" + t.getTietKetThuc() + ")!");
                    return;
                }

                String errNamBD = ValidationUtil.validateNam(view.getNamBatDau());
                if (errNamBD != null) {
                    view.showMessage("Năm bắt đầu: " + errNamBD);
                    return;
                }
                String errNamKT = ValidationUtil.validateNam(view.getNamKetThuc());
                if (errNamKT != null) {
                    view.showMessage("Năm kết thúc: " + errNamKT);
                    return;
                }

                int namBD = Integer.parseInt(view.getNamBatDau());
                int namKT = Integer.parseInt(view.getNamKetThuc());

                if (namKT < namBD) {
                    view.showMessage("Lỗi: Năm kết thúc (" + namKT + ") phải lớn hơn hoặc bằng năm bắt đầu (" + namBD + ")!");
                    return;
                }

                Integer highlightId = null;
                if (editMode[0]) {
                    String maTKB = view.getSelectedMaTKB();
                    if (maTKB == null) {
                        view.showMessage("Vui lòng chọn một bản ghi để sửa!");
                        return;
                    }
                    TKB updated = apiClient.update(maTKB, t);
                    highlightId = updated != null ? updated.getMaTKB() : Integer.parseInt(maTKB);
                    view.showMessage("Cập nhật thời khóa biểu thành công!");
                } else {
                    TKB created = apiClient.create(t);
                    if (created != null) {
                        highlightId = created.getMaTKB();
                    }
                    view.showMessage("Thêm thời khóa biểu thành công!");
                }
                loadDataWithHighlight(highlightId);
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } catch (NumberFormatException ex) {
                view.showMessage("Tiết bắt đầu / kết thúc hoặc năm học phải là số hợp lệ!");
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg != null && (msg.startsWith("Trùng") || msg.startsWith("Phòng học") || msg.startsWith("Lỗi") || msg.contains("ký tự") || msg.contains("kí tự") || msg.contains("tồn tại"))) {
                    view.showMessage(msg);
                } else {
                    view.showMessage("Lỗi: " + msg);
                }
            }
        });

        view.addBtnMoiListener(e -> {
            view.clearForm();
            view.resetBoLoc();
            loadData();
            editMode[0] = false;
            view.getTable().clearSelection();
            setIdleState.run();
        });

        view.addBtnHuyListener(e -> {
            view.clearForm();
            loadData();
            editMode[0] = false;
            setIdleState.run();
        });

        MouseAdapter formClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restoreOrderIfNeeded.run();
            }
        };
        if (view.getPnlInput() != null) {
            view.getPnlInput().addMouseListener(formClickListener);
        }

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

    private void sortTKB(List<TKB> list) {
        if (list == null || list.isEmpty()) return;
        java.text.Collator viCollator = java.text.Collator.getInstance(java.util.Locale.forLanguageTag("vi-VN"));
        list.sort((t1, t2) -> {
            String n1 = t1.getNamHoc() == null ? "" : t1.getNamHoc().trim();
            String n2 = t2.getNamHoc() == null ? "" : t2.getNamHoc().trim();
            int cmpNam = n2.compareToIgnoreCase(n1);
            if (cmpNam != 0) return cmpNam;

            int cmpHK = Integer.compare(t2.getHocKy(), t1.getHocKy());
            if (cmpHK != 0) return cmpHK;

            String l1 = t1.getMaLop() == null ? "" : t1.getMaLop().trim();
            String l2 = t2.getMaLop() == null ? "" : t2.getMaLop().trim();
            int cmpLop = l1.compareToIgnoreCase(l2);
            if (cmpLop != 0) return cmpLop;

            String tm1 = t1.getTenMH() == null ? "" : t1.getTenMH().trim();
            String tm2 = t2.getTenMH() == null ? "" : t2.getTenMH().trim();
            int cmpMon = viCollator.compare(tm1, tm2);
            if (cmpMon != 0) return cmpMon;

            int cmpThu = Integer.compare(t1.getThu(), t2.getThu());
            if (cmpThu != 0) return cmpThu;

            return Integer.compare(t1.getTietBatDau(), t2.getTietBatDau());
        });
    }

    private void loadDataWithHighlight(Integer maTKB) {
        try {
            List<TKB> list = apiClient.getAll();
            list = filterByRole(list);
            sortTKB(list);
            if (maTKB != null) {
                TKB target = null;
                for (TKB item : list) {
                    if (maTKB.equals(item.getMaTKB())) {
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
            List<TKB> list = apiClient.getAll();
            list = filterByRole(list);
            sortTKB(list);
            view.setTableData(list);
            isCustomOrder = false;
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
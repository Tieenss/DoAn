package Controller.Tien;

import Api.Tien.HanhKiemApi;
import Api.Đat.LopApi;
import Model.HanhKiem;
import Model.LopGVCN;
import TienIch.XuatExcel;
import View.Tien.HanhKiemPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import Model.Auth;

public class HanhKiemController {
    
    private HanhKiemPanel view;
    private HanhKiemApi dao;

    public HanhKiemController(HanhKiemPanel view) {
        this.view = view;
        this.dao = new HanhKiemApi();
        
        loadComboBoxData();
        initEvents();
        loadData(); 
    }

    private void loadComboBoxData() {
        LopApi lopApi = new LopApi();
        List<LopGVCN> lops = lopApi.getAllLop();
        List<String> maLops = new ArrayList<>();
        for (LopGVCN l : lops) {
            maLops.add(l.getMaLop());
        }
        view.setMaLopData(maLops);

        List<String> namHocs = dao.getDistinctNamHoc();
        if (namHocs.isEmpty()) {
            namHocs.add("2024-2025");
        }
        view.setNamHocData(namHocs);
    }

    private void initEvents() {
        boolean[] editMode = {false};
        Runnable setIdleState = () -> view.setCrudButtonState(true, false, false, false, false);
        Runnable setAddState = () -> view.setCrudButtonState(false, false, false, true, true);
        Runnable setSelectedState = () -> view.setCrudButtonState(false, true, true, false, true);
        Runnable setEditState = () -> view.setCrudButtonState(false, true, true, true, true);
        setIdleState.run();
        view.addBtnXemListener(e -> loadData());
        view.addBtnTimKiemListener(e -> searchData());
        view.addBtnThemListener(e -> {
            editMode[0] = false;
            view.clearForm();
            view.getTable().clearSelection();
            setAddState.run();
        });

        view.addBtnSuaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                view.showMessage("Vui lòng chọn một bản ghi");
                return;
            }
            editMode[0] = true;
            view.fillFormInput(row);
            setEditState.run();
        });
        view.addBtnLuuListener(e -> {
            HanhKiem hk = view.getHanhKiemInput();
            if (hk.getMaHS().isEmpty()) {
                view.showMessage("Vui lòng chọn hoặc nhập mã học sinh để đánh giá!");
                return;
            }
            if (hk.getXepLoai() == null || hk.getXepLoai().trim().isEmpty()) {
                view.showMessage("Vui lòng chọn Xếp loại hạnh kiểm (Tốt, Khá, Trung bình, Yếu)!");
                return;
            }

            if (!editMode[0]) {
                // 1. THÊM MỚI (INSERT) -> CHECK TRÙNG LẶP
                boolean exists = dao.checkExists(hk.getMaHS(), hk.getNamHoc(), hk.getHocKy());
                if (exists) {
                    view.showMessage(String.format("LỖI TRÙNG LẶP DỮ LIỆU:\nHọc sinh '%s' đã có đánh giá hạnh kiểm trong Học kỳ %d - Năm học %s!\n" +
                            "Hệ thống không cho phép nhập trùng. Vui lòng chọn dòng trên bảng và bấm 'Sửa' để cập nhật.",
                            hk.getMaHS(), hk.getHocKy(), hk.getNamHoc()));
                    return;
                }

                String err = dao.addHanhKiemResult(hk);
                if (err == null) {
                    view.showMessage("Thêm hạnh kiểm học sinh thành công!");
                    loadData();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Thêm thất bại:\n" + err);
                }
            } else {
                // 2. SỬA DỮ LIỆU (UPDATE) -> CẢNH BÁO NGUY HIỂM 2 BƯỚC
                int row = view.getTable().getSelectedRow();
                String tenHS = row >= 0 && view.getTable().getValueAt(row, 1) != null ? view.getTable().getValueAt(row, 1).toString() : "";
                String oldXepLoai = row >= 0 && view.getTable().getValueAt(row, 5) != null ? view.getTable().getValueAt(row, 5).toString() : "";
                String oldNhanXet = row >= 0 && view.getTable().getValueAt(row, 6) != null ? view.getTable().getValueAt(row, 6).toString() : "";

                List<String[]> changes = new ArrayList<>();
                changes.add(new String[]{"Xếp loại hạnh kiểm", oldXepLoai, hk.getXepLoai()});
                changes.add(new String[]{"Nhận xét rèn luyện", oldNhanXet, hk.getNhanXet()});

                List<String> impacts = java.util.Arrays.asList(
                    "Làm thay đổi xếp loại rèn luyện đạo đức trong học kỳ và cả năm của học sinh.",
                    "Ảnh hưởng trực tiếp đến việc xét công nhận Danh hiệu Học sinh Giỏi / Học sinh Tiên tiến (yêu cầu hạnh kiểm Tốt/Khá).",
                    "Tác động trực tiếp đến điều kiện xét cấp Học bổng khuyến khích học tập.",
                    "Ảnh hưởng đến hồ sơ học bạ và kết quả xét Tốt nghiệp THPT / THCS."
                );

                String entityInfo = String.format("Mã HS: %s (%s) | Lớp: %s | HK: %d | Năm: %s",
                        hk.getMaHS(), tenHS, hk.getMaLop(), hk.getHocKy(), hk.getNamHoc());

                boolean pass = TienIch.DangerConfirmDialog.showUpdateConfirmation(
                        view, "CẢNH BÁO NGUY HIỂM: SỬA HẠNH KIỂM HỌC SINH", entityInfo, changes, impacts);

                if (!pass) {
                    return;
                }

                String updateErr = dao.updateHanhKiemResult(hk);
                if (updateErr == null) {
                    view.showMessage("Cập nhật hạnh kiểm thành công!");
                    loadData();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Cập nhật thất bại:\n" + updateErr);
                }
            }
        });

        view.addBtnXoaListener(e -> {
            HanhKiem hk = view.getHanhKiemInput();
            if (hk.getMaHS().isEmpty()) {
                 view.showMessage("Vui lòng chọn dòng cần xóa!"); 
                 return;
            }

            int row = view.getTable().getSelectedRow();
            String tenHS = row >= 0 && view.getTable().getValueAt(row, 1) != null ? view.getTable().getValueAt(row, 1).toString() : "";
            String oldXepLoai = row >= 0 && view.getTable().getValueAt(row, 5) != null ? view.getTable().getValueAt(row, 5).toString() : "";

            String entityInfo = String.format("Học sinh: %s (%s) | Xếp loại: %s | HK: %d | Năm: %s",
                    tenHS, hk.getMaHS(), oldXepLoai, hk.getHocKy(), hk.getNamHoc());

            List<String> impacts = java.util.Arrays.asList(
                "Hồ sơ đánh giá rèn luyện hạnh kiểm của học sinh trong học kỳ này sẽ bị XÓA VĨNH VIỄN.",
                "Học sinh sẽ bị thiếu tiêu chí hạnh kiểm -> Không đủ điều kiện xét danh hiệu thi đua và học bổng.",
                "Không thể tổng kết xếp loại cả năm nếu thiếu hạnh kiểm một trong hai học kỳ.",
                "Thao tác này KHÔNG THỂ KHÔI PHỤC tự động!"
            );

            boolean pass = TienIch.DangerConfirmDialog.showDeleteConfirmation(
                    view, "CẢNH BÁO NGUY HIỂM: XÓA HẠNH KIỂM", entityInfo, impacts);

            if (!pass) {
                return;
            }

            String delErr = dao.deleteHanhKiemResult(hk.getMaHS(), hk.getNamHoc(), hk.getHocKy());
            if (delErr == null) {
                view.showMessage("Xóa hạnh kiểm thành công!");
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } else {
                view.showMessage("Xóa thất bại:\n" + delErr);
            }
        });
        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                if (row >= 0) {
                    editMode[0] = true;
                    view.fillFormInput(row);
                    setSelectedState.run();
                }
            }
        });
        view.addBtnHuyListener(e -> {
            view.clearForm();
            editMode[0] = false;
            view.getTable().clearSelection();
            setIdleState.run();
        });
        view.addBtnXuatExcelListener(e -> {
            XuatExcel.xuatFileExcel(view.getTable(), view);
        });
        view.addMaHS(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String maHS = view.getMaHSInput();
                if (!maHS.isEmpty()) {

                    List<HanhKiem> list = dao.searchHanhKiemByMaHS(maHS, "");
                    if(list != null && !list.isEmpty()) {
                        view.setTenHS(list.get(0).getTenHS());
                    }
                }
            }
        });
    }
    private void loadData() {
        try {
            List<HanhKiem> list;

            if (Auth.isHocSinh()) {

                list = dao.getHanhKiemByMaHS(Auth.maNguoiDung);
                view.hideButtonForStudent();

            } else {

            String maLop = view.getMaLopFilter();
            String namHoc = view.getNamHocFilter();
            int hocKy = view.getHocKyFilter();
            list = dao.getHanhKiemByFilter(maLop, namHoc, hocKy);
            }
            view.setTableData(list);
            
        } catch (Exception ex) {
            view.showMessage("Lỗi tải dữ liệu: " + ex.getMessage());
        }
    }
    private void searchData() {
        String keyword = view.getTuKhoaTimKiem();
        
        if(keyword.isEmpty()) {
            view.showMessage("Vui lòng nhập từ khóa (Tên hoặc Mã HS)!");
            return;
        }

        List<HanhKiem> list;

        if (Auth.isHocSinh()) {
            list = dao.searchHanhKiemByMaHS(Auth.maNguoiDung, keyword);
        } else {
            list = dao.searchHanhKiem(keyword);
        }

        if(list.isEmpty()) {
            view.showMessage("Không tìm thấy kết quả nào cho: " + keyword);
        }
        view.setTableData(list);
    }
}

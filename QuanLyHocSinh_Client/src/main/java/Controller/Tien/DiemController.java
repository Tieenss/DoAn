package Controller.Tien; 

import Api.Tien.DiemApi;
import Api.ThuTrang.MonHocApiClient;
import Api.Đat.LopApi;
import Model.Auth;
import Model.Diem;
import Model.LopGVCN;
import Model.MonHoc;
import View.Tien.QuanLyDiemPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import TienIch.XuatExcel;

public class DiemController { 
    
    private QuanLyDiemPanel view;
    private DiemApi dao;
    private List<MonHoc> monHocList;

    public DiemController(QuanLyDiemPanel view) {
        this.view = view;
        this.dao = new DiemApi();
        
        loadComboBoxData();
        initEvents();
        loadData(); 
    }

    private void loadComboBoxData() {
        LopApi lopApi = new LopApi();
        MonHocApiClient monHocApiClient = new MonHocApiClient();

        List<LopGVCN> lops = lopApi.getAllLop();
        List<String> maLops = new ArrayList<>();
        for (LopGVCN l : lops) {
            maLops.add(l.getMaLop());
        }
        view.setMaLopData(maLops);

        try {
            monHocList = monHocApiClient.getAll();
            view.setMonHocData(monHocList);
        } catch (Exception e) {
            e.printStackTrace();
            
            System.out.println("Lỗi khi tải danh sách môn học từ API: " + e.getMessage());
            monHocList = new ArrayList<>();
            view.setMonHocData(monHocList);
        }

        List<Integer> hks = dao.getDistinctHocKy();
        if (hks.isEmpty()) {
            hks.add(1);
            hks.add(2);
        }
        view.setHocKyData(hks);
        
        List<String> nhs = dao.getDistinctNamHoc();
        if (nhs.isEmpty()) {
            nhs.add("2023-2024");
        }
        view.setNamHocData(nhs);
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
            setAddState.run();
        });

        view.addBtnSuaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                view.showMessage("Vui lòng chọn một dòng trên bảng để sửa điểm!");
                return;
            }
            editMode[0] = true;
            view.fillFormInput(row);
            setEditState.run();
        });

        view.addBtnLuuListener(e -> {
            Diem d = view.getDiemInput();
            if (d == null) {
                view.showMessage("Điểm số phải là số thực hợp lệ (Ví dụ: 8.5)!"); 
                return;
            }
            if (d.getMaHS().isEmpty()) {
                view.showMessage("Vui lòng nhập Mã học sinh!"); 
                return;
            }
            if (d.getMaMH() == null || d.getMaMH().isEmpty()) {
                view.showMessage("Vui lòng chọn Môn học!");
                return;
            }

            // Kiểm tra biên thang điểm từ 0.0 đến 10.0 cho các cột đã nhập
            if ((d.getDiem15p() != null && (d.getDiem15p() < 0 || d.getDiem15p() > 10)) ||
                (d.getDiem1Tiet() != null && (d.getDiem1Tiet() < 0 || d.getDiem1Tiet() > 10)) ||
                (d.getDiemGiuaKy() != null && (d.getDiemGiuaKy() < 0 || d.getDiemGiuaKy() > 10)) ||
                (d.getDiemCuoiKy() != null && (d.getDiemCuoiKy() < 0 || d.getDiemCuoiKy() > 10))) {
                view.showMessage("Lỗi: Tất cả các cột điểm phải nằm trong thang điểm từ 0.0 đến 10.0!");
                return;
            }

            // Điểm tổng kết được tính tự động từ Model.Diem
            Double dtbObj = d.getDiemTongKet();
            String strNewTongKet = dtbObj != null ? String.valueOf(Math.round(dtbObj * 100.0) / 100.0) : "Chưa nhập";

            if (!editMode[0]) {
                // 1. THÊM MỚI (INSERT) -> CHECK TRÙNG LẶP
                boolean exists = dao.checkExists(d.getMaHS(), d.getMaMH(), d.getHocKy(), d.getNamHoc());
                if (exists) {
                    view.showMessage(String.format("LỖI TRÙNG LẶP DỮ LIỆU:\nHọc sinh '%s' đã có bảng điểm môn '%s' trong Học kỳ %d - Năm học %s!\n" +
                            "Hệ thống không cho phép thêm trùng. Vui lòng click vào dòng tương ứng trên bảng và bấm 'Sửa' để cập nhật.",
                            d.getMaHS(), d.getMaMH(), d.getHocKy(), d.getNamHoc()));
                    return;
                }

                String err = dao.addDiem(d);
                if (err == null) {
                    view.showMessage("Thêm mới điểm học sinh thành công!");
                    loadData();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Thêm thất bại: " + err);
                }
            } else {
                // 2. SỬA DỮ LIỆU (UPDATE) -> CẢNH BÁO NGUY HIỂM 2 BƯỚC
                int row = view.getTable().getSelectedRow();
                String old15p = row >= 0 && view.getTable().getValueAt(row, 6) != null ? String.valueOf(view.getTable().getValueAt(row, 6)) : "";
                String old1Tiet = row >= 0 && view.getTable().getValueAt(row, 7) != null ? String.valueOf(view.getTable().getValueAt(row, 7)) : "";
                String oldGiuaKy = row >= 0 && view.getTable().getValueAt(row, 8) != null ? String.valueOf(view.getTable().getValueAt(row, 8)) : "";
                String oldCuoiKy = row >= 0 && view.getTable().getValueAt(row, 9) != null ? String.valueOf(view.getTable().getValueAt(row, 9)) : "";
                String oldTongKet = row >= 0 && view.getTable().getValueAt(row, 10) != null ? String.valueOf(view.getTable().getValueAt(row, 10)) : "";

                List<String[]> changes = new ArrayList<>();
                changes.add(new String[]{"Điểm 15 phút", old15p.isEmpty() ? "Chưa nhập" : old15p, d.getDiem15p() != null ? String.valueOf(d.getDiem15p()) : "Chưa nhập"});
                changes.add(new String[]{"Điểm 1 tiết", old1Tiet.isEmpty() ? "Chưa nhập" : old1Tiet, d.getDiem1Tiet() != null ? String.valueOf(d.getDiem1Tiet()) : "Chưa nhập"});
                changes.add(new String[]{"Điểm Giữa kỳ", oldGiuaKy.isEmpty() ? "Chưa nhập" : oldGiuaKy, d.getDiemGiuaKy() != null ? String.valueOf(d.getDiemGiuaKy()) : "Chưa nhập"});
                changes.add(new String[]{"Điểm Cuối kỳ", oldCuoiKy.isEmpty() ? "Chưa nhập" : oldCuoiKy, d.getDiemCuoiKy() != null ? String.valueOf(d.getDiemCuoiKy()) : "Chưa nhập"});
                changes.add(new String[]{"Điểm Tổng kết", oldTongKet.isEmpty() ? "Chưa nhập" : oldTongKet, strNewTongKet});

                List<String> impacts = java.util.Arrays.asList(
                    "Làm thay đổi Điểm trung bình môn học kỳ và cả năm học của học sinh.",
                    "Ảnh hưởng trực tiếp đến việc xếp loại Học lực (Giỏi / Khá / Trung bình / Yếu).",
                    "Có thể làm học sinh bị mất hoặc hạ bậc danh hiệu thi đua (Học sinh Giỏi / Học sinh Tiên tiến).",
                    "Tác động trực tiếp đến kết quả xét Học bổng và điều kiện Lên lớp / Tốt nghiệp."
                );

                String entityInfo = String.format("Mã HS: %s | Môn: %s | HK: %d | Năm: %s",
                        d.getMaHS(), d.getMaMH(), d.getHocKy(), d.getNamHoc());

                boolean pass = TienIch.DangerConfirmDialog.showUpdateConfirmation(
                        view, "CẢNH BÁO NGUY HIỂM: SỬA ĐIỂM HỌC SINH", entityInfo, changes, impacts);

                if (!pass) {
                    return; // Người dùng đã hủy bỏ thao tác ở bước 1 hoặc bước 2
                }

                String updateErr = dao.updateDiemResult(d);
                if (updateErr == null) {
                    view.showMessage("Đã cập nhật điểm thành công!");
                    loadData();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Cập nhật thất bại: " + updateErr);
                }
            }
        });

        view.addBtnXoaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                view.showMessage("Vui lòng chọn dòng điểm cần xóa trên bảng!");
                return;
            }

            String maHS = String.valueOf(view.getTable().getValueAt(row, 0));
            String tenHS = String.valueOf(view.getTable().getValueAt(row, 1));
            String tenMon = String.valueOf(view.getTable().getValueAt(row, 3));
            String namHoc = String.valueOf(view.getTable().getValueAt(row, 4));
            int hocKy = Integer.parseInt(view.getTable().getValueAt(row, 5).toString());

            String maMH = "";
            if (monHocList != null) {
                for (MonHoc m : monHocList) {
                    if (m.getTenMH().equals(tenMon) || m.getMaMH().equals(tenMon)) {
                        maMH = m.getMaMH();
                        break;
                    }
                }
            }
            if (maMH.isEmpty()) maMH = tenMon;

            String entityInfo = String.format("Học sinh: %s (%s) - Môn: %s - HK: %d - Năm: %s",
                    tenHS, maHS, tenMon, hocKy, namHoc);

            List<String> impacts = java.util.Arrays.asList(
                "Bản ghi điểm môn học này của học sinh sẽ bị XÓA VĨNH VIỄN khỏi hệ thống.",
                "Học sinh sẽ bị thiếu điểm môn này -> Không thể tính Điểm tổng kết học kỳ.",
                "Không đủ điều kiện xét danh hiệu thi đua và xếp loại học lực cho học kỳ.",
                "Thao tác này KHÔNG THỂ KHÔI PHỤC tự động!"
            );

            boolean pass = TienIch.DangerConfirmDialog.showDeleteConfirmation(
                    view, "CẢNH BÁO NGUY HIỂM: XÓA BẢNG ĐIỂM", entityInfo, impacts);

            if (!pass) {
                return;
            }

            String delErr = dao.deleteDiem(maHS, maMH, hocKy, namHoc);
            if (delErr == null) {
                view.showMessage("Xóa bảng điểm thành công!");
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } else {
                view.showMessage("Xóa thất bại: " + delErr);
            }
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
                    view.fillFormInput(row);
                    setSelectedState.run();
                }
            }
        });

        view.addTxtMaHSFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                String maHS = view.getMaHSInput();
                if (!maHS.isEmpty()) {
                    List<Diem> list = dao.getDiemByMaHS(maHS);
                    if (list != null && !list.isEmpty()) {
                        view.setTenHS(list.get(0).getTenHS());
                    }
                }
            }
        });

        view.addBtnXuatExcelListener(e -> {
            XuatExcel.xuatFileExcel(view.getTable(), view);
        });
    }

    private void searchData() {
        String keyword = view.getTuKhoaTimKiem();

        if (keyword.isEmpty()) {
            loadData(); 
            return;
        }

        List<Diem> list = dao.searchDiem(keyword);
        view.setTableData(list);

        if (list.isEmpty()) {
            view.showMessage("Không tìm thấy học sinh nào với từ khóa: " + keyword);
        }
    }

    private void loadData() {
        List<Diem> list;

        if (Auth.isHocSinh()) {
            String maHocSinh = Auth.maNguoiDung.toUpperCase();
            list = dao.getDiemByMaHS(maHocSinh);
            if (view.getBtnCapNhat() != null) {
                view.getBtnCapNhat().setVisible(false);
            }

        } else {
            String maLop = view.getMaLopFilter();
            String maMon = view.getMaMonFilter();
            int hocKy = view.getHocKyFilter();
            String namHoc = view.getNamHocFilter();
            
            if (maLop.isEmpty() && maMon.isEmpty() && hocKy == 0 && namHoc.isEmpty()) {
                list = dao.getAll();
            } else {
                list = dao.getDiemByFilter(maLop, maMon, hocKy, namHoc);
            }
        }
        view.setTableData(list);
    }
}
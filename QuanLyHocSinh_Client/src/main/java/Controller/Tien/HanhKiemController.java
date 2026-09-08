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
            if(hk.getMaHS().isEmpty()) {
                view.showMessage("Vui lòng chọn học sinh trên bảng để đánh giá!");
                return;
            }
            if (dao.saveHanhKiem(hk)) {
                view.showMessage("Lưu hạnh kiểm thành công!");
                loadData();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } else {
                view.showMessage("Lưu thất bại! Có lỗi xảy ra.");
            }
        });
        view.addBtnXoaListener(e -> {
            HanhKiem hk = view.getHanhKiemInput();
            if(hk.getMaHS().isEmpty()) {
                 view.showMessage("Vui lòng chọn dòng cần xóa!"); 
                 return;
            }
            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                view, "Bạn có chắc chắn muốn xóa?", "Xác nhận",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                if (dao.deleteHanhKiem(hk.getMaHS(), hk.getNamHoc(), hk.getHocKy())) {
                    view.showMessage("Xóa thành công!");
                    loadData();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Xóa thất bại!");
                }
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

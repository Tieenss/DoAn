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
            List<MonHoc> mons = monHocApiClient.getAll();
            view.setMonHocData(mons);
        } catch (Exception e) {
            e.printStackTrace();
            
            System.out.println("Lỗi khi tải danh sách môn học từ API: " + e.getMessage());
            view.setMonHocData(new ArrayList<>());
        }

        List<Integer> hks = dao.getDistinctHocKy();
        if (hks.isEmpty()) {
            hks.add(1);
            hks.add(2);
        }
        view.setHocKyData(hks);
    }

    private void initEvents() {
        view.addBtnXemListener(e -> loadData());
        view.addBtnTimKiemListener(e -> searchData());
        view.addBtnCapNhatListener(e -> {
            Diem d = view.getDiemInput();
            if (d == null) {
                view.showMessage("Điểm số phải là số thực (Ví dụ: 8.5)!"); 
                return;
            }
            if (d.getMaHS().isEmpty()) {
                view.showMessage("Vui lòng click chọn học sinh trên bảng trước!"); 
                return;
            }
            if (dao.updateDiem(d)) {
                view.showMessage("Đã cập nhật điểm thành công!");
                loadData();
            } else {
                view.showMessage("Cập nhật thất bại! Hãy kiểm tra kết nối CSDL.");
            }
        });
        view.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().getSelectedRow();
                view.fillFormInput(row); 
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
            
            if (maLop.isEmpty() && maMon.isEmpty() && hocKy == 0) {
                list = dao.getAll();
            } else {
                list = dao.getDiemByFilter(maLop, maMon, hocKy);
            }
        }
        view.setTableData(list);
    }
}
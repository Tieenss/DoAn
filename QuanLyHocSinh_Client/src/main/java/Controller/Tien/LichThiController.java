package Controller.Tien;

import Api.Tien.LichThiApi;
import Model.LichThi;
import Model.LopGVCN;
import View.Tien.LichThiPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import TienIch.XuatExcel;
import Model.MonHoc;
import Model.PhongHoc;
import Api.Đat.LopApi;
import Api.ThuTrang.MonHocApiClient;
import Api.ThuTrang.PhongHocApiClient;

public class LichThiController {
    
    private LichThiPanel view;
    private LichThiApi dao;
    private List<MonHoc> monHocList;
    private List<PhongHoc> phongHocList;
    
    public LichThiController(LichThiPanel view) {
        this.view = view;
        this.dao = new LichThiApi();
        loadComboBoxData();
        initEvents();
        loadAll();
    }

    private void loadComboBoxData() {
        try {
            List<String> kyThis = dao.getDistinctKyThi();
            view.setKyThiData(kyThis);

            MonHocApiClient monApi = new MonHocApiClient();
            monHocList = monApi.getAll();
            List<String> tenMons = new ArrayList<>();
            for (MonHoc m : monHocList) {
                tenMons.add(m.getTenMH());
            }
            view.setMonHocData(tenMons);

            PhongHocApiClient phongApi = new PhongHocApiClient();
            phongHocList = phongApi.getAll();
            List<String> tenPhongs = new ArrayList<>();
            for (PhongHoc p : phongHocList) {
                tenPhongs.add(p.getTenPhong()); 
            }
            view.setPhongHocData(tenPhongs);

            LopApi lopApi = new LopApi();
            List<LopGVCN> lopList = lopApi.getAllLop();
            List<String> maLops = new ArrayList<>();
            for (LopGVCN l : lopList) {
                maLops.add(l.getMaLop());
            }
            view.setLopData(maLops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvents() {
        boolean[] editMode = {false};
        Runnable setIdleState = () -> view.setCrudButtonState(true, false, false, false, false);
        Runnable setAddState = () -> view.setCrudButtonState(false, false, false, true, true);
        Runnable setSelectedState = () -> view.setCrudButtonState(false, true, true, false, true);
        Runnable setEditState = () -> view.setCrudButtonState(false, true, true, true, true);
        setIdleState.run();

        view.addBtnLocDanhSachListener(e -> {
            String kyThi = view.getKyThiFilter();
            String tenMon = view.getMonFilter();
            String phong = view.getPhongFilter();
            String maLop = view.getLopFilter();

            String maMH = "";
            if (!tenMon.isEmpty() && monHocList != null) {
                for (MonHoc m : monHocList) {
                    if (m.getTenMH().equals(tenMon)) {
                        maMH = m.getMaMH();
                        break;
                    }
                }
            }
            
            List<LichThi> list = dao.getLichThiByFilter(kyThi, maMH, phong, maLop);
            view.setTableData(list);
            if (list.isEmpty()) view.showMessage("Không tìm thấy lịch thi phù hợp!");
        });

        view.addBtnTimKiemListener(e -> {
            String kw = view.getKeyword();
            if(kw.isEmpty()) { 
                loadAll();
                return; 
            }
            
            List<LichThi> list = dao.searchLichThi(kw);
            view.setTableData(list);
            
            if(list.isEmpty()) view.showMessage("Không tìm thấy kết quả nào!");
        });
        view.addBtnXemTatCaListener(e -> loadAll());
        view.addBtnThemListener(e -> {
            editMode[0] = false;
            view.clearForm();
            view.getTable().clearSelection();

            int maxId = 0;
            for(int i=0; i<view.getTable().getRowCount(); i++) {
                try {
                    int id = Integer.parseInt(view.getTable().getValueAt(i, 0).toString());
                    if (id > maxId) maxId = id;
                } catch(Exception ex) {}
            }
            view.getCboMaLT().getEditor().setItem(String.valueOf(maxId + 1));
            
            setAddState.run();
        });
        
        view.getCboMaLT().addActionListener(e -> {
            String selected = "";
            if (view.getCboMaLT().getSelectedItem() != null) {
                selected = view.getCboMaLT().getSelectedItem().toString();
            }
            if(!selected.isEmpty()) {
                
                for(int i=0; i<view.getTable().getRowCount(); i++) {
                    if(view.getTable().getValueAt(i, 0).toString().equals(selected)) {
                        view.getTable().setRowSelectionInterval(i, i);

                        break;
                    }
                }
            }
        });
        view.addBtnSuaListener(e -> {
            int row = view.getTable().getSelectedRow();
            if (row == -1) {
                view.showMessage("Vui lòng chọn một bản ghi");
                return;
            }
            editMode[0] = true;
            view.fillForm(row);
            setEditState.run();
        });
        view.addBtnLuuListener(e -> {
            LichThi lt = view.getLichThiInput();

            if (!lt.getMaMH().isEmpty() && monHocList != null) {
                for (MonHoc m : monHocList) {
                    if (m.getTenMH().equals(lt.getMaMH())) {
                        lt.setMaMH(m.getMaMH());
                        break;
                    }
                }
            }

            if (!lt.getMaPhong().isEmpty() && phongHocList != null) {
                for (PhongHoc p : phongHocList) {
                    if (p.getTenPhong() != null && p.getTenPhong().equals(lt.getMaPhong())) {
                        lt.setMaPhong(p.getMaPhong());
                        break;
                    }
                }
            }

            if (lt.getMaMH().isEmpty() || lt.getNgayThi().isEmpty() || lt.getMaLop().isEmpty()) {
                view.showMessage("Vui lòng nhập Mã môn và Ngày thi và Lớp!");
                return;
            }

            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                java.util.Date start = sdf.parse(lt.getGioBatDau());
                java.util.Date end = sdf.parse(lt.getGioKetThuc());
                if (!start.before(end)) {
                    view.showMessage("Lỗi: Giờ kết thúc phải lớn hơn giờ bắt đầu!");
                    return;
                }
            } catch (Exception ex) {
                view.showMessage("Lỗi định dạng giờ!");
                return;
            }

            List<LichThi> allExams = dao.getAllLichThi();
            for (LichThi existing : allExams) {
                if (existing.getMaLT() == lt.getMaLT()) continue; 
                
                if (existing.getNgayThi() != null && existing.getMaPhong() != null &&
                    existing.getNgayThi().equals(lt.getNgayThi()) && 
                    existing.getMaPhong().equals(lt.getMaPhong())) {

                    if (lt.getGioBatDau().compareTo(existing.getGioKetThuc()) < 0 && 
                        lt.getGioKetThuc().compareTo(existing.getGioBatDau()) > 0) {
                        view.showMessage(String.format("Lỗi: Trùng lịch với Mã LT %d (từ %s đến %s) cùng ngày, cùng phòng!", 
                            existing.getMaLT(), existing.getGioBatDau(), existing.getGioKetThuc()));
                        return;
                    }
                }

                if (existing.getNgayThi() != null && existing.getMaLop() != null &&
                        existing.getNgayThi().equals(lt.getNgayThi()) &&
                        existing.getMaLop().equals(lt.getMaLop())) {

                    if (lt.getGioBatDau().compareTo(existing.getGioKetThuc()) < 0 &&
                            lt.getGioKetThuc().compareTo(existing.getGioBatDau()) > 0) {
                        view.showMessage(String.format("Lỗi: Lớp %s đã có lịch thi khác (Mã LT %d, từ %s đến %s) cùng ngày và giờ!",
                                lt.getMaLop(), existing.getMaLT(), existing.getGioBatDau(), existing.getGioKetThuc()));
                        return;
                    }
                }
            }

            if (editMode[0]) {
                if(dao.updateLichThi(lt)) {
                    view.showMessage("Cập nhật thành công!");
                    loadAll();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Cập nhật thất bại!");
                }
            } else {
                if(dao.addLichThi(lt)) {
                    view.showMessage("Thêm lịch thi thành công!");
                    loadAll();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Thêm thất bại! (Kiểm tra xem Mã Môn/Mã Phòng có tồn tại chưa)");
                }
            }
        });
        view.addBtnXoaListener(e -> {
            LichThi lt = view.getLichThiInput();
            if(lt.getMaLT() == 0) {
                 view.showMessage("Vui lòng chọn dòng cần xóa!"); 
                 return;
            }
            int cf = JOptionPane.showConfirmDialog(
                view, "Bạn có chắc muốn xóa lịch thi này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION
            );
            
            if(cf == JOptionPane.YES_OPTION) {
                if(dao.deleteLichThi(lt.getMaLT())) {
                    view.showMessage("Xóa thành công!");
                    loadAll();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Xóa thất bại!");
                }
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
        view.addBtnXuatExcelListener(e -> {
            XuatExcel.xuatFileExcel(view.getTable(), view);
        });
    }
    private void loadAll() {
        List<LichThi> all = dao.getAllLichThi();
        view.setTableData(all);
        
        List<Integer> listMaLT = new ArrayList<>();
        for(LichThi lt : all) {
            listMaLT.add(lt.getMaLT());
        }
        view.setMaLTData(listMaLT);
    }
}

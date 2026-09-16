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

            List<String> namHocs = dao.getDistinctNamHoc();
            view.setNamHocData(namHocs);

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
            String namHoc = view.getLocNamHoc();
            if(kw.isEmpty() && namHoc.isEmpty()) { 
                loadAll();
                return; 
            }
            
            List<LichThi> list = dao.searchLichThi(kw, namHoc);
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
                // SỬA LỊCH THI -> CẢNH BÁO NGUY HIỂM 2 BƯỚC
                int row = view.getTable().getSelectedRow();
                String oldNgay = row >= 0 && view.getTable().getValueAt(row, 3) != null ? view.getTable().getValueAt(row, 3).toString() : "";
                String oldGioBD = row >= 0 && view.getTable().getValueAt(row, 4) != null ? view.getTable().getValueAt(row, 4).toString() : "";
                String oldGioKT = row >= 0 && view.getTable().getValueAt(row, 5) != null ? view.getTable().getValueAt(row, 5).toString() : "";
                String oldPhong = row >= 0 && view.getTable().getValueAt(row, 6) != null ? view.getTable().getValueAt(row, 6).toString() : "";
                String oldLop = row >= 0 && view.getTable().getValueAt(row, 7) != null ? view.getTable().getValueAt(row, 7).toString() : "";

                List<String[]> changes = new ArrayList<>();
                changes.add(new String[]{"Ngày thi", oldNgay, lt.getNgayThi()});
                changes.add(new String[]{"Giờ bắt đầu", oldGioBD, lt.getGioBatDau()});
                changes.add(new String[]{"Giờ kết thúc", oldGioKT, lt.getGioKetThuc()});
                changes.add(new String[]{"Phòng thi", oldPhong, lt.getMaPhong()});
                changes.add(new String[]{"Lớp thi", oldLop, lt.getMaLop()});

                List<String> impacts = java.util.Arrays.asList(
                    "Làm thay đổi thời gian và địa điểm thi của toàn bộ học sinh trong lớp.",
                    "Ảnh hưởng trực tiếp đến lịch phân công cán bộ, giáo viên coi thi (giám thị).",
                    "Có thể gây xung đột lịch thi hoặc lịch học các môn khác nếu không thông báo sớm.",
                    "Yêu cầu thông báo khẩn cấp tới giáo viên bộ môn và học sinh lớp này."
                );

                String entityInfo = String.format("Mã LT: %d | Kỳ thi: %s | Môn: %s | Lớp: %s",
                        lt.getMaLT(), lt.getTenKyThi(), lt.getMaMH(), lt.getMaLop());

                boolean pass = TienIch.DangerConfirmDialog.showUpdateConfirmation(
                        view, "CẢNH BÁO NGUY HIỂM: SỬA LỊCH THI", entityInfo, changes, impacts);

                if (!pass) {
                    return;
                }

                String err = dao.updateLichThiResult(lt);
                if (err == null) {
                    view.showMessage("Cập nhật lịch thi thành công!");
                    loadAll();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Cập nhật thất bại:\n" + err);
                }
            } else {
                String err = dao.addLichThiResult(lt);
                if (err == null) {
                    view.showMessage("Thêm lịch thi thành công!");
                    loadAll();
                    view.clearForm();
                    editMode[0] = false;
                    setIdleState.run();
                } else {
                    view.showMessage("Thêm thất bại:\n" + err);
                }
            }
        });
        view.addBtnXoaListener(e -> {
            LichThi lt = view.getLichThiInput();
            if (lt.getMaLT() == 0) {
                 view.showMessage("Vui lòng chọn dòng cần xóa!"); 
                 return;
            }

            int row = view.getTable().getSelectedRow();
            String kyThi = row >= 0 && view.getTable().getValueAt(row, 1) != null ? view.getTable().getValueAt(row, 1).toString() : "";
            String mon = row >= 0 && view.getTable().getValueAt(row, 2) != null ? view.getTable().getValueAt(row, 2).toString() : "";
            String ngay = row >= 0 && view.getTable().getValueAt(row, 3) != null ? view.getTable().getValueAt(row, 3).toString() : "";
            String lop = row >= 0 && view.getTable().getValueAt(row, 7) != null ? view.getTable().getValueAt(row, 7).toString() : "";

            String entityInfo = String.format("Mã LT: %d | Kỳ thi: %s | Môn: %s | Lớp: %s | Ngày: %s",
                    lt.getMaLT(), kyThi, mon, lop, ngay);

            List<String> impacts = java.util.Arrays.asList(
                "Ca thi của môn học này đối với lớp sẽ bị HỦY HOÀN TOÀN khỏi hệ thống.",
                "Phòng thi đã phân công sẽ bị trống, mất thông tin buổi thi của học sinh.",
                "Học sinh sẽ không có lịch thi môn này nếu không được xếp lịch bù kịp thời.",
                "Thao tác này KHÔNG THỂ KHÔI PHỤC tự động!"
            );

            boolean pass = TienIch.DangerConfirmDialog.showDeleteConfirmation(
                    view, "CẢNH BÁO NGUY HIỂM: XÓA LỊCH THI", entityInfo, impacts);

            if (!pass) {
                return;
            }

            String delErr = dao.deleteLichThiResult(lt.getMaLT());
            if (delErr == null) {
                view.showMessage("Xóa lịch thi thành công!");
                loadAll();
                view.clearForm();
                editMode[0] = false;
                setIdleState.run();
            } else {
                view.showMessage("Xóa thất bại:\n" + delErr);
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

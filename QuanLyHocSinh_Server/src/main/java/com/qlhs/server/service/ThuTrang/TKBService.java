package com.qlhs.server.service.ThuTrang;

import com.qlhs.server.entity.PhongHoc;
import com.qlhs.server.entity.TKB;
import com.qlhs.server.repository.Dat.GiaoVienRepository;
import com.qlhs.server.repository.ThuTrang.MonHocRepository;
import com.qlhs.server.repository.ThuTrang.PhongHocRepository;
import com.qlhs.server.repository.ThuTrang.TKBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TKBService {
    @Autowired
    private TKBRepository tkbRepository;

    @Autowired
    private MonHocRepository monHocRepository;

    @Autowired
    private GiaoVienRepository giaoVienRepository;

    @Autowired
    private PhongHocRepository phongHocRepository;

    private void fillTenMH(List<TKB> list) {
        list.forEach(t -> monHocRepository.findById(t.getMaMH())
                .ifPresent(m -> t.setTenMH(m.getTenMH())));
    }

    private void sortTKB(List<TKB> list) {
        if (list == null || list.isEmpty()) return;
        java.text.Collator viCollator = java.text.Collator.getInstance(new java.util.Locale("vi", "VN"));
        list.sort((t1, t2) -> {
            String n1 = t1.getNamHoc() == null ? "" : t1.getNamHoc().trim();
            String n2 = t2.getNamHoc() == null ? "" : t2.getNamHoc().trim();
            int cmpNam = n2.compareToIgnoreCase(n1);
            if (cmpNam != 0) return cmpNam;

            int hk1 = t1.getHocKy() == null ? 0 : t1.getHocKy();
            int hk2 = t2.getHocKy() == null ? 0 : t2.getHocKy();
            int cmpHK = Integer.compare(hk2, hk1);
            if (cmpHK != 0) return cmpHK;

            String l1 = t1.getMaLop() == null ? "" : t1.getMaLop().trim();
            String l2 = t2.getMaLop() == null ? "" : t2.getMaLop().trim();
            int cmpLop = l1.compareToIgnoreCase(l2);
            if (cmpLop != 0) return cmpLop;

            String tm1 = t1.getTenMH() == null ? "" : t1.getTenMH().trim();
            String tm2 = t2.getTenMH() == null ? "" : t2.getTenMH().trim();
            int cmpMon = viCollator.compare(tm1, tm2);
            if (cmpMon != 0) return cmpMon;

            int thu1 = t1.getThu() == null ? 0 : t1.getThu();
            int thu2 = t2.getThu() == null ? 0 : t2.getThu();
            int cmpThu = Integer.compare(thu1, thu2);
            if (cmpThu != 0) return cmpThu;

            int tiet1 = t1.getTietBatDau() == null ? 0 : t1.getTietBatDau();
            int tiet2 = t2.getTietBatDau() == null ? 0 : t2.getTietBatDau();
            return Integer.compare(tiet1, tiet2);
        });
    }

    public List<TKB> getAllTKB() {
        List<TKB> list = tkbRepository.findAll();
        fillTenMH(list);
        sortTKB(list);
        return list;
    }
    public List<TKB> getByMaLop(String maLop) {
        List<TKB> list = tkbRepository.findByMaLop(maLop);
        fillTenMH(list);
        sortTKB(list);
        return list;
    }

    public List<TKB> getByMaMH(String maMH) {
        List<TKB> list = tkbRepository.findByMaMH(maMH);
        fillTenMH(list);
        sortTKB(list);
        return list;
    }

    public List<TKB> getByMaPhong(String maPhong) {
        List<TKB> list = tkbRepository.findByMaPhong(maPhong);
        fillTenMH(list);
        sortTKB(list);
        return list;
    }

    public List<TKB> getByMaGV(String maGV) {
        List<TKB> list = tkbRepository.findByMaGV(maGV);
        fillTenMH(list);
        sortTKB(list);
        return list;
    }

    public List<TKB> filter(String maLop, String maMH, Integer thu, String namHoc, Integer hocKy) {
        List<TKB> list = tkbRepository.filterTKB(maLop, maMH, thu, namHoc, hocKy);
        fillTenMH(list);
        sortTKB(list);
        return list;
    }

    public List<String> getDistinctMaLop() {
        return tkbRepository.getDistinctMaLop();
    }

    public List<String> getDistinctNamHoc() {
        return tkbRepository.getDistinctNamHoc();
    }

    public Optional<TKB> getByIdTKB(Integer maTKB) {
        return tkbRepository.findById(maTKB);
    }

    public TKB save(TKB tkb) {
        return tkbRepository.save(tkb);
    }

    public void delete(Integer maTKB) {
        tkbRepository.deleteById(maTKB);
    }

    public boolean existsByIdTKB(Integer maTKB) {
        return tkbRepository.existsById(maTKB);
    }

    public String checkTrungLich(TKB tkb) {
        if (tkb == null) return null;

        // Kiểm tra phòng có đang trong tình trạng bảo trì không
        if (tkb.getMaPhong() != null) {
            Optional<PhongHoc> phongOpt = phongHocRepository.findById(tkb.getMaPhong());
            if (phongOpt.isPresent() && "Bảo trì".equalsIgnoreCase(phongOpt.get().getTinhTrang())) {
                return "Phòng học " + phongOpt.get().getTenPhong() + " đang bảo trì, không thể xếp thời khóa biểu!";
            }
        }

        List<TKB> all = tkbRepository.findAll();
        for (TKB t : all) {
            // Bỏ qua chính bản ghi đang cập nhật
            if (tkb.getMaTKB() != null && tkb.getMaTKB().equals(t.getMaTKB())) {
                continue;
            }

            // Kiểm tra năm học: cùng năm học
            if (tkb.getNamHoc() != null && t.getNamHoc() != null
                    && !tkb.getNamHoc().trim().equalsIgnoreCase(t.getNamHoc().trim())) {
                continue;
            }

            // Kiểm tra học kỳ: cùng học kỳ
            if (tkb.getHocKy() != null && t.getHocKy() != null
                    && !tkb.getHocKy().equals(t.getHocKy())) {
                continue;
            }

            // Kiểm tra thứ: cùng thứ
            if (tkb.getThu() != null && t.getThu() != null
                    && !tkb.getThu().equals(t.getThu())) {
                continue;
            }

            // Kiểm tra giao thoa tiết học:
            // [tkb.BD, tkb.KT] và [t.BD, t.KT] trùng nhau khi:
            // tkb.tietBatDau <= t.tietKetThuc && tkb.tietKetThuc >= t.tietBatDau
            if (tkb.getTietBatDau() != null && tkb.getTietKetThuc() != null
                    && t.getTietBatDau() != null && t.getTietKetThuc() != null) {
                boolean overlap = (tkb.getTietBatDau() <= t.getTietKetThuc()
                        && tkb.getTietKetThuc() >= t.getTietBatDau());
                if (!overlap) {
                    continue;
                }
            } else {
                continue;
            }

            // 1. Kiểm tra trùng lịch lớp học
            if (tkb.getMaLop() != null && t.getMaLop() != null
                    && tkb.getMaLop().trim().equalsIgnoreCase(t.getMaLop().trim())) {
                return "Trùng lịch học: Lớp " + t.getMaLop() + " đã có lịch học vào Thứ " + t.getThu()
                        + " (Tiết " + t.getTietBatDau() + " - " + t.getTietKetThuc() + ")!";
            }

            // 2. Kiểm tra trùng lịch giáo viên giảng dạy
            if (tkb.getMaGV() != null && t.getMaGV() != null
                    && tkb.getMaGV().trim().equalsIgnoreCase(t.getMaGV().trim())) {
                String tenGV = giaoVienRepository.findById(t.getMaGV())
                        .map(g -> g.getHoTen()).orElse(t.getMaGV());
                return "Trùng lịch dạy: Giáo viên " + tenGV + " đã có lịch dạy lớp " + t.getMaLop()
                        + " vào Thứ " + t.getThu() + " (Tiết " + t.getTietBatDau() + " - " + t.getTietKetThuc() + ")!";
            }

            // 3. Kiểm tra trùng phòng học
            if (tkb.getMaPhong() != null && t.getMaPhong() != null
                    && tkb.getMaPhong().trim().equalsIgnoreCase(t.getMaPhong().trim())) {
                String tenPhong = phongHocRepository.findById(t.getMaPhong())
                        .map(p -> p.getTenPhong()).orElse(t.getMaPhong());
                return "Trùng phòng học: Phòng " + tenPhong + " đã xếp cho lớp " + t.getMaLop()
                        + " vào Thứ " + t.getThu() + " (Tiết " + t.getTietBatDau() + " - " + t.getTietKetThuc() + ")!";
            }
        }
        return null;
    }

    public boolean isTrungTiet(TKB tkb) {
        return checkTrungLich(tkb) != null;
    }
}
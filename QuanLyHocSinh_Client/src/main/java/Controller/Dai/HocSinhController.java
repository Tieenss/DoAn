package Controller.Dai;

import Api.HaTrang.HocPhiApiClient;
import Api.ThuTrang.TKBApiClient;
import Api.Tien.DiemApi;
import Api.Tien.HanhKiemApi;
import Api.Tien.LichThiApi;
import Api.Đai.DoiTuongUuTienApi;
import Api.Đai.HocSinhApi;
import Api.Đat.GiaoVienApi;
import Api.Đat.LopApi;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class HocSinhController {

    private HocSinhApi api = new HocSinhApi();
    private LopApi lopApi = new LopApi();
    private GiaoVienApi giaoVienApi = new GiaoVienApi();
    private TKBApiClient tkbApi = new TKBApiClient();
    private DiemApi diemApi = new DiemApi();
    private HanhKiemApi hanhKiemApi = new HanhKiemApi();
    private HocPhiApiClient hocPhiApi = new HocPhiApiClient();
    private LichThiApi lichThiApi = new LichThiApi();
    private DoiTuongUuTienApi doiTuongApi = new DoiTuongUuTienApi();


    private String cacheMaHS;
    private List<Diem> cacheDiem = new ArrayList<>();
    private List<HanhKiem> cacheHanhKiem = new ArrayList<>();
    private List<Hocphi> cacheHocPhi = new ArrayList<>();

    private Map<String, String> cacheTenDT = new HashMap<>();
    private boolean daTaiDanhSachDT = false;

    private void taiDuLieu(String maHS, boolean lamMoi) {
        if (!lamMoi && maHS != null && maHS.equals(cacheMaHS)) {
            return;
        }
        cacheMaHS = maHS;
        List<Diem> d = diemApi.getDiemByMaHS(maHS);
        List<HanhKiem> h = hanhKiemApi.getHanhKiemByMaHSWithPermission(maHS);
        List<Hocphi> p = hocPhiApi.getByMaHS(maHS);
        cacheDiem = d != null ? d : new ArrayList<>();
        cacheHanhKiem = h != null ? h : new ArrayList<>();
        cacheHocPhi = p != null ? p : new ArrayList<>();
    }

    private void themNamHoc(Set<String> tap, String raw) {
        String nh = chuanHoaNamHoc(raw);
        if (laNamHocHopLe(nh)) {
            tap.add(nh);
        }
    }

    private List<String> layDanhSachNamHocTuCache() {
        Set<String> tap = new TreeSet<>(Comparator.reverseOrder());
        for (Diem d : cacheDiem) if (d != null) themNamHoc(tap, d.getNamHoc());
        for (HanhKiem h : cacheHanhKiem) if (h != null) themNamHoc(tap, h.getNamHoc());
        for (Hocphi p : cacheHocPhi) if (p != null) themNamHoc(tap, p.getNamHoc());
        return new ArrayList<>(tap);
    }

    public List<String> getDanhSachNamHoc(String maHS) {
        if (maHS == null || maHS.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            taiDuLieu(maHS, true);   // luôn lấy mới khi mở hồ sơ
            return layDanhSachNamHocTuCache();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void loadTable(DefaultTableModel model) {
        model.setRowCount(0);

        List<HocSinh> list;

        if (Auth.isHocSinh()) {
            HocSinh hs = api.getHocSinh(Auth.maNguoiDung);
            list = new ArrayList<>();
            if (hs != null) {
                list.add(hs);
            }
        } else if (Auth.isGiaoVien()) {
            list = locTheoLopChuNhiem(api.getAllHocSinh());
        } else {
            list = api.getAllHocSinh();
        }

        if (list == null) {
            return;
        }

        for (HocSinh hs : list) {
            model.addRow(new Object[]{
                    hs.getMaHS(),
                    hs.getHoTen(),
                    formatToDDMMYYYY(hs.getNgaySinh()),
                    hs.getGioiTinh(),
                    hs.getDiaChi(),
                    hs.getMaLop(),
                    hs.getMaDT(),
                    hs.getNienKhoa()
            });
        }

    }

    private String formatToDDMMYYYY(String yyyyMMdd) {
        if (yyyyMMdd == null || yyyyMMdd.isEmpty()) return "";
        try {
            java.util.Date d = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(yyyyMMdd);
            return new java.text.SimpleDateFormat("dd/MM/yyyy").format(d);
        } catch (Exception e) {
            return yyyyMMdd;
        }
    }

    public boolean them(HocSinh hs) {
        return api.insertHocSinh(hs);
    }

    public boolean sua(HocSinh hs) {
        return api.updateHocSinh(hs);
    }

    public boolean xoa(String maHS) {
        return api.deleteHocSinh(maHS);
    }

    public void loadComboMaLop(JComboBox<String> cbo) {
        cbo.removeAllItems();

        if (Auth.isGiaoVien()) {
            for (String ma : getDanhSachMaLopChuNhiem()) {
                cbo.addItem(ma);
            }
            return;
        }

        List<String> list = api.getAllMaLop();
        if (list == null) return;

        for (String ma : list) {
            cbo.addItem(ma);
        }
    }

    public void loadComboMaDT(JComboBox<String> cbo) {
        cbo.removeAllItems();
        List<String> list = api.getAllMaDT();
        if (list == null) return;
        for (String ma : list) {
            cbo.addItem(ma);
        }
    }

    public boolean timKiem(String keyword, String nienKhoa, DefaultTableModel model) {
        model.setRowCount(0);
        if (nienKhoa.equals("Tất cả")) {
            nienKhoa = "";
        }

        List<HocSinh> list = api.search(keyword, nienKhoa);

        if (list == null) {
            throw new RuntimeException("Không thể kết nối tới Server.");
        }

        if (Auth.isGiaoVien()) {
            list = locTheoLopChuNhiem(list);
        }

        if (list.isEmpty()) {
            return false;
        }

        for (HocSinh hs : list) {
            model.addRow(new Object[]{
                hs.getMaHS(),
                hs.getHoTen(),
                formatToDDMMYYYY(hs.getNgaySinh()),
                hs.getGioiTinh(),
                hs.getDiaChi(),
                hs.getMaLop(),
                hs.getMaDT(),
                hs.getNienKhoa()
            });
        }
        return true;
    }

    public void loadComboLocNienKhoa(JComboBox<String> cbo) {
        cbo.removeAllItems();
        cbo.addItem("Tất cả");

        List<String> list = api.getDistinctNienKhoa();

        if (list == null) return;

        for (String nk : list) {
            cbo.addItem(nk);
        }
    }

    public HocSinh getThongTinCaNhan() {
        return api.getHocSinh(Auth.maNguoiDung);
    }

    public Set<String> getDanhSachMaLopChuNhiem() {
        Set<String> dsMaLop = new LinkedHashSet<>();
        if (Auth.maNguoiDung == null) {
            return dsMaLop;
        }
        try {
            List<LopGVCN> listLop = lopApi.getAllLop();
            if (listLop != null) {
                for (LopGVCN lop : listLop) {
                    if (lop != null && lop.getMaLop() != null
                            && Auth.maNguoiDung.equalsIgnoreCase(lop.getMaGVCN())) {
                        dsMaLop.add(lop.getMaLop().trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsMaLop;
    }

    private List<HocSinh> locTheoLopChuNhiem(List<HocSinh> all) {
        List<HocSinh> kq = new ArrayList<>();
        if (all == null) {
            return kq;
        }
        Set<String> maLopChuNhiem = getDanhSachMaLopChuNhiem();
        for (HocSinh hs : all) {
            if (hs != null && hs.getMaLop() != null
                    && maLopChuNhiem.contains(hs.getMaLop().trim())) {
                kq.add(hs);
            }
        }
        return kq;
    }

    public Lop getLopCuaHocSinh(String maLop) {
        if (maLop == null || maLop.trim().isEmpty()) {
            return null;
        }
        return lopApi.getById(maLop);
    }

    public Giaovien getGiaoVienChuNhiem(String maGVCN) {
        if (maGVCN == null || maGVCN.trim().isEmpty()) {
            return null;
        }
        try {
            return giaoVienApi.getByMaGV(maGVCN);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Đếm số học sinh trong lớp.
     * Trả về -1 nếu không xác định được.
     */
    public int getSiSoLop(String maLop) {
        if (maLop == null || maLop.trim().isEmpty()) {
            return -1;
        }
        try {
            int count = 0;
            for (HocSinh h : api.getAllHocSinh()) {
                if (maLop.equals(h.getMaLop())) {
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Lấy danh sách phòng học mà lớp đang sử dụng trong TKB.
     * Một lớp có thể học ở nhiều phòng.
     * Ví dụ:
     * P.201, P.305
     */
    public String getPhongHocLop(String maLop) {
        if (maLop == null || maLop.trim().isEmpty()) {
            return "-";
        }
        try {
            Set<String> maPhongs = new LinkedHashSet<>();
            for (TKB t : tkbApi.getByFilter(maLop, "", 0)) {
                if (t.getMaPhong() != null && !t.getMaPhong().trim().isEmpty()) {
                    maPhongs.add(t.getMaPhong());
                }
            }
            if (maPhongs.isEmpty()) {
                return "-";
            }
            Map<String, String> tenPhong = new HashMap<>();

            for (Map<String, String> m : tkbApi.getDanhSachPhong()) {
                tenPhong.put(m.get("ma"), m.get("ten"));
            }
            StringBuilder sb = new StringBuilder();
            for (String maPhong : maPhongs) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(tenPhong.getOrDefault(maPhong, maPhong));
            }
            return sb.toString();
        } catch (Exception e) {
            return "-";
        }
    }

    private String giaTriRong(String value) {
        return value == null || value.trim().isEmpty() ? "-" : value.trim();
    }

    private static boolean laNamHocHopLe(String namHoc) {
        if (namHoc == null) {
            return false;
        }
        String[] p = namHoc.trim().split("-");
        if (p.length != 2) {
            return false;
        }
        try {
            int namDau = Integer.parseInt(p[0].trim());
            int namCuoi = Integer.parseInt(p[1].trim());
            return namCuoi == namDau + 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String chuanHoaNamHoc(String v) {
        return v == null ? "" : v.trim().replaceAll("\\s*-\\s*", "-");
    }

    public ThongKeHocSinh getThongKeHocSinh(String maHS) {
        return getThongKeHocSinh(maHS, null);
    }

    public ThongKeHocSinh getThongKeHocSinh(String maHS, String namHocChon) {
        ThongKeHocSinh result = new ThongKeHocSinh();
        result.setNamHoc("");
        result.setHocKy(0);
        result.setGpa("-");
        result.setGpaMoTa("Chưa có điểm");
        result.setXepLoai("-");
        result.setXepLoaiMoTa("Chưa có dữ liệu");
        result.setSoMon("-");
        result.setSoMonMoTa("Chưa có điểm");
        result.setHocPhi("-");
        result.setHocPhiMoTa("Chưa có dữ liệu");

        if (maHS == null || maHS.trim().isEmpty()) {
            return result;
        }
        try {
            taiDuLieu(maHS, true);
            // 1) Năm học cần xem: theo lựa chọn, không có thì lấy mới nhất
            String namHocXem = chuanHoaNamHoc(namHocChon);
            if (!laNamHocHopLe(namHocXem)) {
                List<String> ds = layDanhSachNamHocTuCache();
                namHocXem = ds.isEmpty() ? "" : ds.get(0);
            }
            if (namHocXem.isEmpty()) {
                return result;
            }

            // 2) Điểm TB cả năm + Số môn học trong năm học được chọn
            // Công thức: TB cả năm = (HK1 + 2 * HK2) / 3
            Map<String, Map<Integer, Double>> diemTheoMon = new HashMap<>();
            for (Diem diem : cacheDiem) {
                if (diem == null
                        || !namHocXem.equals(chuanHoaNamHoc(diem.getNamHoc()))
                        || diem.getDiemTongKet() == null) {
                    continue;
                }
                String maMH = diem.getMaMH() != null && !diem.getMaMH().trim().isEmpty()
                        ? diem.getMaMH().trim()
                        : (diem.getTenMH() != null ? diem.getTenMH().trim() : "");

                if (maMH.isEmpty()) {
                    continue;
                }

                int hocKy = diem.getHocKy();
                if (hocKy == 1 || hocKy == 2) {
                    diemTheoMon
                            .computeIfAbsent(maMH, k -> new HashMap<>())
                            .put(hocKy, diem.getDiemTongKet());
                }
            }

            if (!diemTheoMon.isEmpty()) {
                double tongTBM = 0;
                int soMon = 0;

                for (Map<Integer, Double> diemHocKy : diemTheoMon.values()) {
                    Double hk1 = diemHocKy.get(1);
                    Double hk2 = diemHocKy.get(2);
                    double tbMon;
                    if (hk1 != null && hk2 != null) {
                        // Có đủ HK1 + HK2
                        tbMon = (hk1 + 2 * hk2) / 3.0;
                    } else if (hk2 != null) {
                        // Chỉ có HK2
                        tbMon = hk2;
                    } else if (hk1 != null) {
                        // Chỉ có HK1
                        tbMon = hk1;
                    } else {
                        continue;
                    }
                    tongTBM += tbMon;
                    soMon++;
                }

                if (soMon > 0) {
                    // Điểm TB cả năm = trung bình TB cả năm của các môn
                    double gpaNam = tongTBM / soMon;
                    result.setGpa(String.format("%.2f", gpaNam));
                    result.setGpaMoTa("Năm học " + namHocXem);

                    result.setSoMon(String.valueOf(soMon));
                    result.setSoMonMoTa("Năm học " + namHocXem);
                }
            }

            // 3) Hạnh kiểm cả năm
            HanhKiem hkNam = null;
            for (HanhKiem hk : cacheHanhKiem) {
                if (hk == null || !namHocXem.equals(chuanHoaNamHoc(hk.getNamHoc()))) continue;
                // Ưu tiên lấy xếp loại của kỳ mới nhất (HK2)
                if (hkNam == null || hk.getHocKy() > hkNam.getHocKy()) {
                    hkNam = hk;
                }
            }
            if (hkNam != null) {
                result.setXepLoai(giaTriRong(hkNam.getXepLoai()));
                result.setXepLoaiMoTa("Năm học " + namHocXem);
            }

            // 4) Học phí cả năm
            List<Hocphi> dsHocPhiNam = new ArrayList<>();

            for (Hocphi hp : cacheHocPhi) {
                if (hp != null
                        && namHocXem.equals(chuanHoaNamHoc(hp.getNamHoc()))) {
                    dsHocPhiNam.add(hp);
                }
            }

            if (!dsHocPhiNam.isEmpty()) {
                boolean tatCaDaNop = true;

                for (Hocphi hp : dsHocPhiNam) {
                    String trangThai = hp.getTrangThai() != null
                            ? hp.getTrangThai().trim()
                            : "";

                    if (!trangThai.equalsIgnoreCase("Đã nộp")
                            && !trangThai.equalsIgnoreCase("Đã hoàn thành")) {
                        tatCaDaNop = false;
                        break;
                    }
                }

                result.setHocPhi(tatCaDaNop ? "Đã nộp" : "Chưa nộp");
                result.setHocPhiMoTa("Năm học " + namHocXem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getNamHocMacDinh(String maHS) {
        if (maHS == null || maHS.trim().isEmpty()) return null;
        try {
            taiDuLieu(maHS, false);
            String namTot = "";
            for (Diem d : cacheDiem) {
                if (d == null || d.getDiemTongKet() == null) continue;
                String nh = chuanHoaNamHoc(d.getNamHoc());
                if (laNamHocHopLe(nh) && nh.compareTo(namTot) > 0) {
                    namTot = nh;
                }
            }
            if (!namTot.isEmpty()) return namTot;
            List<String> ds = layDanhSachNamHocTuCache();
            return ds.isEmpty() ? null : ds.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    private void taiDanhSachDoiTuong(boolean lamMoi) {
        if (!lamMoi && daTaiDanhSachDT) {
            return;
        }
        cacheTenDT.clear();
        daTaiDanhSachDT = true;   // đặt trước để mất mạng cũng không retry mãi
        try {
            List<DoiTuongUuTien> ds = doiTuongApi.getAll();
            if (ds == null) {
                return;
            }
            for (DoiTuongUuTien dt : ds) {
                if (dt == null || dt.getMaDT() == null || dt.getMaDT().trim().isEmpty()
                        || dt.getTenDT() == null || dt.getTenDT().trim().isEmpty()) {
                    continue;
                }
                cacheTenDT.put(dt.getMaDT().trim(), dt.getTenDT().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTenDoiTuongUuTien(String maDT) {
        if (maDT == null || maDT.trim().isEmpty()) {
            return null;
        }
        taiDanhSachDoiTuong(false);
        return cacheTenDT.get(maDT.trim());
    }

    public String hienThiDoiTuongUuTien(String maDT) {
        if (maDT == null || maDT.trim().isEmpty()) {
            return "";   // panel sẽ đổi thành "-"
        }
        String ten = getTenDoiTuongUuTien(maDT);
        if (ten == null) {
            return maDT.trim();   // không tra được thì hiện lại mã, không để ô trống
        }
        return ten + " (" + maDT.trim() + ")";
    }

    public HocSinh getThongTinHocSinhByMa(String maHS) {
        if (maHS == null || maHS.trim().isEmpty()) {
            return null;
        }
        return api.getHocSinh(maHS.trim());
    }

    public List<LichThi> getLichThiCuaLop(String maLop) {
        if (maLop == null || maLop.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            List<LichThi> dsLichThi = lichThiApi.getLichThiByFilter("", "", "", maLop);

            if (dsLichThi == null) {
                return new ArrayList<>();
            }
            dsLichThi.sort((a, b) -> {
                String x = a.getNgayThi() == null ? "" : a.getNgayThi();
                String y = b.getNgayThi() == null ? "" : b.getNgayThi();

                return x.compareTo(y);
            });
            return dsLichThi;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
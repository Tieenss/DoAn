package Model;

import com.google.gson.annotations.SerializedName;

public class Diem {
    @SerializedName(value = "maHS", alternate = {"MaHS", "MAHS", "mahs"})
    private String maHS;
    @SerializedName(value = "tenHS", alternate = {"TenHS", "TENHS", "tenhs"})
    private String tenHS;
    @SerializedName(value = "maLop", alternate = {"MaLop", "MALOP", "malop"})
    private String maLop;
    @SerializedName(value = "maMH", alternate = {"MaMH", "MAMH", "mamh"})
    private String maMH;
    @SerializedName(value = "tenMH", alternate = {"TenMH", "TENMH", "tenmh"})
    private String tenMH;
    @SerializedName(value = "hocKy", alternate = {"HocKy", "HOCKY", "hocky"})
    private int hocKy;
    @SerializedName(value = "namHoc", alternate = {"NamHoc", "NAMHOC", "namhoc"})
    private String namHoc;

    @SerializedName(value = "diem15p", alternate = {"Diem15p", "DIEM15P", "diem15P"})
    private Double diem15p;
    @SerializedName(value = "diem1Tiet", alternate = {"Diem1Tiet", "DIEM1TIET", "diem1tiet"})
    private Double diem1Tiet;
    @SerializedName(value = "diemGiuaKy", alternate = {"DiemGiuaKy", "DIEMGIUAKY", "diemgiuaky"})
    private Double diemGiuaKy;
    @SerializedName(value = "diemCuoiKy", alternate = {"DiemCuoiKy", "DIEMCUOIKY", "diemcuoiky"})
    private Double diemCuoiKy;
    @SerializedName(value = "diemTongKet", alternate = {"DiemTongKet", "DIEMTONGKET", "diemtongket"})
    private Double diemTongKet;

    public Diem() {}

    public Diem(String maHS, String maMH, int hocKy, Double diem15p, Double diem1Tiet, Double diemGiuaKy, Double diemCuoiKy) {
        this.maHS = maHS;
        this.maMH = maMH;
        this.hocKy = hocKy;
        this.diem15p = diem15p;
        this.diem1Tiet = diem1Tiet;
        this.diemGiuaKy = diemGiuaKy;
        this.diemCuoiKy = diemCuoiKy;
    }

    public String getMaHS() { return maHS; }
    public void setMaHS(String maHS) { this.maHS = maHS; }

    public String getTenHS() { return tenHS; }
    public void setTenHS(String tenHS) { this.tenHS = tenHS; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    public String getNamHoc() { return namHoc; }
    public void setNamHoc(String namHoc) { this.namHoc = namHoc; }

    public Double getDiem15p() { return diem15p; }
    public void setDiem15p(Double diem15p) { this.diem15p = diem15p; }

    public Double getDiem1Tiet() { return diem1Tiet; }
    public void setDiem1Tiet(Double diem1Tiet) { this.diem1Tiet = diem1Tiet; }

    public Double getDiemGiuaKy() { return diemGiuaKy; }
    public void setDiemGiuaKy(Double diemGiuaKy) { this.diemGiuaKy = diemGiuaKy; }

    public Double getDiemCuoiKy() { return diemCuoiKy; }
    public void setDiemCuoiKy(Double diemCuoiKy) { this.diemCuoiKy = diemCuoiKy; }

    public Double getDiemTongKet() {
        if (diemTongKet != null) return diemTongKet;
        if (diem15p == null && diem1Tiet == null && diemGiuaKy == null && diemCuoiKy == null) {
            return null;
        }
        double d15 = diem15p != null ? diem15p : 0.0;
        double d1t = diem1Tiet != null ? diem1Tiet : 0.0;
        double dgk = diemGiuaKy != null ? diemGiuaKy : 0.0;
        double dck = diemCuoiKy != null ? diemCuoiKy : 0.0;
        return (d15 + d1t * 2 + dgk * 2 + dck * 3) / 8.0;
    }

    public void setDiemTongKet(Double diemTongKet) {
        this.diemTongKet = diemTongKet;
    }

    public boolean isChuaNhapDiem() {
        return diem15p == null && diem1Tiet == null && diemGiuaKy == null && diemCuoiKy == null;
    }
}
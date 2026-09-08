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

    @SerializedName(value = "diem15p", alternate = {"Diem15p", "DIEM15P", "diem15P"})
    private double diem15p;
    @SerializedName(value = "diem1Tiet", alternate = {"Diem1Tiet", "DIEM1TIET", "diem1tiet"})
    private double diem1Tiet;
    @SerializedName(value = "diemGiuaKy", alternate = {"DiemGiuaKy", "DIEMGIUAKY", "diemgiuaky"})
    private double diemGiuaKy;
    @SerializedName(value = "diemCuoiKy", alternate = {"DiemCuoiKy", "DIEMCUOIKY", "diemcuoiky"})
    private double diemCuoiKy;

    public Diem() {}

    public Diem(String maHS, String maMH, int hocKy, double diem15p, double diem1Tiet, double diemGiuaKy, double diemCuoiKy) {
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

    public double getDiem15p() { return diem15p; }
    public void setDiem15p(double diem15p) { this.diem15p = diem15p; }

    public double getDiem1Tiet() { return diem1Tiet; }
    public void setDiem1Tiet(double diem1Tiet) { this.diem1Tiet = diem1Tiet; }

    public double getDiemGiuaKy() { return diemGiuaKy; }
    public void setDiemGiuaKy(double diemGiuaKy) { this.diemGiuaKy = diemGiuaKy; }

    public double getDiemCuoiKy() { return diemCuoiKy; }
    public void setDiemCuoiKy(double diemCuoiKy) { this.diemCuoiKy = diemCuoiKy; }

    public double getDiemTongKet() {
        return (diem15p + diem1Tiet + diemGiuaKy * 2 + diemCuoiKy * 3) / 7.0;
    }
}
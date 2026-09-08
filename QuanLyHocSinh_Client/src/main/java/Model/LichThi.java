package Model;

import com.google.gson.annotations.SerializedName;

public class LichThi {
    @SerializedName(value = "maLT", alternate = {"MaLT", "MALT", "malt"})
    private int maLT;
    
    @SerializedName(value = "tenKyThi", alternate = {"TenKyThi", "TENKYTHI", "tenkythi"})
    private String tenKyThi;
    
    @SerializedName(value = "maMH", alternate = {"MaMH", "MAMH", "mamh"})
    private String maMH;
    
    @SerializedName(value = "tenMH", alternate = {"TenMH", "TENMH", "tenmh"})
    private String tenMH;
    
    @SerializedName(value = "ngayThi", alternate = {"NgayThi", "NGAYTHI", "ngaythi"})
    private String ngayThi;     
    
    @SerializedName(value = "gioBatDau", alternate = {"GioBatDau", "GIOBATDAU", "giobatdau"})
    private String gioBatDau;  
    
    @SerializedName(value = "gioKetThuc", alternate = {"GioKetThuc", "GIOKETTHUC", "gioketthuc"})
    private String gioKetThuc;  
    
    @SerializedName(value = "maPhong", alternate = {"MaPhong", "MAPHONG", "maphong"})
    private String maPhong;

    @SerializedName(value = "maLop", alternate = {"MaLop", "MALOP", "malop"})
    private String maLop;

    @SerializedName(value = "tenLop", alternate = {"TenLop", "TENLOP", "tenlop"})
    private String tenLop;

    public LichThi() {}

    public LichThi(int maLT, String tenKyThi, String maMH, String ngayThi, String gioBatDau, String gioKetThuc, String maPhong) {
        this.maLT = maLT;
        this.tenKyThi = tenKyThi;
        this.maMH = maMH;
        this.ngayThi = ngayThi;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.maPhong = maPhong;
    }

    public int getMaLT() { return maLT; }
    public void setMaLT(int maLT) { this.maLT = maLT; }

    public String getTenKyThi() { return tenKyThi; }
    public void setTenKyThi(String tenKyThi) { this.tenKyThi = tenKyThi; }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public String getNgayThi() { return ngayThi; }
    public void setNgayThi(String ngayThi) { this.ngayThi = ngayThi; }

    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }

    public String getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(String gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
}
package Model;

import com.google.gson.annotations.SerializedName;

public class HanhKiem {
    @SerializedName(value = "maHS", alternate = {"MaHS", "MAHS", "mahs"})
    private String maHS;
    @SerializedName(value = "tenHS", alternate = {"TenHS", "TENHS", "tenhs"})
    private String tenHS;
    @SerializedName(value = "maLop", alternate = {"MaLop", "MALOP", "malop", "lopHocSinh", "LopHocSinh"})
    private String maLop;
    @SerializedName(value = "hocKy", alternate = {"HocKy", "HOCKY", "hocky"})
    private int hocKy;
    @SerializedName(value = "namHoc", alternate = {"NamHoc", "NAMHOC", "namhoc"})
    private String namHoc;
    @SerializedName(value = "xepLoai", alternate = {"XepLoai", "XEPLOAI", "xeploai"})
    private String xepLoai;
    @SerializedName(value = "nhanXet", alternate = {"NhanXet", "NHANXET", "nhanxet"})
    private String nhanXet;

    public HanhKiem() {}

    public HanhKiem(String maHS, String tenHS, int hocKy, String namHoc, String xepLoai, String nhanXet) {
        this.maHS = maHS;
        this.tenHS = tenHS;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
        this.xepLoai = xepLoai;
        this.nhanXet = nhanXet;
    }

    public String getMaHS() { return maHS; }
    public void setMaHS(String maHS) { this.maHS = maHS; }

    public String getTenHS() { return tenHS; }
    public void setTenHS(String tenHS) { this.tenHS = tenHS; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    public String getNamHoc() { return namHoc; }
    public void setNamHoc(String namHoc) { this.namHoc = namHoc; }

    public String getXepLoai() { return xepLoai; }
    public void setXepLoai(String xepLoai) { this.xepLoai = xepLoai; }

    public String getNhanXet() { return nhanXet; }
    public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }
}

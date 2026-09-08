package Model;

import com.google.gson.annotations.SerializedName;

public class Lop {
    @SerializedName(value = "maLop", alternate = {"MaLop", "MALOP", "malop"})
    private String maLop;
    @SerializedName(value = "tenLop", alternate = {"TenLop", "TENLOP", "tenlop"})
    private String tenLop;
    @SerializedName(value = "nienKhoa", alternate = {"NienKhoa", "NIENKHOA", "nienkhoa"})
    private String nienKhoa;
    @SerializedName(value = "maGVCN", alternate = {"MaGVCN", "MAGVCN", "magvcn"})
    private String maGVCN;

    public Lop() { }

    public Lop(String maLop, String tenLop, String nienKhoa, String maGVCN) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.nienKhoa = nienKhoa;
        this.maGVCN = maGVCN;
    }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getNienKhoa() { return nienKhoa; }
    public void setNienKhoa(String nienKhoa) { this.nienKhoa = nienKhoa; }

    public String getMaGVCN() { return maGVCN; }
    public void setMaGVCN(String maGVCN) { this.maGVCN = maGVCN; }
}
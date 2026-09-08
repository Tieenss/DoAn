
package Model;

public class Hocphi {
    private int maHP;
    private String maHS;
    private String tenHocSinh;
    private String maLop;
    private int hocKy;
    private String namHoc;
    private long tongTien;
    private long mienGiam;
    private long phaiDong;
    private String trangThai;

    public Hocphi() {
    }

    public Hocphi(int maHP, String maHS, String tenHocSinh, String maLop, int hocKy, String namHoc, long tongTien, long mienGiam, long phaiDong, String trangThai) {
        this.maHP = maHP;
        this.maHS = maHS;
        this.tenHocSinh = tenHocSinh;
        this.maLop = maLop;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
        this.tongTien = tongTien;
        this.mienGiam = mienGiam;
        this.phaiDong = phaiDong;
        this.trangThai = trangThai;
    }

    public int getMaHP() {
        return maHP;
    }

    public void setMaHP(int maHP) {
        this.maHP = maHP;
    }

    public String getMaHS() {
        return maHS;
    }

    public void setMaHS(String maHS) {
        this.maHS = maHS;
    }

    public String getTenHocSinh() {
        return tenHocSinh;
    }

    public void setTenHocSinh(String tenHocSinh) {
        this.tenHocSinh = tenHocSinh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    public long getMienGiam() {
        return mienGiam;
    }

    public void setMienGiam(long mienGiam) {
        this.mienGiam = mienGiam;
    }

    public long getPhaiDong() {
        return phaiDong;
    }

    public void setPhaiDong(long phaiDong) {
        this.phaiDong = phaiDong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}

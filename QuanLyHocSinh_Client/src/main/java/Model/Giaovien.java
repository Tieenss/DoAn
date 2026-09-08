package Model;

public class Giaovien {

    private String maGV;
    private String hoTen;
    private String ngaySinh;
    private String sdt;
    private String maToHop;
    private String tenToHop;

    public Giaovien() {
    }

    public Giaovien(String maGV, String hoTen, String ngaySinh,
                    String sdt, String maToHop, String tenToHop) {
        this.maGV = maGV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.maToHop = maToHop;
        this.tenToHop = tenToHop;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMaToHop() {
        return maToHop;
    }

    public void setMaToHop(String maToHop) {
        this.maToHop = maToHop;
    }

    public String getTenToHop() {
        return tenToHop;
    }

    public void setTenToHop(String tenToHop) {
        this.tenToHop = tenToHop;
    }

    @Override
    public String toString() {
        return hoTen;
    }
}
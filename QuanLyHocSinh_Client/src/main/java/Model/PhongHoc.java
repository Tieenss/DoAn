
package Model;

public class PhongHoc {
     private String maPhong;
    private String tenPhong;
    private int sucChua;
    private String loaiPhong;
    private String tinhTrang;

    public PhongHoc() {}

    public PhongHoc(String maPhong, String tenPhong, int sucChua, String loaiPhong, String tinhTrang) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.sucChua = sucChua;
        this.loaiPhong = loaiPhong;
        this.tinhTrang = tinhTrang;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public int getSucChua() {
        return sucChua;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

}

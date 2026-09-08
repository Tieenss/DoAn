
package Model;

public class TKB {
    private int maTKB;
    private String maLop;
    private String maMH;
    private String tenMH;
    private String maGV;
    private String maPhong;
    private int thu;
    private int tietBatDau;
    private int tietKetThuc;

    public TKB() {
    }

    public TKB(int maTKB, String maLop, String maMH, String tenMH, String maGV, String maPhong, int thu, int tietBatDau, int tietKetThuc) {
        this.maTKB = maTKB;
        this.maLop = maLop;
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.maGV = maGV;
        this.maPhong = maPhong;
        this.thu = thu;
        this.tietBatDau = tietBatDau;
        this.tietKetThuc = tietKetThuc;
    }

    public TKB(int maTKB, String maLop, String maMH, String maGV, String maPhong, int thu, int tietBatDau, int tietKetThuc) {
        this.maTKB = maTKB;
        this.maLop = maLop;
        this.maMH = maMH;
        this.tenMH = "";
        this.maGV = maGV;
        this.maPhong = maPhong;
        this.thu = thu;
        this.tietBatDau = tietBatDau;
        this.tietKetThuc = tietKetThuc;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }

    public int getMaTKB() {
        return maTKB;
    }

    public String getMaLop() {
        return maLop;
    }

    public String getMaMH() {
        return maMH;
    }

    public String getMaGV() {
        return maGV;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public int getThu() {
        return thu;
    }

    public int getTietBatDau() {
        return tietBatDau;
    }

    public int getTietKetThuc() {
        return tietKetThuc;
    }

    public void setMaTKB(int maTKB) {
        this.maTKB = maTKB;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public void setTietBatDau(int tietBatDau) {
        this.tietBatDau = tietBatDau;
    }

    public void setTietKetThuc(int tietKetThuc) {
        this.tietKetThuc = tietKetThuc;
    }
    
}

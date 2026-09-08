package Model;

public class DoiTuongUuTien {
    private String maDT;
    private String tenDT;
    private double tiLeGiam;

    public DoiTuongUuTien() {}

    public DoiTuongUuTien(String maDT, String tenDT, double tiLeGiam) {
        this.maDT = maDT;
        this.tenDT = tenDT;
        this.tiLeGiam = tiLeGiam;
    }

    public String getMaDT() {
        return maDT;
    }

    public void setMaDT(String maDT) {
        this.maDT = maDT;
    }

    public String getTenDT() {
        return tenDT;
    }

    public void setTenDT(String tenDT) {
        this.tenDT = tenDT;
    }

    public double getTiLeGiam() {
        return tiLeGiam;
    }

    public void setTiLeGiam(double tiLeGiam) {
        this.tiLeGiam = tiLeGiam;
    }
}

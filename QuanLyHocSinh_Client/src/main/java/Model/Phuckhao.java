
package Model;
import java.util.Date;

public class Phuckhao {
    private int maPK;
    private String maHS;
    private String maMH;
    private String lyDo;
    private Date ngayGui;
    private String trangThai;

    public Phuckhao() {
    }

    public Phuckhao(int maPK, String maHS, String maMH, String lyDo, Date ngayGui, String trangThai) {
        this.maPK = maPK;
        this.maHS = maHS;
        this.maMH = maMH;
        this.lyDo = lyDo;
        this.ngayGui = ngayGui;
        this.trangThai = trangThai;
    }

    public int getMaPK() {
        return maPK;
    }

    public void setMaPK(int maPK) {
        this.maPK = maPK;
    }

    public String getMaHS() {
        return maHS;
    }

    public void setMaHS(String maHS) {
        this.maHS = maHS;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public Date getNgayGui() {
        return ngayGui;
    }

    public void setNgayGui(Date ngayGui) {
        this.ngayGui = ngayGui;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}

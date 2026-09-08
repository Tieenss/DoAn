
package Model;
import java.util.Date;

public class Thongbao {
    private int maTB;
    private String tieuDe;
    private String noiDung;
    private Date ngayTao;
    private String nguoiGui;

    public Thongbao() {
    }

    public Thongbao(int maTB, String tieuDe, String noiDung, Date ngayTao, String nguoiGui) {
        this.maTB = maTB;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.ngayTao = ngayTao;
        this.nguoiGui = nguoiGui;
    }

    public int getMaTB() {
        return maTB;
    }

    public void setMaTB(int maTB) {
        this.maTB = maTB;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getNguoiGui() {
        return nguoiGui;
    }

    public void setNguoiGui(String nguoiGui) {
        this.nguoiGui = nguoiGui;
    }

}

package Model;

public class ThongKeHocSinh {

    private String namHoc;
    private int hocKy;

    // GPA
    private String gpa;
    private String gpaMoTa;

    // Hạnh kiểm
    private String xepLoai;
    private String xepLoaiMoTa;

    // Số môn
    private String soMon;
    private String soMonMoTa;

    // Học phí
    private String hocPhi;
    private String hocPhiMoTa;

    public ThongKeHocSinh() {
    }

    // =========================================================
    // GETTER / SETTER
    // =========================================================

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getGpaMoTa() {
        return gpaMoTa;
    }

    public void setGpaMoTa(String gpaMoTa) {
        this.gpaMoTa = gpaMoTa;
    }

    public String getXepLoai() {
        return xepLoai;
    }

    public void setXepLoai(String xepLoai) {
        this.xepLoai = xepLoai;
    }

    public String getXepLoaiMoTa() {
        return xepLoaiMoTa;
    }

    public void setXepLoaiMoTa(String xepLoaiMoTa) {
        this.xepLoaiMoTa = xepLoaiMoTa;
    }

    public String getSoMon() {
        return soMon;
    }

    public void setSoMon(String soMon) {
        this.soMon = soMon;
    }

    public String getSoMonMoTa() {
        return soMonMoTa;
    }

    public void setSoMonMoTa(String soMonMoTa) {
        this.soMonMoTa = soMonMoTa;
    }

    public String getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(String hocPhi) {
        this.hocPhi = hocPhi;
    }

    public String getHocPhiMoTa() {
        return hocPhiMoTa;
    }

    public void setHocPhiMoTa(String hocPhiMoTa) {
        this.hocPhiMoTa = hocPhiMoTa;
    }
}
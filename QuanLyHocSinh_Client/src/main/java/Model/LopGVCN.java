package Model;

import com.google.gson.annotations.SerializedName;

public class LopGVCN extends Lop {
    @SerializedName(value = "tenGVCN", alternate = {"TenGVCN", "TENGVCN", "tengvcn", "hoTen", "HoTen"})
    private String tenGVCN;

    public LopGVCN() { super(); }

    public String getTenGVCN() { return tenGVCN; }
    public void setTenGVCN(String tenGVCN) { this.tenGVCN = tenGVCN; }
}
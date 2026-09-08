package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.qlhs.server.entity.HocSinh;

@Entity
@Table(name = "HocPhi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocPhi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHP")
    private int maHP;

    @Column(name = "MaHS", length = 50)
    private String maHS;

    @Transient
    private String maLop;

    @Transient
    private String tenHocSinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaHS", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private HocSinh hocSinh;

    public String getMaLop() {
        return (hocSinh != null) ? hocSinh.getMaLop() : this.maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenHocSinh() {
        return (hocSinh != null) ? hocSinh.getHoTen() : this.tenHocSinh;
    }

    public void setTenHocSinh(String tenHocSinh) {
        this.tenHocSinh = tenHocSinh;
    }

    @Column(name = "HocKy")
    private int hocKy;

    @Column(name = "NamHoc", length = 20)
    private String namHoc;

    @Column(name = "TongTien")
    private long tongTien;

    @Column(name = "MienGiam")
    private long mienGiam;

    @Column(name = "PhaiDong")
    private long phaiDong;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

}
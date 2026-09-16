package com.qlhs.server.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ThoiKhoaBieu")
public class TKB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTKB")
    private Integer maTKB;

    @Column(name = "MaLop")
    private String maLop;

    @Column(name = "MaMH")
    private String maMH;

    @Column(name = "MaGV")
    private String maGV;

    @Column(name = "MaPhong")
    private String maPhong;

    @Column(name = "Thu")
    private Integer thu;

    @Column(name = "TietBatDau")
    private Integer tietBatDau;

    @Column(name = "TietKetThuc")
    private Integer tietKetThuc;

    @Column(name = "NamHoc", length = 20)
    private String namHoc;

    @Column(name = "HocKy")
    private Integer hocKy;

    @Transient
    private String tenMH;

    public TKB() {
    }

    public TKB(Integer maTKB, String maLop, String maMH, String maGV, String maPhong, Integer thu, Integer tietBatDau, Integer tietKetThuc, String namHoc, Integer hocKy, String tenMH) {
        this.maTKB = maTKB;
        this.maLop = maLop;
        this.maMH = maMH;
        this.maGV = maGV;
        this.maPhong = maPhong;
        this.thu = thu;
        this.tietBatDau = tietBatDau;
        this.tietKetThuc = tietKetThuc;
        this.namHoc = namHoc;
        this.hocKy = hocKy;
        this.tenMH = tenMH;
    }

    public Integer getMaTKB() {
        return maTKB;
    }

    public void setMaTKB(Integer maTKB) {
        this.maTKB = maTKB;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getMaGV() {
        return maGV;
    }

    public void setMaGV(String maGV) {
        this.maGV = maGV;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public Integer getThu() {
        return thu;
    }

    public void setThu(Integer thu) {
        this.thu = thu;
    }

    public Integer getTietBatDau() {
        return tietBatDau;
    }

    public void setTietBatDau(Integer tietBatDau) {
        this.tietBatDau = tietBatDau;
    }

    public Integer getTietKetThuc() {
        return tietKetThuc;
    }

    public void setTietKetThuc(Integer tietKetThuc) {
        this.tietKetThuc = tietKetThuc;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    public Integer getHocKy() {
        return hocKy;
    }

    public void setHocKy(Integer hocKy) {
        this.hocKy = hocKy;
    }

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }
}
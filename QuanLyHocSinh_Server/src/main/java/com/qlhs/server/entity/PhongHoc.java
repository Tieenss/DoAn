package com.qlhs.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PhongHoc")
public class PhongHoc {
    @Id
    @Column(name = "MaPhong")
    private String maPhong;

    @Column(name = "TenPhong")
    private String tenPhong;

    @Column(name = "SucChua")
    private int sucChua;

    @Column(name = "LoaiPhong")
    private String loaiPhong;

    @Column(name = "TinhTrang")
    private String tinhTrang;

    public PhongHoc() {
    }

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

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
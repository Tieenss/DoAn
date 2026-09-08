package com.qlhs.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PhongHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
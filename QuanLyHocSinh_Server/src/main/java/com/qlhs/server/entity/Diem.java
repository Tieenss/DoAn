package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "Diem")
@IdClass(Diem.DiemId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diem {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiemId implements Serializable {
        private String maHS;
        private String maMH;
        private int hocKy;
    }

    @Id
    @Column(name = "MaHS", length = 50)
    private String maHS;

    @Id
    @Column(name = "MaMH", length = 50)
    private String maMH;

    @Id
    @Column(name = "HocKy")
    private int hocKy;

    @Column(name = "Diem15p")
    private double diem15p;

    @Column(name = "Diem1Tiet")
    private double diem1Tiet;

    @Column(name = "DiemGiuaKy")
    private double diemGiuaKy;

    @Column(name = "DiemCuoiKy")
    private double diemCuoiKy;

    @Column(name = "DiemTongKet")
    private double diemTongKet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaHS", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private HocSinh hocSinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaMH", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private MonHoc monHoc;

    @Transient
    public String getTenHS() { return hocSinh != null ? hocSinh.getHoTen() : null; }

    @Transient
    public String getMaLop() { return hocSinh != null ? hocSinh.getMaLop() : null; }

    @Transient
    public String getTenMH() { return monHoc != null ? monHoc.getTenMH() : null; }
}

package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "LichThi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LichThi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "MaLT")
    private int maLT;

    @Column(name = "TenKyThi", columnDefinition = "NVARCHAR(255)")
    private String tenKyThi;

    @Column(name = "MaMH", length = 50)
    private String maMH;

    @Column(name = "NgayThi")
    private LocalDate ngayThi;

    @Column(name = "GioBatDau")
    private LocalTime gioBatDau;

    @Column(name = "GioKetThuc")
    private LocalTime gioKetThuc;

    @Column(name = "MaPhong", length = 50)
    private String maPhong;

    @Column(name = "MaLop", length = 10)
    private String maLop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaMH", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private MonHoc monHoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLop", insertable = false, updatable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Lop lop;

    @Transient
    public String getTenMH() {
        return monHoc != null ? monHoc.getTenMH() : null;
    }

    @Transient
    public String getTenLop() {
        return lop != null ? lop.getTenLop() : null;
    }
}

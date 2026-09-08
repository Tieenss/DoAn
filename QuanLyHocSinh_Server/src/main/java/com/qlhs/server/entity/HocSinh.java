package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "HocSinh")
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class HocSinh {

    @Id 
    @Column(name = "maHS", length = 50)
    private String maHS;

    @Column(name = "hoTen", columnDefinition = "NVARCHAR(255)")
    private String hoTen;

    @Column(name = "ngaySinh")
    private LocalDate ngaySinh;

    @Column(name = "gioiTinh", columnDefinition = "NVARCHAR(10)")
    private String gioiTinh;

    @Column(name = "diaChi", columnDefinition = "NVARCHAR(255)")
    private String diaChi;

    @Column(name = "maLop", length = 50)
    private String maLop;

    @Column(name = "maDT", length = 50)
    private String maDT;
}

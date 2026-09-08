package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="PhucKhao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhucKhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MaPK")
    private int maPK;

    @Column(name="MaHS",length=50)
    private String maHS;

    @Column(name="MaMH",length=50)
    private String maMH;

    @Column(name="LyDo",length=255)
    private String lyDo;

    @Temporal(TemporalType.DATE)
    @Column(name="NgayGui")
    private Date ngayGui;

    @Column(name="TrangThai",length=50)
    private String trangThai;

}
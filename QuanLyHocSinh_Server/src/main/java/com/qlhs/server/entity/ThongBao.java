package com.qlhs.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="ThongBao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThongBao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="MaTB")
    private int maTB;

    @Column(name="TieuDe", length=200)
    private String tieuDe;

    @Column(name="NoiDung", length=1000)
    private String noiDung;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+7") 
    @Column(name="NgayTao")
    private Date ngayTao;

    @Column(name="NguoiGui", length=100)
    private String nguoiGui;
}
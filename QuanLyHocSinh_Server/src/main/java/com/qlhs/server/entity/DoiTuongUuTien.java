package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DoiTuongUuTien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoiTuongUuTien {

    @Id
    @Column(name = "MaDT", length = 50)
    private String maDT;

    @Column(name = "TenDT", columnDefinition = "NVARCHAR(255)")
    private String tenDT;

    @Column(name = "TiLeGiamHocPhi")
    private double tiLeGiam;
}
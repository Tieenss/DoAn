package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {

    @Id
    @Column(name = "TenDangNhap", length = 50)
    private String tenDangNhap;

    @Column(name = "MatKhau", length = 100)
    private String matKhau;

    @Column(name = "Quyen", length = 50)
    private String quyen;

    @Column(name = "MaNguoiDung", length = 50)
    private String maNguoiDung;
}

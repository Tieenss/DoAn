package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Lop")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lop {

    @Id
    @Column(name = "MaLop", length = 10)
    private String maLop;

    @Column(name = "TenLop", nullable = false, length = 50)
    private String tenLop;

    @Column(name = "NienKhoa", length = 20)
    private String nienKhoa;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaGVCN")
    private GiaoVien giaoVienChuNhiem;

    @JsonProperty("maGVCN")
    public void setMaGVCNFromJson(String maGVCN) {
        if (maGVCN != null && !maGVCN.isEmpty()) {
            GiaoVien gv = new GiaoVien();
            gv.setMaGV(maGVCN);
            this.giaoVienChuNhiem = gv;
        }
    }
}
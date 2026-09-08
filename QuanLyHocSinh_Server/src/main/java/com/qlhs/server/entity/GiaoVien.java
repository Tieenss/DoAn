package com.qlhs.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@Entity
@Table(name = "GiaoVien")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class GiaoVien {

    @Id
    @Column(name = "MaGV")
    private String maGV;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "NgaySinh")
    private String ngaySinh;

    @Column(name = "SDT")
    private String sdt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaToHop")
    @JsonBackReference
    private ToHopMon toHopMon;

    @JsonProperty("maToHop")
    public void setMaToHopFromJson(String maToHop) {
        if (maToHop != null && !maToHop.isEmpty()) {
            ToHopMon th = new ToHopMon();
            th.setMaToHop(maToHop);
            this.toHopMon = th;
        }
    }
}
package com.qlhs.server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ToHopMon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToHopMon {

    @Id
    @Column(name = "MaToHop", length = 10)
    private String maToHop;

    @Column(name = "TenToHop", nullable = false, length = 100)
    private String tenToHop;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "toHopMon")
    private List<GiaoVien> giaoViens;
}
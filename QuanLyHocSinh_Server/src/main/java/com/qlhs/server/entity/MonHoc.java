package com.qlhs.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MonHoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonHoc {
    @Id
    @Column (name = "MaMH", length = 50)
    private String maMH;

    @Column (name = "TenMH", length = 100)
    private String tenMH;
}

package com.qlhs.server.repository.Dai;

import com.qlhs.server.entity.DoiTuongUuTien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoiTuongUuTienRepository extends JpaRepository<DoiTuongUuTien, String> {

    List<DoiTuongUuTien> findByMaDTContaining(String maDT);

    List<DoiTuongUuTien> findByTenDTContaining(String tenDT);

}
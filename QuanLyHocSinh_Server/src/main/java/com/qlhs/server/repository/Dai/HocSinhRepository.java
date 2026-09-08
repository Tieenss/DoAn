package com.qlhs.server.repository.Dai;

import com.qlhs.server.entity.HocSinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HocSinhRepository extends JpaRepository<HocSinh, String> {

    List<HocSinh> findByHoTenContaining(String hoTen);

    List<HocSinh> findByMaHSContaining(String maHS);
}

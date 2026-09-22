package com.qlhs.server.repository.ThuTrang;

import com.qlhs.server.entity.PhongHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhongHocRepository extends JpaRepository<PhongHoc, String> {

    List<PhongHoc> findAllByOrderByMaPhongAsc();

    @Query("SELECT p FROM PhongHoc p WHERE " +
            "(:ma IS NULL OR :ma = '' OR LOWER(p.maPhong) LIKE LOWER(CONCAT('%', :ma, '%'))) AND " +
            "(:loai IS NULL OR :loai = '' OR p.loaiPhong = :loai) AND " +
            "(:tinhTrang IS NULL OR :tinhTrang = '' OR p.tinhTrang = :tinhTrang) " +
            "ORDER BY p.maPhong ASC")
    List<PhongHoc> searchPhongHoc(@Param("ma") String ma,
                                  @Param("loai") String loai,
                                  @Param("tinhTrang") String tinhTrang);
}
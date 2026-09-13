package com.qlhs.server.repository.HaTrang;

import com.qlhs.server.entity.HocPhi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HocPhiRepository extends JpaRepository<HocPhi, Integer> {
    @Query("SELECT t FROM HocPhi t JOIN FETCH t.hocSinh")
    List<HocPhi> findAllWithDetails();

    @Query("SELECT t FROM HocPhi t JOIN FETCH t.hocSinh WHERE t.maHS = :maHS")
    List<HocPhi> findByMaHS(@Param("maHS") String maHS);

    @Query("SELECT t FROM HocPhi t JOIN FETCH t.hocSinh WHERE " +
            "(:namHoc = '' OR t.namHoc = :namHoc) AND " +
            "(LOWER(t.hocSinh.maLop) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.hocSinh.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.maHS) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<HocPhi> searchByKeyword(@Param("keyword") String keyword, @Param("namHoc") String namHoc);

    @Query("SELECT DISTINCT t.namHoc FROM HocPhi t WHERE t.namHoc IS NOT NULL ORDER BY t.namHoc")
    List<String> getDistinctNamHoc();
}
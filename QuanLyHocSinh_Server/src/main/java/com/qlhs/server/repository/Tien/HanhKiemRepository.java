package com.qlhs.server.repository.Tien;

import com.qlhs.server.entity.HanhKiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface HanhKiemRepository extends JpaRepository<HanhKiem, HanhKiem.HanhKiemId> {

       @Query("SELECT hk FROM HanhKiem hk JOIN FETCH hk.hocSinh hs " +
                     "WHERE hs.maLop LIKE %:maLop% AND hk.namHoc LIKE %:namHoc% AND (:hocKy = 0 OR hk.hocKy = :hocKy)")
       List<HanhKiem> getHanhKiemByFilter(@Param("maLop") String maLop, @Param("namHoc") String namHoc,
                     @Param("hocKy") int hocKy);

       @Query("SELECT hk FROM HanhKiem hk JOIN FETCH hk.hocSinh hs " +
                     "WHERE hk.maHS LIKE %:keyword% OR hs.hoTen LIKE %:keyword%")
       List<HanhKiem> searchHanhKiem(@Param("keyword") String keyword);

       @Query("SELECT hk FROM HanhKiem hk JOIN FETCH hk.hocSinh hs " +
                     "WHERE hk.maHS = :maHS")
       List<HanhKiem> getHanhKiemByMaHS(@Param("maHS") String maHS);

       @Query("SELECT DISTINCT hk.namHoc FROM HanhKiem hk ORDER BY hk.namHoc DESC")
       List<String> getDistinctNamHoc();
}

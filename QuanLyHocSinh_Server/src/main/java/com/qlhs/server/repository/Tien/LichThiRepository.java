package com.qlhs.server.repository.Tien;

import com.qlhs.server.entity.LichThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LichThiRepository extends JpaRepository<LichThi, Integer> {

    @Query("SELECT l FROM LichThi l WHERE l.tenKyThi LIKE %:keyword% OR l.maMH LIKE %:keyword%")
    List<LichThi> searchLichThi(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT l.tenKyThi FROM LichThi l")
    List<String> getDistinctKyThi();

    @Query("SELECT DISTINCT l.namHoc FROM LichThi l WHERE l.namHoc IS NOT NULL ORDER BY l.namHoc")
    List<String> getDistinctNamHoc();

    @Query("SELECT l FROM LichThi l JOIN FETCH l.monHoc LEFT JOIN FETCH l.lop WHERE l.tenKyThi LIKE %:tenKyThi% AND l.maMH LIKE %:maMH% AND l.maPhong LIKE %:maPhong% AND l.maLop LIKE %:maLop%")
    List<LichThi> filterLichThi(@Param("tenKyThi") String tenKyThi, @Param("maMH") String maMH,
            @Param("maPhong") String maPhong, @Param("maLop") String maLop);

    @Query("SELECT l FROM LichThi l JOIN FETCH l.monHoc LEFT JOIN FETCH l.lop")
    List<LichThi> getAllLichThiWithTenMon();

    @Query("SELECT l FROM LichThi l JOIN FETCH l.monHoc LEFT JOIN FETCH l.lop WHERE (:namHoc = '' OR l.namHoc = :namHoc) AND (l.tenKyThi LIKE %:keyword% OR l.monHoc.tenMH LIKE %:keyword% OR l.maMH LIKE %:keyword% OR l.maLop LIKE %:keyword% OR l.lop.tenLop LIKE %:keyword%)")
    List<LichThi> searchLichThiNative(@Param("keyword") String keyword, @Param("namHoc") String namHoc);
}

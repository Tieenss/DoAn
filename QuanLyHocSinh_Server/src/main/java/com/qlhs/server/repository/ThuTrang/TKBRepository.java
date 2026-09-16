package com.qlhs.server.repository.ThuTrang;

import com.qlhs.server.entity.TKB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TKBRepository extends JpaRepository<TKB, Integer> {

    @Query("SELECT t FROM TKB t WHERE " +
            "(:maLop IS NULL OR :maLop = '' OR t.maLop = :maLop) AND " +
            "(:maMH IS NULL OR :maMH = '' OR t.maMH IN " +
            "  (SELECT m.maMH FROM MonHoc m WHERE LOWER(m.tenMH) LIKE LOWER(CONCAT('%', :maMH, '%')) OR LOWER(m.maMH) LIKE LOWER(CONCAT('%', :maMH, '%')))) AND " +
            "(:thu IS NULL OR :thu = 0 OR t.thu = :thu) AND " +
            "(:namHoc IS NULL OR :namHoc = '' OR t.namHoc = :namHoc) AND " +
            "(:hocKy IS NULL OR :hocKy = 0 OR t.hocKy = :hocKy)")
    List<TKB> filterTKB(@Param("maLop") String maLop,
                        @Param("maMH") String maMH,
                        @Param("thu") Integer thu,
                        @Param("namHoc") String namHoc,
                        @Param("hocKy") Integer hocKy);

    @Query("SELECT DISTINCT t.maLop FROM TKB t")
    List<String> getDistinctMaLop();

    @Query("SELECT DISTINCT t.namHoc FROM TKB t WHERE t.namHoc IS NOT NULL AND t.namHoc != '' ORDER BY t.namHoc DESC")
    List<String> getDistinctNamHoc();

    List<TKB> findByMaLop(String maLop);
    List<TKB> findByMaMH(String maMH);
    List<TKB> findByMaPhong(String maPhong);
    List<TKB> findByMaGV(String maGV);
    List<TKB> findByThu(Integer thu);
}
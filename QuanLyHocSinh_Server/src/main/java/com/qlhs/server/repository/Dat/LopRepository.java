package com.qlhs.server.repository.Dat;

import com.qlhs.server.entity.Lop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LopRepository extends JpaRepository<Lop, String> {

    String BASE_QUERY =
            "SELECT " +
                    "l.MaLop AS maLop, " +
                    "l.TenLop AS tenLop, " +
                    "l.NienKhoa AS nienKhoa, " +
                    "l.MaGVCN AS maGVCN, " +
                    "gv.HoTen AS tenGVCN " +
                    "FROM Lop l " +
                    "LEFT JOIN GiaoVien gv ON l.MaGVCN = gv.MaGV ";

    @Query(value = BASE_QUERY, nativeQuery = true)
    List<Map<String, Object>> findAllLopWithGVCN();

    @Query(value = BASE_QUERY +
            "WHERE l.MaLop LIKE %:keyword% " +
            "OR l.TenLop LIKE %:keyword% " +
            "OR l.NienKhoa LIKE %:keyword% " +
            "OR gv.HoTen LIKE %:keyword%",
            nativeQuery = true)
    List<Map<String, Object>> searchLop(@Param("keyword") String keyword);

    @Query(value = BASE_QUERY +
            "WHERE l.MaLop = :maLop",
            nativeQuery = true)
    Map<String, Object> findByMaLopWithGVCN(@Param("maLop") String maLop);

    @Query("SELECT DISTINCT l.nienKhoa FROM Lop l ORDER BY l.nienKhoa")
    List<String> findDistinctNienKhoa();

}
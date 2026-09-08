package com.qlhs.server.repository.Dat;

import com.qlhs.server.entity.GiaoVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GiaoVienRepository extends JpaRepository<GiaoVien, String> {

    String BASE_QUERY =
            "SELECT " +
                    "gv.MaGV AS maGV, " +
                    "gv.HoTen AS hoTen, " +
                    "gv.NgaySinh AS ngaySinh, " +
                    "gv.SDT AS sdt, " +
                    "th.MaToHop AS maToHop, " +
                    "th.TenToHop AS tenToHop " +
                    "FROM GiaoVien gv " +
                    "LEFT JOIN ToHopMon th ON gv.MaToHop = th.MaToHop ";

    @Query(value = BASE_QUERY, nativeQuery = true)
    List<Map<String, Object>> findAllGiaoVienWithToHop();

    @Query(value = BASE_QUERY +
            "WHERE gv.MaGV LIKE %:keyword% " +
            "OR gv.HoTen LIKE %:keyword% " +
            "OR gv.SDT LIKE %:keyword% " +
            "OR th.TenToHop LIKE %:keyword%",
            nativeQuery = true)
    List<Map<String, Object>> searchGiaoVien(@Param("keyword") String keyword);

    @Query(value = BASE_QUERY +
            "WHERE gv.MaGV = :maGV",
            nativeQuery = true)
    Map<String, Object> findByMaGVWithToHop(@Param("maGV") String maGV);

}
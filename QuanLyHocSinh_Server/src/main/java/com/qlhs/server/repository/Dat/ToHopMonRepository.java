package com.qlhs.server.repository.Dat;

import com.qlhs.server.entity.ToHopMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ToHopMonRepository extends JpaRepository<ToHopMon, String> {

    String BASE_QUERY =
            "SELECT MaToHop AS maToHop, " +
                    "TenToHop AS tenToHop " +
                    "FROM ToHopMon ";

    @Query(value = BASE_QUERY, nativeQuery = true)
    List<Map<String, Object>> findAllToHopMon();

    @Query(value = BASE_QUERY +
            "WHERE MaToHop = :maToHop",
            nativeQuery = true)
    List<Map<String, Object>> findByMaToHop(@Param("maToHop") String maToHop);

    @Query(value = BASE_QUERY +
            "WHERE MaToHop LIKE %:keyword% " +
            "OR TenToHop LIKE %:keyword%",
            nativeQuery = true)
    List<Map<String, Object>> searchToHopMon(@Param("keyword") String keyword);

    @Query(value = "SELECT DISTINCT TenToHop FROM ToHopMon ORDER BY TenToHop",
            nativeQuery = true)
    List<String> findDistinctTenToHop();
}
package com.qlhs.server.repository.HaTrang;

import com.qlhs.server.entity.PhucKhao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhucKhaoRepository extends JpaRepository<PhucKhao, Integer> {
    List<PhucKhao> findByMaHS(String maHS);

    @Query("SELECT p FROM PhucKhao p WHERE " +
            "LOWER(p.maMH) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.maHS) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.lyDo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.trangThai) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<PhucKhao> searchByKeyword(@Param("keyword") String keyword);
}
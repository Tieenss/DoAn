package com.qlhs.server.repository.Dai;

import com.qlhs.server.entity.HocSinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HocSinhRepository extends JpaRepository<HocSinh, String> {

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT h.nienKhoa FROM HocSinh h WHERE h.nienKhoa IS NOT NULL ORDER BY h.nienKhoa")
    List<String> findDistinctNienKhoa();

    @org.springframework.data.jpa.repository.Query("SELECT h FROM HocSinh h WHERE (:nienKhoa = '' OR h.nienKhoa = :nienKhoa) AND (h.maHS LIKE %:keyword% OR h.hoTen LIKE %:keyword%)")
    List<HocSinh> search(@org.springframework.data.repository.query.Param("keyword") String keyword, @org.springframework.data.repository.query.Param("nienKhoa") String nienKhoa);
}

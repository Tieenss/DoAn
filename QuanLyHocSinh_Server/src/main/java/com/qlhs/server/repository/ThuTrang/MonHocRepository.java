package com.qlhs.server.repository.ThuTrang;

import com.qlhs.server.entity.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonHocRepository extends JpaRepository<MonHoc, String> {

    @Query("SELECT m FROM MonHoc m WHERE m.maMH LIKE %:keyword% OR m.tenMH LIKE %:keyword%")
    List<MonHoc> searchMonHoc(@Param("keyword") String keyword);
}
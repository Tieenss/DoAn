package com.qlhs.server.repository.HaTrang;

import com.qlhs.server.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {

    @Query("SELECT t FROM ThongBao t WHERE " +
            "t.tieuDe LIKE CONCAT('%', :keyword, '%') OR " +
            "t.noiDung LIKE CONCAT('%', :keyword, '%') OR " +
            "t.nguoiGui LIKE CONCAT('%', :keyword, '%')")
    List<ThongBao> searchByKeyword(@Param("keyword") String keyword);
}
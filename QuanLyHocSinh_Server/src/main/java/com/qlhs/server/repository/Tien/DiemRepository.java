package com.qlhs.server.repository.Tien;

import com.qlhs.server.entity.Diem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiemRepository extends JpaRepository<Diem, Diem.DiemId> {

    @Query("SELECT d FROM Diem d JOIN FETCH d.hocSinh JOIN FETCH d.monHoc")
    List<Diem> findAllDiemWithDetails();

    @Query("SELECT d FROM Diem d JOIN FETCH d.hocSinh hs JOIN FETCH d.monHoc " +
            "WHERE (:maLop = '' OR hs.maLop = :maLop) AND (:maMH = '' OR d.maMH = :maMH) AND (:hocKy = 0 OR d.hocKy = :hocKy)")
    List<Diem> findDiemByFilter(@Param("maLop") String maLop, @Param("maMH") String maMH, @Param("hocKy") int hocKy);

    @Query("SELECT d FROM Diem d JOIN FETCH d.hocSinh hs JOIN FETCH d.monHoc " +
            "WHERE d.maHS LIKE %:keyword% OR hs.hoTen LIKE %:keyword%")
    List<Diem> searchDiem(@Param("keyword") String keyword);

    @Query("SELECT d FROM Diem d JOIN FETCH d.hocSinh JOIN FETCH d.monHoc " +
            "WHERE d.maHS = :maHS")
    List<Diem> findDiemByMaHSWithDetails(@Param("maHS") String maHS);

    @Query("SELECT DISTINCT d.hocKy FROM Diem d ORDER BY d.hocKy")
    List<Integer> findDistinctHocKy();
}

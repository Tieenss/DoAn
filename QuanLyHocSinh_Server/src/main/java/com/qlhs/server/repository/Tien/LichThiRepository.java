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

    @Query("SELECT l FROM LichThi l JOIN FETCH l.monHoc LEFT JOIN FETCH l.lop " +
           "WHERE l.ngayThi = :ngayThi AND l.maPhong = :maPhong AND l.maLT != :excludeId " +
           "AND l.gioBatDau < :gioKetThuc AND l.gioKetThuc > :gioBatDau")
    List<LichThi> findRoomConflicts(
            @Param("ngayThi") java.time.LocalDate ngayThi,
            @Param("maPhong") String maPhong,
            @Param("gioBatDau") java.time.LocalTime gioBatDau,
            @Param("gioKetThuc") java.time.LocalTime gioKetThuc,
            @Param("excludeId") int excludeId);

    @Query("SELECT l FROM LichThi l JOIN FETCH l.monHoc LEFT JOIN FETCH l.lop " +
           "WHERE l.ngayThi = :ngayThi AND l.maLop = :maLop AND l.maLT != :excludeId " +
           "AND l.gioBatDau < :gioKetThuc AND l.gioKetThuc > :gioBatDau")
    List<LichThi> findClassConflicts(
            @Param("ngayThi") java.time.LocalDate ngayThi,
            @Param("maLop") String maLop,
            @Param("gioBatDau") java.time.LocalTime gioBatDau,
            @Param("gioKetThuc") java.time.LocalTime gioKetThuc,
            @Param("excludeId") int excludeId);

    @Query("SELECT l FROM LichThi l JOIN FETCH l.monHoc LEFT JOIN FETCH l.lop " +
           "WHERE l.maLop = :maLop AND l.maMH = :maMH AND l.tenKyThi = :tenKyThi " +
           "AND l.namHoc = :namHoc AND l.hocKy = :hocKy AND l.maLT != :excludeId")
    List<LichThi> findSubjectConflicts(
            @Param("maLop") String maLop,
            @Param("maMH") String maMH,
            @Param("tenKyThi") String tenKyThi,
            @Param("namHoc") String namHoc,
            @Param("hocKy") int hocKy,
            @Param("excludeId") int excludeId);
}

package com.qlhs.server.repository.Dai;

import com.qlhs.server.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {

    Optional<TaiKhoan> findByTenDangNhapAndMatKhau(String tenDangNhap, String matKhau);

    List<TaiKhoan> findByTenDangNhapContainingOrQuyenContainingOrMaNguoiDungContaining(
            String tenDangNhap,
            String quyen,
            String maNguoiDung
    );
}
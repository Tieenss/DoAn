package com.qlhs.server.service.Dai;

import com.qlhs.server.entity.TaiKhoan;
import com.qlhs.server.repository.Dai.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public TaiKhoan checkLogin(String username, String password) {
        Optional<TaiKhoan> tk =
                taiKhoanRepository.findByTenDangNhapAndMatKhau(username, password);
        return tk.orElse(null); 
    }

    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanRepository.findAll();
    }

    public Optional<TaiKhoan> getTaiKhoanByTenDangNhap(String tenDangNhap) {
        return taiKhoanRepository.findById(tenDangNhap);
    }

    public List<TaiKhoan> searchTaiKhoan(String keyword) {
        return taiKhoanRepository
                .findByTenDangNhapContainingOrQuyenContainingOrMaNguoiDungContaining(
                        keyword,
                        keyword,
                        keyword
                );
    }

    public TaiKhoan saveTaiKhoan(TaiKhoan taiKhoan) {

        if (taiKhoan.getTenDangNhap() == null ||
                taiKhoan.getTenDangNhap().isBlank()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống!");
        }

        if (taiKhoan.getMatKhau() == null ||
                taiKhoan.getMatKhau().isBlank()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống!");
        }

        if (taiKhoan.getQuyen() == null ||
                taiKhoan.getQuyen().isBlank()) {
            throw new IllegalArgumentException("Quyền không được để trống!");
        }

        return taiKhoanRepository.save(taiKhoan);
    }

    public void deleteTaiKhoan(String tenDangNhap) {
        taiKhoanRepository.deleteById(tenDangNhap);
    }

}
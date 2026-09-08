package com.qlhs.server.restControl.Dai;

import com.qlhs.server.entity.TaiKhoan;
import com.qlhs.server.service.Dai.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/taikhoan")
public class TaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {

        String username = loginData.get("tenDangNhap");
        String password = loginData.get("matKhau");

        TaiKhoan tk = taiKhoanService.checkLogin(username, password);

        if (tk != null) {
            return ResponseEntity.ok(tk);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Sai tài khoản hoặc mật khẩu");
    }

    @GetMapping
    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanService.getAllTaiKhoan();
    }

    @GetMapping("/{tenDangNhap}")
    public ResponseEntity<TaiKhoan> getTaiKhoanByTenDangNhap(
            @PathVariable String tenDangNhap) {

        return taiKhoanService.getTaiKhoanByTenDangNhap(tenDangNhap)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<TaiKhoan> searchTaiKhoan(
            @RequestParam String keyword) {

        return taiKhoanService.searchTaiKhoan(keyword);
    }

    @PostMapping
    public TaiKhoan saveTaiKhoan(
            @RequestBody TaiKhoan taiKhoan) {

        return taiKhoanService.saveTaiKhoan(taiKhoan);
    }

    @DeleteMapping("/{tenDangNhap}")
    public ResponseEntity<Void> deleteTaiKhoan(
            @PathVariable String tenDangNhap) {

        taiKhoanService.deleteTaiKhoan(tenDangNhap);

        return ResponseEntity.ok().build();
    }

}
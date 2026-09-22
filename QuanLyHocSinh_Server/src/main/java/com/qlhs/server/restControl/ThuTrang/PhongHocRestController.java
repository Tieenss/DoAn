package com.qlhs.server.restControl.ThuTrang;

import com.qlhs.server.entity.PhongHoc;
import com.qlhs.server.service.ThuTrang.PhongHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phonghoc")
public class PhongHocRestController {
    @Autowired
    private PhongHocService phongHocService;

    @GetMapping
    public List<PhongHoc> getAllPH(){
        return phongHocService.getAllPH();
    }

    @GetMapping("/{maPhong}")
    public ResponseEntity<PhongHoc> getByIdPH(@PathVariable String maPhong){
        return phongHocService.getByIdPH(maPhong)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<PhongHoc> search(
            @RequestParam(defaultValue = "") String ma,
            @RequestParam(defaultValue = "") String loai,
            @RequestParam(defaultValue = "") String tinhTrang) {
        if (ma.isEmpty() &&
                (loai.isEmpty() || loai.equals("Tất cả")) &&
                (tinhTrang.isEmpty() || tinhTrang.equals("Tất cả"))) {
            return phongHocService.getAllPH();
        }

        String queryLoai = "Tất cả".equals(loai) ? "" : loai;
        String queryTinhTrang = "Tất cả".equals(tinhTrang) ? "" : tinhTrang;

        return phongHocService.search(ma, queryLoai, queryTinhTrang);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PhongHoc phongHoc){
        String maPhong = phongHoc.getMaPhong() == null ? "" : phongHoc.getMaPhong().trim();
        String tenPhong = phongHoc.getTenPhong() == null ? "" : phongHoc.getTenPhong().trim();
        phongHoc.setMaPhong(maPhong);
        phongHoc.setTenPhong(tenPhong);

        if (maPhong.length() < 2) {
            return ResponseEntity.badRequest().body("Mã phòng học: Vui lòng nhập từ 2 ký tự trở lên và không được để trống!");
        }
        if (!maPhong.matches("^[a-zA-Z0-9]+$")) {
            return ResponseEntity.badRequest().body("Mã phòng học: Không được chứa ký tự đặc biệt! Vui lòng nhập lại thông tin.");
        }
        if (tenPhong.length() < 2) {
            return ResponseEntity.badRequest().body("Tên phòng học: Vui lòng nhập từ 2 ký tự trở lên và không được để trống!");
        }
        if (!tenPhong.matches("^[a-zA-Z0-9\\p{L} ]+$")) {
            return ResponseEntity.badRequest().body("Tên phòng học: Không được chứa ký tự đặc biệt! Vui lòng nhập lại thông tin.");
        }
        if (phongHoc.getSucChua() <= 0) {
            return ResponseEntity.badRequest().body("Sức chứa phòng học phải là số nguyên dương lớn hơn 0!");
        }
        if (phongHocService.existsPH(maPhong)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Mã phòng đã tồn tại!");
        }
        if (phongHocService.existsByTenPhong(tenPhong)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên phòng đã tồn tại!");
        }
        return ResponseEntity.ok(phongHocService.save(phongHoc));
    }

    @PutMapping("/{maPhong}")
    public ResponseEntity<?> update(@PathVariable String maPhong, @RequestBody PhongHoc phongHoc){
        if (!phongHocService.existsPH(maPhong)){
            return ResponseEntity.notFound().build();
        }
        String tenPhong = phongHoc.getTenPhong() == null ? "" : phongHoc.getTenPhong().trim();
        phongHoc.setTenPhong(tenPhong);

        if (tenPhong.length() < 2) {
            return ResponseEntity.badRequest().body("Tên phòng học: Vui lòng nhập từ 2 ký tự trở lên và không được để trống!");
        }
        if (!tenPhong.matches("^[a-zA-Z0-9\\p{L} ]+$")) {
            return ResponseEntity.badRequest().body("Tên phòng học: Không được chứa ký tự đặc biệt! Vui lòng nhập lại thông tin.");
        }
        if (phongHoc.getSucChua() <= 0) {
            return ResponseEntity.badRequest().body("Sức chứa phòng học phải là số nguyên dương lớn hơn 0!");
        }
        if (phongHocService.existsByTenPhongExcluding(maPhong, tenPhong)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tên phòng đã tồn tại!");
        }
        phongHoc.setMaPhong(maPhong);
        return ResponseEntity.ok(phongHocService.save(phongHoc));
    }

    @DeleteMapping("/{maPhong}")
    public ResponseEntity<PhongHoc> delete(@PathVariable String maPhong){
        if (!phongHocService.existsPH(maPhong)){
            return ResponseEntity.notFound().build();
        }
        phongHocService.delete(maPhong);
        return ResponseEntity.ok().build();
    }
}
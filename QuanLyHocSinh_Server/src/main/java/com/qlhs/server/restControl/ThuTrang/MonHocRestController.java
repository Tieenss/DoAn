package com.qlhs.server.restControl.ThuTrang;

import com.qlhs.server.entity.MonHoc;
import com.qlhs.server.service.ThuTrang.MonHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monhoc")
public class MonHocRestController {

    @Autowired
    private MonHocService monHocService;

    @GetMapping
    public List<MonHoc> getAllMH() {
        return monHocService.getAllMH();
    }
    @GetMapping("/{maMH}")
    public ResponseEntity<MonHoc> getByIdMH(@PathVariable String maMH) {
        return monHocService.getByIdMH(maMH)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<MonHoc> search(@RequestParam(defaultValue = "") String keyword) {
        return monHocService.search(keyword);
    }

    @PostMapping
    public ResponseEntity<?> createMH(@RequestBody MonHoc monHoc) {
        if (monHoc.getMaMH() == null || monHoc.getMaMH().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mã môn học không được để trống!");
        }
        if (monHoc.getTenMH() == null || monHoc.getTenMH().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên môn học không được để trống!");
        }
        if (monHocService.existsMH(monHoc.getMaMH().trim())) {
            return ResponseEntity.status(409).body("Mã môn học đã tồn tại!");
        }
        if (monHocService.existsByTenMH(monHoc.getTenMH().trim())) {
            return ResponseEntity.status(422).body("Tên môn học đã tồn tại!");
        }
        return ResponseEntity.ok(monHocService.saveMH(monHoc));
    }

    @PutMapping("/{maMH}")
    public ResponseEntity<?> updateMH(@PathVariable String maMH, @RequestBody MonHoc monHoc) {
        if (!monHocService.existsMH(maMH)) {
            return ResponseEntity.notFound().build();
        }
        if (monHoc.getTenMH() == null || monHoc.getTenMH().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên môn học không được để trống!");
        }
        if (monHocService.existsByTenMHExcluding(maMH, monHoc.getTenMH().trim())) {
            return ResponseEntity.status(422).body("Tên môn học đã tồn tại!");
        }
        monHoc.setMaMH(maMH);
        return ResponseEntity.ok(monHocService.saveMH(monHoc));
    }
    @DeleteMapping("/{maMH}")
    public ResponseEntity<Void> deleteMH(@PathVariable String maMH) {
        if (!monHocService.existsMH(maMH)) {
            return ResponseEntity.notFound().build();
        }
        monHocService.deleteMH(maMH);
        return ResponseEntity.ok().build();
    }
}

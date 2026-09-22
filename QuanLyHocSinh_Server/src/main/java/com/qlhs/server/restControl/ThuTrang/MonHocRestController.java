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
        String maMH = monHoc.getMaMH() == null ? "" : monHoc.getMaMH().trim();
        String tenMH = monHoc.getTenMH() == null ? "" : monHoc.getTenMH().trim();
        monHoc.setMaMH(maMH);
        monHoc.setTenMH(tenMH);

        if (maMH.length() < 2) {
            return ResponseEntity.badRequest().body("Mã môn học: Vui lòng nhập từ 2 ký tự trở lên và không được để trống!");
        }
        if (!maMH.matches("^[a-zA-Z0-9]+$")) {
            return ResponseEntity.badRequest().body("Mã môn học: Không được chứa ký tự đặc biệt! Vui lòng nhập lại thông tin.");
        }
        if (tenMH.length() < 2) {
            return ResponseEntity.badRequest().body("Tên môn học: Vui lòng nhập từ 2 ký tự trở lên và không được để trống!");
        }
        if (!tenMH.matches("^[a-zA-Z0-9\\p{L} ]+$")) {
            return ResponseEntity.badRequest().body("Tên môn học: Không được chứa ký tự đặc biệt! Vui lòng nhập lại thông tin.");
        }

        if (monHocService.existsMH(maMH)) {
            return ResponseEntity.status(409).body("Mã môn học đã tồn tại!");
        }
        if (monHocService.existsByTenMH(tenMH)) {
            return ResponseEntity.status(422).body("Tên môn học đã tồn tại!");
        }
        return ResponseEntity.ok(monHocService.saveMH(monHoc));
    }

    @PutMapping("/{maMH}")
    public ResponseEntity<?> updateMH(@PathVariable String maMH, @RequestBody MonHoc monHoc) {
        if (!monHocService.existsMH(maMH)) {
            return ResponseEntity.notFound().build();
        }
        String tenMH = monHoc.getTenMH() == null ? "" : monHoc.getTenMH().trim();
        monHoc.setTenMH(tenMH);

        if (tenMH.length() < 2) {
            return ResponseEntity.badRequest().body("Tên môn học: Vui lòng nhập từ 2 ký tự trở lên và không được để trống!");
        }
        if (!tenMH.matches("^[a-zA-Z0-9\\p{L} ]+$")) {
            return ResponseEntity.badRequest().body("Tên môn học: Không được chứa ký tự đặc biệt! Vui lòng nhập lại thông tin.");
        }

        if (monHocService.existsByTenMHExcluding(maMH, tenMH)) {
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

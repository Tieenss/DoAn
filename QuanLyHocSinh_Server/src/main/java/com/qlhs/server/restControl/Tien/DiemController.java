package com.qlhs.server.restControl.Tien;

import com.qlhs.server.entity.Diem;
import com.qlhs.server.service.Tien.DiemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diem")
public class DiemController {

    @Autowired
    private DiemService diemService;

    @GetMapping
    public List<Diem> getAllDiem() {
        return diemService.getAllDiem();
    }

    @GetMapping("/filter")
    public List<Diem> getDiemByFilter(
            @RequestParam String maLop,
            @RequestParam String maMH,
            @RequestParam int hocKy,
            @RequestParam(required = false, defaultValue = "") String namHoc) {
        return diemService.getDiemByFilter(maLop, maMH, hocKy, namHoc);
    }

    @GetMapping("/search")
    public List<Diem> searchDiem(@RequestParam String keyword) {
        return diemService.searchDiem(keyword);
    }

    @GetMapping("/hocky")
    public List<Integer> getDistinctHocKy() {
        return diemService.getDistinctHocKy();
    }

    @GetMapping("/namhoc")
    public List<String> getDistinctNamHoc() {
        return diemService.getDistinctNamHoc();
    }

    @GetMapping("/hocsinh/{maHS}")
    public List<Diem> getDiemByMaHS(@PathVariable String maHS) {
        return diemService.getDiemByMaHS(maHS);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkExists(
            @RequestParam String maHS,
            @RequestParam String maMH,
            @RequestParam int hocKy,
            @RequestParam String namHoc) {
        return ResponseEntity.ok(diemService.exists(maHS, maMH, hocKy, namHoc));
    }

    @PostMapping
    public ResponseEntity<?> createDiem(@RequestBody Diem diem) {
        if (diemService.exists(diem.getMaHS(), diem.getMaMH(), diem.getHocKy(), diem.getNamHoc())) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT)
                    .body("Điểm của học sinh " + diem.getMaHS() + " môn " + diem.getMaMH() + 
                          " học kỳ " + diem.getHocKy() + " năm học " + diem.getNamHoc() + " đã tồn tại trong hệ thống!");
        }
        return ResponseEntity.ok(diemService.saveDiem(diem));
    }

    @PutMapping
    public ResponseEntity<?> updateDiem(@RequestBody Diem diem) {
        if (!diemService.exists(diem.getMaHS(), diem.getMaMH(), diem.getHocKy(), diem.getNamHoc())) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy bản ghi điểm của học sinh để cập nhật!");
        }
        return ResponseEntity.ok(diemService.saveDiem(diem));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiem(
            @RequestParam String maHS,
            @RequestParam String maMH,
            @RequestParam int hocKy,
            @RequestParam String namHoc) {
        if (!diemService.exists(maHS, maMH, hocKy, namHoc)) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy bản ghi điểm để xóa!");
        }
        diemService.delete(maHS, maMH, hocKy, namHoc);
        return ResponseEntity.ok("Xóa điểm thành công!");
    }
}

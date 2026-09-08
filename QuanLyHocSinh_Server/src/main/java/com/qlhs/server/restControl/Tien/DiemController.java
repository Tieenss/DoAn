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
            @RequestParam int hocKy) {
        return diemService.getDiemByFilter(maLop, maMH, hocKy);
    }

    @GetMapping("/search")
    public List<Diem> searchDiem(@RequestParam String keyword) {
        return diemService.searchDiem(keyword);
    }

    @GetMapping("/hocky")
    public List<Integer> getDistinctHocKy() {
        return diemService.getDistinctHocKy();
    }

    @GetMapping("/hocsinh/{maHS}")
    public List<Diem> getDiemByMaHS(@PathVariable String maHS) {
        return diemService.getDiemByMaHS(maHS);
    }

    @PostMapping
    public ResponseEntity<Diem> createOrUpdateDiem(@RequestBody Diem diem) {
        return ResponseEntity.ok(diemService.saveDiem(diem));
    }
}

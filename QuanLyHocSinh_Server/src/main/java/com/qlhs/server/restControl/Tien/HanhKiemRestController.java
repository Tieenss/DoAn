package com.qlhs.server.restControl.Tien;

import com.qlhs.server.entity.HanhKiem;
import com.qlhs.server.service.Tien.HanhKiemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hanhkiem")
public class HanhKiemRestController {
    @Autowired
    private HanhKiemService hanhKiemService;

    @GetMapping("/filter")
    public List<HanhKiem> getByFilter(@RequestParam(defaultValue = "") String maLop,
            @RequestParam(defaultValue = "") String namHoc,
            @RequestParam(defaultValue = "0") int hocKy) {
        return hanhKiemService.getHanhKiemByFilter(maLop, namHoc, hocKy);
    }

    @GetMapping("/search")
    public List<HanhKiem> search(@RequestParam String keyword) {
        return hanhKiemService.searchHanhKiem(keyword);
    }

    @GetMapping("/namhoc")
    public List<String> getDistinctNamHoc() {
        return hanhKiemService.getDistinctNamHoc();
    }

    @GetMapping("/mahs/{maHS}")
    public List<HanhKiem> getByMaHS(@PathVariable String maHS) {
        return hanhKiemService.getHanhKiemByMaHS(maHS);
    }

    @PostMapping
    public ResponseEntity<HanhKiem> save(@RequestBody HanhKiem hk) {
        return ResponseEntity.ok(hanhKiemService.save(hk));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam String maHS,
            @RequestParam String namHoc,
            @RequestParam int hocKy) {
        if (!hanhKiemService.exists(maHS, namHoc, hocKy)) {
            return ResponseEntity.notFound().build();
        }
        hanhKiemService.delete(maHS, namHoc, hocKy);
        return ResponseEntity.ok().build();
    }
}

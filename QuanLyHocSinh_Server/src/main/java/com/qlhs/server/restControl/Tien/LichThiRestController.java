package com.qlhs.server.restControl.Tien;

import com.qlhs.server.entity.LichThi;
import com.qlhs.server.service.Tien.LichThiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lichthi")
public class LichThiRestController {
    @Autowired
    private LichThiService lichThiService;

    @GetMapping
    public List<LichThi> getAll() {
        return lichThiService.getAllLichThi();
    }

    @GetMapping("/search")
    public List<LichThi> search(@RequestParam String keyword) {
        return lichThiService.searchLichThi(keyword);
    }

    @GetMapping("/kythi")
    public List<String> getDistinctKyThi() {
        return lichThiService.getDistinctKyThi();
    }

    @GetMapping("/filter")
    public List<LichThi> filter(@RequestParam(required = false, defaultValue = "") String kyThi,
            @RequestParam(required = false, defaultValue = "") String maMH,
            @RequestParam(required = false, defaultValue = "") String maPhong,
            @RequestParam(required = false, defaultValue = "") String maLop) {
        return lichThiService.getLichThiByFilter(kyThi, maMH, maPhong, maLop);
    }

    @PostMapping
    public ResponseEntity<LichThi> add(@RequestBody LichThi lt) {
        
        return ResponseEntity.ok(lichThiService.save(lt));
    }

    @PutMapping("/{maLT}")
    public ResponseEntity<LichThi> update(@PathVariable int maLT, @RequestBody LichThi lt) {
        if (!lichThiService.exists(maLT)) {
            return ResponseEntity.notFound().build();
        }
        lt.setMaLT(maLT);
        return ResponseEntity.ok(lichThiService.save(lt));
    }

    @DeleteMapping("/{maLT}")
    public ResponseEntity<Void> delete(@PathVariable int maLT) {
        if (!lichThiService.exists(maLT)) {
            return ResponseEntity.notFound().build();
        }
        lichThiService.delete(maLT);
        return ResponseEntity.ok().build();
    }
}

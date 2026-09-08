package com.qlhs.server.restControl.ThuTrang;

import com.qlhs.server.entity.TKB;
import com.qlhs.server.service.ThuTrang.TKBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.qlhs.server.repository.Dat.LopRepository;
import com.qlhs.server.repository.Dat.GiaoVienRepository;
import com.qlhs.server.repository.ThuTrang.PhongHocRepository;
import com.qlhs.server.repository.ThuTrang.MonHocRepository;
@RestController
@RequestMapping("/api/tkb")
public class TKBRestController {
    @Autowired
    private TKBService tkbService;
    
    @Autowired
    private LopRepository lopRepository;
    
    @Autowired
    private GiaoVienRepository giaoVienRepository;
    
    @Autowired
    private PhongHocRepository phongHocRepository;
    
    @Autowired
    private MonHocRepository monHocRepository;

    @GetMapping
    public List<TKB> getAllTKB() {
        return tkbService.getAllTKB();
    }
    
    @GetMapping("/{maTKB}")
    public ResponseEntity<TKB> getByIdTKB(@PathVariable Integer maTKB) {
        return tkbService.getByIdTKB(maTKB)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lop/{maLop}")
    public List<TKB> getByMaLop(@PathVariable String maLop) {
        return tkbService.getByMaLop(maLop);
    }

    @GetMapping("/monhoc/{maMH}")
    public List<TKB> getByMaMH(@PathVariable String maMH) {
        return tkbService.getByMaMH(maMH);
    }

    @GetMapping("/phong/{maPhong}")
    public List<TKB> getByMaPhong(@PathVariable String maPhong) {
        return tkbService.getByMaPhong(maPhong);
    }

    @GetMapping("/giaovien/{maGV}")
    public List<TKB> getByMaGV(@PathVariable String maGV) {
        return tkbService.getByMaGV(maGV);
    }

    @GetMapping("/filter")
    public List<TKB> filter(
            @RequestParam(defaultValue = "") String maLop,
            @RequestParam(defaultValue = "") String maMH,
            @RequestParam(defaultValue = "0") Integer thu) {
            
        if (maLop.equals("Tất cả")) {
            maLop = "";
        }

        if (maLop.isEmpty() && maMH.isEmpty() && (thu == null || thu == 0)) {
            return tkbService.getAllTKB();
        }

        return tkbService.filter(maLop, maMH, thu);
    }

    @GetMapping("/danhsachlop")
    public List<String> getDanhSachLop() {
        return tkbService.getDistinctMaLop();
    }

    @GetMapping("/danhsachlop/tatca")
    public List<Map<String, String>> getDanhSachLopTatCa() {
        return lopRepository.findAll().stream().map(l -> {
            Map<String, String> m = new HashMap<>();
            m.put("ma", l.getMaLop());
            m.put("ten", l.getTenLop());
            return m;
        }).collect(Collectors.toList());
    }

    @GetMapping("/danhsachgv")
    public List<Map<String, String>> getDanhSachGV() {
        return giaoVienRepository.findAll().stream().map(g -> {
            Map<String, String> m = new HashMap<>();
            m.put("ma", g.getMaGV());
            m.put("ten", g.getHoTen());
            return m;
        }).collect(Collectors.toList());
    }

    @GetMapping("/danhsachphong")
    public List<Map<String, String>> getDanhSachPhong() {
        return phongHocRepository.findAll().stream().map(p -> {
            Map<String, String> m = new HashMap<>();
            m.put("ma", p.getMaPhong());
            m.put("ten", p.getTenPhong());
            return m;
        }).collect(Collectors.toList());
    }

    @GetMapping("/danhsachmon")
    public List<Map<String, String>> getDanhSachMon() {
        return monHocRepository.findAll().stream().map(mh -> {
            Map<String, String> m = new HashMap<>();
            m.put("ma", mh.getMaMH());
            m.put("ten", mh.getTenMH());
            return m;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<TKB> create(@RequestBody TKB tkb) {
        if (tkb.getMaTKB() != null && tkbService.existsByIdTKB(tkb.getMaTKB())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tkbService.save(tkb));
    }

    @PutMapping("/{maTKB}")
    public ResponseEntity<TKB> update(@PathVariable Integer maTKB, @RequestBody TKB tkb) {
        if (!tkbService.existsByIdTKB(maTKB)) {
            return ResponseEntity.notFound().build();
        }
        tkb.setMaTKB(maTKB);
        return ResponseEntity.ok(tkbService.save(tkb));
    }

    @DeleteMapping("/{maTKB}")
    public ResponseEntity<TKB> delete(@PathVariable Integer maTKB) {
        if (!tkbService.existsByIdTKB(maTKB)) {
            return ResponseEntity.notFound().build();
        }
        tkbService.delete(maTKB);
        return ResponseEntity.ok().build();
    }
}
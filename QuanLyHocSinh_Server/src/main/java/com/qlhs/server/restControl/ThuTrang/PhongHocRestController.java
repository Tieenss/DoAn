package com.qlhs.server.restControl.ThuTrang;

import com.qlhs.server.entity.PhongHoc;
import com.qlhs.server.service.ThuTrang.PhongHocService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<PhongHoc> create(@RequestBody PhongHoc phongHoc){
        if (phongHocService.existsPH(phongHoc.getMaPhong())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(phongHocService.save(phongHoc));
    }

    @PutMapping("/{maPhong}")
    public ResponseEntity<PhongHoc> update(@PathVariable String maPhong, @RequestBody PhongHoc phongHoc){
        if (!phongHocService.existsPH(maPhong)){
            return ResponseEntity.notFound().build();
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
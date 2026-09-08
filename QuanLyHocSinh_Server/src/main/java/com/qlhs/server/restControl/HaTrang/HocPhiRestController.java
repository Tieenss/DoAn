package com.qlhs.server.restControl.HaTrang;

import com.qlhs.server.entity.HocPhi;
import com.qlhs.server.service.HaTrang.HocPhiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hocphi")
public class HocPhiRestController {

    @Autowired
    private HocPhiService hocPhiService;

    @GetMapping
    public List<HocPhi> getAllHP() {
        return hocPhiService.getAllHP();
    }

    @GetMapping("/{maHP}")
    public ResponseEntity<HocPhi> getByIdHP(@PathVariable int maHP) {

        return hocPhiService.getByIdHP(maHP)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/search")
    public List<HocPhi> search(@RequestParam(defaultValue = "") String keyword) {

        return hocPhiService.search(keyword);

    }

    @GetMapping("/filter")
    public List<HocPhi> filter(

            @RequestParam(defaultValue = "") String maLop,
            @RequestParam(defaultValue = "0") int hocKy,
            @RequestParam(defaultValue = "") String namHoc){

        return hocPhiService.filter(maLop,hocKy,namHoc);

    }

    @GetMapping("/namhoc")
    public List<String> getNamHoc(@RequestParam String maLop){

        return hocPhiService.getNamHocByMaLop(maLop);

    }

    @GetMapping("/hocsinh/{maHS}")
    public List<HocPhi> getByMaHS(@PathVariable String maHS) {
        return hocPhiService.getByMaHS(maHS);
    }

    @PostMapping
    public ResponseEntity<HocPhi> createHP(@RequestBody HocPhi hocPhi) {
        
        hocPhi.setMaHP(0);

        return ResponseEntity.ok(hocPhiService.saveHP(hocPhi));
    }

    @PutMapping("/{maHP}")
    public ResponseEntity<HocPhi> updateHP(@PathVariable int maHP,
                                           @RequestBody HocPhi hocPhi) {

        if (!hocPhiService.existsHP(maHP)) {
            return ResponseEntity.notFound().build();
        }

        hocPhi.setMaHP(maHP);

        return ResponseEntity.ok(hocPhiService.saveHP(hocPhi));

    }

    @DeleteMapping("/{maHP}")
    public ResponseEntity<Void> deleteHP(@PathVariable int maHP) {

        if (!hocPhiService.existsHP(maHP)) {
            return ResponseEntity.notFound().build();
        }

        hocPhiService.deleteHP(maHP);

        return ResponseEntity.ok().build();

    }

}
package com.qlhs.server.restControl.HaTrang;

import com.qlhs.server.entity.ThongBao;
import com.qlhs.server.service.HaTrang.ThongBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/thongbao")
public class ThongBaoRestController {

    @Autowired
    private ThongBaoService thongBaoService;

    @GetMapping
    public List<ThongBao> getAllTB(){
        return thongBaoService.getAllTB();
    }

    @GetMapping("/{maTB}")
    public ResponseEntity<ThongBao> getByIdTB(@PathVariable int maTB){
        return thongBaoService.getByIdTB(maTB)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<ThongBao> search(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword){
        return thongBaoService.search(keyword.trim());
    }

    @PostMapping
    public ResponseEntity<ThongBao> createTB(@RequestBody ThongBao thongBao){
        
        if(thongBao.getNgayTao() == null) {
            thongBao.setNgayTao(new Date());
        }
        return ResponseEntity.ok(thongBaoService.saveTB(thongBao));
    }

    @PutMapping("/{maTB}")
    public ResponseEntity<ThongBao> updateTB(@PathVariable int maTB, @RequestBody ThongBao thongBao){
        if(!thongBaoService.existsTB(maTB)){
            return ResponseEntity.notFound().build();
        }
        thongBao.setMaTB(maTB);
        if(thongBao.getNgayTao() == null) {
            thongBao.setNgayTao(new Date());
        }
        return ResponseEntity.ok(thongBaoService.saveTB(thongBao));
    }

    @DeleteMapping("/{maTB}")
    public ResponseEntity<Void> deleteTB(@PathVariable int maTB){
        if(!thongBaoService.existsTB(maTB)){
            return ResponseEntity.notFound().build();
        }
        thongBaoService.deleteTB(maTB);
        return ResponseEntity.ok().build();
    }
}
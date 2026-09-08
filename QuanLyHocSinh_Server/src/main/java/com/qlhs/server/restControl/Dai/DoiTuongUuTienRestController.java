package com.qlhs.server.restControl.Dai;

import com.qlhs.server.entity.DoiTuongUuTien;
import com.qlhs.server.service.Dai.DoiTuongUuTienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doituong")
@CrossOrigin(origins = "*")
public class DoiTuongUuTienRestController {

    @Autowired
    private DoiTuongUuTienService service;

    @GetMapping
    public List<DoiTuongUuTien> getAll() {
        return service.getAll();
    }

    @GetMapping("/{maDT}")
    public DoiTuongUuTien getById(@PathVariable String maDT) {
        return service.getById(maDT).orElse(null);
    }

    @PostMapping
    public DoiTuongUuTien insert(@RequestBody DoiTuongUuTien dt) {
        return service.save(dt);
    }

    @PutMapping("/{maDT}")
    public DoiTuongUuTien update(@PathVariable String maDT,
                                 @RequestBody DoiTuongUuTien dt) {

        dt.setMaDT(maDT);

        return service.save(dt);
    }

    @DeleteMapping("/{maDT}")
    public void delete(@PathVariable String maDT) {
        service.delete(maDT);
    }

    @GetMapping("/search")
    public List<DoiTuongUuTien> search(@RequestParam String keyword) {
        return service.search(keyword);
    }
}
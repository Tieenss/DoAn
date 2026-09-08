package com.qlhs.server.restControl.Dai;

import com.qlhs.server.entity.HocSinh;
import com.qlhs.server.service.Dai.HocSinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hocsinh")
@CrossOrigin(origins = "*")
public class HocSinhController {

    @Autowired
    private HocSinhService service;

    @GetMapping
    public List<HocSinh> getAllHocSinh() {
        return service.getAllHocSinh();
    }

    @GetMapping("/{maHS}")
    public Optional<HocSinh> getHocSinh(@PathVariable String maHS) {
        return service.getHocSinhById(maHS);
    }

    @PostMapping
    public HocSinh insert(@RequestBody HocSinh hs) {
        return service.saveHocSinh(hs);
    }

    @PutMapping("/{maHS}")
    public HocSinh update(@PathVariable String maHS,
                          @RequestBody HocSinh hs) {

        hs.setMaHS(maHS);

        return service.saveHocSinh(hs);

    }

    @DeleteMapping("/{maHS}")
    public void delete(@PathVariable String maHS) {

        service.deleteHocSinh(maHS);

    }

    @GetMapping("/search")
    public List<HocSinh> search(
            @RequestParam String keyword) {

        return service.search(keyword);

    }

    @GetMapping("/malop")
    public List<String> getAllMaLop() {

        return service.getAllMaLop();

    }

    @GetMapping("/madoituong")
    public List<String> getAllMaDT() {

        return service.getAllMaDT();

    }

}
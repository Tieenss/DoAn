package com.qlhs.server.restControl.HaTrang;

import com.qlhs.server.entity.PhucKhao;
import com.qlhs.server.service.HaTrang.PhucKhaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phuckhao")
public class PhucKhaoRestController {

    @Autowired
    private PhucKhaoService phucKhaoService;

    @GetMapping
    public List<PhucKhao> getAllPK(){

        return phucKhaoService.getAllPK();

    }

    @GetMapping("/{maPK}")
    public ResponseEntity<PhucKhao> getByIdPK(@PathVariable int maPK){

        return phucKhaoService.getByIdPK(maPK)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/search")
    public List<PhucKhao> search(@RequestParam(defaultValue = "") String keyword){

        return phucKhaoService.search(keyword);

    }

    @GetMapping("/hocsinh/{maHS}")
    public List<PhucKhao> getByMaHS(@PathVariable String maHS) {
        return phucKhaoService.getByMaHS(maHS);
    }

    @PostMapping
    public ResponseEntity<PhucKhao> createPK(@RequestBody PhucKhao phucKhao){

        if(phucKhaoService.existsPK(phucKhao.getMaPK())){
            return ResponseEntity.status(409).body(null);
        }

        return ResponseEntity.ok(phucKhaoService.savePK(phucKhao));

    }

    @PutMapping("/{maPK}")
    public ResponseEntity<PhucKhao> updatePK(@PathVariable int maPK,
                                             @RequestBody PhucKhao phucKhao){

        if(!phucKhaoService.existsPK(maPK)){
            return ResponseEntity.notFound().build();
        }

        phucKhao.setMaPK(maPK);

        return ResponseEntity.ok(phucKhaoService.savePK(phucKhao));

    }

    @DeleteMapping("/{maPK}")
    public ResponseEntity<Void> deletePK(@PathVariable int maPK){

        if(!phucKhaoService.existsPK(maPK)){
            return ResponseEntity.notFound().build();
        }

        phucKhaoService.deletePK(maPK);

        return ResponseEntity.ok().build();

    }

}
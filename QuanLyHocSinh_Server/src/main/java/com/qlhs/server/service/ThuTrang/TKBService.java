package com.qlhs.server.service.ThuTrang;

import com.qlhs.server.entity.TKB;
import com.qlhs.server.repository.ThuTrang.MonHocRepository;
import com.qlhs.server.repository.ThuTrang.TKBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TKBService {
    @Autowired
    private TKBRepository tkbRepository;

    @Autowired
    private MonHocRepository monHocRepository;

    private void fillTenMH(List<TKB> list) {
        list.forEach(t -> monHocRepository.findById(t.getMaMH())
                .ifPresent(m -> t.setTenMH(m.getTenMH())));
    }

    public List<TKB> getAllTKB() {
        List<TKB> list = tkbRepository.findAll();
        fillTenMH(list);
        return list;
    }
    public List<TKB> getByMaLop(String maLop) {
        List<TKB> list = tkbRepository.findByMaLop(maLop);
        fillTenMH(list);
        return list;
    }

    public List<TKB> getByMaMH(String maMH) {
        List<TKB> list = tkbRepository.findByMaMH(maMH);
        fillTenMH(list);
        return list;
    }

    public List<TKB> getByMaPhong(String maPhong) {
        List<TKB> list = tkbRepository.findByMaPhong(maPhong);
        fillTenMH(list);
        return list;
    }

    public List<TKB> getByMaGV(String maGV) {
        List<TKB> list = tkbRepository.findByMaGV(maGV);
        fillTenMH(list);
        return list;
    }

    public List<TKB> filter(String maLop, String maMH, Integer thu) {
        List<TKB> list = tkbRepository.filterTKB(maLop, maMH, thu);
        fillTenMH(list);
        return list;
    }

    public List<String> getDistinctMaLop() {
        return tkbRepository.getDistinctMaLop();
    }

    public Optional<TKB> getByIdTKB(Integer maTKB) {
        return tkbRepository.findById(maTKB);
    }

    public TKB save(TKB tkb) {
        return tkbRepository.save(tkb);
    }

    public void delete(Integer maTKB) {
        tkbRepository.deleteById(maTKB);
    }

    public boolean existsByIdTKB(Integer maTKB) {
        return tkbRepository.existsById(maTKB);
    }

    public boolean isTrungTiet(TKB tkb) {
        return tkbRepository.findAll().stream().anyMatch(t ->
                t.getThu().equals(tkb.getThu()) && (
                        t.getMaLop().equals(tkb.getMaLop()) ||
                                t.getMaGV().equals(tkb.getMaGV()) ||
                                t.getMaPhong().equals(tkb.getMaPhong())
                ) &&
                        !(tkb.getTietBatDau() > t.getTietKetThuc() || tkb.getTietKetThuc() < t.getTietBatDau())
        );
    }
}
package com.qlhs.server.service.ThuTrang;

import com.qlhs.server.entity.MonHoc;
import com.qlhs.server.repository.ThuTrang.MonHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonHocService {
    @Autowired
    private MonHocRepository monHocRepository;

    public List<MonHoc> getAllMH() { return monHocRepository.findAll(); }

    public Optional<MonHoc> getByIdMH(String maMH) { return monHocRepository.findById(maMH); }

    public List<MonHoc> search(String keyword) { return monHocRepository.searchMonHoc(keyword); }

    public MonHoc saveMH(MonHoc monHoc) { return monHocRepository.save(monHoc); }

    public void deleteMH(String maMH) { monHocRepository.deleteById(maMH); }

    public boolean existsMH(String maMH) { return monHocRepository.existsById(maMH); }

    public boolean existsByTenMH(String tenMH) {
        return monHocRepository.findAll().stream()
                .anyMatch(m -> m.getTenMH().equalsIgnoreCase(tenMH));
    }
}
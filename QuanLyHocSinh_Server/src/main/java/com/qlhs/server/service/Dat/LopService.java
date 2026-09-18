package com.qlhs.server.service.Dat;

import com.qlhs.server.entity.Lop;
import com.qlhs.server.repository.Dat.LopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LopService {

    @Autowired
    private LopRepository lopRepository;

    public List<Map<String, Object>> getAllLop() {
        return lopRepository.findAllLopWithGVCN();
    }

    public List<Map<String, Object>> searchLop(String keyword, String nienKhoa) {
        return lopRepository.searchLop(keyword, nienKhoa);
    }

    public Map<String, Object> getLopById(String maLop) {
        return lopRepository.findByMaLopWithGVCN(maLop);
    }

    public Lop saveLop(Lop lop) {
        if (lop.getNienKhoa() != null && !lop.getNienKhoa().trim().isEmpty()) {
            if (!lop.getNienKhoa().matches("^\\d{4}-\\d{4}$")) {
                throw new IllegalArgumentException("Niên khóa không đúng định dạng YYYY-YYYY (Ví dụ: 2021-2024)");
            }
        }
        return lopRepository.save(lop);
    }

    public void deleteLop(String maLop) {
        lopRepository.deleteById(maLop);
    }

    public List<String> getDistinctNienKhoa() {
        return lopRepository.findDistinctNienKhoa();
    }

    public boolean existsLop(String maLop) {
        return lopRepository.existsById(maLop);
    }
}
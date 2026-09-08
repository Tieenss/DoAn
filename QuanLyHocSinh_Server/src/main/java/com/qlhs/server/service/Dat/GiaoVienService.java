package com.qlhs.server.service.Dat;

import com.qlhs.server.entity.GiaoVien;
import com.qlhs.server.repository.Dat.GiaoVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GiaoVienService {

    @Autowired
    private GiaoVienRepository giaoVienRepository;

    public List<Map<String, Object>> getAllGiaoVien() {
        return giaoVienRepository.findAllGiaoVienWithToHop();
    }

    public List<Map<String, Object>> searchGiaoVien(String keyword) {
        return giaoVienRepository.searchGiaoVien(keyword);
    }

    public Map<String, Object> getGiaoVienById(String maGV) {
        return giaoVienRepository.findByMaGVWithToHop(maGV);
    }

    public GiaoVien saveGiaoVien(GiaoVien giaoVien) {
        return giaoVienRepository.save(giaoVien);
    }

    public void deleteGiaoVien(String maGV) {
        giaoVienRepository.deleteById(maGV);
    }
}
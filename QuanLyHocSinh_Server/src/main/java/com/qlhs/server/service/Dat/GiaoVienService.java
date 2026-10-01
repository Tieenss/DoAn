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

    @Autowired
    private com.qlhs.server.repository.Dat.LopRepository lopRepository;

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
        List<com.qlhs.server.entity.Lop> lops = lopRepository.findByGiaoVienChuNhiem_MaGV(maGV);
        if (!lops.isEmpty()) {
            String tenLops = lops.stream()
                    .map(com.qlhs.server.entity.Lop::getTenLop)
                    .collect(java.util.stream.Collectors.joining(", "));
            throw new IllegalStateException("Giáo viên này đang làm chủ nhiệm lớp: " + tenLops);
        }
        giaoVienRepository.deleteById(maGV);
    }

    public boolean existsGiaoVien(String maGV) {
        return giaoVienRepository.existsById(maGV);
    }
}
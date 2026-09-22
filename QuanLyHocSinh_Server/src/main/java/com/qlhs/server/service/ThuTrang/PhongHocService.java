package com.qlhs.server.service.ThuTrang;

import com.qlhs.server.entity.PhongHoc;
import com.qlhs.server.repository.ThuTrang.PhongHocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhongHocService {
    @Autowired
    private PhongHocRepository phongHocRepository;

    public List<PhongHoc> getAllPH() { return phongHocRepository.findAllByOrderByMaPhongAsc(); }

    public Optional<PhongHoc> getByIdPH(String maPhong) { return phongHocRepository.findById(maPhong); }

    public List<PhongHoc> search(String ma, String loai, String tinhTrang) {
        return phongHocRepository.searchPhongHoc(ma, loai, tinhTrang);
    }

    public PhongHoc save(PhongHoc phongHoc) { return phongHocRepository.save(phongHoc); }

    public void delete(String maPhong) { phongHocRepository.deleteById(maPhong); }

    public boolean existsPH(String maPhong) {
        if (maPhong == null) return false;
        return phongHocRepository.existsById(maPhong);
    }

    public boolean existsByTenPhong(String tenPhong) {
        if (tenPhong == null) return false;
        return phongHocRepository.findAll().stream()
                .anyMatch(p -> p.getTenPhong() != null && p.getTenPhong().trim().equalsIgnoreCase(tenPhong.trim()));
    }

    public boolean existsByTenPhongExcluding(String maPhong, String tenPhong) {
        if (tenPhong == null) return false;
        return phongHocRepository.findAll().stream()
                .anyMatch(p -> (maPhong == null || !p.getMaPhong().equalsIgnoreCase(maPhong))
                        && p.getTenPhong() != null && p.getTenPhong().trim().equalsIgnoreCase(tenPhong.trim()));
    }
}
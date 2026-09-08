package com.qlhs.server.service.Tien;

import com.qlhs.server.entity.Diem;
import com.qlhs.server.repository.Tien.DiemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiemService {

    @Autowired
    private DiemRepository diemRepository;

    public List<Diem> getAllDiem() {
        return diemRepository.findAllDiemWithDetails();
    }

    public List<Diem> getDiemByFilter(String maLop, String maMH, int hocKy) {
        return diemRepository.findDiemByFilter(maLop, maMH, hocKy);
    }

    public List<Diem> searchDiem(String keyword) {
        return diemRepository.searchDiem(keyword);
    }

    public List<Diem> getDiemByMaHS(String maHS) {
        return diemRepository.findDiemByMaHSWithDetails(maHS);
    }

    public List<Integer> getDistinctHocKy() {
        return diemRepository.findDistinctHocKy();
    }

    public Diem saveDiem(Diem diem) {
        return diemRepository.save(diem);
    }
}

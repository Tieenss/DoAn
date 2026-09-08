package com.qlhs.server.service.Tien;

import com.qlhs.server.entity.LichThi;
import com.qlhs.server.repository.Tien.LichThiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LichThiService {
    @Autowired
    private LichThiRepository lichThiRepository;

    public List<LichThi> getAllLichThi() {
        return lichThiRepository.getAllLichThiWithTenMon();
    }

    public List<LichThi> searchLichThi(String keyword) {
        return lichThiRepository.searchLichThiNative(keyword);
    }

    public List<String> getDistinctKyThi() {
        return lichThiRepository.getDistinctKyThi();
    }

    public List<LichThi> getLichThiByFilter(String tenKyThi, String maMH, String maPhong, String maLop) {
        return lichThiRepository.filterLichThi(tenKyThi, maMH, maPhong, maLop);
    }

    @SuppressWarnings("null")
    public LichThi save(LichThi lt) {
        return lichThiRepository.save(lt);
    }

    public void delete(int maLT) {
        lichThiRepository.deleteById(maLT);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean exists(int maLT) {
        return lichThiRepository.existsById(maLT);
    }
}

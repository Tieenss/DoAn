package com.qlhs.server.service.Tien;

import com.qlhs.server.entity.HanhKiem;
import com.qlhs.server.repository.Tien.HanhKiemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HanhKiemService {
    @Autowired
    private HanhKiemRepository hanhKiemRepository;

    public List<HanhKiem> getHanhKiemByFilter(String maLop, String namHoc, int hocKy) {
        return hanhKiemRepository.getHanhKiemByFilter(maLop, namHoc, hocKy);
    }

    public List<HanhKiem> searchHanhKiem(String keyword) {
        return hanhKiemRepository.searchHanhKiem(keyword);
    }

    public List<HanhKiem> getHanhKiemByMaHS(String maHS) {
        return hanhKiemRepository.getHanhKiemByMaHS(maHS);
    }

    public List<String> getDistinctNamHoc() {
        return hanhKiemRepository.getDistinctNamHoc();
    }

    public HanhKiem save(HanhKiem hk) {
        return hanhKiemRepository.save(hk);
    }

    public void delete(String maHS, String namHoc, int hocKy) {
        hanhKiemRepository.deleteById(new HanhKiem.HanhKiemId(maHS, namHoc, hocKy));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean exists(String maHS, String namHoc, int hocKy) {
        return hanhKiemRepository.existsById(new HanhKiem.HanhKiemId(maHS, namHoc, hocKy));
    }
}

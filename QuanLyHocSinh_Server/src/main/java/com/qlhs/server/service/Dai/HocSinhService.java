package com.qlhs.server.service.Dai;

import com.qlhs.server.entity.HocSinh;
import com.qlhs.server.repository.Dai.HocSinhRepository;
import com.qlhs.server.repository.HaTrang.HocPhiRepository;
import com.qlhs.server.repository.Tien.DiemRepository;
import com.qlhs.server.repository.Tien.HanhKiemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
public class HocSinhService {

    @Autowired
    private HocSinhRepository repository;

    @Autowired
    private DiemRepository diemRepository;

    @Autowired
    private HanhKiemRepository hanhKiemRepository;

    @Autowired
    private HocPhiRepository hocPhiRepository;

    public List<HocSinh> getAllHocSinh() {
        return repository.findAll();
    }

    public Optional<HocSinh> getHocSinhById(String maHS) {
        return repository.findById(maHS);
    }

    public HocSinh saveHocSinh(HocSinh hs) {
        return repository.save(hs);
    }

    @Transactional
    public void deleteHocSinh(String maHS) {
        if (diemRepository.existsByMaHS(maHS)) {
            throw new IllegalStateException("Học sinh đang có dữ liệu điểm, không thể xóa!");
        }

        if (hanhKiemRepository.existsByMaHS(maHS)) {
            throw new IllegalStateException("Học sinh đang có dữ liệu hạnh kiểm, không thể xóa!");
        }

        if (hocPhiRepository.existsByMaHS(maHS)) {
            throw new IllegalStateException("Học sinh đang có dữ liệu học phí, không thể xóa!");
        }
        repository.deleteById(maHS);
    }

    public List<HocSinh> search(String keyword, String nienKhoa) {
        return repository.search(keyword, nienKhoa);
    }

    public List<String> getAllMaLop() {

        List<String> list = new ArrayList<>();

        for (HocSinh hs : repository.findAll()) {

            if (hs.getMaLop() != null &&
                    !list.contains(hs.getMaLop())) {

                list.add(hs.getMaLop());

            }

        }

        return list;

    }

    public List<String> getAllMaDT() {

        List<String> list = new ArrayList<>();

        for (HocSinh hs : repository.findAll()) {

            if (hs.getMaDT() != null &&
                    !list.contains(hs.getMaDT())) {

                list.add(hs.getMaDT());

            }

        }

        return list;

    }

    public List<String> getDistinctNienKhoa() {
        return repository.findDistinctNienKhoa();
    }

}
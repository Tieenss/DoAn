package com.qlhs.server.service.Dai;

import com.qlhs.server.entity.HocSinh;
import com.qlhs.server.repository.Dai.HocSinhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
public class HocSinhService {

    @Autowired
    private HocSinhRepository repository;

    public List<HocSinh> getAllHocSinh() {
        return repository.findAll();
    }

    public Optional<HocSinh> getHocSinhById(String maHS) {
        return repository.findById(maHS);
    }

    public HocSinh saveHocSinh(HocSinh hs) {
        return repository.save(hs);
    }

    public void deleteHocSinh(String maHS) {
        repository.deleteById(maHS);
    }

    public List<HocSinh> search(String keyword) {

        LinkedHashSet<HocSinh> result = new LinkedHashSet<>();

        result.addAll(repository.findByMaHSContaining(keyword));
        result.addAll(repository.findByHoTenContaining(keyword));

        return new ArrayList<>(result);
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

}
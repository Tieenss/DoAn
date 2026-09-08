package com.qlhs.server.service.Dai;

import com.qlhs.server.entity.DoiTuongUuTien;
import com.qlhs.server.repository.Dai.DoiTuongUuTienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DoiTuongUuTienService {

    @Autowired
    private DoiTuongUuTienRepository repository;

    public List<DoiTuongUuTien> getAll() {
        return repository.findAll();
    }

    public Optional<DoiTuongUuTien> getById(String maDT) {
        return repository.findById(maDT);
    }

    public DoiTuongUuTien save(DoiTuongUuTien dt) {
        return repository.save(dt);
    }

    public void delete(String maDT) {
        repository.deleteById(maDT);
    }

    public List<DoiTuongUuTien> search(String keyword) {

        LinkedHashSet<DoiTuongUuTien> result = new LinkedHashSet<>();

        result.addAll(repository.findByMaDTContaining(keyword));
        result.addAll(repository.findByTenDTContaining(keyword));

        return new ArrayList<>(result);
    }
}
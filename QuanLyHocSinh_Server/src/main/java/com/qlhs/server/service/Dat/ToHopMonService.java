package com.qlhs.server.service.Dat;

import com.qlhs.server.entity.ToHopMon;
import com.qlhs.server.repository.Dat.ToHopMonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ToHopMonService {

    @Autowired
    private ToHopMonRepository toHopMonRepository;

    public List<Map<String, Object>> getAllToHopMon() {
        return toHopMonRepository.findAllToHopMon();
    }

    public List<Map<String, Object>> searchToHopMon(String keyword) {
        return toHopMonRepository.searchToHopMon(keyword);
    }

    public List<Map<String, Object>> getToHopMonById(String maToHop) {
        return toHopMonRepository.findByMaToHop(maToHop);
    }

    public ToHopMon saveToHopMon(ToHopMon toHopMon) {
        return toHopMonRepository.save(toHopMon);
    }

    public void deleteToHopMon(String maToHop) {
        toHopMonRepository.deleteById(maToHop);
    }

    public boolean existsToHopMon(String maToHop) {
        return toHopMonRepository.existsById(maToHop);
    }

    public List<String> getDistinctTenToHop() {
        return toHopMonRepository.findDistinctTenToHop();
    }
}
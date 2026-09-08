package com.qlhs.server.service.HaTrang;

import com.qlhs.server.entity.HocPhi;
import com.qlhs.server.repository.HaTrang.HocPhiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HocPhiService {

    @Autowired
    private HocPhiRepository hocPhiRepository;

    public List<HocPhi> getAllHP() {
        return hocPhiRepository.findAllWithDetails();
    }

    public Optional<HocPhi> getByIdHP(int maHP) {
        return hocPhiRepository.findById(maHP);
    }

    public List<HocPhi> search(String keyword) {
        return hocPhiRepository.searchByKeyword(keyword);
    }

    public List<HocPhi> filter(String maLop,int hocKy,String namHoc){

        return hocPhiRepository.findAllWithDetails()
                .stream()
                .filter(x->
                        (maLop.isEmpty() || (x.getMaLop() != null && x.getMaLop().equalsIgnoreCase(maLop)))
                                &&
                                (hocKy==0 || x.getHocKy()==hocKy)
                                &&
                                (namHoc.isEmpty() || x.getNamHoc().equals(namHoc))
                )
                .toList();
    }

    public List<HocPhi> getByMaHS(String maHS) {
        return hocPhiRepository.findByMaHS(maHS);
    }

    public List<String> getNamHocByMaLop(String maLop){

        return hocPhiRepository.findAllWithDetails()
                .stream()
                .filter(x -> x.getMaLop() != null && x.getMaLop().equalsIgnoreCase(maLop))
                .map(HocPhi::getNamHoc)
                .distinct()
                .toList();

    }

    public HocPhi saveHP(HocPhi hocPhi) {
        return hocPhiRepository.save(hocPhi);
    }

    public void deleteHP(int maHP) {
        hocPhiRepository.deleteById(maHP);
    }

    public boolean existsHP(int maHP) {
        return hocPhiRepository.existsById(maHP);
    }

}
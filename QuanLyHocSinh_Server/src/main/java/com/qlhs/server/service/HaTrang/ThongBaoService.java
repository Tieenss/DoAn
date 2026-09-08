package com.qlhs.server.service.HaTrang;

import com.qlhs.server.entity.ThongBao;
import com.qlhs.server.repository.HaTrang.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ThongBaoService {

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    public List<ThongBao> getAllTB(){
        return thongBaoRepository.findAll();
    }

    public Optional<ThongBao> getByIdTB(int maTB){
        return thongBaoRepository.findById(maTB);
    }

    public List<ThongBao> search(String keyword){
        return thongBaoRepository.searchByKeyword(keyword);
    }

    public ThongBao saveTB(ThongBao thongBao){
        if (thongBao.getNgayTao() == null) {
            thongBao.setNgayTao(new Date());
        }
        return thongBaoRepository.save(thongBao);
    }

    public void deleteTB(int maTB){
        thongBaoRepository.deleteById(maTB);
    }

    public boolean existsTB(int maTB){
        return thongBaoRepository.existsById(maTB);
    }
}
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

    public List<LichThi> searchLichThi(String keyword, String namHoc) {
        return lichThiRepository.searchLichThiNative(keyword, namHoc);
    }

    public List<String> getDistinctKyThi() {
        return lichThiRepository.getDistinctKyThi();
    }

    public List<String> getDistinctNamHoc() {
        return lichThiRepository.getDistinctNamHoc();
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

    public String checkConflict(LichThi lt) {
        if (lt.getNgayThi() == null || lt.getGioBatDau() == null || lt.getGioKetThuc() == null) {
            return "Vui lòng cung cấp đầy đủ ngày thi, giờ bắt đầu và giờ kết thúc!";
        }
        if (!lt.getGioBatDau().isBefore(lt.getGioKetThuc())) {
            return "Lỗi thời gian: Giờ kết thúc phải lớn hơn giờ bắt đầu!";
        }

        // 1. Kiểm tra xung đột phòng thi
        if (lt.getMaPhong() != null && !lt.getMaPhong().trim().isEmpty()) {
            List<LichThi> roomConflicts = lichThiRepository.findRoomConflicts(
                    lt.getNgayThi(), lt.getMaPhong(), lt.getGioBatDau(), lt.getGioKetThuc(), lt.getMaLT());
            if (!roomConflicts.isEmpty()) {
                LichThi c = roomConflicts.get(0);
                return String.format("Trùng phòng thi %s: Đã có ca thi (Mã #%d, từ %s đến %s) cùng ngày!",
                        lt.getMaPhong(), c.getMaLT(), c.getGioBatDau(), c.getGioKetThuc());
            }
        }

        // 2. Kiểm tra xung đột lớp thi
        if (lt.getMaLop() != null && !lt.getMaLop().trim().isEmpty()) {
            List<LichThi> classConflicts = lichThiRepository.findClassConflicts(
                    lt.getNgayThi(), lt.getMaLop(), lt.getGioBatDau(), lt.getGioKetThuc(), lt.getMaLT());
            if (!classConflicts.isEmpty()) {
                LichThi c = classConflicts.get(0);
                return String.format("Trùng lịch lớp %s: Lớp đã có ca thi khác (Mã #%d, từ %s đến %s) cùng ngày!",
                        lt.getMaLop(), c.getMaLT(), c.getGioBatDau(), c.getGioKetThuc());
            }
        }

        // 3. Kiểm tra trùng môn thi cho lớp trong cùng một kỳ thi
        if (lt.getMaLop() != null && lt.getMaMH() != null && lt.getTenKyThi() != null) {
            List<LichThi> subConflicts = lichThiRepository.findSubjectConflicts(
                    lt.getMaLop(), lt.getMaMH(), lt.getTenKyThi(), lt.getNamHoc(), lt.getHocKy(), lt.getMaLT());
            if (!subConflicts.isEmpty()) {
                return String.format("Lớp %s đã có lịch thi môn %s trong kỳ thi '%s' (HK%d - %s)!",
                        lt.getMaLop(), lt.getMaMH(), lt.getTenKyThi(), lt.getHocKy(), lt.getNamHoc());
            }
        }

        return null;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean exists(int maLT) {
        return lichThiRepository.existsById(maLT);
    }
}

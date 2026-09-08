package Controller.Dai;

import Api.Đai.HocSinhApi;
import Model.Auth;
import Model.HocSinh;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class HocSinhController {

    private HocSinhApi api = new HocSinhApi();

    public void loadTable(DefaultTableModel model) {

        model.setRowCount(0);

        List<HocSinh> list;

        if (Auth.isHocSinh()) {

            HocSinh hs = api.getHocSinh(Auth.maNguoiDung);

            list = new ArrayList<>();

            if (hs != null) {
                list.add(hs);
            }

        } else {

            list = api.getAllHocSinh();

        }

        if (list == null) {
            return;
        }

        for (HocSinh hs : list) {

            model.addRow(new Object[]{
                    hs.getMaHS(),
                    hs.getHoTen(),
                    formatToDDMMYYYY(hs.getNgaySinh()),
                    hs.getGioiTinh(),
                    hs.getDiaChi(),
                    hs.getMaLop(),
                    hs.getMaDT()
            });

        }

    }

    private String formatToDDMMYYYY(String yyyyMMdd) {
        if (yyyyMMdd == null || yyyyMMdd.isEmpty()) return "";
        try {
            java.util.Date d = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(yyyyMMdd);
            return new java.text.SimpleDateFormat("dd/MM/yyyy").format(d);
        } catch (Exception e) {
            return yyyyMMdd;
        }
    }

    public boolean them(HocSinh hs) {
        return api.insertHocSinh(hs);
    }

    public boolean sua(HocSinh hs) {
        return api.updateHocSinh(hs);
    }

    public boolean xoa(String maHS) {
        return api.deleteHocSinh(maHS);
    }

    public void loadComboMaLop(JComboBox<String> cbo) {

        cbo.removeAllItems();

        List<String> list = api.getAllMaLop();

        if (list == null) return;

        for (String ma : list) {
            cbo.addItem(ma);
        }

    }

    public void loadComboMaDT(JComboBox<String> cbo) {

        cbo.removeAllItems();

        List<String> list = api.getAllMaDT();

        if (list == null) return;

        for (String ma : list) {
            cbo.addItem(ma);
        }

    }

    public boolean timKiem(String keyword, DefaultTableModel model) {

        model.setRowCount(0);

        List<HocSinh> list = api.search(keyword);

        if (list == null) {
            throw new RuntimeException("Không thể kết nối tới Server.");
        }

        if (list.isEmpty()) {
            return false;
        }

        for (HocSinh hs : list) {
            model.addRow(new Object[]{
                hs.getMaHS(),
                hs.getHoTen(),
                formatToDDMMYYYY(hs.getNgaySinh()),
                hs.getGioiTinh(),
                hs.getDiaChi(),
                hs.getMaLop(),
                hs.getMaDT()
            });
        }

        return true;
    }

    public HocSinh getThongTinCaNhan() {

        return api.getHocSinh(Auth.maNguoiDung);

    }

}